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
package io.atlasmap.kafkaconnect.v2;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import io.atlasmap.v2.BaseInspectionRequest;

/**
 * The top container object of Kafka Connect Document inspection request that AtlasMap UI sends
 * to the backend.
 */
@JsonRootName("KafkaConnectInspectionRequest")
@JsonTypeInfo(include = JsonTypeInfo.As.PROPERTY, use = JsonTypeInfo.Id.CLASS, property = "jsonType")
public class KafkaConnectInspectionRequest extends BaseInspectionRequest {

    /** Schema data. */
    private String schemaData;

    /**
     * Gets the value of the schemaData property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchemaData() {
        return schemaData;
    }

    /**
     * Sets the value of the schemaData property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchemaData(String value) {
        this.schemaData = value;
    }

    @Override
    public boolean equals(Object object) {
        if (!super.equals(object)) {
            return false;
        }
        final KafkaConnectInspectionRequest that = ((KafkaConnectInspectionRequest) object);
        {
            String leftSchemaData;
            leftSchemaData = this.getSchemaData();
            String rightSchemaData;
            rightSchemaData = that.getSchemaData();
            if (this.schemaData!= null) {
                if (that.schemaData!= null) {
                    if (!leftSchemaData.equals(rightSchemaData)) {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                if (that.schemaData!= null) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int currentHashCode = (super.hashCode() * 31);
        {
            currentHashCode = (currentHashCode* 31);
            String theJsonData;
            theJsonData = this.getSchemaData();
            if (this.schemaData!= null) {
                currentHashCode += theJsonData.hashCode();
            }
        }
        return currentHashCode;
    }

}