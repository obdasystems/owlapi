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

import static org.semanticweb.owlapi.impl.InternalizedEntities.FALSELITERAL;
import static org.semanticweb.owlapi.impl.InternalizedEntities.LANGSTRING;
import static org.semanticweb.owlapi.impl.InternalizedEntities.OWL_BACKWARD_COMPATIBLE_WITH;
import static org.semanticweb.owlapi.impl.InternalizedEntities.OWL_BOTTOM_DATA_PROPERTY;
import static org.semanticweb.owlapi.impl.InternalizedEntities.OWL_BOTTOM_OBJECT_PROPERTY;
import static org.semanticweb.owlapi.impl.InternalizedEntities.OWL_DEPRECATED;
import static org.semanticweb.owlapi.impl.InternalizedEntities.OWL_INCOMPATIBLE_WITH;
import static org.semanticweb.owlapi.impl.InternalizedEntities.OWL_NOTHING;
import static org.semanticweb.owlapi.impl.InternalizedEntities.OWL_THING;
import static org.semanticweb.owlapi.impl.InternalizedEntities.OWL_TOP_DATA_PROPERTY;
import static org.semanticweb.owlapi.impl.InternalizedEntities.OWL_TOP_OBJECT_PROPERTY;
import static org.semanticweb.owlapi.impl.InternalizedEntities.OWL_VERSION_INFO;
import static org.semanticweb.owlapi.impl.InternalizedEntities.PLAIN;
import static org.semanticweb.owlapi.impl.InternalizedEntities.RDFSLITERAL;
import static org.semanticweb.owlapi.impl.InternalizedEntities.RDFS_COMMENT;
import static org.semanticweb.owlapi.impl.InternalizedEntities.RDFS_IS_DEFINED_BY;
import static org.semanticweb.owlapi.impl.InternalizedEntities.RDFS_LABEL;
import static org.semanticweb.owlapi.impl.InternalizedEntities.RDFS_SEE_ALSO;
import static org.semanticweb.owlapi.impl.InternalizedEntities.TRUELITERAL;
import static org.semanticweb.owlapi.impl.InternalizedEntities.XSDBOOLEAN;
import static org.semanticweb.owlapi.impl.InternalizedEntities.XSDDOUBLE;
import static org.semanticweb.owlapi.impl.InternalizedEntities.XSDFLOAT;
import static org.semanticweb.owlapi.impl.InternalizedEntities.XSDINTEGER;
import static org.semanticweb.owlapi.impl.InternalizedEntities.XSDSTRING;
import static org.semanticweb.owlapi.impl.InternalizedEntities.negativeFloatZero;
import static org.semanticweb.owlapi.utilities.OWLAPIPreconditions.checkIterableNotNull;
import static org.semanticweb.owlapi.utilities.OWLAPIPreconditions.checkNotNull;
import static org.semanticweb.owlapi.utilities.OWLAPIPreconditions.verifyNotNull;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.NodeID;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataComplementOf;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataIntersectionOf;
import org.semanticweb.owlapi.model.OWLDataMaxCardinality;
import org.semanticweb.owlapi.model.OWLDataMinCardinality;
import org.semanticweb.owlapi.model.OWLDataOneOf;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLDataUnionOf;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDatatypeDefinitionAxiom;
import org.semanticweb.owlapi.model.OWLDatatypeRestriction;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointUnionAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLFacetRestriction;
import org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLHasKeyAxiom;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLIrreflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasSelf;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectInverseOf;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLPropertyExpression;
import org.semanticweb.owlapi.model.OWLReflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLRuntimeException;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;
import org.semanticweb.owlapi.model.OWLSubAnnotationPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;
import org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OntologyConfigurator;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.semanticweb.owlapi.model.SWRLBuiltInAtom;
import org.semanticweb.owlapi.model.SWRLClassAtom;
import org.semanticweb.owlapi.model.SWRLDArgument;
import org.semanticweb.owlapi.model.SWRLDataPropertyAtom;
import org.semanticweb.owlapi.model.SWRLDataRangeAtom;
import org.semanticweb.owlapi.model.SWRLDifferentIndividualsAtom;
import org.semanticweb.owlapi.model.SWRLIArgument;
import org.semanticweb.owlapi.model.SWRLIndividualArgument;
import org.semanticweb.owlapi.model.SWRLLiteralArgument;
import org.semanticweb.owlapi.model.SWRLObjectPropertyAtom;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.model.SWRLSameIndividualAtom;
import org.semanticweb.owlapi.model.SWRLVariable;
import org.semanticweb.owlapi.model.parameters.ConfigurationOptions;
import org.semanticweb.owlapi.utilities.OWLAPIStreamUtils;
import org.semanticweb.owlapi.utilities.XMLUtils;
import org.semanticweb.owlapi.utility.VersionInfo;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.OWLFacet;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

/**
 * @author Matthew Horridge, The University Of Manchester, Bio-Health Informatics Group
 * @since 2.0.0
 */
@Singleton
public class OWLDataFactoryImpl implements OWLDataFactory {

    private static final String PREFIX_MANAGER_CANNOT_BE_NULL = "prefixManager cannot be null";
    private static final Logger LOGGER = LoggerFactory.getLogger(OWLDataFactoryImpl.class);
//@formatter:off
    private static final LoadingCache<String, String>                        iriNamespaces        = builder(XMLUtils::getNCNamePrefix);
    private static final Cache<String, IRI>                                  iris                 = Caffeine.newBuilder().maximumSize(size()).build();
    private static final LoadingCache<OWLAnnotation, OWLAnnotation>          annotations          = builder(x -> x);
    private static final LoadingCache<IRI,           OWLClass>               classes              = builder(OWLClassImpl::new);
    private static final LoadingCache<IRI,           OWLObjectProperty>      objectProperties     = builder(OWLObjectPropertyImpl::new);
    private static final LoadingCache<IRI,           OWLDataProperty>        dataProperties       = builder(OWLDataPropertyImpl::new);
    private static final LoadingCache<IRI,           OWLDatatype>            datatypes            = builder(OWLDatatypeImpl::new);
    private static final LoadingCache<IRI,           OWLNamedIndividual>     individuals          = builder(OWLNamedIndividualImpl::new);
    private static final LoadingCache<IRI,           OWLAnnotationProperty>  annotationProperties = builder(OWLAnnotationPropertyImpl::new);
//@formatter:on
    private static final AtomicLong COUNTER = new AtomicLong(System.nanoTime());

    private OntologyConfigurator config;

    /**
     * @param config configuration parameter
     */
    public OWLDataFactoryImpl(OntologyConfigurator config) {
        this.config = config;
    }

    protected static long size() {
        return ConfigurationOptions.CACHE_SIZE.getValue(Integer.class, Collections.emptyMap())
            .longValue();
    }

