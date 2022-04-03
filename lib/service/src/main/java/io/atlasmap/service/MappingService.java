/*
 * Copyright (C) 2017 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.atlasmap.service;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.atlasmap.api.AtlasContext;
import io.atlasmap.api.AtlasException;
import io.atlasmap.api.AtlasPreviewContext;
import io.atlasmap.api.AtlasSession;
import io.atlasmap.core.ADMArchiveHandler;
import io.atlasmap.v2.AtlasMapping;
import io.atlasmap.v2.Audits;
import io.atlasmap.v2.Mapping;
import io.atlasmap.v2.ProcessMappingRequest;
import io.atlasmap.v2.ProcessMappingResponse;
import io.atlasmap.v2.Validations;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * Provides backend REST services for handling mappings.
 * @see AtlasService
 * @see DocumentService
 */
@Path("/project/{mappingDefinitionId}/mapping")
public class MappingService extends BaseAtlasService {
    private static final Logger LOG = LoggerFactory.getLogger(MappingService.class);

    @Context
    private ResourceContext resourceContext;
    private AtlasService atlasService;
    private final AtlasPreviewContext previewContext;

    /**
     * A constructor.
     */
    public MappingService() {
        if (resourceContext == null) {
            throw new IllegalStateException("JAX-RS ResourceContext is not injected");
        }
        this.atlasService = resourceContext.getResource(AtlasService.class);
        this.previewContext = this.atlasService.getContextFactory().createPreviewContext();
    }

    /**
     * A constructor.
     * @param parent parent {@link AtlasService}
     */
    public MappingService(AtlasService parent) {
        this.atlasService = parent;
        this.previewContext = this.atlasService.getContextFactory().createPreviewContext();
    }

