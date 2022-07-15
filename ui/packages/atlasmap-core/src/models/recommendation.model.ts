/*
    Copyright (C) 2017 Red Hat, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

            http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

import { Field } from ".";

export class RecommendationField {
  path: string;
  name: string;
  type: string;
  usercreated: boolean;
  isArray: boolean;
  isAttribute: boolean;
  isCollection:  boolean;
  isPrimitive: boolean;
  namespaceAlias: string | null;
  artifactid: string;

  constructor(field: Field) {
    this.path=field.path;
    this.name=field.name;
    this.type=field.type;
    this.usercreated=field.userCreated;
    this.isArray=field.isArray;
    this.isAttribute=field.isAttribute;
    this.isCollection=field.isCollection;
    this.isPrimitive=field.isPrimitive;
    this.namespaceAlias=field.namespaceAlias;

    // this._identifier = ErrorInfo.errorIdentifierCounter.toString();
    // ErrorInfo.errorIdentifierCounter++;
    // Object.assign(this, init);
  }
  
}

export class RecommendationRequest {
  soruceArtifactid: string;
  targetArtifactid: string;
  field: RecommendationField;
}