    /**
     * Defaults to a new Ontologyconfigurator instance
     */
    @Inject
    public OWLDataFactoryImpl() {
        this(new OntologyConfigurator());
    }

    private <T> List<T> sortedList(Class<T> witness, Stream<? extends T> stream) {
        if (config.shouldAllowDuplicatesInConstructSets()) {
            return stream.filter(Objects::nonNull).sorted().map(witness::cast).toList();
        }
        return OWLAPIStreamUtils.sorted(witness, stream);
    }

    @Override
    public void purge() {
        classes.invalidateAll();
        objectProperties.invalidateAll();
        dataProperties.invalidateAll();
        datatypes.invalidateAll();
        individuals.invalidateAll();
        annotationProperties.invalidateAll();
        annotations.invalidateAll();
    }

    @Override
    public OWLOntologyID getOWLOntologyID(@Nullable IRI iri, @Nullable IRI versionIRI) {
        return getOWLOntologyID(opt(iri), opt(versionIRI));
    }

    private Optional<IRI> opt(@Nullable IRI i) {
        if (i == null || NodeID.isAnonymousNodeIRI(i)) {
            return Optional.empty();
        }
        if (!i.isAbsolute()) {
            LOGGER.error(
                "Ontology IRIs must be absolute; IRI {} is relative and will be made absolute by prefixing urn:absolute: to it",
                i);
            return Optional.ofNullable(getIRI("urn:absolute:" + i));
        }
        return Optional.ofNullable(i);
    }

    @Override
    public OWLOntologyID getOWLOntologyID(Optional<IRI> iri, Optional<IRI> version) {
        return new OWLOntologyIDImpl(iri, version);
    }

    @Override
    public IRI getNextDocumentIRI(String prefix) {
        return getIRI(prefix + COUNTER.incrementAndGet());
    }

    @Override
    public IRI getIRI(String str) {
        checkNotNull(str, "str cannot be null");
        IRI cached = iris.getIfPresent(str);
        if (cached != null) {
            return cached;
        }
        int index = XMLUtils.getNCNameSuffixIndex(str);
        IRI created;
        if (index < 0) {
            // no ncname
            created = new IRIImpl(verifyNotNull(iriNamespaces.get(str)), "");
        } else {
            created = getIRI(str.substring(0, index), str.substring(index));
        }
        iris.put(str, created);
        return created;
    }

    @Override
    public IRI getIRI(@Nullable String prefix, @Nullable String suffix) {
        if (prefix == null && suffix == null) {
            throw new IllegalArgumentException("prefix and suffix cannot both be null");
        }
        if (prefix == null) {
            return getIRI(verifyNotNull(suffix));
        }
        if (suffix == null) {
            // suffix set deliberately to null is used only in blank node
            // management
            // this is not great but blank nodes should be changed to not refer
            // to IRIs at all
            // XXX address blank node issues with iris
            return getIRI(prefix);
        }
        int index = XMLUtils.getNCNameSuffixIndex(prefix);
        int test = XMLUtils.getNCNameSuffixIndex(suffix);
        if (index == -1 && test == 0) {
            // the prefix does not contain an ncname character and there is
            // no illegal character in the suffix
            // the split is therefore correct
            // this IRI is not cached; creating the string key would remove the
            // memory advantage
            return new IRIImpl(verifyNotNull(iriNamespaces.get(prefix)), suffix);
        }
        // otherwise the split is wrong; we could obtain the right split by
        // using index and test, but it's just as easy to use the other
        // constructor
        String key = prefix + suffix;
        IRI created = getIRI(key);
        iris.put(key, created);
        return created;
    }

    @Override
    public OWLAnnotationProperty getOWLAnnotationProperty(String iri) {
        return getOWLAnnotationProperty(getIRI(iri));
    }

    @Override
    public OWLAnnotationProperty getOWLAnnotationProperty(String namespace,
        @Nullable String remainder) {
        return getOWLAnnotationProperty(getIRI(namespace, remainder));
    }

    @Override
    public OWLAnnotationProperty getOWLAnnotationProperty(String abbreviatedIRI,
        PrefixManager prefixManager) {
        checkNotNull(abbreviatedIRI, "abbreviatedIRI cannot be null");
        checkNotNull(prefixManager, PREFIX_MANAGER_CANNOT_BE_NULL);
        return getOWLAnnotationProperty(prefixManager.getIRI(abbreviatedIRI, this));
    }

    @Override
    public OWLClass getOWLClass(String iri) {
        return getOWLClass(getIRI(iri));
    }

    @Override
    public OWLClass getOWLClass(String namespace, @Nullable String remainder) {
        return getOWLClass(getIRI(namespace, remainder));
    }

    @Override
    public OWLClass getOWLClass(String abbreviatedIRI, PrefixManager prefixManager) {
        checkNotNull(abbreviatedIRI, "iri cannot be null");
        checkNotNull(prefixManager, PREFIX_MANAGER_CANNOT_BE_NULL);
        return getOWLClass(prefixManager.getIRI(abbreviatedIRI, this));
    }

    @Override
    public OWLDataProperty getOWLDataProperty(String iri) {
        return getOWLDataProperty(getIRI(iri));
    }

    @Override
    public OWLDataProperty getOWLDataProperty(String namespace, @Nullable String remainder) {
        return getOWLDataProperty(getIRI(namespace, remainder));
    }

    @Override
    public OWLDataProperty getOWLDataProperty(String abbreviatedIRI, PrefixManager prefixManager) {
        checkNotNull(abbreviatedIRI, "abbreviatedIRI canno be null");
        checkNotNull(prefixManager, PREFIX_MANAGER_CANNOT_BE_NULL);
        return getOWLDataProperty(prefixManager.getIRI(abbreviatedIRI, this));
    }

    @Override
    public OWLDatatype getOWLDatatype(String iri) {
        return getOWLDatatype(getIRI(iri));
    }

    @Override
    public OWLDatatype getOWLDatatype(String namespace, @Nullable String remainder) {
        return getOWLDatatype(getIRI(namespace, remainder));
    }

    @Override
    public OWLDatatype getOWLDatatype(String abbreviatedIRI, PrefixManager prefixManager) {
        checkNotNull(abbreviatedIRI, "abbreviatedIRI cannot be null");
        checkNotNull(prefixManager, PREFIX_MANAGER_CANNOT_BE_NULL);
        return getOWLDatatype(prefixManager.getIRI(abbreviatedIRI, this));
    }

    @Override
    public OWLNamedIndividual getOWLNamedIndividual(String iri) {
        return getOWLNamedIndividual(getIRI(iri));
    }

    @Override
    public OWLNamedIndividual getOWLNamedIndividual(String namespace, @Nullable String remainder) {
        return getOWLNamedIndividual(getIRI(namespace, remainder));
    }

