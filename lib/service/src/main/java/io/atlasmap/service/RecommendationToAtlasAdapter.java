
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
            RecommendationField recommendationinputField=recommendationMapping.getInputField();
            atlasinputField.setFieldType(FieldType.STRING);
            atlasinputField.setName(recommendationinputField.getName());
            atlasinputField.setPath(recommendationinputField.getFieldPath());
            atlasinputField.setDocId(source.getId());
            atlasmapping.getInputField().add(atlasinputField);

            Field atlasoutputField=new XmlField();
            RecommendationField recommendationoutputField=recommendationMapping.getOutputField();
            atlasoutputField.setFieldType(FieldType.STRING);
            atlasoutputField.setName(recommendationoutputField.getName());
            atlasoutputField.setPath(recommendationoutputField.getFieldPath());
            atlasoutputField.setDocId(target.getId());
            atlasmapping.getOutputField().add(atlasoutputField);
            mapping.getMappings().getMapping().add(atlasmapping);
            
            
        });

      
        
        
        


        
    }
}