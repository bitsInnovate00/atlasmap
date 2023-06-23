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
package io.atlasmap.builder;

import java.util.List;

import io.atlasmap.core.DefaultAtlasSession;
import io.atlasmap.v2.Field;
import io.atlasmap.v2.FieldGroup;

public class AtlasCollectionField extends AtlasField {

    public AtlasCollectionField(DefaultAtlasSession session) {
        super(session);
        // TODO Auto-generated constructor stub
    }

    public AtlasField get(int index) {
        FieldGroup fieldGroup = (FieldGroup) getRawField();
        List<Field> fields = fieldGroup.getField();
        if (fields != null && !fields.isEmpty()) {
            Field rawField = fields.get(index);
            AtlasField atlasField = copy();
            atlasField.setRawField(rawField);
            return atlasField;
        } else {

            return null;
        }
    }

    public AtlasCollectionField get(String path) {
        return null;
    }

    public int size() {
        return 1;
    }

    public List<AtlasField> elements() {
        return null;
    }

}
