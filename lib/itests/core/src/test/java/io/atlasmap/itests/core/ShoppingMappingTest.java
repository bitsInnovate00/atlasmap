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

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.xmlunit.assertj.XmlAssert;

import io.atlasmap.api.AtlasContext;
import io.atlasmap.api.AtlasSession;
import io.atlasmap.builder.DefaultAtlasMappingBuilder;
import io.atlasmap.core.DefaultAtlasContextFactory;

public class ShoppingMappingTest {

    @Test
    public void test() throws Exception {
        URL url = Thread.currentThread().getContextClassLoader().getResource("mappings/shopping/shoppingResponseMapping.json");
        AtlasContext context = DefaultAtlasContextFactory.getInstance().createContext(url.toURI());
        AtlasSession session = context.createSession();
        String sourceJson = new String(Files.readAllBytes(Paths.get(
                Thread.currentThread().getContextClassLoader().getResource("data/shopping/multiODnativeResponse.xml").toURI())));
            // session.setSourceDocument("AirAvailabilityRS-atlas-93bbf193-bae6-4638-b921-2f951addc488", sourceJson);
            session.setSourceDocument("AirAvailabilityRS-atlas-eed8666a-81fd-4175-926f-b0d7279c18fc", sourceJson);
            context.process(session);
            System.out.println(TestHelper.printAudit(session));
            // assertFalse(session.hasErrors(), TestHelper.printAudit(session));
            // String targetXml = (String) session.getTargetDocument("AirShopResponse-8c0e6bf4-ce0f-4523-8cd7-1ae746d98a1f");
            String targetXml = (String) session.getTargetDocument("AirShopResponse-1af7506b-fdbb-4095-98af-beea10b98f36");
             System.out.println("target XML is "+ targetXml);
            // assertNotNull("target XML is null", targetXml);
            // HashMap<String, String> namespaces = new HashMap<>();
            // namespaces.put("tns", "http://atlasmap.io/itests/builder");
            // XmlAssert.assertThat(targetXml).withNamespaceContext(namespaces)
            //     .valueByXPath("//foo/bar/test").isEqualTo("4123562");
    }

   
}
