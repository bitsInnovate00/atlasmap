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

import com.fasterxml.jackson.annotation.JsonIgnore;

// @JsonRootName("RecommendationField")
// @JsonTypeInfo(include = JsonTypeInfo.As.PROPERTY, use = JsonTypeInfo.Id.CLASS, property = "jsonType")
public class RecommendationField implements Serializable {
    
    
    protected String nativeFieldId;
    protected String fieldPath;
    protected String name;
    @JsonIgnore
    protected String artifactId;
    protected String fieldType;
    private Integer minLength;
    private Integer maxLength;
    @JsonIgnore
    private String dataFormat;
    @JsonIgnore
    private String[] values;
    //Need to derive from values column using Janusgraph master data api
    @JsonIgnore
    private String[] derivedValueTypes;

    // move to PlanningRecomField
    @JsonIgnore
    private String businessContext;
    //taxonomy terms extracted from documentation
    @JsonIgnore
    private String[] docTerms;

    // move to PlanningRecomField
    @JsonIgnore
    private double score;

    private boolean leafNode;

    private int hierarchyDepth;



    public RecommendationField ()
    {

    }


    public boolean isLeafNode() {
        return leafNode;
    }


    public void setLeafNode(boolean leafNode) {
        this.leafNode = leafNode;
    }


    public int getHierarchyDepth() {
        return hierarchyDepth;
    }


    public void setHierarchyDepth(int hierarchyDepth) {
        this.hierarchyDepth = hierarchyDepth;
    }


    public Integer getMinLength() {
        return minLength;
    }



    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }



    public Integer getMaxLength() {
        return maxLength;
    }



    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }



    public String getFieldPath() {
        return fieldPath;
    }

    public void setFieldPath(String fieldPath) {
        this.fieldPath = fieldPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }



    public String getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(String dataFormat) {
        this.dataFormat = dataFormat;
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }

    public String getBusinessContext() {
        return businessContext;
    }

    public void setBusinessContext(String businessContext) {
        this.businessContext = businessContext;
    }

    public String[] getDocTerms() {
        return docTerms;
    }

    public void setDocTerms(String[] docTerms) {
        this.docTerms = docTerms;
    }

    @Override
    public String toString() {
        return "RecommendationField [artifactId=" + artifactId + ", fieldPath=" + fieldPath + ", fieldType=" + fieldType
                + ", name=" + name + "]";
    }


    public String[] getDerivedValueTypes() {
        return derivedValueTypes;
    }


    public void setDerivedValueTypes(String[] derivedValueTypes) {
        this.derivedValueTypes = derivedValueTypes;
    }


    public double getScore() {
        return score;
    }


    public void setScore(double score) {
        this.score = score;
    }


    


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((nativeFieldId == null) ? 0 : nativeFieldId.hashCode());
        result = prime * result + ((fieldPath == null) ? 0 : fieldPath.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RecommendationField other = (RecommendationField) obj;
        if (nativeFieldId == null) {
            if (other.nativeFieldId != null)
                return false;
        } else if (!nativeFieldId.equals(other.nativeFieldId))
            return false;
        if (fieldPath == null) {
            if (other.fieldPath != null)
                return false;
        } else if (!fieldPath.equals(other.fieldPath))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }


    public String getNativeFieldId() {
        return nativeFieldId;
    }


    public void setNativeFieldId(String nativeFieldId) {
        this.nativeFieldId = nativeFieldId;
    }

    

}