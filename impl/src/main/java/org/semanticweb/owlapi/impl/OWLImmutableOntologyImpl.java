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
package org.semanticweb.owlapi.impl;

import static org.semanticweb.owlapi.utilities.OWLAPIPreconditions.checkNotNull;
import static org.semanticweb.owlapi.utilities.OWLAPIPreconditions.verifyNotNull;
import static org.semanticweb.owlapi.utilities.OWLAPIStreamUtils.asSet;
import static org.semanticweb.owlapi.utilities.OWLAPIStreamUtils.empty;
import static org.semanticweb.owlapi.utilities.OWLAPIStreamUtils.streamFromSorted;

import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import org.semanticweb.owlapi.documents.IRIDocumentTarget;
import org.semanticweb.owlapi.documents.StreamDocumentTarget;
import org.semanticweb.owlapi.io.OWLOntologyDocumentTarget;
import org.semanticweb.owlapi.io.OWLStorer;
import org.semanticweb.owlapi.io.OWLStorerFactory;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.HasDatatypesInSignature;
import org.semanticweb.owlapi.model.HasSignature;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.ImmutableOWLOntologyChangeException;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLAxiomCollection;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDatatypeDefinitionAxiom;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointUnionAxiom;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLHasKeyAxiom;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLIndividualAxiom;
import org.semanticweb.owlapi.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLIrreflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLObjectVisitorEx;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLPrimitive;
import org.semanticweb.owlapi.model.OWLReflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OntologyConfigurator;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.model.parameters.AxiomAnnotations;
import org.semanticweb.owlapi.model.parameters.ChangeApplied;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.model.parameters.Navigation;
import org.semanticweb.owlapi.utilities.OWLAPIStreamUtils;
import org.semanticweb.owlapi.utilities.OWLAxiomSearchFilter;
import org.semanticweb.owlapi.utilities.PrefixManagerImpl;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import com.github.benmanes.caffeine.cache.LoadingCache;

/**
 * @author Matthew Horridge, The University Of Manchester, Bio-Health Informatics Group
 * @since 2.0.0
 */
public class OWLImmutableOntologyImpl extends OWLAxiomIndexImpl implements OWLOntology {

    // @formatter:off
    protected static LoadingCache<OWLImmutableOntologyImpl, Set<OWLEntity>>              ontsignatures =                     build(OWLImmutableOntologyImpl::build);
    protected static LoadingCache<OWLImmutableOntologyImpl, List<OWLAnonymousIndividual>> ontanonCaches =                    build(key -> key.ints.owlAnonymousIndividualReferences  .keySet().distinct().sorted().toList());
    protected static LoadingCache<OWLImmutableOntologyImpl, List<OWLClass>>              ontclassesSignatures =              build(key -> key.ints.owlClassReferences                .keySet().distinct().sorted().toList());
    protected static LoadingCache<OWLImmutableOntologyImpl, List<OWLDataProperty>>       ontdataPropertySignatures =         build(key -> key.ints.owlDataPropertyReferences         .keySet().distinct().sorted().toList());
    protected static LoadingCache<OWLImmutableOntologyImpl, List<OWLObjectProperty>>     ontobjectPropertySignatures =       build(key -> key.ints.owlObjectPropertyReferences       .keySet().distinct().sorted().toList());
    protected static LoadingCache<OWLImmutableOntologyImpl, List<OWLDatatype>>           ontdatatypeSignatures =             build(key -> Stream.concat(key.ints.owlDatatypeReferences.keySet(), key.ints.getOntologyAnnotations().flatMap(HasDatatypesInSignature::datatypesInSignature)).distinct().sorted().toList());
    protected static LoadingCache<OWLImmutableOntologyImpl, List<OWLNamedIndividual>>    ontindividualSignatures =           build(key -> key.ints.owlIndividualReferences           .keySet().distinct().sorted().toList());
    protected static LoadingCache<OWLImmutableOntologyImpl, List<OWLAnnotationProperty>> ontannotationPropertiesSignatures = build(key -> Stream.concat(key.ints.annotationProperties(), key.ints.getOntologyAnnotations().flatMap(OWLAnnotation::annotationPropertiesInSignature)).distinct().sorted().toList());
    // @formatter:on
    protected static void invalidateOntologyCaches(OWLImmutableOntologyImpl o) {
        ontsignatures.invalidate(o);
        ontanonCaches.invalidate(o);
        ontclassesSignatures.invalidate(o);
        ontdataPropertySignatures.invalidate(o);
        ontobjectPropertySignatures.invalidate(o);
        ontdatatypeSignatures.invalidate(o);
        ontindividualSignatures.invalidate(o);
        ontannotationPropertiesSignatures.invalidate(o);
    }

