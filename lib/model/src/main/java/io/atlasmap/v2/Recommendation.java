/**
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
package io.atlasmap.v2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

// @JsonRootName("Recommendation")
// @JsonTypeInfo(include = JsonTypeInfo.As.PROPERTY, use = JsonTypeInfo.Id.CLASS, property = "jsonType")
public class Recommendation implements Serializable {

    public Recommendation()
    {

    }

    private static final long serialVersionUID = 1L;

    protected String sourceArtifactId;

    protected String targetArtifactId;

    protected List<RecommendationMapping> mappings;


    public String getSourceArtifactId() {
        return sourceArtifactId;
    }

    public void setSourceArtifactId(String sourceArtifactId) {
        this.sourceArtifactId = sourceArtifactId;
    }

    public String getTargetArtifactId() {
        return targetArtifactId;
    }

    public void setTargetArtifactId(String targetArtifactId) {
        this.targetArtifactId = targetArtifactId;
    }

    public void setMappings(List<RecommendationMapping> mappings) {
        this.mappings = mappings;
    }

    public List<RecommendationMapping> getMappings() {
        return mappings;
    }

    // @JsonRootName("Mapping")
    // @JsonTypeInfo(include = JsonTypeInfo.As.PROPERTY, use = JsonTypeInfo.Id.CLASS, property = "jsonType")
     

    @Override
    public String toString() {
        return "Recommendation [mappings=" + mappings + ", sourceArtifactId=" + sourceArtifactId + ", targetArtifactId="
                + targetArtifactId + "]";
    }

    
}

 