    @Override
    public OWLNamedIndividual getOWLNamedIndividual(String abbreviatedIRI,
        PrefixManager prefixManager) {
        checkNotNull(abbreviatedIRI, "curi canno be null");
        checkNotNull(prefixManager, PREFIX_MANAGER_CANNOT_BE_NULL);
        return getOWLNamedIndividual(prefixManager.getIRI(abbreviatedIRI, this));
    }

    @Override
    public OWLObjectProperty getOWLObjectProperty(String iri) {
        return getOWLObjectProperty(getIRI(iri));
    }

    @Override
    public OWLObjectProperty getOWLObjectProperty(String namespace, @Nullable String remainder) {
        return getOWLObjectProperty(getIRI(namespace, remainder));
    }

    @Override
    public OWLObjectProperty getOWLObjectProperty(String abbreviatedIRI,
        PrefixManager prefixManager) {
        checkNotNull(abbreviatedIRI, "curi canno be null");
        checkNotNull(prefixManager, PREFIX_MANAGER_CANNOT_BE_NULL);
        return getOWLObjectProperty(prefixManager.getIRI(abbreviatedIRI, this));
    }

    @Override
    public SWRLVariable getSWRLVariable(String iri) {
        return getSWRLVariable(getIRI(iri));
    }

    @Override
    public SWRLVariable getSWRLVariable(String namespace, @Nullable String remainder) {
        return getSWRLVariable(getIRI(namespace, remainder));
    }

    @Override
    public <E extends OWLEntity> E getOWLEntity(EntityType<E> entityType, IRI iri) {
        return entityType.buildEntity(iri, this);
    }

    @Override
    public OWLClass getOWLClass(IRI iri) {
        return classes.get(iri);
    }

    @Override
    public OWLAnnotationProperty getRDFSLabel() {
        return RDFS_LABEL;
    }

    @Override
    public OWLAnnotationProperty getRDFSComment() {
        return RDFS_COMMENT;
    }

    @Override
    public OWLAnnotationProperty getRDFSSeeAlso() {
        return RDFS_SEE_ALSO;
    }

    @Override
    public OWLAnnotationProperty getRDFSIsDefinedBy() {
        return RDFS_IS_DEFINED_BY;
    }

    @Override
    public OWLAnnotationProperty getOWLVersionInfo() {
        return OWL_VERSION_INFO;
    }

    @Override
    public OWLAnnotationProperty getOWLBackwardCompatibleWith() {
        return OWL_BACKWARD_COMPATIBLE_WITH;
    }

    @Override
    public OWLAnnotationProperty getOWLIncompatibleWith() {
        return OWL_INCOMPATIBLE_WITH;
    }

    @Override
    public OWLAnnotationProperty getOWLDeprecated() {
        return OWL_DEPRECATED;
    }

    @Override
    public OWLClass getOWLThing() {
        return OWL_THING;
    }

    @Override
    public OWLClass getOWLNothing() {
        return OWL_NOTHING;
    }

    @Override
    public OWLDataProperty getOWLBottomDataProperty() {
        return OWL_BOTTOM_DATA_PROPERTY;
    }

    @Override
    public OWLObjectProperty getOWLBottomObjectProperty() {
        return OWL_BOTTOM_OBJECT_PROPERTY;
    }

    @Override
    public OWLDataProperty getOWLTopDataProperty() {
        return OWL_TOP_DATA_PROPERTY;
    }

    @Override
    public OWLObjectProperty getOWLTopObjectProperty() {
        return OWL_TOP_OBJECT_PROPERTY;
    }

    @Override
    public OWLObjectProperty getOWLObjectProperty(IRI iri) {
        return objectProperties.get(iri);
    }

    @Override
    public OWLDataProperty getOWLDataProperty(IRI iri) {
        return dataProperties.get(iri);
    }

    @Override
    public OWLNamedIndividual getOWLNamedIndividual(IRI iri) {
        return individuals.get(iri);
    }

    @Override
    public OWLAnonymousIndividual getOWLAnonymousIndividual(String nodeId) {
        return new OWLAnonymousIndividualImpl(NodeID.getNodeID(nodeId));
    }

    @Override
    public OWLAnonymousIndividual getOWLAnonymousIndividual() {
        return new OWLAnonymousIndividualImpl(NodeID.getNodeID(null));
    }

    @Override
    public OWLDatatype getOWLDatatype(IRI iri) {
        return datatypes.get(iri);
    }

    @Override
    public OWLLiteral getOWLLiteral(boolean value) {
        return value ? TRUELITERAL : FALSELITERAL;
    }

    @Override
    public OWLDataOneOf getOWLDataOneOf(Stream<? extends OWLLiteral> values) {
        return new OWLDataOneOfImpl(sortedList(OWLLiteral.class, values));
    }

    @Override
    public OWLDataComplementOf getOWLDataComplementOf(OWLDataRange dataRange) {
        return new OWLDataComplementOfImpl(dataRange);
    }

    @Override
    public OWLDataComplementOf getOWLDataComplementOf(OWL2Datatype dataRange) {
        return getOWLDataComplementOf(dataRange.getDatatype(this));
    }

    @Override
    public OWLDataIntersectionOf getOWLDataIntersectionOf(
        Stream<? extends OWLDataRange> dataRanges) {
        return new OWLDataIntersectionOfImpl(sortedList(OWLDataRange.class, dataRanges));
    }

    @Override
    public OWLDataUnionOf getOWLDataUnionOf(Stream<? extends OWLDataRange> dataRanges) {
        return new OWLDataUnionOfImpl(sortedList(OWLDataRange.class, dataRanges));
    }

    @Override
    public OWLDatatypeRestriction getOWLDatatypeRestriction(OWLDatatype dataType,
        Collection<OWLFacetRestriction> facetRestrictions) {
        return new OWLDatatypeRestrictionImpl(dataType, facetRestrictions);
    }

    @Override
    public OWLDatatypeRestriction getOWLDatatypeRestriction(OWLDatatype dataType, OWLFacet facet,
        OWLLiteral typedLiteral) {
        return new OWLDatatypeRestrictionImpl(dataType,
            Collections.singleton(getOWLFacetRestriction(facet, typedLiteral)));
    }

    @Override
    public OWLFacetRestriction getOWLFacetRestriction(OWLFacet facet, OWLLiteral facetValue) {
        return new OWLFacetRestrictionImpl(facet, facetValue);
    }

    @Override
    public OWLObjectIntersectionOf getOWLObjectIntersectionOf(
        Stream<? extends OWLClassExpression> operands) {
        return new OWLObjectIntersectionOfImpl(sortedList(OWLClassExpression.class, operands));
    }

    @Override
    public OWLDataAllValuesFrom getOWLDataAllValuesFrom(OWLDataPropertyExpression property,
        OWLDataRange dataRange) {
        return new OWLDataAllValuesFromImpl(property, dataRange);
    }

    @Override
    public OWLDataAllValuesFrom getOWLDataAllValuesFrom(OWLDataPropertyExpression property,
        OWL2Datatype dataRange) {
        return getOWLDataAllValuesFrom(property, dataRange.getDatatype(this));
    }