    private static Set<OWLEntity> build(OWLImmutableOntologyImpl key) {
        Stream<OWLEntity> stream =
            Stream.of(key.classesInSignature(), key.objectPropertiesInSignature(),
                key.dataPropertiesInSignature(), key.individualsInSignature(),
                key.datatypesInSignature(), key.annotationPropertiesInSignature(),
                key.annotations().flatMap(OWLAnnotation::signature)).flatMap(x -> x);
        return asSet(stream.distinct().sorted());
    }

    @Nullable
    protected OWLOntologyManager manager;
    protected OWLDataFactory df;
    protected OWLOntologyID ontologyID;
    private PrefixManager prefixManager;
    protected OntologyConfigurator config;

    /**
     * @param manager ontology manager
     * @param ontologyID ontology id
     * @param config ontology configurator
     */
    public OWLImmutableOntologyImpl(OWLOntologyManager manager, OWLOntologyID ontologyID,
        OntologyConfigurator config) {
        this.manager = checkNotNull(manager, "manager cannot be null");
        this.ontologyID = checkNotNull(ontologyID, "ontologyID cannot be null");
        df = manager.getOWLDataFactory();
        prefixManager = new PrefixManagerImpl();
        this.config = new OntologyConfigurator(config);
    }

    @Override
    public OntologyConfigurator getOntologyConfigurator() {
        return config;
    }

    @Override
    public void setOntologyConfigurator(OntologyConfigurator configurator) {
        config = configurator;
    }

    @Override
    public PrefixManager getPrefixManager() {
        return prefixManager;
    }

    @Override
    public void setPrefixManager(PrefixManager prefixManager) {
        this.prefixManager = verifyNotNull(prefixManager, "prefixManager cannot be null");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(100);
        sb.append("Ontology(").append(ontologyID).append(") [Axioms: ").append(ints.getAxiomCount())
            .append(" Logical Axioms: ").append(ints.getLogicalAxiomCount())
            .append("] First 20 axioms: {");
        ints.getAxioms().limit(20).forEach(a -> sb.append(a).append(' '));
        sb.append('}');
        return sb.toString();
    }

    @Override
    public OWLOntologyManager getOWLOntologyManager() {
        OWLOntologyManager m = manager;
        if (m == null) {
            throw new IllegalStateException("Manager on ontology " + getOntologyID()
                + " is null; the ontology is no longer associated to a manager. Ensure the ontology is not being used after being removed from its manager.");
        }
        return verifyNotNull(m, "manager cannot be null at this stage");
    }

    @Override
    public void setOWLOntologyManager(@Nullable OWLOntologyManager manager) {
        this.manager = manager;
    }

    @Override
    public OWLOntologyID getOntologyID() {
        return ontologyID;
    }

    @Override
    public boolean isAnonymous() {
        return ontologyID.isAnonymous();
    }

    @Override
    public boolean isEmpty() {
        return ints.isEmpty();
    }

    @Override
    public <T extends OWLAxiom> int getAxiomCount(AxiomType<T> axiomType) {
        return ints.getAxiomCount(axiomType);
    }

    @Override
    public int getAxiomCount() {
        return ints.getAxiomCount();
    }

    @Override
    public boolean containsAxiom(OWLAxiom axiom) {
        return Internals.contains(ints.getAxiomsByType(), axiom.getAxiomType(), axiom);
    }

