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
package io.atlasmap.kafkaconnect.service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.atlasmap.kafkaconnect.core.KafkaConnectUtil;
import io.atlasmap.kafkaconnect.inspect.KafkaConnectInspectionService;
import io.atlasmap.kafkaconnect.v2.KafkaConnectConstants;
import io.atlasmap.kafkaconnect.v2.KafkaConnectDocument;
import io.atlasmap.kafkaconnect.v2.KafkaConnectInspectionRequest;
import io.atlasmap.kafkaconnect.v2.KafkaConnectInspectionResponse;
import io.atlasmap.kafkaconnect.v2.KafkaConnectSchemaType;
import io.atlasmap.service.AtlasService;
import io.atlasmap.service.ModuleService;
import io.atlasmap.v2.Field;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * Kafka Connect Service provides Kafka Connect schema inspection service which generate an AtlasMap Document object from
 * Kafka Connect schema such as JSON or AVRO.
 */
@Path("/kafkaconnect/")
public class KafkaConnectService extends ModuleService {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaConnectService.class);

    @Context
    private ResourceContext resourceContext;

    /**
     * Inspects a Kafka Connect schema and return a Document object.
     * @param request {@link KafkaConnectInspectionRequest}
     * @return {@link KafkaConnectInspectionResponse}
     */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    @Path("/inspect")
    @Operation(summary = "Inspect Kafka Connect Schema", description = "Inspect a Kafka Connect schema and return a Document object")
    @RequestBody(description = "KafkaConnectInspectionRequest object", content = @Content(schema = @Schema(implementation = KafkaConnectInspectionRequest.class)))
    @ApiResponses(@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = KafkaConnectInspectionResponse.class)), description = "Return a Document object represented by KafkaConnectDocument"))
    public Response inspect(InputStream request) {
        return inspect(fromJson(request, KafkaConnectInspectionRequest.class));
    }

    /**
     * Inspects a Kafka Connect schema and return a Document object.
     * @param request request
     * @return {@link KafkaConnectInspectionResponse}
     */
    public Response inspect(KafkaConnectInspectionRequest request) {
        long startTime = System.currentTimeMillis();

        KafkaConnectInspectionResponse response = new KafkaConnectInspectionResponse();
        KafkaConnectDocument d = null;

        try {

            ClassLoader loader = resourceContext != null
                    ? resourceContext.getResource(AtlasService.class).getLibraryLoader()
                    : KafkaConnectService.class.getClassLoader();
            KafkaConnectInspectionService s = new KafkaConnectInspectionService(loader);

            String schemaTypeStr = request.getOptions().get(KafkaConnectConstants.OPTIONS_SCHEMA_TYPE);
            KafkaConnectSchemaType schemaType = KafkaConnectSchemaType.valueOf(schemaTypeStr);
            HashMap<String, Object> options = KafkaConnectUtil.repackParserOptions(request.getOptions());
    
            switch (schemaType) {
                case JSON:
                    d = s.inspectJson(request.getSchemaData(), options);
                    break;
                case AVRO:
                    d = s.inspectAvro(request.getSchemaData(), options);
                    break;
                default:
                    response.setErrorMessage("Unsupported inspection type: " + schemaType);
                    break;
            }
        } catch (Exception e) {
            LOG.error("Error inspecting Kafka Connect schema: " + e.getMessage(), e);
            response.setErrorMessage(e.getMessage());
        } finally {
            response.setExecutionTime(System.currentTimeMillis() - startTime);
        }

        response.setKafkaConnectDocument(d);
        return Response.ok().entity(toJson(response)).build();
    }

    @Override
    public Field getField(String path, boolean recursive) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Field> searchFields(String keywords) {
        // TODO Auto-generated method stub
        return null;
    }
}