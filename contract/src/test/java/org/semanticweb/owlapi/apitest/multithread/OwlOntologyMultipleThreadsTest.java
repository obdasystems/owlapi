/*
 * This file is part of the OWL API.
 *
 * The contents of this file are subject to the LGPL License, Version 3.0.
 *
 * Copyright (C) 2011, The University of Manchester
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 *
 * Alternatively, the contents of this file may be used under the terms of the Apache License, Version 2.0
 * in which case, the provisions of the Apache License Version 2.0 are applicable instead of those above.
 *
 * Copyright 2011, University of Manchester
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.semanticweb.owlapi.apitest.multithread;

import static org.semanticweb.owlapi.model.parameters.AxiomAnnotations.IGNORE_AXIOM_ANNOTATIONS;
import static org.semanticweb.owlapi.model.parameters.Imports.EXCLUDED;
import static org.semanticweb.owlapi.model.parameters.Imports.INCLUDED;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.apitest.TestFiles;
import org.semanticweb.owlapi.apitest.baseclasses.TestBase;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;
import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;

class OwlOntologyMultipleThreadsTest extends TestBase {

    private static class TestCallback implements Runnable {

        private final OWLOntology o1;
        private final OWLOntology o2;

        TestCallback(OWLOntology o1, OWLOntology o2) {
            this.o1 = o1;
            this.o2 = o2;
        }

        void consume(@SuppressWarnings("unused") Object o) {}

        @Override
        public void run() {
            for (int index = 0; index < 100; index++) {
                o1.isEmpty();
                o1.annotationsAsList().forEach(this::consume);
                o1.signature(INCLUDED).forEach(this::consume);
                o1.signature(EXCLUDED).forEach(this::consume);
                o1.getOWLOntologyManager();
                o1.getOntologyID();
                o1.isAnonymous();
                o1.directImportsDocuments().forEach(this::consume);
                o1.directImports().forEach(this::consume);
                o1.imports().forEach(this::consume);
                o1.importsClosure().forEach(this::consume);
                o1.importsDeclarations().forEach(this::consume);
                o1.axioms().forEach(this::consume);
                o1.getAxiomCount();
                List<OWLClass> classes = o1.classesInSignature().toList();
                o1.classesInSignature(INCLUDED).forEach(this::consume);
                o1.classesInSignature(EXCLUDED).forEach(this::consume);
                List<OWLObjectProperty> objectProperties =
                    o1.objectPropertiesInSignature(INCLUDED).toList();
                o1.objectPropertiesInSignature(EXCLUDED).forEach(this::consume);
                o1.objectPropertiesInSignature().forEach(this::consume);
                List<OWLDataProperty> dataProperties = o1.dataPropertiesInSignature().toList();
                o1.dataPropertiesInSignature(INCLUDED).forEach(this::consume);
                o1.dataPropertiesInSignature(EXCLUDED).forEach(this::consume);
                List<OWLNamedIndividual> individuals = o1.individualsInSignature().toList();
                o1.individualsInSignature(INCLUDED).forEach(this::consume);
                o1.individualsInSignature(EXCLUDED).forEach(this::consume);
                List<OWLAnonymousIndividual> anonIndividuals =
                    o1.referencedAnonymousIndividuals(EXCLUDED).toList();
                o1.datatypesInSignature().forEach(this::consume);
                o1.datatypesInSignature(INCLUDED).forEach(this::consume);
                o1.datatypesInSignature(EXCLUDED).forEach(this::consume);
                o1.annotationPropertiesInSignature(EXCLUDED).forEach(this::consume);
                for (OWLObjectProperty o : objectProperties) {
                    o1.axioms(o, EXCLUDED).forEach(this::consume);
                    o1.containsObjectPropertyInSignature(o.getIRI(), EXCLUDED);
                    o1.containsObjectPropertyInSignature(o.getIRI(), INCLUDED);
                    o1.containsObjectPropertyInSignature(o.getIRI(), EXCLUDED);
                    o1.objectSubPropertyAxiomsForSubProperty(o).forEach(this::consume);
                    o1.objectSubPropertyAxiomsForSuperProperty(o).forEach(this::consume);
                    o1.objectPropertyDomainAxioms(o).forEach(this::consume);
                    o1.objectPropertyRangeAxioms(o).forEach(this::consume);
                    o1.inverseObjectPropertyAxioms(o).forEach(this::consume);
                    o1.equivalentObjectPropertiesAxioms(o).forEach(this::consume);
                    o1.disjointObjectPropertiesAxioms(o).forEach(this::consume);
                    o1.functionalObjectPropertyAxioms(o).forEach(this::consume);
                    o1.inverseFunctionalObjectPropertyAxioms(o).forEach(this::consume);
                    o1.symmetricObjectPropertyAxioms(o).forEach(this::consume);
                    o1.asymmetricObjectPropertyAxioms(o).forEach(this::consume);
                    o1.reflexiveObjectPropertyAxioms(o).forEach(this::consume);
                    o1.irreflexiveObjectPropertyAxioms(o).forEach(this::consume);
                    o1.transitiveObjectPropertyAxioms(o).forEach(this::consume);
                }
                for (OWLClass cl : classes) {
                    o1.axioms(cl, EXCLUDED).forEach(this::consume);
                    o1.containsClassInSignature(cl.getIRI(), EXCLUDED);
                    o1.containsClassInSignature(cl.getIRI(), INCLUDED);
                    o1.containsClassInSignature(cl.getIRI(), EXCLUDED);
                    o1.subClassAxiomsForSubClass(cl).forEach(this::consume);
                    o1.subClassAxiomsForSuperClass(cl).forEach(this::consume);
                    o1.equivalentClassesAxioms(cl).forEach(this::consume);
                    o1.disjointClassesAxioms(cl).forEach(this::consume);
                    o1.disjointUnionAxioms(cl).forEach(this::consume);
                    o1.hasKeyAxioms(cl).forEach(this::consume);
                    o1.classAssertionAxioms(cl).forEach(this::consume);
                }
                for (OWLDataProperty property : dataProperties) {
                    o1.axioms(property, EXCLUDED).forEach(this::consume);
                    o1.containsDataPropertyInSignature(property.getIRI(), EXCLUDED);
                    o1.containsDataPropertyInSignature(property.getIRI(), INCLUDED);
                    o1.containsDataPropertyInSignature(property.getIRI(), EXCLUDED);
                    o1.dataSubPropertyAxiomsForSubProperty(property).forEach(this::consume);
                    o1.dataSubPropertyAxiomsForSuperProperty(property).forEach(this::consume);
                    o1.dataPropertyDomainAxioms(property).forEach(this::consume);
                    o1.dataPropertyRangeAxioms(property).forEach(this::consume);
                    o1.equivalentDataPropertiesAxioms(property).forEach(this::consume);
                    o1.disjointDataPropertiesAxioms(property).forEach(this::consume);
                    o1.functionalDataPropertyAxioms(property).forEach(this::consume);
                }
                for (OWLNamedIndividual individual : individuals) {
                    o1.axioms(individual, EXCLUDED).forEach(this::consume);
                    o1.containsIndividualInSignature(individual.getIRI(), EXCLUDED);
                    o1.containsIndividualInSignature(individual.getIRI(), INCLUDED);
                    o1.containsIndividualInSignature(individual.getIRI(), EXCLUDED);
                    o1.classAssertionAxioms(individual).forEach(this::consume);
                    o1.dataPropertyAssertionAxioms(individual).forEach(this::consume);
                    o1.objectPropertyAssertionAxioms(individual).forEach(this::consume);
                    o1.negativeObjectPropertyAssertionAxioms(individual).forEach(this::consume);
                    o1.negativeDataPropertyAssertionAxioms(individual).forEach(this::consume);
                    o1.sameIndividualAxioms(individual).forEach(this::consume);
                    o1.differentIndividualAxioms(individual).forEach(this::consume);
                }
                for (OWLAnonymousIndividual individual : anonIndividuals) {
                    assert individual != null;
                    o1.axioms(individual, EXCLUDED).forEach(this::consume);
                }
                for (AxiomType<?> ax : AxiomType.axiomTypes()) {
                    assert ax != null;
                    o1.axioms(ax).forEach(this::consume);
                    o1.axioms(ax, INCLUDED).forEach(this::consume);
                    o1.axioms(ax, EXCLUDED).forEach(this::consume);
                }
                for (OWLDatatype type : o1.datatypesInSignature().toList()) {
                    o1.axioms(type, EXCLUDED).forEach(this::consume);
                    o1.containsDatatypeInSignature(type.getIRI(), EXCLUDED);
                    o1.containsDatatypeInSignature(type.getIRI(), INCLUDED);
                    o1.containsDatatypeInSignature(type.getIRI(), EXCLUDED);
                    o1.datatypeDefinitions(type).forEach(this::consume);
                }
                for (OWLAnnotationProperty property : o1.annotationPropertiesInSignature(EXCLUDED)
                    .toList()) {
                    assert property != null;
                    o1.axioms(property, EXCLUDED).forEach(this::consume);
                    o1.containsAnnotationPropertyInSignature(property.getIRI(), EXCLUDED);
                    o1.containsAnnotationPropertyInSignature(property.getIRI(), INCLUDED);
                    o1.containsAnnotationPropertyInSignature(property.getIRI(), EXCLUDED);
                    o1.subAnnotationPropertyOfAxioms(property).forEach(this::consume);
                    o1.annotationPropertyDomainAxioms(property).forEach(this::consume);
                    o1.annotationPropertyRangeAxioms(property).forEach(this::consume);
                }
                for (AxiomType<?> ax : AxiomType.axiomTypes()) {
                    assert ax != null;
                    o1.getAxiomCount(ax);
                    o1.getAxiomCount(ax, INCLUDED);
                    o1.getAxiomCount(ax, EXCLUDED);
                }
                o1.logicalAxioms().forEach(this::consume);
                o1.getLogicalAxiomCount();
                for (OWLAxiom ax : o1.logicalAxioms().toList()) {
                    assert ax != null;
                    o1.containsAxiom(ax);
                    o1.containsAxiom(ax, INCLUDED, IGNORE_AXIOM_ANNOTATIONS);
                    o1.containsAxiom(ax, EXCLUDED, IGNORE_AXIOM_ANNOTATIONS);
                }
                for (OWLAxiom ax : o1.logicalAxioms().toList()) {
                    assert ax != null;
                    o1.containsAxiom(ax, EXCLUDED, IGNORE_AXIOM_ANNOTATIONS);
                    o1.containsAxiom(ax, INCLUDED, IGNORE_AXIOM_ANNOTATIONS);
                    o1.containsAxiom(ax, EXCLUDED, IGNORE_AXIOM_ANNOTATIONS);
                }
                for (OWLAxiom ax : o1.logicalAxioms().toList()) {
                    assert ax != null;
                    o1.axiomsIgnoreAnnotations(ax, EXCLUDED).forEach(this::consume);
                    o1.axiomsIgnoreAnnotations(ax, INCLUDED).forEach(this::consume);
                    o1.axiomsIgnoreAnnotations(ax, EXCLUDED).forEach(this::consume);
                }
                o1.generalClassAxioms().forEach(this::consume);
                anonIndividuals.forEach(individual -> o1.referencingAxioms(individual, EXCLUDED)
                    .forEach(this::consume));
                o1.signature().forEach(entity -> {
                    assert entity != null;
                    o1.referencingAxioms(entity, EXCLUDED).forEach(this::consume);
                    o1.referencingAxioms(entity, INCLUDED).forEach(this::consume);
                    o1.referencingAxioms(entity, EXCLUDED).forEach(this::consume);
                    o1.declarationAxioms(entity).forEach(this::consume);
                    o1.containsEntityInSignature(entity, INCLUDED);
                    o1.containsEntityInSignature(entity, EXCLUDED);
                    o1.containsEntityInSignature(entity);
                    o1.containsEntityInSignature(entity.getIRI(), EXCLUDED);
                    o1.containsEntityInSignature(entity.getIRI(), INCLUDED);
                    o1.entitiesInSignature(entity.getIRI()).forEach(this::consume);
                    o1.entitiesInSignature(entity.getIRI(), EXCLUDED).forEach(this::consume);
                    o1.entitiesInSignature(entity.getIRI(), INCLUDED).forEach(this::consume);
                    o1.isDeclared(entity);
                    o1.isDeclared(entity, INCLUDED);
                    o1.isDeclared(entity, EXCLUDED);
                    if (entity instanceof OWLAnnotationSubject) {
                        o1.annotationAssertionAxioms((OWLAnnotationSubject) entity)
                            .forEach(this::consume);
                    }
                });
                List<OWLAxiom> axioms = o1.axioms().toList();
                for (OWLAxiom ax : axioms) {
                    o1.add(ax);
                    o2.remove(ax);
                }
            }
        }
    }

    @Test
    void testLockingOwlOntologyImpl() {
        OWLOntology o = loadFrom(TestFiles.KOALA, new RDFXMLDocumentFormat());
        MultiThreadChecker checker = new MultiThreadChecker(5);
        checker.check(new TestCallback(o, createAnon()));
        String trace = checker.getTrace();
        System.out.println(trace);
    }

    static class MultiThreadChecker {

        public static final int defaultRep = 10;
        protected int rep = defaultRep;
        protected final PrintStream p;
        private final ByteArrayOutputStream out = new ByteArrayOutputStream();
        private boolean successful = false;

        public MultiThreadChecker(int i) {
            this();
            if (i > 0) {
                rep = i;
            }
        }

        public MultiThreadChecker() {
            p = new PrintStream(out);
        }

        public void check(Runnable cb) {
            AtomicLong counter = new AtomicLong(0);
            final long start = System.currentTimeMillis();
            ExecutorService service = Executors.newFixedThreadPool(rep);
            List<Callable<Object>> list = new ArrayList<>();
            for (int index = 0; index < rep * rep; index++) {
                list.add(() -> {
                    try {
                        cb.run();
                        counter.incrementAndGet();
                    } catch (Throwable ex) {
                        ex.printStackTrace(p);
                        printout(start, counter);
                    }
                    return null;
                });
            }
            long end = System.currentTimeMillis();
            try {
                service.invokeAll(list);
                end = System.currentTimeMillis() - end;
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            printout(end, counter);
        }

        protected void printout(long end, AtomicLong counter) {
            long expected = rep * rep;
            p.println("elapsed time (ms): " + end + "\nSuccessful threads: " + counter.get()
                + "\t expected: " + expected);
            successful = counter.get() == expected;
        }

        public boolean isSuccessful() {
            return successful;
        }

        public String getTrace() {
            p.flush();
            return out.toString();
        }
    }
}