    @Override
    public Stream<OWLAxiom> axioms() {
        return ints.getAxioms();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends OWLAxiom> Stream<T> axioms(AxiomType<T> axiomType) {
        return (Stream<T>) ints.getAxiomsByType().getValues(axiomType);
    }

    @Override
    public Stream<OWLLogicalAxiom> logicalAxioms() {
        return ints.getLogicalAxioms();
    }

    @Override
    public int getLogicalAxiomCount() {
        return ints.getLogicalAxiomCount();
    }

    @Override
    public <T extends OWLAxiom> int getAxiomCount(AxiomType<T> axiomType, Imports imports) {
        return imports.stream(this).mapToInt(o -> o.getAxiomCount(axiomType)).sum();
    }

    @Override
    public int getAxiomCount(Imports imports) {
        return imports.stream(this).mapToInt(OWLAxiomCollection::getAxiomCount).sum();
    }

    @Override
    public Stream<OWLAxiom> tboxAxioms(Imports imports) {
        return AxiomType.tboxAxiomTypes().stream().flatMap(t -> axioms(t, imports));
    }

    @Override
    public Stream<OWLAxiom> aboxAxioms(Imports imports) {
        return AxiomType.aboxAxiomTypes().stream().flatMap(t -> axioms(t, imports));
    }

    @Override
    public Stream<OWLAxiom> rboxAxioms(Imports imports) {
        return AxiomType.rboxAxiomTypes().stream().flatMap(t -> axioms(t, imports));
    }

    @Override
    public int getLogicalAxiomCount(Imports imports) {
        return imports.stream(this).mapToInt(OWLAxiomCollection::getLogicalAxiomCount).sum();
    }

    @Override
    public Stream<OWLAnnotation> annotations() {
        return ints.getOntologyAnnotations().sorted();
    }

    @Override
    public List<OWLAnnotation> annotationsAsList() {
        return annotations().toList();
    }

    @Override
    public Stream<OWLClassAxiom> generalClassAxioms() {
        return ints.getGeneralClassAxioms();
    }

    @Override
    public boolean containsAxiom(OWLAxiom axiom, Imports imports,
        AxiomAnnotations ignoreAnnotations) {
        return imports.stream(this).anyMatch(o -> ignoreAnnotations.contains(o, axiom));
    }

    @Override
    public Stream<OWLAxiom> axiomsIgnoreAnnotations(OWLAxiom axiom) {
        return axioms(axiom.getAxiomType()).map(OWLAxiom.class::cast)
            .filter(ax -> ax.equalsIgnoreAnnotations(axiom));
    }

    @Override
    public boolean containsAxiomIgnoreAnnotations(OWLAxiom axiom) {
        if (containsAxiom(axiom)) {
            return true;
        }
        return axioms(axiom.getAxiomType()).anyMatch(ax -> ax.equalsIgnoreAnnotations(axiom));
    }

    @Override
    public Stream<OWLAxiom> axiomsIgnoreAnnotations(OWLAxiom axiom, Imports imports) {
        return imports.stream(this).flatMap(o -> o.axiomsIgnoreAnnotations(axiom));
    }

    @Override
    public boolean containsClassInSignature(IRI iri, Imports imports) {
        return imports.stream(this).anyMatch(o -> o.containsClassInSignature(iri));
    }

    @Override
    public boolean containsObjectPropertyInSignature(IRI iri, Imports imports) {
        return imports.stream(this).anyMatch(o -> o.containsObjectPropertyInSignature(iri));
    }

    @Override
    public boolean containsDataPropertyInSignature(IRI iri, Imports imports) {
        return imports.stream(this).anyMatch(o -> o.containsDataPropertyInSignature(iri));
    }

    @Override
    public boolean containsAnnotationPropertyInSignature(IRI iri, Imports imports) {
        boolean result =
            imports.stream(this).anyMatch(o -> o.containsAnnotationPropertyInSignature(iri));
        if (result) {
            return result;
        }
        return checkOntologyAnnotations(df.getOWLAnnotationProperty(iri));
    }

    private boolean checkOntologyAnnotations(OWLAnnotationProperty p) {
        return ints.getOntologyAnnotations().anyMatch(ann -> ann.getProperty().equals(p));
    }

    @Override
    public boolean containsIndividualInSignature(IRI iri, Imports imports) {
        return imports.stream(this).anyMatch(o -> o.containsIndividualInSignature(iri));
    }

    @Override
    public boolean containsDatatypeInSignature(IRI iri, Imports imports) {
        return imports.stream(this).anyMatch(o -> o.containsDatatypeInSignature(iri));
    }

    @Override
    public boolean containsEntitiesOfTypeInSignature(EntityType<?> type) {
        return ints.anyEntities(type);
    }

    @Override
    public Stream<OWLEntity> entitiesInSignature(IRI iri) {
        // XXX cache?
        return unsortedSignature().filter(c -> c.getIRI().equals(iri)).sorted();
    }

    @Override
    public Stream<OWLEntity> unsortedSignature() {
        return Stream
            .of(ints.owlClassReferences.keySet(), ints.owlObjectPropertyReferences.keySet(),
                ints.owlDataPropertyReferences.keySet(), ints.owlIndividualReferences.keySet(),
                ints.owlDatatypeReferences.keySet(), ints.owlAnnotationPropertyReferences.keySet(),
                ints.getOntologyAnnotations().map(OWLAnnotation::getProperty))
            .flatMap(Function.identity());
    }

    @Override
    public Set<IRI> getPunnedIRIs(Imports includeImportsClosure) {
        return OWLOntology.getPunnedIRIs(
            includeImportsClosure.stream(this).flatMap(HasSignature::unsortedSignature));
    }

    @Override
    public boolean isDeclared(OWLEntity owlEntity) {
        return ints.isDeclared(owlEntity);
    }

    @Override
    public boolean containsEntityInSignature(OWLEntity owlEntity) {
        // Do not use the cached signature if it has not been created already.
        // Creating the cache while this method is called during updates leads to very expensive
        // lookups.
        Set<OWLEntity> set = ontsignatures.getIfPresent(this);
        if (set == null) {
            BiPredicate<Internals, OWLEntity> c =
                CONTAINMENT_PREDICATES.get(owlEntity.getEntityType());
            if (c != null && c.test(ints, owlEntity)) {
                return true;
            }
            return annotations().flatMap(OWLAnnotation::signature).anyMatch(owlEntity::equals);
        }
        return set.contains(owlEntity);
    }

    private static final Map<EntityType<?>, BiPredicate<Internals, OWLEntity>> CONTAINMENT_PREDICATES =
        containmentPredicates();

    private static Map<EntityType<?>, BiPredicate<Internals, OWLEntity>> containmentPredicates() {
        Map<EntityType<?>, BiPredicate<Internals, OWLEntity>> map = new HashMap<>();
        map.put(EntityType.CLASS, (i, e) -> i.containsClassInSignature(e.asOWLClass()));
        map.put(EntityType.OBJECT_PROPERTY,
            (i, e) -> i.containsObjectPropertyInSignature(e.asOWLObjectProperty()));
        map.put(EntityType.DATA_PROPERTY,
            (i, e) -> i.containsDataPropertyInSignature(e.asOWLDataProperty()));
        map.put(EntityType.NAMED_INDIVIDUAL,
            (i, e) -> i.containsIndividualInSignature(e.asOWLNamedIndividual()));
        map.put(EntityType.DATATYPE, (i, e) -> i.containsDatatypeInSignature(e.asOWLDatatype()));
        map.put(EntityType.ANNOTATION_PROPERTY,
            (i, e) -> i.containsAnnotationPropertyInSignature(e.asOWLAnnotationProperty()));
        return map;
    }

    @Override
    public Stream<OWLEntity> signature() {
        return streamFromSorted(verifyNotNull(ontsignatures.get(this)));
    }

    @Override
    public Stream<OWLAnonymousIndividual> anonymousIndividuals() {
        return streamFromSorted(verifyNotNull(ontanonCaches.get(this)));
    }

    @Override
    public Stream<OWLClass> classesInSignature() {
        return streamFromSorted(verifyNotNull(ontclassesSignatures.get(this)));
    }

    @Override
    public Stream<OWLDataProperty> dataPropertiesInSignature() {
        return streamFromSorted(verifyNotNull(ontdataPropertySignatures.get(this)));
    }

    @Override
    public Stream<OWLObjectProperty> objectPropertiesInSignature() {
        return streamFromSorted(verifyNotNull(ontobjectPropertySignatures.get(this)));
    }

    @Override
    public Stream<OWLNamedIndividual> individualsInSignature() {
        return streamFromSorted(verifyNotNull(ontindividualSignatures.get(this)));
    }

    @Override
    public Stream<OWLDatatype> datatypesInSignature() {
        return streamFromSorted(verifyNotNull(ontdatatypeSignatures.get(this)));
    }

    @Override
    public Stream<OWLAnonymousIndividual> referencedAnonymousIndividuals() {
        return anonymousIndividuals();
    }

    @Override
    public Stream<OWLAnnotationProperty> annotationPropertiesInSignature() {
        return streamFromSorted(verifyNotNull(ontannotationPropertiesSignatures.get(this)));
    }

    @Override
    public Stream<OWLImportsDeclaration> importsDeclarations() {
        return ints.getImportsDeclarations();
    }

    @Override
    public Stream<IRI> directImportsDocuments() {
        return ints.getImportsDeclarations().map(OWLImportsDeclaration::getIRI);
    }

    @Override
    public Stream<OWLOntology> imports() {
        return getOWLOntologyManager().imports(this);
    }

    @Override
    public Stream<OWLOntology> directImports() {
        return getOWLOntologyManager().directImports(this);
    }

    @Override
    public Stream<OWLOntology> importsClosure() {
        return getOWLOntologyManager().importsClosure(this);
    }

    @Override
    public Stream<OWLClassAxiom> axioms(OWLClass cls) {
        return ints.classAxiomsByClass.values(cls, OWLClassAxiom.class);
    }

    @Override
    public Stream<OWLObjectPropertyAxiom> axioms(OWLObjectPropertyExpression property) {
        return Stream.of(asymmetricObjectPropertyAxioms(property),
            reflexiveObjectPropertyAxioms(property), symmetricObjectPropertyAxioms(property),
            irreflexiveObjectPropertyAxioms(property), transitiveObjectPropertyAxioms(property),
            inverseFunctionalObjectPropertyAxioms(property),
            functionalObjectPropertyAxioms(property), inverseObjectPropertyAxioms(property),
            objectPropertyDomainAxioms(property), equivalentObjectPropertiesAxioms(property),
            disjointObjectPropertiesAxioms(property), objectPropertyRangeAxioms(property),
            objectSubPropertyAxiomsForSubProperty(property)).flatMap(x -> x);
    }

    @Override
    public Stream<OWLDataPropertyAxiom> axioms(OWLDataProperty property) {
        return Stream.of(dataPropertyDomainAxioms(property),
            equivalentDataPropertiesAxioms(property), disjointDataPropertiesAxioms(property),
            dataPropertyRangeAxioms(property), functionalDataPropertyAxioms(property),
            dataSubPropertyAxiomsForSubProperty(property)).flatMap(x -> x);
    }

    @Override
    public Stream<OWLIndividualAxiom> axioms(OWLIndividual individual) {
        return Stream.of(classAssertionAxioms(individual),
            objectPropertyAssertionAxioms(individual), dataPropertyAssertionAxioms(individual),
            negativeObjectPropertyAssertionAxioms(individual),
            negativeDataPropertyAssertionAxioms(individual), sameIndividualAxioms(individual),
            differentIndividualAxioms(individual)).flatMap(x -> x);
    }

    @Override
    public Stream<OWLDatatypeDefinitionAxiom> axioms(OWLDatatype datatype) {
        return datatypeDefinitions(datatype);
    }

    @Override
    public Stream<OWLAxiom> referencingAxioms(OWLPrimitive owlEntity) {
        if (owlEntity instanceof OWLEntity e) {
            return ints.getReferencingAxioms(e);
        } else if (owlEntity instanceof OWLAnonymousIndividual i) {
            return ints.owlAnonymousIndividualReferences.values(i, OWLAxiom.class);
        } else if (owlEntity.isIRI()) {
            Set<OWLAxiom> axioms = new HashSet<>();
            String iriString = owlEntity.toString();
            // axioms referring entities with this IRI, data property assertions
            // with IRI as subject, annotations with IRI as subject or object.
            entitiesInSignature((IRI) owlEntity)
                .forEach(e -> OWLAPIStreamUtils.add(axioms, referencingAxioms(e)));
            axioms(AxiomType.DATA_PROPERTY_ASSERTION)
                .filter(ax -> OWL2Datatype.XSD_ANY_URI.matches(ax.getObject().getDatatype()))
                .filter(ax -> ax.getObject().getLiteral().equals(iriString)).forEach(axioms::add);
            axioms(AxiomType.ANNOTATION_ASSERTION)
                .forEach(ax -> examineAssertion(owlEntity, axioms, ax));
            axioms(AxiomType.ANNOTATION_PROPERTY_DOMAIN)
                .forEach(ax -> examineDomain(owlEntity, axioms, ax));
            axioms(AxiomType.ANNOTATION_PROPERTY_RANGE)
                .forEach(ax -> examineRange(owlEntity, axioms, ax));
            axioms().filter(OWLAxiom::isAnnotated)
                .forEach(ax -> examineAnnotations(owlEntity, axioms, ax));
            return axioms.stream();
        } else if (owlEntity instanceof OWLLiteral l) {
            Set<OWLAxiom> axioms = new HashSet<>();
            FindLiterals v = new FindLiterals(l);
            AxiomType.axiomTypes().stream().flatMap(this::axioms)
                .filter(ax -> ax.accept(v).booleanValue()).forEach(axioms::add);
            return axioms.stream();
        }
        return empty();
    }

    private static class FindLiterals implements OWLObjectVisitorEx<Boolean> {
        private final OWLLiteral value;

        public FindLiterals(OWLLiteral value) {
            this.value = value;
        }

        @Override
        public Boolean doDefault(OWLObject object) {
            return processStream(object.componentsAnnotationsFirst());
        }

        @Override
        public Boolean visit(OWLAnnotation node) {
            if (node.getValue().equals(value)) {
                return Boolean.TRUE;
            }
            return node.annotations().map(x -> x.accept(this)).filter(Boolean.TRUE::equals)
                .findAny().orElse(Boolean.FALSE);
        }

        protected Boolean processStream(Stream<?> s) {
            return Boolean.valueOf(s.map(o -> switch (o) {
                case OWLObject obj -> obj.accept(this);
                case Stream<?> st -> processStream(st);
                case Collection<?> c -> processStream(c.stream());
                default -> Boolean.FALSE;
            }).anyMatch(Boolean.TRUE::equals));
        }

        @Override
        public Boolean visit(OWLLiteral node) {
            return Boolean.valueOf(node.equals(value));
        }
    }

    protected boolean hasLiteralInAnnotations(OWLPrimitive owlEntity, OWLAxiom ax) {
        return ax.annotations().anyMatch(a -> a.getValue().equals(owlEntity));
    }

    protected void examineAssertion(OWLPrimitive owlEntity, Set<OWLAxiom> axioms,
        OWLAnnotationAssertionAxiom ax) {
        if (ax.getSubject().equals(owlEntity)) {
            axioms.add(ax);
        } else {
            if (ax.annotationValue().equals(owlEntity)) {
                axioms.add(ax);
            } else {
                ax.getValue().asLiteral().ifPresent(lit -> {
                    if (OWL2Datatype.XSD_ANY_URI.matches(lit.getDatatype())
                        && lit.getLiteral().equals(owlEntity.toString())) {
                        axioms.add(ax);
                    }
                });
            }
        }
    }

    protected void examineDomain(OWLPrimitive owlEntity, Set<OWLAxiom> axioms,
        OWLAnnotationPropertyDomainAxiom ax) {
        if (ax.getDomain().equals(owlEntity)) {
            axioms.add(ax);
        }
    }

    protected void examineRange(OWLPrimitive owlEntity, Set<OWLAxiom> axioms,
        OWLAnnotationPropertyRangeAxiom ax) {
        if (ax.getRange().equals(owlEntity)) {
            axioms.add(ax);
        }
    }

    protected void examineAnnotations(OWLPrimitive owlEntity, Set<OWLAxiom> axioms, OWLAxiom ax) {
        if (recurse(ax.annotations(), owlEntity::equals)) {
            axioms.add(ax);
        }
    }

    private static boolean recurse(Stream<OWLAnnotation> s, Predicate<OWLAnnotationValue> p) {
        return s.anyMatch(a -> p.test(a.annotationValue()) || recurse(a.annotations(), p));
    }

    // OWLAxiomIndex
    @Override
    public Stream<OWLAnnotationAssertionAxiom> annotationAssertionAxioms(
        OWLAnnotationSubject entity, Imports imports) {
        return imports.stream(this).flatMap(o -> o.annotationAssertionAxioms(entity));
    }

    @Override
    public Stream<OWLAnnotationAssertionAxiom> annotationAssertionAxioms(
        OWLAnnotationSubject entity) {
        return ints.annotationAssertionAxiomsBySubject.getValues(entity);
    }

    @Override
    public Stream<OWLAsymmetricObjectPropertyAxiom> asymmetricObjectPropertyAxioms(
        OWLObjectPropertyExpression property) {
        return ints.asymmetricPropertyAxiomsByProperty.getValues(property);
    }

    @Override
    public Stream<OWLClassAssertionAxiom> classAssertionAxioms(OWLClassExpression ce) {
        if (ce.isNamed()) {
            return ints.classAssertionAxiomsByClass.getValues(ce);
        }
        return ints.axiomsByType.getValues(AxiomType.CLASS_ASSERTION)
            .map(OWLClassAssertionAxiom.class::cast).filter(x -> x.getClassExpression().equals(ce));
    }

    @Override
    public Stream<OWLClassAssertionAxiom> classAssertionAxioms(OWLIndividual individual) {
        return ints.classAssertionAxiomsByIndividual.getValues(individual);
    }

    @Override
    public Stream<OWLDataPropertyAssertionAxiom> dataPropertyAssertionAxioms(
        OWLIndividual individual) {
        return ints.dataPropertyAssertionsByIndividual.getValues(individual);
    }

    @Override
    public Stream<OWLDataPropertyDomainAxiom> dataPropertyDomainAxioms(OWLDataProperty property) {
        return ints.dataPropertyDomainAxiomsByProperty.getValues(property);
    }

    @Override
    public Stream<OWLDataPropertyRangeAxiom> dataPropertyRangeAxioms(OWLDataProperty property) {
        return ints.dataPropertyRangeAxiomsByProperty.getValues(property);
    }

    @Override
    public Stream<OWLSubDataPropertyOfAxiom> dataSubPropertyAxiomsForSubProperty(
        OWLDataProperty subProperty) {
        return ints.dataSubPropertyAxiomsBySubPosition.getValues(subProperty);
    }

    @Override
    public Stream<OWLSubDataPropertyOfAxiom> dataSubPropertyAxiomsForSuperProperty(
        OWLDataPropertyExpression superProperty) {
        return ints.dataSubPropertyAxiomsBySuperPosition.getValues(superProperty);
    }

    @Override
    public Stream<OWLDifferentIndividualsAxiom> differentIndividualAxioms(
        OWLIndividual individual) {
        return ints.differentIndividualsAxiomsByIndividual.getValues(individual);
    }

    @Override
    public Stream<OWLDisjointClassesAxiom> disjointClassesAxioms(OWLClass cls) {
        return ints.disjointClassesAxiomsByClass.getValues(cls);
    }

    @Override
    public Stream<OWLDisjointDataPropertiesAxiom> disjointDataPropertiesAxioms(
        OWLDataProperty property) {
        return ints.disjointDataPropertyAxiomsByProperty.getValues(property);
    }

    @Override
    public Stream<OWLDisjointObjectPropertiesAxiom> disjointObjectPropertiesAxioms(
        OWLObjectPropertyExpression property) {
        return ints.disjointObjectPropertyAxiomsByProperty.getValues(property);
    }

    @Override
    public Stream<OWLDisjointUnionAxiom> disjointUnionAxioms(OWLClass owlClass) {
        return ints.disjointUnionAxiomsByClass.getValues(owlClass);
    }

    @Override
    public Stream<OWLEquivalentClassesAxiom> equivalentClassesAxioms(OWLClass cls) {
        return ints.equivalentClassesAxiomsByClass.getValues(cls);
    }

    @Override
    public Stream<OWLEquivalentDataPropertiesAxiom> equivalentDataPropertiesAxioms(
        OWLDataProperty property) {
        return ints.equivalentDataPropertyAxiomsByProperty.getValues(property);
    }

    @Override
    public Stream<OWLEquivalentObjectPropertiesAxiom> equivalentObjectPropertiesAxioms(
        OWLObjectPropertyExpression property) {
        return ints.equivalentObjectPropertyAxiomsByProperty.getValues(property);
    }

    @Override
    public Stream<OWLFunctionalDataPropertyAxiom> functionalDataPropertyAxioms(
        OWLDataPropertyExpression property) {
        return ints.functionalDataPropertyAxiomsByProperty.getValues(property);
    }

    @Override
    public Stream<OWLFunctionalObjectPropertyAxiom> functionalObjectPropertyAxioms(
        OWLObjectPropertyExpression property) {
        return ints.functionalObjectPropertyAxiomsByProperty.getValues(property);
    }

    @Override
    public Stream<OWLHasKeyAxiom> hasKeyAxioms(OWLClass cls) {
        return ints.hasKeyAxiomsByClass.getValues(cls);
    }

    @Override
    public Stream<OWLInverseFunctionalObjectPropertyAxiom> inverseFunctionalObjectPropertyAxioms(
        OWLObjectPropertyExpression property) {
        return ints.inverseFunctionalPropertyAxiomsByProperty.getValues(property);
    }

    @Override
    public Stream<OWLInverseObjectPropertiesAxiom> inverseObjectPropertyAxioms(
        OWLObjectPropertyExpression property) {
        return ints.inversePropertyAxiomsByProperty.getValues(property);
    }

    @Override
    public Stream<OWLIrreflexiveObjectPropertyAxiom> irreflexiveObjectPropertyAxioms(
        OWLObjectPropertyExpression property) {
        return ints.irreflexivePropertyAxiomsByProperty.getValues(property);
    }

    @Override
    public Stream<OWLNegativeDataPropertyAssertionAxiom> negativeDataPropertyAssertionAxioms(
        OWLIndividual individual) {
        return ints.negativeDataPropertyAssertionAxiomsByIndividual.getValues(individual);
    }

    @Override
    public Stream<OWLNegativeObjectPropertyAssertionAxiom> negativeObjectPropertyAssertionAxioms(
        OWLIndividual individual) {
        return ints.negativeObjectPropertyAssertionAxiomsByIndividual.getValues(individual);
    }

    @Override
    public Stream<OWLObjectPropertyAssertionAxiom> objectPropertyAssertionAxioms(
        OWLIndividual individual) {
        return ints.objectPropertyAssertionsByIndividual.getValues(individual);
    }

    @Override
    public Stream<OWLObjectPropertyDomainAxiom> objectPropertyDomainAxioms(
        OWLObjectPropertyExpression property) {
        return ints.objectPropertyDomainAxiomsByProperty.getValues(property);
    }

    @Override
    public Stream<OWLObjectPropertyRangeAxiom> objectPropertyRangeAxioms(
        OWLObjectPropertyExpression property) {
        return ints.objectPropertyRangeAxiomsByProperty.getValues(property);
    }

    @Override
    public Stream<OWLSubObjectPropertyOfAxiom> objectSubPropertyAxiomsForSubProperty(
        OWLObjectPropertyExpression subProperty) {
        return ints.objectSubPropertyAxiomsBySubPosition.getValues(subProperty);
    }

    @Override
    public Stream<OWLSubObjectPropertyOfAxiom> objectSubPropertyAxiomsForSuperProperty(
        OWLObjectPropertyExpression superProperty) {
        return ints.objectSubPropertyAxiomsBySuperPosition.getValues(superProperty);
    }

    @Override
    public Stream<OWLReflexiveObjectPropertyAxiom> reflexiveObjectPropertyAxioms(
        OWLObjectPropertyExpression property) {
        return ints.reflexivePropertyAxiomsByProperty.getValues(property);
    }

    @Override
    public Stream<OWLSameIndividualAxiom> sameIndividualAxioms(OWLIndividual individual) {
        return ints.sameIndividualsAxiomsByIndividual.getValues(individual);
    }

    @Override
    public Stream<OWLSubClassOfAxiom> subClassAxiomsForSubClass(OWLClass cls) {
        return ints.subClassAxiomsBySubPosition.getValues(cls);
    }

    @Override
    public Stream<OWLSubClassOfAxiom> subClassAxiomsForSuperClass(OWLClass cls) {
        return ints.subClassAxiomsBySuperPosition.getValues(cls);
    }

    @Override
    public Stream<OWLDeclarationAxiom> declarationAxioms(OWLEntity subject) {
        return ints.declarationsByEntity.getValues(subject);
    }

    @Override
    public Stream<OWLSymmetricObjectPropertyAxiom> symmetricObjectPropertyAxioms(
        OWLObjectPropertyExpression property) {
        return ints.symmetricPropertyAxiomsByProperty.getValues(property);
    }

    @Override
    public Stream<OWLTransitiveObjectPropertyAxiom> transitiveObjectPropertyAxioms(
        OWLObjectPropertyExpression property) {
        return ints.transitivePropertyAxiomsByProperty.getValues(property);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <A extends OWLAxiom> Stream<A> axioms(Class<A> type,
        Class<? extends OWLObject> explicitClass, OWLObject entity, Navigation forSubPosition) {
        Optional<MapPointer<OWLObject, A>> optional =
            ints.get((Class<OWLObject>) explicitClass, type, forSubPosition);
        if (optional.isPresent()) {
            return optional.get().values(entity, type);
        }
        if (entity instanceof OWLEntity e) {
            return axioms(AxiomType.getTypeForClass(type))
                .filter(a -> a.containsEntityInSignature(e));
        }
        return empty();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends OWLAxiom> Stream<T> axioms(OWLAxiomSearchFilter filter, Object key,
        Imports imports) {
        return imports.stream(this).flatMap(o -> (Stream<T>) o.axioms(filter, key));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends OWLAxiom> Stream<T> axioms(OWLAxiomSearchFilter filter, Object key) {
        Collection<T> c = (Collection<T>) ints.filterAxioms(filter, key);
        return c.stream();
    }

    @Override
    public boolean contains(OWLAxiomSearchFilter filter, Object key) {
        return ints.contains(filter, key);
    }

    @Override
    public boolean contains(OWLAxiomSearchFilter filter, Object key, Imports imports) {
        return imports.stream(this).anyMatch(o -> o.contains(filter, key));
    }

    @Override
    public boolean containsDatatypeInSignature(IRI iri) {
        return ints.containsDatatypeInSignature(iri);
    }

    @Override
    public boolean containsClassInSignature(IRI iri) {
        return ints.containsClassInSignature(iri);
    }

    @Override
    public boolean containsObjectPropertyInSignature(IRI iri) {
        return ints.containsObjectPropertyInSignature(iri);
    }

    @Override
    public boolean containsDataPropertyInSignature(IRI iri) {
        return ints.containsDataPropertyInSignature(iri);
    }

    @Override
    public boolean containsAnnotationPropertyInSignature(IRI iri) {
        return ints.containsAnnotationPropertyInSignature(iri);
    }

    @Override
    public boolean containsIndividualInSignature(IRI iri) {
        return ints.containsIndividualInSignature(iri);
    }

    @Override
    public ChangeApplied enactChange(OWLOntologyChange change) {
        throw new ImmutableOWLOntologyChangeException(change.getChangeData(),
            getOntologyID().toString());
    }

    @Override
    public void saveOntology(OWLDocumentFormat ontologyFormat,
        OWLOntologyDocumentTarget documentTarget) throws OWLOntologyStorageException {
        for (OWLStorerFactory storerFactory : getOWLOntologyManager().getOntologyStorers()) {
            OWLStorer storer = storerFactory.createStorer();
            // XXX review storer factory interface
            if (storer.canStoreOntology(ontologyFormat)) {
                documentTarget.store(storer, this, ontologyFormat);
                return;
            }
        }
        throw new OWLOntologyStorageException(
            "Could not find an ontology storer which can handle the format: " + ontologyFormat);
    }

    @Override
    public void saveOntology() throws OWLOntologyStorageException {
        saveOntology(getOWLOntologyManager().getNonnullOntologyFormat(this),
            new IRIDocumentTarget(getOWLOntologyManager().getOntologyDocumentIRI(this)));
    }

    @Override
    public void saveOntology(IRI documentIRI) throws OWLOntologyStorageException {
        // XXX attach format to ontology
        // XXX attach document iri to ontology
        saveOntology(getOWLOntologyManager().getNonnullOntologyFormat(this),
            new IRIDocumentTarget(documentIRI));
    }

    @Override
    public void saveOntology(OWLDocumentFormat ontologyFormat) throws OWLOntologyStorageException {
        saveOntology(ontologyFormat,
            new IRIDocumentTarget(getOWLOntologyManager().getOntologyDocumentIRI(this)));
    }

    @Override
    public void saveOntology(OutputStream outputStream) throws OWLOntologyStorageException {
        saveOntology(getOWLOntologyManager().getNonnullOntologyFormat(this),
            new StreamDocumentTarget(outputStream));
    }

    @Override
    public void saveOntology(OWLDocumentFormat ontologyFormat, IRI documentIRI)
        throws OWLOntologyStorageException {
        saveOntology(getOWLOntologyManager().getNonnullOntologyFormat(this),
            new IRIDocumentTarget(documentIRI));
    }

    @Override
    public void saveOntology(OWLDocumentFormat ontologyFormat, OutputStream outputStream)
        throws OWLOntologyStorageException {
        saveOntology(ontologyFormat, new StreamDocumentTarget(outputStream));
    }

    @Override
    public void saveOntology(OWLOntologyDocumentTarget documentTarget)
        throws OWLOntologyStorageException {
        saveOntology(getOWLOntologyManager().getNonnullOntologyFormat(this), documentTarget);
    }
}