    @Override
    public OWLDataExactCardinality getOWLDataExactCardinality(int cardinality,
        OWLDataPropertyExpression property) {
        return new OWLDataExactCardinalityImpl(property, cardinality, getTopDatatype());
    }

    @Override
    public OWLDataExactCardinality getOWLDataExactCardinality(int cardinality,
        OWLDataPropertyExpression property, OWLDataRange dataRange) {
        return new OWLDataExactCardinalityImpl(property, cardinality, dataRange);
    }

    @Override
    public OWLDataExactCardinality getOWLDataExactCardinality(int cardinality,
        OWLDataPropertyExpression property, OWL2Datatype dataRange) {
        return getOWLDataExactCardinality(cardinality, property, dataRange.getDatatype(this));
    }

    @Override
    public OWLDataMaxCardinality getOWLDataMaxCardinality(int cardinality,
        OWLDataPropertyExpression property) {
        return new OWLDataMaxCardinalityImpl(property, cardinality, getTopDatatype());
    }

    @Override
    public OWLDataMaxCardinality getOWLDataMaxCardinality(int cardinality,
        OWLDataPropertyExpression property, OWLDataRange dataRange) {
        return new OWLDataMaxCardinalityImpl(property, cardinality, dataRange);
    }

    @Override
    public OWLDataMaxCardinality getOWLDataMaxCardinality(int cardinality,
        OWLDataPropertyExpression property, OWL2Datatype dataRange) {
        return getOWLDataMaxCardinality(cardinality, property, dataRange.getDatatype(this));
    }

    @Override
    public OWLDataMinCardinality getOWLDataMinCardinality(int cardinality,
        OWLDataPropertyExpression property) {
        return new OWLDataMinCardinalityImpl(property, cardinality, getTopDatatype());
    }

    @Override
    public OWLDataMinCardinality getOWLDataMinCardinality(int cardinality,
        OWLDataPropertyExpression property, OWLDataRange dataRange) {
        return new OWLDataMinCardinalityImpl(property, cardinality, dataRange);
    }

    @Override
    public OWLDataMinCardinality getOWLDataMinCardinality(int cardinality,
        OWLDataPropertyExpression property, OWL2Datatype dataRange) {
        return getOWLDataMinCardinality(cardinality, property, dataRange.getDatatype(this));
    }

    @Override
    public OWLDataSomeValuesFrom getOWLDataSomeValuesFrom(OWLDataPropertyExpression property,
        OWLDataRange dataRange) {
        return new OWLDataSomeValuesFromImpl(property, dataRange);
    }

    @Override
    public OWLDataSomeValuesFrom getOWLDataSomeValuesFrom(OWLDataPropertyExpression property,
        OWL2Datatype dataRange) {
        return getOWLDataSomeValuesFrom(property, dataRange.getDatatype(this));
    }

    @Override
    public OWLDataHasValue getOWLDataHasValue(OWLDataPropertyExpression property,
        OWLLiteral value) {
        return new OWLDataHasValueImpl(property, value);
    }

    @Override
    public OWLObjectComplementOf getOWLObjectComplementOf(OWLClassExpression operand) {
        return new OWLObjectComplementOfImpl(operand);
    }

    @Override
    public OWLObjectAllValuesFrom getOWLObjectAllValuesFrom(OWLObjectPropertyExpression property,
        OWLClassExpression classExpression) {
        return new OWLObjectAllValuesFromImpl(property, classExpression);
    }

    @Override
    public OWLObjectOneOf getOWLObjectOneOf(Stream<? extends OWLIndividual> values) {
        return new OWLObjectOneOfImpl(sortedList(OWLIndividual.class, values));
    }

    @Override
    public OWLObjectExactCardinality getOWLObjectExactCardinality(int cardinality,
        OWLObjectPropertyExpression property) {
        return new OWLObjectExactCardinalityImpl(property, cardinality, OWL_THING);
    }

    @Override
    public OWLObjectExactCardinality getOWLObjectExactCardinality(int cardinality,
        OWLObjectPropertyExpression property, OWLClassExpression classExpression) {
        return new OWLObjectExactCardinalityImpl(property, cardinality, classExpression);
    }

    @Override
    public OWLObjectMinCardinality getOWLObjectMinCardinality(int cardinality,
        OWLObjectPropertyExpression property) {
        return new OWLObjectMinCardinalityImpl(property, cardinality, OWL_THING);
    }

    @Override
    public OWLObjectMinCardinality getOWLObjectMinCardinality(int cardinality,
        OWLObjectPropertyExpression property, OWLClassExpression classExpression) {
        return new OWLObjectMinCardinalityImpl(property, cardinality, classExpression);
    }

    @Override
    public OWLObjectMaxCardinality getOWLObjectMaxCardinality(int cardinality,
        OWLObjectPropertyExpression property) {
        return new OWLObjectMaxCardinalityImpl(property, cardinality, OWL_THING);
    }

    @Override
    public OWLObjectMaxCardinality getOWLObjectMaxCardinality(int cardinality,
        OWLObjectPropertyExpression property, OWLClassExpression classExpression) {
        return new OWLObjectMaxCardinalityImpl(property, cardinality, classExpression);
    }

    @Override
    public OWLObjectHasSelf getOWLObjectHasSelf(OWLObjectPropertyExpression property) {
        return new OWLObjectHasSelfImpl(property);
    }

    @Override
    public OWLObjectSomeValuesFrom getOWLObjectSomeValuesFrom(OWLObjectPropertyExpression property,
        OWLClassExpression classExpression) {
        return new OWLObjectSomeValuesFromImpl(property, classExpression);
    }

    @Override
    public OWLObjectHasValue getOWLObjectHasValue(OWLObjectPropertyExpression property,
        OWLIndividual individual) {
        return new OWLObjectHasValueImpl(property, individual);
    }

    @Override
    public OWLObjectUnionOf getOWLObjectUnionOf(Stream<? extends OWLClassExpression> operands) {
        return new OWLObjectUnionOfImpl(sortedList(OWLClassExpression.class, operands));
    }

    @Override
    public OWLAsymmetricObjectPropertyAxiom getOWLAsymmetricObjectPropertyAxiom(
        OWLObjectPropertyExpression propertyExpression, Collection<OWLAnnotation> anns) {
        return new OWLAsymmetricObjectPropertyAxiomImpl(propertyExpression, anns);
    }

    @Override
    public OWLDataPropertyDomainAxiom getOWLDataPropertyDomainAxiom(
        OWLDataPropertyExpression property, OWLClassExpression domain,
        Collection<OWLAnnotation> anns) {
        return new OWLDataPropertyDomainAxiomImpl(property, domain, anns);
    }

