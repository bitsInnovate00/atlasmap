/**
 * Copyright (C) 2017 Red Hat, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.atlasmap.functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.atlasmap.core.AtlasPath;
import io.atlasmap.core.AtlasPath.SegmentContext;
import io.atlasmap.core.BaseFunctionFactory;
import io.atlasmap.expression.Expression;
import io.atlasmap.expression.parser.ParseException;
import io.atlasmap.v2.AtlasModelFactory;
import io.atlasmap.v2.CollectionType;
import io.atlasmap.v2.Field;
import io.atlasmap.v2.FieldGroup;
import io.atlasmap.v2.SimpleField;

public class COPYPARENT extends BaseFunctionFactory {

    // COPYPARENT("${xml-source:/root/class<>/classid}","${xml-source:/root/class<>/student<>/stdid}")
    private SegmentContext lastCollectionSegment;

    /**
     * COPYPARENT(${xml-source:/root/class<>/classid},${xml-source:/root/class<>/student<>})
     */
    @Override
    public Expression create(List<Expression> args) throws ParseException {
        if (args.size() != 2) {
            throw new ParseException("COPYPARENT expects 2 arguments.");
        }
        // ${xml-source:/root/class<>/classid}
        Expression parentExpression = args.get(0);
        // ${xml-source:/root/class<>/student<>}
        Expression childExpression = args.get(1);
        return (ctx) -> {
            // list of class ids
            Field parentFieldGroup = parentExpression.evaluate(ctx);
            List<Field> parentCollection = parentFieldGroup instanceof FieldGroup
                    ? ((FieldGroup) parentFieldGroup).getField()
                    : Arrays.asList(parentFieldGroup);

            // list of student complex objects
            Field childFieldGroup = childExpression.evaluate(ctx);
            // List<Field> childCollection = childFieldGroup instanceof FieldGroup
            //         ? ((FieldGroup) childFieldGroup).getField()
            //         : Arrays.asList(childFieldGroup);
             List<Field> childCollection=((FieldGroup) childFieldGroup).getField().stream().filter(f -> (f instanceof FieldGroup)).collect(Collectors.toList());
             List<Field> childFilteredList =childCollection.stream().filter(f -> (f instanceof FieldGroup)).flatMap(f -> ((FieldGroup)f).getField().stream().filter(fi -> (fi instanceof FieldGroup))).collect(Collectors.toList());
            // List<Field> childFilteredList=(childCollection.stream().flatMap(f -> (Fi))getField().stream().filter(f -> (f instanceof FieldGroup)).collect(Collectors.toList());
            // Object unwraField = AtlasModelFactory.unwrapField(childFieldGroup); 
            List<Field> unwrapChildFields=(List<Field>) childCollection.stream().flatMap(AtlasModelFactory::unwrapFieldGroup).collect(Collectors.toList()) ;
                  
            List<Field> selected = new ArrayList<>();
            final FieldGroup answer = AtlasModelFactory.createFieldGroupFrom(childFieldGroup, true);
            // answer field has studentid. Now we need to add classid to the field group

            // List<SegmentContext> childSegments = new
            // AtlasPath(childFieldGroup.getPath()).getSegments(true);

            // for (int i=1; i<childSegments.size(); i++) {
            // SegmentContext segment = childSegments.get(i);
            // }

            // for (int j=0;i<childCollection.size();j++)
            // {
            // Field childField=childCollection.get(j);
            // AtlasPath childFieldPath=new AtlasPath(childField.getPath());
            // List<SegmentContext>
            // childCollectionSegments=childFieldPath.getCollectionSegments(true);
            // for(SegmentContext segment:childCollectionSegments)
            // {
            // String childParentPath=childFieldPath.getSegmentPath(segment);
            // Field filteredParent=filterParent(childParentPath,parentCollection);
            // }

            // }
            List<Field> finalAnswer=new ArrayList<>();
            for (int i = 0; i < parentCollection.size(); i++) {
                Field parentField = parentCollection.get(i);
                // /root/class<1>/classid
                AtlasPath parentPath = new AtlasPath(parentField.getPath());
                // /class<1>
                SegmentContext parentSegment = parentPath.getLastCollectionSegment();
                // /class<1>/student<1> , /class<1>/student<2>
                List<Field> filteredChildFields = filterChildFields(parentSegment, childFilteredList);
                for (Field filteredChildField : filteredChildFields) {
                    FieldGroup fieldGrouptmp = (FieldGroup) filteredChildField;
                     FieldGroup fieldGroup = AtlasModelFactory.createFieldGroupFrom(fieldGrouptmp, false);
                    Field subField = new SimpleField();
                    AtlasPath subPath = new AtlasPath(fieldGroup.getPath());
                    String parentPathStr = subPath.getSegmentPath(subPath.getLastSegmentParent());
                    AtlasModelFactory.copyField(parentField, subField, false);
                    subField.setPath(fieldGroup.getPath() + AtlasPath.PATH_SEPARATOR + parentField.getName());
                    subField.setIndex(null);
                    subField.setValue(parentField.getValue());
                    // /root/class<1>/student<1>/classid
                    fieldGroup.getField().add(subField);
                    finalAnswer.add(subField);
                }
                // answer.getField().addAll(filteredChildFields);

            }

            answer.getField().addAll(finalAnswer);
            // answer.setPath(AtlasModelFactory.GENERATED_PATH);
            // for (Field f : parentCollection) {
            // Field fs = (Field) childExpression.evaluate((subCtx) -> {
            // if (subCtx != null &&
            // AtlasModelFactory.GENERATED_PATH.equals(answer.getPath())) {
            // answer.setPath(parentFieldGroup.getPath() +
            // (subCtx.startsWith(AtlasPath.PATH_SEPARATOR) ? subCtx :
            // (AtlasPath.PATH_SEPARATOR + subCtx)));
            // }
            // return AtlasPath.extractChildren(f, subCtx);
            // });
            // selected.add(fs);
            // }
            // if (selected.size() == 1) {
            // return selected.get(0);
            // }
            // answer.getField().addAll(selected);
            return answer;
        };
    }

    private List<Field> filterChildFields(SegmentContext parentSegment, List<Field> childCollection) {

     // TODO: childCollection should drilldown to the final terminal field and then check . the recursion is missing here. to fix 
     
        List<Field> fields = childCollection.stream().filter(field -> {
            AtlasPath path = new AtlasPath(field.getPath());
            return (path.getOriginalPath().indexOf(parentSegment.getExpression()) != -1);

        }).collect(Collectors.toList());
        return fields;
    }

    private Field filterParent(String childParentPath, List<Field> parentCollection) {

        for (Field field : parentCollection) {

            if (field.getPath().equals(childParentPath))
                return field;
        }
        return null;
    }

}
