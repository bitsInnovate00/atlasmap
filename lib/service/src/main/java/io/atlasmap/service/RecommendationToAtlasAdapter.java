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

import java.util.ArrayList;
import java.util.List;

import io.atlasmap.v2.AtlasMapping;
import io.atlasmap.v2.DataSource;
import io.atlasmap.v2.Field;
import io.atlasmap.v2.FieldType;
import io.atlasmap.v2.Mapping;
import io.atlasmap.v2.Recommendation;
import io.atlasmap.v2.RecommendationField;
import io.atlasmap.xml.v2.XmlField;

public class RecommendationToAtlasAdapter {

    public static void covertMapping(AtlasMapping mapping, Recommendation recommendation) {
        // final List<Mapping> atlasMappings=new ArrayList<Mapping>();
        DataSource source=mapping.getDataSource().get(0);
        DataSource target=mapping.getDataSource().get(1);
        recommendation.getMappings().stream().forEach(recommendationMapping -> {
            Mapping atlasmapping=new Mapping();
            Field atlasinputField=new XmlField();
            RecommendationField recommendationinputField=(RecommendationField) recommendationMapping.getInputField();
            atlasinputField.setFieldType(FieldType.STRING);
            atlasinputField.setName(recommendationinputField.getName());
            atlasinputField.setPath(recommendationinputField.getPath());
            atlasinputField.setDocId(source.getId());
            atlasmapping.getInputField().add(atlasinputField);

            Field atlasoutputField=new XmlField();
            RecommendationField recommendationoutputField=(RecommendationField) recommendationMapping.getOutputField();
            atlasoutputField.setFieldType(FieldType.STRING);
            atlasoutputField.setName(recommendationoutputField.getName());
            atlasoutputField.setPath(recommendationoutputField.getPath());
            atlasoutputField.setDocId(target.getId());
            atlasmapping.getOutputField().add(atlasoutputField);
            mapping.getMappings().getMapping().add(atlasmapping);
            
            
        });

      
        
        
        


        
    }
}