    @Override
    public OWLDataPropertyRangeAxiom getOWLDataPropertyRangeAxiom(
        OWLDataPropertyExpression property, OWLDataRange owlDataRange,
        Collection<OWLAnnotation> anns) {
        return new OWLDataPropertyRangeAxiomImpl(property, owlDataRange, anns);
    }

    @Override
    public OWLDataPropertyRangeAxiom getOWLDataPropertyRangeAxiom(
        OWLDataPropertyExpression property, OWL2Datatype owlDataRange,
        Collection<OWLAnnotation> anns) {
        return getOWLDataPropertyRangeAxiom(property, owlDataRange.getDatatype(this), anns);
    }

    @Override
    public OWLSubDataPropertyOfAxiom getOWLSubDataPropertyOfAxiom(
        OWLDataPropertyExpression subProperty, OWLDataPropertyExpression superProperty,
        Collection<OWLAnnotation> anns) {
        return new OWLSubDataPropertyOfAxiomImpl(subProperty, superProperty, anns);
    }

    @Override
    public OWLDeclarationAxiom getOWLDeclarationAxiom(OWLEntity owlEntity,
        Collection<OWLAnnotation> anns) {
        return new OWLDeclarationAxiomImpl(owlEntity, anns);
    }

    @Override
    public OWLDifferentIndividualsAxiom getOWLDifferentIndividualsAxiom(
        Collection<? extends OWLIndividual> inds, Collection<OWLAnnotation> anns) {
        return new OWLDifferentIndividualsAxiomImpl(sortedList(OWLIndividual.class, inds.stream()),
            anns);
    }

    @Override
    public OWLDisjointClassesAxiom getOWLDisjointClassesAxiom(
        Collection<? extends OWLClassExpression> classExpressions, Collection<OWLAnnotation> anns) {
        checkIterableNotNull(classExpressions, "classExpressions cannot be null or contain null",
            true);
        checkIterableNotNull(anns, "annotations cannot be null", true);
        // Hack to handle the case where classExpressions has only a single
        // member
        // which will usually be the result of :x owl:disjointWith :x .
        List<OWLClassExpression> sortedList =
            sortedList(OWLClassExpression.class, classExpressions.stream());
        if (sortedList.size() == 1 && !config.shouldAllowDuplicatesInConstructSets()) {
            OWLClassExpression classExpression = classExpressions.iterator().next();
            if (classExpression.isOWLThing()) {
                throw new OWLRuntimeException(
                    "DisjointClasses(owl:Thing) cannot be created. It is not a syntactically valid OWL 2 axiom. If the intent is to declare owl:Thing as disjoint with itself and therefore empty, it cannot be created as a DisjointClasses axiom. Please rewrite it as SubClassOf(owl:Thing, owl:Nothing).");
            }
            if (classExpression.isOWLNothing()) {
                throw new OWLRuntimeException(
                    "DisjointClasses(owl:Nothing) cannot be created. It is not a syntactically valid OWL 2 axiom. If the intent is to declare owl:Nothing as disjoint with itself and therefore empty, it cannot be created as a DisjointClasses axiom, and it is also redundant as owl:Nothing is always empty. Please rewrite it as SubClassOf(owl:Nothing, owl:Nothing) or remove the axiom.");
            }
            List<OWLClassExpression> modifiedClassExpressions =
                Stream.of(OWL_THING, classExpression).sorted().toList();
            return new OWLDisjointClassesAxiomImpl(modifiedClassExpressions,
                makeSingletonDisjoinClassWarningAnnotation(anns, classExpression, OWL_THING));
        }
        return new OWLDisjointClassesAxiomImpl(sortedList, anns);
    }

    protected Set<OWLAnnotation> makeSingletonDisjoinClassWarningAnnotation(
        Collection<OWLAnnotation> anns, OWLClassExpression classExpression,
        OWLClassExpression addedClass) {
        Set<OWLAnnotation> modifiedAnnotations = new HashSet<>(anns.size() + 1);
        modifiedAnnotations.addAll(anns);
        String provenanceComment =
            String.format("%s on %s", VersionInfo.getVersionInfo().getGeneratedByMessage(),
                new SimpleDateFormat().format(new Date()));
        OWLAnnotation provenanceAnnotation =
            getOWLAnnotation(RDFS_COMMENT, getOWLLiteral(provenanceComment));
        Set<OWLAnnotation> metaAnnotations = Collections.singleton(provenanceAnnotation);
        String changeComment =
            String.format("DisjointClasses(%s) replaced by DisjointClasses(%s %s)", classExpression,
                classExpression, addedClass);
        modifiedAnnotations
            .add(getOWLAnnotation(RDFS_COMMENT, getOWLLiteral(changeComment), metaAnnotations));
        return modifiedAnnotations;
    }

    @Override
    public OWLDisjointDataPropertiesAxiom getOWLDisjointDataPropertiesAxiom(
        Collection<? extends OWLDataPropertyExpression> properties,
        Collection<OWLAnnotation> anns) {
        return new OWLDisjointDataPropertiesAxiomImpl(
            sortedList(OWLDataPropertyExpression.class, properties.stream()), anns);
    }

    @Override
    public OWLDisjointObjectPropertiesAxiom getOWLDisjointObjectPropertiesAxiom(
        Collection<? extends OWLObjectPropertyExpression> properties,
        Collection<OWLAnnotation> anns) {
        return new OWLDisjointObjectPropertiesAxiomImpl(
            sortedList(OWLObjectPropertyExpression.class, properties.stream()), anns);
    }

    @Override
    public OWLEquivalentClassesAxiom getOWLEquivalentClassesAxiom(
        Collection<? extends OWLClassExpression> classExpressions, Collection<OWLAnnotation> anns) {
        return new OWLEquivalentClassesAxiomImpl(
            sortedList(OWLClassExpression.class, classExpressions.stream()), anns);
    }

    @Override
    public OWLEquivalentDataPropertiesAxiom getOWLEquivalentDataPropertiesAxiom(
        Collection<? extends OWLDataPropertyExpression> properties,
        Collection<OWLAnnotation> anns) {
        return new OWLEquivalentDataPropertiesAxiomImpl(
            sortedList(OWLDataPropertyExpression.class, properties.stream()), anns);
    }

    @Override
    public OWLFunctionalDataPropertyAxiom getOWLFunctionalDataPropertyAxiom(
        OWLDataPropertyExpression property, Collection<OWLAnnotation> anns) {
        return new OWLFunctionalDataPropertyAxiomImpl(property, anns);
    }

    @Override
    public OWLFunctionalObjectPropertyAxiom getOWLFunctionalObjectPropertyAxiom(
        OWLObjectPropertyExpression property, Collection<OWLAnnotation> anns) {
        return new OWLFunctionalObjectPropertyAxiomImpl(property, anns);
    }

    @Override
    public OWLImportsDeclaration getOWLImportsDeclaration(IRI importedOntologyIRI) {
        return new OWLImportsDeclarationImpl(importedOntologyIRI);
    }

