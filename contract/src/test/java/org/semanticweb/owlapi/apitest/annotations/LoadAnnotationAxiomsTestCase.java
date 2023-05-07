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
package org.semanticweb.owlapi.apitest.annotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.semanticweb.owlapi.utilities.OWLAPIStreamUtils.asUnorderedSet;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.apitest.baseclasses.TestBase;
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OntologyConfigurator;

/**
 * @author Matthew Horridge, The University of Manchester, Bio-Health Informatics Group
 * @since 3.1.0
 */
class LoadAnnotationAxiomsTestCase extends TestBase {

    @Test
    void testIgnoreAnnotations() {
        OWLOntology ont = createAnon();
        ont.add(SubClassOf(CLASSES.A, CLASSES.B),
            AnnotationAssertion(RDFSComment(), CLASSES.A.getIRI(), Literal("Hello world")),
            AnnotationPropertyDomain(RDFSComment(), CLASSES.A.getIRI()),
            AnnotationPropertyRange(RDFSComment(), CLASSES.B.getIRI()),
            SubAnnotationPropertyOf(ANNPROPS.MY_COMMENT, RDFSComment()));
        reload(ont, new RDFXMLDocumentFormat());
        reload(ont, new OWLXMLDocumentFormat());
        reload(ont, new TurtleDocumentFormat());
        reload(ont, new FunctionalSyntaxDocumentFormat());
    }

    private void reload(OWLOntology ontology, OWLDocumentFormat format) {
        Set<OWLAxiom> axioms = asUnorderedSet(ontology.axioms());
        Set<OWLAxiom> annotationAxioms =
            asUnorderedSet(axioms.stream().filter(OWLAxiom::isAnnotationAxiom));
        OntologyConfigurator withAnnosConfig = new OntologyConfigurator();
        OWLOntology reloadedWithAnnoAxioms = reload(ontology, format, withAnnosConfig);
        equal(ontology, reloadedWithAnnoAxioms);
        OntologyConfigurator withoutAnnosConfig =
            new OntologyConfigurator().setLoadAnnotationAxioms(false);
        OWLOntology reloadedWithoutAnnoAxioms = reload(ontology, format, withoutAnnosConfig);
        assertNotEquals(axioms, asUnorderedSet(reloadedWithoutAnnoAxioms.axioms()));
        Set<OWLAxiom> axiomsMinusAnnotationAxioms = new HashSet<>(axioms);
        axiomsMinusAnnotationAxioms.removeAll(annotationAxioms);
        assertEquals(axiomsMinusAnnotationAxioms,
            asUnorderedSet(reloadedWithoutAnnoAxioms.axioms()));
    }

    private OWLOntology reload(OWLOntology ontology, OWLDocumentFormat format,
        OntologyConfigurator configuration) {
        OWLOntology reloaded =
            loadWithConfig(saveOntology(ontology, format), format, configuration);
        reloaded.remove(reloaded.axioms(AxiomType.DECLARATION));
        return reloaded;
    }
}
