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
package org.semanticweb.owlapi.apitest.ontology;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.apitest.baseclasses.TestBase;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.SetOntologyID;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group
 * @since 3.2.0
 */
class HashCodeTestCase extends TestBase {

    private static final String THREE = "3.0";

    @Test
    void testSetContainsLong() {
        OWLLiteral litNoComp = Literal("3", Long());
        OWLLiteral litNoComp2 = Literal("3", Long());
        OWLLiteral litLongImpl = Literal(3L);
        assertEquals(litNoComp, litLongImpl);
        assertEquals(litNoComp.hashCode(), litLongImpl.hashCode());
        assertEquals(litNoComp.getLiteral(), litLongImpl.getLiteral());
        Set<OWLLiteral> lncset = new HashSet<>();
        lncset.add(litNoComp);
        assertTrue(lncset.contains(litNoComp2));
        assertTrue(lncset.contains(litLongImpl));
    }

    @Test
    void testSetContainsInt() {
        OWLLiteral litNoComp = Literal("3", Integer());
        OWLLiteral litNoComp2 = Literal("3", Integer());
        OWLLiteral litIntImpl = Literal(3);
        assertEquals(litNoComp, litIntImpl);
        assertEquals(litNoComp.hashCode(), litIntImpl.hashCode());
        assertEquals(litNoComp.getLiteral(), litIntImpl.getLiteral());
        Set<OWLLiteral> lncset = new HashSet<>();
        lncset.add(litNoComp);
        assertTrue(lncset.contains(litNoComp2));
        assertTrue(lncset.contains(litIntImpl));
    }

    @Test
    void testSetContainsDouble() {
        OWLLiteral litNoComp = Literal(THREE, Double());
        OWLLiteral litNoComp2 = Literal(THREE, Double());
        OWLLiteral litIntImpl = Literal(3.0D);
        assertEquals(litNoComp, litIntImpl);
        assertEquals(litNoComp.hashCode(), litIntImpl.hashCode());
        assertEquals(litNoComp.getLiteral(), litIntImpl.getLiteral());
        Set<OWLLiteral> lncset = new HashSet<>();
        lncset.add(litNoComp);
        assertTrue(lncset.contains(litNoComp2));
        assertTrue(lncset.contains(litIntImpl));
    }

    @Test
    void testSetContainsFloat() {
        OWLLiteral litNoComp = Literal(THREE, Float());
        OWLLiteral litNoComp2 = Literal(THREE, Float());
        OWLLiteral litIntImpl = Literal(3.0F);
        assertEquals(litNoComp, litIntImpl);
        assertEquals(litNoComp.hashCode(), litIntImpl.hashCode());
        assertEquals(litNoComp.getLiteral(), litIntImpl.getLiteral());
        Set<OWLLiteral> lncset = new HashSet<>();
        lncset.add(litNoComp);
        assertTrue(lncset.contains(litNoComp2));
        assertTrue(lncset.contains(litIntImpl));
    }

    @Test
    void testSetContainsBoolean() {
        OWLLiteral litNoComp = Literal("true", Boolean());
        OWLLiteral litNoComp2 = Literal("true", Boolean());
        OWLLiteral litIntImpl = Literal(true);
        assertEquals(litNoComp, litIntImpl);
        assertEquals(litNoComp.hashCode(), litIntImpl.hashCode());
        assertEquals(litNoComp.getLiteral(), litIntImpl.getLiteral());
        Set<OWLLiteral> lncset = new HashSet<>();
        lncset.add(litNoComp);
        assertTrue(lncset.contains(litNoComp2));
        assertTrue(lncset.contains(litIntImpl));
    }

    @Test
    void shouldHaveSameHashCodeForOntologies() {
        final OWLOntology ontology = createAnon();
        int hash = ontology.hashCode();
        IRI iri = iri("urn:test:", "ontology");
        ontology.applyChange(new SetOntologyID(ontology, iri));
        int otherHash = ontology.hashCode();
        assertNotEquals(hash, otherHash);
        assertNotNull(m.getOntology(iri));
        assertNotNull(m.getOntology(ontology.getOntologyID()));
    }

    @Test
    void shouldHaveSameHashCodeForOntologies1() {
        OWLOntologyID id1 = OntologyID(iri("http://purl.org/dc/elements/1.1/", ""));
        OWLOntologyID id2 = OntologyID(iri("http://purl.org/dc/elements/1.1/", ""));
        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }
}