    @Override
    public OWLDataPropertyAssertionAxiom getOWLDataPropertyAssertionAxiom(
        OWLDataPropertyExpression property, OWLIndividual subject, OWLLiteral object,
        Collection<OWLAnnotation> anns) {
        return new OWLDataPropertyAssertionAxiomImpl(subject, property, object, anns);
    }

    @Override
    public OWLNegativeDataPropertyAssertionAxiom getOWLNegativeDataPropertyAssertionAxiom(
        OWLDataPropertyExpression property, OWLIndividual subject, OWLLiteral object,
        Collection<OWLAnnotation> anns) {
        return new OWLNegativeDataPropertyAssertionAxiomImpl(subject, property, object, anns);
    }

    @Override
    public OWLNegativeObjectPropertyAssertionAxiom getOWLNegativeObjectPropertyAssertionAxiom(
        OWLObjectPropertyExpression property, OWLIndividual subject, OWLIndividual object,
        Collection<OWLAnnotation> anns) {
        return new OWLNegativeObjectPropertyAssertionAxiomImpl(subject, property, object, anns);
    }

    @Override
    public OWLClassAssertionAxiom getOWLClassAssertionAxiom(OWLClassExpression classExpression,
        OWLIndividual individual, Collection<OWLAnnotation> anns) {
        return new OWLClassAssertionAxiomImpl(individual, classExpression, anns);
    }

    @Override
    public OWLInverseFunctionalObjectPropertyAxiom getOWLInverseFunctionalObjectPropertyAxiom(
        OWLObjectPropertyExpression property, Collection<OWLAnnotation> anns) {
        return new OWLInverseFunctionalObjectPropertyAxiomImpl(property, anns);
    }

    @Override
    public OWLIrreflexiveObjectPropertyAxiom getOWLIrreflexiveObjectPropertyAxiom(
        OWLObjectPropertyExpression property, Collection<OWLAnnotation> anns) {
        return new OWLIrreflexiveObjectPropertyAxiomImpl(property, anns);
    }

    @Override
    public OWLObjectPropertyDomainAxiom getOWLObjectPropertyDomainAxiom(
        OWLObjectPropertyExpression property, OWLClassExpression classExpression,
        Collection<OWLAnnotation> anns) {
        return new OWLObjectPropertyDomainAxiomImpl(property, classExpression, anns);
    }

    @Override
    public OWLObjectPropertyRangeAxiom getOWLObjectPropertyRangeAxiom(
        OWLObjectPropertyExpression property, OWLClassExpression range,
        Collection<OWLAnnotation> anns) {
        return new OWLObjectPropertyRangeAxiomImpl(property, range, anns);
    }

    @Override
    public OWLSubObjectPropertyOfAxiom getOWLSubObjectPropertyOfAxiom(
        OWLObjectPropertyExpression subProperty, OWLObjectPropertyExpression superProperty,
        Collection<OWLAnnotation> anns) {
        return new OWLSubObjectPropertyOfAxiomImpl(subProperty, superProperty, anns);
    }

    @Override
    public OWLReflexiveObjectPropertyAxiom getOWLReflexiveObjectPropertyAxiom(
        OWLObjectPropertyExpression property, Collection<OWLAnnotation> anns) {
        return new OWLReflexiveObjectPropertyAxiomImpl(property, anns);
    }

    @Override
    public OWLSameIndividualAxiom getOWLSameIndividualAxiom(
        Collection<? extends OWLIndividual> inds, Collection<OWLAnnotation> anns) {
        return new OWLSameIndividualAxiomImpl(sortedList(OWLIndividual.class, inds.stream()), anns);
    }

    @Override
    public OWLSubClassOfAxiom getOWLSubClassOfAxiom(OWLClassExpression subClass,
        OWLClassExpression superClass, Collection<OWLAnnotation> anns) {
        return new OWLSubClassOfAxiomImpl(subClass, superClass, anns);
    }

    @Override
    public OWLSymmetricObjectPropertyAxiom getOWLSymmetricObjectPropertyAxiom(
        OWLObjectPropertyExpression property, Collection<OWLAnnotation> anns) {
        return new OWLSymmetricObjectPropertyAxiomImpl(property, anns);
    }

    @Override
    public OWLTransitiveObjectPropertyAxiom getOWLTransitiveObjectPropertyAxiom(
        OWLObjectPropertyExpression property, Collection<OWLAnnotation> anns) {
        return new OWLTransitiveObjectPropertyAxiomImpl(property, anns);
    }

    @Override
    public OWLObjectInverseOf getOWLObjectInverseOf(OWLObjectProperty property) {
        return new OWLObjectInverseOfImpl(property);
    }

    @Override
    public OWLInverseObjectPropertiesAxiom getOWLInverseObjectPropertiesAxiom(
        OWLObjectPropertyExpression forwardProperty, OWLObjectPropertyExpression inverseProperty,
        Collection<OWLAnnotation> anns) {
        return new OWLInverseObjectPropertiesAxiomImpl(forwardProperty, inverseProperty, anns);
    }

    @Override
    public OWLSubPropertyChainOfAxiom getOWLSubPropertyChainOfAxiom(
        List<? extends OWLObjectPropertyExpression> chain,
        OWLObjectPropertyExpression superProperty, Collection<OWLAnnotation> anns) {
        return new OWLSubPropertyChainAxiomImpl(chain, superProperty, anns);
    }

    @Override
    public OWLHasKeyAxiom getOWLHasKeyAxiom(OWLClassExpression ce,
        Collection<? extends OWLPropertyExpression> properties, Collection<OWLAnnotation> anns) {
        return new OWLHasKeyAxiomImpl(ce, properties, anns);
    }

    @Override
    public OWLDisjointUnionAxiom getOWLDisjointUnionAxiom(OWLClass owlClass,
        Stream<? extends OWLClassExpression> classExpressions, Collection<OWLAnnotation> anns) {
        return new OWLDisjointUnionAxiomImpl(owlClass,
            sortedList(OWLClassExpression.class, classExpressions), anns);
    }

    @Override
    public OWLEquivalentObjectPropertiesAxiom getOWLEquivalentObjectPropertiesAxiom(
        Collection<? extends OWLObjectPropertyExpression> properties,
        Collection<OWLAnnotation> anns) {
        return new OWLEquivalentObjectPropertiesAxiomImpl(
            sortedList(OWLObjectPropertyExpression.class, properties.stream()), anns);
    }

    @Override
    public OWLObjectPropertyAssertionAxiom getOWLObjectPropertyAssertionAxiom(
        OWLObjectPropertyExpression property, OWLIndividual individual, OWLIndividual object,
        Collection<OWLAnnotation> anns) {
        return new OWLObjectPropertyAssertionAxiomImpl(individual, property, object, anns);
    }

