/* This file is part of the OWL API.
 * The contents of this file are subject to the LGPL License, Version 3.0.
 * Copyright 2014, The University of Manchester
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 * Alternatively, the contents of this file may be used under the terms of the Apache License, Version 2.0 in which case, the provisions of the Apache License Version 2.0 are applicable instead of those above.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License. */
package org.semanticweb.owlapi.apitest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EnumSet;

import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.apitest.baseclasses.TestBase;
import org.semanticweb.owlapi.documents.StringDocumentTarget;
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.vocab.Namespaces;
import org.semanticweb.owlapi.vocab.XSDVocabulary;

class NamespacesTestCase extends TestBase {

    @Test
    void shouldFindInNamespace() {
        EnumSet<Namespaces> reserved =
            EnumSet.of(Namespaces.OWL, Namespaces.RDF, Namespaces.RDFS, Namespaces.XSD);
        for (Namespaces n : Namespaces.values()) {
            IRI iriNamespace = iri(n.getPrefixIRI(), "test");
            boolean reservedVocabulary = iriNamespace.isReservedVocabulary();
            assertEquals(reservedVocabulary, reserved.contains(n), iriNamespace
                + " reserved? Should be " + reserved.contains(n) + " but is " + reservedVocabulary);
        }
    }

    @Test
    void shouldParseXSDSTRING() {
        // given
        String shortVersion = "xsd:string";
        // when
        XSDVocabulary value = XSDVocabulary.parseShortName(shortVersion);
        // then
        assertEquals(XSDVocabulary.STRING, value);
        assertEquals(String(), Datatype(value.getIRI()));
    }

    @Test
    void shouldFailToParseInvalidString() {
        // given
        String st = "xsd:st";
        // when
        assertThrows(IllegalArgumentException.class, () -> XSDVocabulary.parseShortName(st));
        // then
        // an exception should have been thrown
    }

    @Test
    void shouldSetPrefix() {
        OWLClass item = Class("http://test.owl/test#", "item");
        OWLDeclarationAxiom declaration = Declaration(item);
        OWLOntology o1 = createAnon();
        FunctionalSyntaxDocumentFormat pm1 = new FunctionalSyntaxDocumentFormat();
        o1.getPrefixManager().withPrefix(":", "http://test.owl/test#");
        m.setOntologyFormat(o1, pm1);
        o1.addAxiom(declaration);
        StringDocumentTarget target1 = saveOntology(o1);
        OWLOntology o2 = createAnon();
        FunctionalSyntaxDocumentFormat pm2 = new FunctionalSyntaxDocumentFormat();
        o2.getPrefixManager().withPrefix(":", "http://test.owl/test#");
        o2.addAxiom(declaration);
        StringDocumentTarget target2 = saveOntology(o2, pm2);
        assertTrue(target2.toString().contains("Declaration(Class(:item))"));
        assertEquals(target1.toString(), target2.toString());
    }
}
