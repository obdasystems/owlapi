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
package org.semanticweb.owlapi.apitest.anonymous;

import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.apitest.TestFiles;
import org.semanticweb.owlapi.apitest.baseclasses.TestBase;
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

class AnonymousFunctionalRoundtripTestCase extends TestBase {

    @Test
    void shouldRoundTripFixed() {
        loadFrom(TestFiles.FIXED, new FunctionalSyntaxDocumentFormat());
    }

    @Test
    void shouldRoundTripBroken() {
        OWLOntology o = loadFrom(TestFiles.BROKEN, new RDFXMLDocumentFormat());
        FunctionalSyntaxDocumentFormat format = new FunctionalSyntaxDocumentFormat();
        o.getPrefixManager().withDefaultPrefix(NS + '#');
        OWLOntology o1 = roundTrip(o, format);
        equal(o, o1);
    }

    @Test
    void shouldRoundTrip() {
        OWLIndividual anon = AnonymousIndividual();
        OWLOntology ontology = create(iri(NS, ""));
        ontology.add(SubClassOf(CLASSES.C, ObjectHasValue(OBJPROPS.P, anon)),
            ClassAssertion(CLASSES.D, anon),
            DataPropertyAssertion(DATAPROPS.DP, anon, Literal("hello")));
        RDFXMLDocumentFormat format = new RDFXMLDocumentFormat();
        ontology.getPrefixManager().withDefaultPrefix(NS + '#');
        ontology = roundTrip(ontology, format);
        FunctionalSyntaxDocumentFormat format2 = new FunctionalSyntaxDocumentFormat();
        ontology = roundTrip(ontology, format2);
    }
}