    @Override
    public OWLSubAnnotationPropertyOfAxiom getOWLSubAnnotationPropertyOfAxiom(
        OWLAnnotationProperty sub, OWLAnnotationProperty sup, Collection<OWLAnnotation> anns) {
        return new OWLSubAnnotationPropertyOfAxiomImpl(sub, sup, anns);
    }

    // Annotations
    @Override
    public OWLAnnotationProperty getOWLAnnotationProperty(IRI iri) {
        return annotationProperties.get(iri);
    }

    @Override
    public OWLAnnotation getOWLAnnotation(OWLAnnotationProperty property,
        OWLAnnotationValue value) {
        return new OWLAnnotationImplNotAnnotated(property, value);
    }

    @Override
    public OWLAnnotation getOWLAnnotation(OWLAnnotationProperty property, OWLAnnotationValue value,
        Stream<OWLAnnotation> anns) {
        return annotations.get(new OWLAnnotationImpl(property, value, anns));
    }

    @Override
    public OWLAnnotationAssertionAxiom getOWLAnnotationAssertionAxiom(OWLAnnotationSubject subject,
        OWLAnnotation annotation) {
        return getOWLAnnotationAssertionAxiom(annotation.getProperty(), subject,
            annotation.getValue(), annotation.annotationsAsList());
    }

    @Override
    public OWLAnnotationAssertionAxiom getOWLAnnotationAssertionAxiom(OWLAnnotationSubject subject,
        OWLAnnotation annotation, Collection<OWLAnnotation> anns) {
        return getOWLAnnotationAssertionAxiom(annotation.getProperty(), subject,
            annotation.getValue(), anns);
    }

    @Override
    public OWLAnnotationAssertionAxiom getOWLAnnotationAssertionAxiom(
        OWLAnnotationProperty property, OWLAnnotationSubject subject, OWLAnnotationValue value,
        Collection<OWLAnnotation> anns) {
        return new OWLAnnotationAssertionAxiomImpl(subject, property, value, anns);
    }

    @Override
    public OWLAnnotationAssertionAxiom getDeprecatedOWLAnnotationAssertionAxiom(IRI subject) {
        return getOWLAnnotationAssertionAxiom(getOWLDeprecated(), subject, getOWLLiteral(true));
    }

    @Override
    public OWLAnnotationPropertyDomainAxiom getOWLAnnotationPropertyDomainAxiom(
        OWLAnnotationProperty prop, IRI domain, Collection<OWLAnnotation> anns) {
        return new OWLAnnotationPropertyDomainAxiomImpl(prop, domain, anns);
    }

    @Override
    public OWLAnnotationPropertyRangeAxiom getOWLAnnotationPropertyRangeAxiom(
        OWLAnnotationProperty prop, IRI range, Collection<OWLAnnotation> anns) {
        return new OWLAnnotationPropertyRangeAxiomImpl(prop, range, anns);
    }

    // SWRL
    @Override
    public SWRLRule getSWRLRule(Collection<? extends SWRLAtom> body,
        Collection<? extends SWRLAtom> head, Collection<OWLAnnotation> anns) {
        return new SWRLRuleImpl(body, head, anns);
    }

    @Override
    public SWRLRule getSWRLRule(Collection<? extends SWRLAtom> body,
        Collection<? extends SWRLAtom> head) {
        return getSWRLRule(body, head, Collections.emptyList());
    }

    @Override
    public SWRLClassAtom getSWRLClassAtom(OWLClassExpression predicate, SWRLIArgument arg) {
        return new SWRLClassAtomImpl(predicate, arg);
    }

    @Override
    public SWRLDataRangeAtom getSWRLDataRangeAtom(OWLDataRange predicate, SWRLDArgument arg) {
        return new SWRLDataRangeAtomImpl(predicate, arg);
    }

    @Override
    public SWRLDataRangeAtom getSWRLDataRangeAtom(OWL2Datatype predicate, SWRLDArgument arg) {
        return getSWRLDataRangeAtom(predicate.getDatatype(this), arg);
    }

    @Override
    public SWRLObjectPropertyAtom getSWRLObjectPropertyAtom(OWLObjectPropertyExpression property,
        SWRLIArgument arg0, SWRLIArgument arg1) {
        return new SWRLObjectPropertyAtomImpl(property, arg0, arg1);
    }

    @Override
    public SWRLDataPropertyAtom getSWRLDataPropertyAtom(OWLDataPropertyExpression property,
        SWRLIArgument arg0, SWRLDArgument arg1) {
        return new SWRLDataPropertyAtomImpl(property, arg0, arg1);
    }

    @Override
    public SWRLBuiltInAtom getSWRLBuiltInAtom(IRI builtInIRI, List<SWRLDArgument> args) {
        return new SWRLBuiltInAtomImpl(builtInIRI, args);
    }

    @Override
    public SWRLVariable getSWRLVariable(IRI variable) {
        return new SWRLVariableImpl(variable);
    }

    @Override
    public SWRLIndividualArgument getSWRLIndividualArgument(OWLIndividual individual) {
        return new SWRLIndividualArgumentImpl(individual);
    }

    @Override
    public SWRLLiteralArgument getSWRLLiteralArgument(OWLLiteral literal) {
        return new SWRLLiteralArgumentImpl(literal);
    }

    @Override
    public SWRLDifferentIndividualsAtom getSWRLDifferentIndividualsAtom(SWRLIArgument arg0,
        SWRLIArgument arg1) {
        return new SWRLDifferentIndividualsAtomImpl(
            getOWLObjectProperty(OWLRDFVocabulary.OWL_DIFFERENT_FROM), arg0, arg1);
    }

    @Override
    public SWRLSameIndividualAtom getSWRLSameIndividualAtom(SWRLIArgument arg0,
        SWRLIArgument arg1) {
        return new SWRLSameIndividualAtomImpl(getOWLObjectProperty(OWLRDFVocabulary.OWL_SAME_AS),
            arg0, arg1);
    }

    @Override
    public OWLDatatypeDefinitionAxiom getOWLDatatypeDefinitionAxiom(OWLDatatype datatype,
        OWLDataRange dataRange, Collection<OWLAnnotation> anns) {
        return new OWLDatatypeDefinitionAxiomImpl(datatype, dataRange, anns);
    }

    @Override
    public OWLDatatypeDefinitionAxiom getOWLDatatypeDefinitionAxiom(OWLDatatype datatype,
        OWL2Datatype dataRange, Collection<OWLAnnotation> anns) {
        return getOWLDatatypeDefinitionAxiom(datatype, dataRange.getDatatype(this), anns);
    }

    @Override
    public OWLLiteral getOWLLiteral(String lexicalValue, OWLDatatype datatype) {
        checkNotNull(lexicalValue, "lexicalValue cannot be null");
        checkNotNull(datatype, "datatype cannot be null");
        if (datatype.isRDFPlainLiteral() || datatype.equals(LANGSTRING)) {
            int sep = lexicalValue.lastIndexOf('@');
            if (sep != -1) {
                String lex = lexicalValue.substring(0, sep);
                String lang = lexicalValue.substring(sep + 1);
                return verifyNotNull(getBasicLiteral(lex, lang, LANGSTRING));
            } else {
                return verifyNotNull(getBasicLiteral(lexicalValue, XSDSTRING));
            }
        }
        // check the special cases
        return verifyNotNull(parseSpecialCases(lexicalValue, datatype));
    }

