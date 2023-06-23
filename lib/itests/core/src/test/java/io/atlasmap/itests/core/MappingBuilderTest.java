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
package io.atlasmap.itests.core;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.definition.KiePackage;
import org.kie.api.definition.rule.Rule;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.xmlunit.assertj.XmlAssert;

import com.ibsplc.iRes.shoppingrs.AirAvailabilityRS;
import com.ibsplc.iRes.shoppingrs.OriginDestinationInfo;
import com.ibsplc.iRes.shoppingrs.TripInfoType;

import io.atlasmap.api.AtlasContext;
import io.atlasmap.api.AtlasSession;
import io.atlasmap.builder.AtlasField;
import io.atlasmap.builder.DefaultAtlasMappingBuilder;
import io.atlasmap.core.DefaultAtlasContextFactory;

public class MappingBuilderTest {

    @Test
    public void test() throws Exception {
        URL url = Thread.currentThread().getContextClassLoader().getResource("mappings/ndc-mapping.json");
        File file = new File("/home/bittu/work/atlasmap/lib/itests/core/src/test/resources/mappings/ndc-mapping.json");
        AtlasContext context = DefaultAtlasContextFactory.getInstance().createContext(file);
        AtlasSession session = context.createSession();
        String sourceJson = new String(Files.readAllBytes(Paths.get("/home/bittu/work/atlasmap/lib/itests/core/src/test/resources/data/iflyresResponse.xml")));
                // Thread.currentThread().getContextClassLoader().getResource("data/iflyresResponse.xml").toURI())));
            // System.out.println(" source json "+sourceJson);
                session.setSourceDocument("iFlyRes_AirShoppingRS-atlas-9acff547-b60a-4507-8920-090488c7d368", sourceJson);
            context.process(session);
            // assertFalse(session.hasErrors(), TestHelper.printAudit(session));
            String targetXml = (String) session.getTargetDocument("IATA_AirShoppingRS-213-2c49a12b-3f18-4d89-bcb2-ac286c94e0ad");
            System.out.println(" target xml "+targetXml);
            assertNotNull("target XML is null", targetXml);
            // HashMap<String, String> namespaces = new HashMap<>();
            // namespaces.put("tns", "http://atlasmap.io/itests/builder");
            // XmlAssert.assertThat(targetXml).withNamespaceContext(namespaces)
            //     .valueByXPath("//foo/bar/test").isEqualTo("4123562");
    }
    // /ns2:AirAvailabilityRS/OriginDestinationInfo[1]/TripInfo[1]
    public static class JsonXmlBuilder extends DefaultAtlasMappingBuilder {
        @Override
        public void processMapping() throws Exception {

            AtlasField readField=read("iFlyRes_AirShoppingRS-atlas-9acff547-b60a-4507-8920-090488c7d368", "/tns:AirAvailabilityRS/OriginDestinationInfo<>");
// Object unwrapObj=readField.unwrap("iFlyRes_AirShoppingRS-atlas-9acff547-b60a-4507-8920-090488c7d368", TripInfoType.class,"com.ibsplc.iRes.shoppingrs");
// List<TripInfoType> orignList=(List<TripInfoType>)unwrapObj;
// OriginDestinationInfo info1=orignList.get(0);


            // AtlasField readField=read("iFlyRes_AirShoppingRS-atlas-9acff547-b60a-4507-8920-090488c7d368", "/tns:AirAvailabilityRS");
// Object unwrapObj=readField.unwrap("iFlyRes_AirShoppingRS-atlas-9acff547-b60a-4507-8920-090488c7d368", AirAvailabilityRS.class,"com.ibsplc.iRes.shoppingrs");
Object unwrapObj=readField.unwrap("iFlyRes_AirShoppingRS-atlas-9acff547-b60a-4507-8920-090488c7d368", OriginDestinationInfo.class,"com.ibsplc.iRes.shoppingrs");
List<OriginDestinationInfo> orignList=(List<OriginDestinationInfo>)unwrapObj;

// AirAvailabilityRS rs=(AirAvailabilityRS)unwrapObj;
// KieServices kieServices = KieServices.Factory.get();
//         KieContainer kContainer = kieServices.getKieClasspathContainer();
//         KieBase kieBase = kContainer.getKieBase("ndc");
//         for ( KiePackage kp : kieBase.getKiePackages() ) {
//             for (Rule rule : kp.getRules()) {
//                 System.out.println("kp " + kp + " rule " + rule.getName());
//             }
//         }
//         KieSession session = kieBase.newKieSession();
//         session.insert(orignList.get(0));
//         session.insert(orignList.get(1));
//         // session.insert(rs.getOriginDestinationInfo().get(0));
//         // session.insert(rs.getOriginDestinationInfo().get(1));
//         // session.insert(readField);
//         session.fireAllRules();
// // List<OriginDestinationInfo> orignList=(List<OriginDestinationInfo>)unwrapObj;
// OriginDestinationInfo info1=orignList.get(0);
// System.out.println(info1.getOrigin() +" origin info ...");
// boolean isGMP=info1.getDestination().equals("GMP");
                readField.write("IATA_AirShoppingRS-213-2c49a12b-3f18-4d89-bcb2-ac286c94e0ad", "/foo/bar/test");
        }
    }

}
