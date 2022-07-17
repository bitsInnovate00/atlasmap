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

/**
 * The enumeration of Kafka Connect schema type.
 */
public enum KafkaConnectSchemaType {
    /** JSON. */
    JSON("JSON"),
    /** AVRO. */
    AVRO("AVRO"),
    /** PROTOBUF. */
    PROTOBUF("PROTOBUF");

    private String value;

    /**
     * A constructor.
     * @param inspectionType inspection type
     */
    KafkaConnectSchemaType(String inspectionType) {
        value = inspectionType;
    }

    /**
     * Gets the value.
     * @return value
     */
    public String value(){
        return this.value;
    }

    
}