    @Override
    public OWLLiteral getOWLLiteral(int value) {
        return new OWLLiteralImplInteger(value);
    }

    @Override
    public OWLLiteral getOWLLiteral(long value) {
        return new OWLLiteralImplLong(value);
    }

    @Override
    public OWLLiteral getOWLLiteral(double value) {
        return new OWLLiteralImplDouble(value);
    }

    @Override
    public OWLLiteral getOWLLiteral(float value) {
        return new OWLLiteralImplFloat(value);
    }

    @Override
    public OWLLiteral getOWLLiteral(String value) {
        return new OWLLiteralImplString(value);
    }

    @Override
    public OWLLiteral getOWLLiteral(String literal, @Nullable String lang) {
        String normalisedLang;
        if (lang == null) {
            normalisedLang = "";
        } else {
            normalisedLang = lang.trim().toLowerCase(Locale.ENGLISH);
        }
        if (normalisedLang.isEmpty()) {
            return new OWLLiteralImplString(literal);
        } else {
            return new OWLLiteralImplPlain(literal, normalisedLang);
        }
    }

    @Override
    public OWLDatatype getBooleanOWLDatatype() {
        return XSDBOOLEAN;
    }

    @Override
    public OWLDatatype getStringOWLDatatype() {
        return XSDSTRING;
    }

    @Override
    public OWLDatatype getDoubleOWLDatatype() {
        return XSDDOUBLE;
    }

    @Override
    public OWLDatatype getFloatOWLDatatype() {
        return XSDFLOAT;
    }

    @Override
    public OWLDatatype getIntegerOWLDatatype() {
        return XSDINTEGER;
    }

    @Override
    public OWLDatatype getTopDatatype() {
        return RDFSLITERAL;
    }

    @Override
    public OWLDatatype getRDFPlainLiteral() {
        return PLAIN;
    }

    private static <F, T> LoadingCache<F, T> builder(CacheLoader<F, T> f) {
        return Caffeine.newBuilder().weakKeys().maximumSize(size()).build(f);
    }

    protected OWLLiteral parseSpecialCases(String lexicalValue, OWLDatatype datatype) {
        OWLLiteral literal;
        try {
            if (datatype.isString()) {
                literal = getOWLLiteral(lexicalValue);
            } else if (datatype.isBoolean()) {
                literal = getOWLLiteral(OWLLiteralImpl.asBoolean(lexicalValue));
            } else if (datatype.isFloat()) {
                literal = parseFloat(lexicalValue, datatype);
            } else if (datatype.isDouble()) {
                literal = getOWLLiteral(Double.parseDouble(lexicalValue));
            } else if (datatype.isInteger()) {
                literal = parseInteger(lexicalValue, datatype);
            } else if (datatype.isLong()) {
                literal = parseLong(lexicalValue, datatype);
            } else {
                literal = getBasicLiteral(lexicalValue, datatype);
            }
        } catch (@SuppressWarnings("unused") NumberFormatException e) {
            // some literal is malformed, i.e., wrong format
            literal = getBasicLiteral(lexicalValue, datatype);
        }
        return literal;
    }

    protected static final boolean isBlank(CharSequence c) {
        for (int i = 0; i < c.length(); i++) {
            if (!Character.isWhitespace(c.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    protected static final boolean firstNonblankIsZero(CharSequence c) {
        for (int i = 0; i < c.length(); i++) {
            if (!Character.isWhitespace(c.charAt(i))) {
                return c.charAt(i) == '0';
            }
        }
        return false;
    }

    protected static final boolean isNegativeZero(CharSequence c) {
        for (int i = 0; i < c.length(); i++) {
            if (!Character.isWhitespace(c.charAt(i))) {
                return i <= c.length() - 4 && c.charAt(i) == '-' && c.charAt(i + 1) == '0'
                    && c.charAt(i + 2) == '.' && c.charAt(i + 3) == '0';
            }
        }
        return false;
    }

    protected OWLLiteral parseInteger(String lexicalValue, OWLDatatype datatype) {
        OWLLiteral literal;
        if (isBlank(lexicalValue)) {
            literal = getBasicLiteral(lexicalValue, datatype);
        } else {
            // again, some W3C tests require padding zeroes to make
            // literals different
            if (firstNonblankIsZero(lexicalValue)) {
                literal = getBasicLiteral(lexicalValue, datatype);
            } else {
                try {
                    // this is fine for values that can be parsed as
                    // ints - not all values are
                    literal = getOWLLiteral(Integer.parseInt(lexicalValue));
                } catch (@SuppressWarnings("unused") NumberFormatException ex) {
                    // try as a big decimal
                    literal = getBasicLiteral(lexicalValue, datatype);
                }
            }
        }
        return literal;
    }

    protected OWLLiteral parseLong(String lexicalValue, OWLDatatype datatype) {
        OWLLiteral literal;
        if (isBlank(lexicalValue)) {
            literal = getBasicLiteral(lexicalValue, datatype);
        } else {
            // again, some W3C tests require padding zeroes to make
            // literals different
            if (firstNonblankIsZero(lexicalValue)) {
                literal = getBasicLiteral(lexicalValue, datatype);
            } else {
                try {
                    // this is fine for values that can be parsed as
                    // ints - not all values are
                    literal = getOWLLiteral(Long.parseLong(lexicalValue));
                } catch (@SuppressWarnings("unused") NumberFormatException ex) {
                    // try as a big decimal
                    literal = getBasicLiteral(lexicalValue, datatype);
                }
            }
        }
        return literal;
    }

    protected OWLLiteral parseFloat(String lexicalValue, OWLDatatype datatype) {
        if (isNegativeZero(lexicalValue)) {
            return negativeFloatZero;
        }
        try {
            float f = Float.parseFloat(lexicalValue);
            return getOWLLiteral(f);
        } catch (@SuppressWarnings("unused") NumberFormatException e) {
            return getBasicLiteral(lexicalValue, datatype);
        }
    }

    protected OWLLiteral getBasicLiteral(String lexicalValue, OWLDatatype datatype) {
        return getBasicLiteral(lexicalValue, "", datatype);
    }

    protected OWLLiteral getBasicLiteral(String lexicalValue, String lang,
        @Nullable OWLDatatype datatype) {
        if (datatype == null || datatype.isRDFPlainLiteral() || datatype.equals(LANGSTRING)) {
            return new OWLLiteralImplPlain(lexicalValue, lang);
        }
        return new OWLLiteralImpl(lexicalValue, lang, datatype);
    }
}