    /**
     * Remove mappings defined in a given mapping definition.
     * @param mappingDefinitionId mapping definition ID
     * @return empty response
     */
    @DELETE
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Remove Mapping", description = "Remove mappings from mapping definition")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "The mappings for the specified mapping definition was removed successfully"),
        @ApiResponse(responseCode = "204", description = "The specified mapping definition was not found")})
    public Response removeMappingRequest(@Parameter(description = "Mapping Definition ID") @PathParam("mappingDefinitionId") Integer mappingDefinitionId) {
        try {
            ADMArchiveHandler handler = atlasService.loadExplodedMappingDirectory(mappingDefinitionId);
            AtlasMapping def = handler.getMappingDefinition();
            def.getMappings().getMapping().clear();
            handler.persist();
        } catch (AtlasException e) {
            LOG.error("Error removing mappings from a mapping definition file for ID:" + mappingDefinitionId, e);
            throw new WebApplicationException(e.getMessage(), e, Status.INTERNAL_SERVER_ERROR);
        }
        return Response.ok().build();
    }

    /**
     * Retrieve a mapping file saved on the server.
     * @param mappingDefinitionId mapping definition ID
     * @return file
     */
    @GET
    @Path("/")
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML,MediaType.APPLICATION_OCTET_STREAM})
    @Operation(summary = "Get Mapping", description = "Retrieve a mapping file saved on the server")
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = AtlasMapping.class)), description = "Return a mapping file content"),
        @ApiResponse(responseCode = "204", description = "Mapping file was not found"),
        @ApiResponse(responseCode = "500", description = "Mapping file access error")})
    public Response getMappingRequest(
      @Parameter(description = "Mapping Definition ID") @PathParam("mappingDefinitionId") Integer mappingDefinitionId) {
        LOG.debug("getMappingRequest: {}", mappingDefinitionId);
        ADMArchiveHandler admHandler = this.atlasService.loadExplodedMappingDirectory(mappingDefinitionId);

        byte[] serialized = null;
        try {
            serialized = admHandler.getMappingDefinitionBytes();
        } catch (Exception e) {
            LOG.error("Error retrieving mapping definition file for ID:" + mappingDefinitionId, e);
            throw new WebApplicationException(e.getMessage(), e, Status.INTERNAL_SERVER_ERROR);
        }
        if (LOG.isDebugEnabled() && serialized != null) {
            LOG.debug(new String(serialized));
        }
        if (serialized == null) {
            LOG.debug("Mapping definition not found for ID:{}", mappingDefinitionId);
            return Response.noContent().build();
        }
        return Response.ok().entity(serialized).build();
    }

        /**
     * Updates existing mapping file on the server.
     * @param mapping mapping
     * @param mappingDefinitionId mapping definition ID
     * @param uriInfo URI info
     * @return empty response
     */
    @PUT
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update Mapping", description = "Update existing mapping file on the server")
    @RequestBody(description = "Mapping file content", content = @Content(schema = @Schema(implementation = AtlasMapping.class)))
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Succeeded"))
    public Response updateMappingRequest(
            InputStream mapping,
            @Parameter(description = "Mapping Definition ID") @PathParam("mappingDefinitionId") Integer mappingDefinitionId,
            @Context UriInfo uriInfo) {
        ADMArchiveHandler handler = atlasService.loadExplodedMappingDirectory(mappingDefinitionId);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        try {
            handler.setMappingDefinitionBytes(mapping);
            handler.persist();
            builder.path(handler.getMappingDefinition().getName());
        } catch (AtlasException e) {
            LOG.error("Error saving Mapping Definition file.\n" + e.getMessage(), e);
            throw new WebApplicationException(e.getMessage(), e, Status.INTERNAL_SERVER_ERROR);
        }

        return Response.ok().location(builder.build()).build();
    }

    /**
     * Validates the mapping file.
     * @param mapping mapping
     * @param mappingDefinitionId mapping definition ID
     * @param uriInfo URI info
     * @return {@link Validations} validation result
     */
    @POST
    @Path("/validate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Validate Mapping", description = "Validate mapping file")
    @RequestBody(description = "Mapping file content", content = @Content(schema = @Schema(implementation = AtlasMapping.class)))
    @ApiResponses(@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation =  Validations.class)), description = "Return a validation result"))
    public Response validateMappingRequest(InputStream mapping,
                                           @Parameter(description = "Mapping Definition ID") @PathParam("mappingDefinitionId") Integer mappingDefinitionId,
                                           @Context UriInfo uriInfo) {
        try {
            AtlasMapping atlasMapping = fromJson(mapping, AtlasMapping.class);
            LOG.debug("Validate mappings: {}", atlasMapping.getName());
            return validateMapping(mappingDefinitionId, atlasMapping, uriInfo);
        } catch (AtlasException | IOException e) {
            throw new WebApplicationException(e.getMessage(), e, Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Performs mapping validation.
     * @param mappingDefinitionId mapping definition ID
     * @param mapping mapping
     * @param uriInfo URI info
     * @return {@link Validations} validation result
     * @throws IOException unexpected error
     * @throws AtlasException unexpected error
     */
    private Response validateMapping(Integer mappingDefinitionId, AtlasMapping mapping, UriInfo uriInfo) throws IOException, AtlasException {
        AtlasSession session;
        synchronized (atlasService.getContextFactory()) {
            AtlasContext context = atlasService.getContextFactory().createContext(mapping);
            session = context.createSession();
            context.processValidation(session);
        }

        Validations validations = session.getValidations();
        if (session.getValidations() == null) {
            validations = new Validations();
        }

        return Response.ok().entity(toJson(validations)).build();
    }

    /**
     * Processes mapping by feeding input data.
     * @param request request
     * @param mappingDefinitionId Mapping Definition ID
     * @param uriInfo URI info
     * @return {@link ProcessMappingResponse} which holds the result of the mappings
     */
    @POST
    @Path("/process")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Process Mapping", description = "Process Mapping by feeding input data")
    @RequestBody(description = "Mapping file content", content = @Content(schema = @Schema(implementation = AtlasMapping.class)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ProcessMappingResponse.class)), description = "Return a mapping result"),
            @ApiResponse(responseCode = "204", description = "Skipped empty mapping execution") })
    public Response processMappingRequest(InputStream request,
            @Parameter(description = "Mapping Definition ID") @PathParam("mappingDefinitionId") Integer mappingDefinitionId,
            @Context UriInfo uriInfo) {
        ProcessMappingRequest pmr = fromJson(request, ProcessMappingRequest.class);
        if (pmr.getAtlasMapping() != null) {
            throw new WebApplicationException("Whole mapping execution is not yet supported");
        }
        Mapping mapping = pmr.getMapping();
        if (mapping == null) {
            return Response.noContent().build();
        }
        Audits audits = null;
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Preview request: {}", new String(toJson(mapping)));
            }
            audits = previewContext.processPreview(mapping);
        } catch (AtlasException e) {
            throw new WebApplicationException("Unable to process mapping preview", e);
        }
        ProcessMappingResponse response = new ProcessMappingResponse();
        response.setMapping(mapping);
        if (audits != null) {
            response.setAudits(audits);
        }
        byte[] serialized = toJson(response);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Preview outcome: {}", new String(serialized));
        }
        return Response.ok().entity(serialized).build();
    }

}