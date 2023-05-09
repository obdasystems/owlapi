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
import static org.semanticweb.owlapi.utilities.OWLAPIStreamUtils.asUnorderedSet;
import static org.semanticweb.owlapi.utility.CollectionFactory.createSyncMap;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.stream.Stream;

import javax.annotation.Nullable;
import javax.inject.Inject;

import org.semanticweb.owlapi.documents.FileDocumentSource;
import org.semanticweb.owlapi.documents.IRIDocumentSource;
import org.semanticweb.owlapi.documents.StreamDocumentSource;
import org.semanticweb.owlapi.impl.concurrent.ConcurrentPriorityCollection;
import org.semanticweb.owlapi.io.OWLOntologyDocumentSource;
import org.semanticweb.owlapi.io.OWLParserFactory;
import org.semanticweb.owlapi.io.OWLStorerFactory;
import org.semanticweb.owlapi.io.OntologyIRIMappingNotFoundException;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AddImport;
import org.semanticweb.owlapi.model.AddOntologyAnnotation;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.DefaultChangeBroadcastStrategy;
import org.semanticweb.owlapi.model.DefaultImpendingChangeBroadcastStrategy;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.ImpendingOWLOntologyChangeBroadcastStrategy;
import org.semanticweb.owlapi.model.ImpendingOWLOntologyChangeListener;
import org.semanticweb.owlapi.model.MissingImportEvent;
import org.semanticweb.owlapi.model.MissingImportListener;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLAxiomChange;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.model.OWLMutableOntology;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyAlreadyExistsException;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyChangeBroadcastStrategy;
import org.semanticweb.owlapi.model.OWLOntologyChangeListener;
import org.semanticweb.owlapi.model.OWLOntologyChangeProgressListener;
import org.semanticweb.owlapi.model.OWLOntologyChangeVetoException;
import org.semanticweb.owlapi.model.OWLOntologyChangesVetoedListener;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyDocumentAlreadyExistsException;
import org.semanticweb.owlapi.model.OWLOntologyFactory;
import org.semanticweb.owlapi.model.OWLOntologyFactoryNotFoundException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.model.OWLOntologyLoaderListener;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyRenameException;
import org.semanticweb.owlapi.model.OWLRuntimeException;
import org.semanticweb.owlapi.model.OntologyConfigurator;
import org.semanticweb.owlapi.model.PriorityCollectionSorting;
import org.semanticweb.owlapi.model.RemoveAxiom;
import org.semanticweb.owlapi.model.RemoveImport;
import org.semanticweb.owlapi.model.SetOntologyID;
import org.semanticweb.owlapi.model.UnknownOWLOntologyException;
import org.semanticweb.owlapi.model.UnloadableImportException;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.model.parameters.OntologyCopy;
import org.semanticweb.owlapi.utilities.PriorityCollection;
import org.semanticweb.owlapi.utility.OWLAnnotationPropertyTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Matthew Horridge, The University Of Manchester, Bio-Health Informatics Group
 * @since 2.0.0
 */
public class OWLOntologyManagerImpl
    implements OWLOntologyManager, OWLOntologyFactory.OWLOntologyCreationHandler {

    private static final String BADLISTENER = "BADLY BEHAVING LISTENER: {} has been removed";
    private static final Logger LOGGER = LoggerFactory.getLogger(OWLOntologyManagerImpl.class);
    protected final Map<OWLOntologyID, OWLOntology> ontologiesByID = createSyncMap();
    protected final Map<OWLOntologyID, IRI> documentIRIsByID = createSyncMap();
    protected final Map<OWLOntologyID, OWLDocumentFormat> ontologyFormatsByOntology =
        createSyncMap();
    protected final Map<OWLImportsDeclaration, OWLOntologyID> ontologyIDsByImportsDeclaration =
        createSyncMap();
    protected final AtomicInteger loadCount = new AtomicInteger(0);
    protected final AtomicInteger importsLoadCount = new AtomicInteger(0);
    protected final Map<IRI, Object> importedIRIs = createSyncMap();
    protected final OWLDataFactory dataFactory;
    protected final Map<OWLOntologyID, Set<OWLOntology>> importsClosureCache = createSyncMap();
    // XXX not covered by write lock
    protected final List<MissingImportListener> missingImportsListeners =
        new CopyOnWriteArrayList<>();
    protected final List<OWLOntologyLoaderListener> loaderListeners = new CopyOnWriteArrayList<>();
    protected final List<OWLOntologyChangeProgressListener> progressListeners =
        new CopyOnWriteArrayList<>();
    protected final AtomicLong autoGeneratedURICounter = new AtomicLong();
    private final AtomicBoolean broadcastChanges = new AtomicBoolean(true);
    protected OWLOntologyChangeBroadcastStrategy defaultChangeBroadcastStrategy =
        new DefaultChangeBroadcastStrategy();
    protected ImpendingOWLOntologyChangeBroadcastStrategy defaultImpendingChangeBroadcastStrategy =
        new DefaultImpendingChangeBroadcastStrategy();
    private transient Map<OWLOntologyChangeListener, OWLOntologyChangeBroadcastStrategy> listenerMap =
        createSyncMap();
    private transient Map<ImpendingOWLOntologyChangeListener, ImpendingOWLOntologyChangeBroadcastStrategy> impendingChangeListenerMap =
        createSyncMap();
    private transient List<OWLOntologyChangesVetoedListener> vetoListeners = new ArrayList<>();
    private OntologyConfigurator configProvider = new OntologyConfigurator();
    protected final PriorityCollection<OWLOntologyIRIMapper> documentMappers;
    protected final PriorityCollection<OWLOntologyFactory> ontologyFactories;
    protected final PriorityCollection<OWLParserFactory> parserFactories;
    protected final PriorityCollection<OWLStorerFactory> ontologyStorers;
    private final Lock readLock;
    private final Lock writeLock;
    private final ReadWriteLock lock;

    /**
     * @param dataFactory data factory
     * @param readWriteLock lock
     */
    @Inject
    public OWLOntologyManagerImpl(OWLDataFactory dataFactory, ReadWriteLock readWriteLock) {
        this(dataFactory, readWriteLock, PriorityCollectionSorting.ON_SET_INJECTION_ONLY);
    }

    /**
     * @param dataFactory data factory
     * @param readWriteLock lock
     * @param sorting sorting option
     */
    public OWLOntologyManagerImpl(OWLDataFactory dataFactory, ReadWriteLock readWriteLock,
        PriorityCollectionSorting sorting) {
        this.dataFactory = checkNotNull(dataFactory, "dataFactory cannot be null");
        readLock = readWriteLock.readLock();
        writeLock = readWriteLock.writeLock();
        lock = readWriteLock;
        documentMappers = new ConcurrentPriorityCollection<>(readWriteLock, sorting);
        ontologyFactories = new ConcurrentPriorityCollection<>(readWriteLock, sorting);
        parserFactories = new ConcurrentPriorityCollection<>(readWriteLock, sorting);
        ontologyStorers = new ConcurrentPriorityCollection<>(readWriteLock, sorting);
    }

    @Override
    public void clearOntologies() {
        writeLock.lock();
        try {
            documentIRIsByID.clear();
            impendingChangeListenerMap.clear();
            importedIRIs.clear();
            importsClosureCache.clear();
            listenerMap.clear();
            loaderListeners.clear();
            missingImportsListeners.clear();
            ontologiesByID.values().forEach(o -> o.setOWLOntologyManager(null));
            ontologiesByID.clear();
            ontologyFormatsByOntology.clear();
            ontologyIDsByImportsDeclaration.clear();
            progressListeners.clear();
            vetoListeners.clear();
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public OntologyConfigurator getOntologyConfigurator() {
        readLock.lock();
        try {
            return configProvider;
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void setOntologyConfigurator(OntologyConfigurator configurator) {
        writeLock.lock();
        try {
            configProvider = configurator;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public OWLDataFactory getOWLDataFactory() {
        return dataFactory;
    }

    @Override
    public Stream<OWLOntology> ontologies() {
        readLock.lock();
        try {
            // XXX investigate lockable access to streams
            return ontologiesByID.values().stream();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Stream<OWLOntology> ontologies(OWLAxiom axiom) {
        readLock.lock();
        try {
            // XXX check default
            return ontologies().filter(o -> o.containsAxiom(axiom));
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean contains(OWLOntology ontology) {
        readLock.lock();
        try {
            return ontologiesByID.containsValue(ontology);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean contains(IRI iri) {
        checkNotNull(iri, "Ontology IRI cannot be null");
        readLock.lock();
        try {
            return ids().anyMatch(o -> o.match(iri));
        } finally {
            readLock.unlock();
        }
        // FIXME: ParsableOWLOntologyFactory seems to call this method with a
        // document/physical IRI,
        // but this method fails the general case where the ontology was loaded
        // from the given IRI directly, but was then renamed
    }

    /**
     * @return stream of ids
     */
    protected Stream<OWLOntologyID> ids() {
        readLock.lock();
        try {
            return ontologiesByID.keySet().stream();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean contains(OWLOntologyID id) {
        if (id.isAnonymous()) {
            return false;
        }
        readLock.lock();
        try {
            if (ontologiesByID.containsKey(id)) {
                return true;
            }
            return ids().anyMatch(id::match);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean containsVersion(IRI iri) {
        readLock.lock();
        try {
            return ids().anyMatch(o -> o.matchVersion(iri));
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Stream<OWLOntologyID> ontologyIDsByVersion(IRI iri) {
        readLock.lock();
        try {
            return ids().filter(o -> o.matchVersion(iri));
        } finally {
            readLock.unlock();
        }
    }

    @Override
    @Nullable
    public OWLOntology getOntology(IRI iri) {
        OWLOntologyID ontologyID =
            getOWLDataFactory().getOWLOntologyID(Optional.ofNullable(iri), Optional.empty());
        readLock.lock();
        try {
            OWLOntology result = ontologiesByID.get(ontologyID);
            if (result != null) {
                return result;
            }
            Optional<Entry<OWLOntologyID, OWLOntology>> findAny =
                ontologiesByID.entrySet().stream().filter(o -> o.getKey().match(iri)).findAny();
            return findAny.isPresent() ? findAny.get().getValue() : null;
        } finally {
            readLock.unlock();
        }
    }

    @Override
    @Nullable
    public OWLOntology getOntology(OWLOntologyID id) {
        readLock.lock();
        try {
            OWLOntology result = ontologiesByID.get(id);
            if (result == null && id.isNamed()) {
                Optional<OWLOntologyID> findAny =
                    ids().filter(o -> o.matchOntology(id.getOntologyIRI().get())).findAny();
                if (findAny.isPresent()) {
                    result = ontologiesByID.get(findAny.get());
                }
            }
            // HACK: This extra clause is necessary to make getOntology match
            // the behaviour of createOntology in cases where a documentIRI has
            // been recorded, based on the mappers, but an ontology has not
            // been stored in ontologiesByID
            if (result == null) {
                checkDocumentIRI(id);
            }
            return result;
        } finally {
            readLock.unlock();
        }
    }

    protected void checkDocumentIRI(OWLOntologyID id) {
        IRI documentIRI = getDocumentIRIFromMappers(id);
        if (documentIRI != null && documentIRIsByID.values().contains(documentIRI)) {
            throw new OWLRuntimeException(
                new OWLOntologyDocumentAlreadyExistsException(documentIRI));
        }
    }

    @Override
    public Stream<OWLOntology> versions(IRI ontology) {
        readLock.lock();
        try {
            return OWLOntologyManager.super.versions(ontology);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    @Nullable
    public OWLOntology getImportedOntology(OWLImportsDeclaration declaration) {
        readLock.lock();
        try {
            OWLOntologyID ontologyID = ontologyIDsByImportsDeclaration.get(declaration);
            if (ontologyID == null) {
                // No such ontology has been loaded through an import
                // declaration, but it might have been loaded manually.
                // Using the IRI to retrieve it will either find the ontology or
                // return null.
                // Last possibility is an import by document IRI; if the
                // ontology is not found by IRI, check by document IRI.
                OWLOntology ontology = getOntology(declaration.getIRI());
                if (ontology == null) {
                    ontology = getOntologyByDocumentIRI(declaration.getIRI());
                }
                return ontology;
            } else {
                return getOntology(ontologyID);
            }
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Stream<OWLOntology> directImports(OWLOntology ontology) {
        readLock.lock();
        try {
            if (!contains(ontology)) {
                throw new UnknownOWLOntologyException(ontology.getOntologyID());
            }
            return ontology.importsDeclarations().map(this::getImportedOntology)
                .filter(Objects::nonNull);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Stream<OWLOntology> imports(OWLOntology ontology) {
        readLock.lock();
        try {
            // XXX caches for imports and imports closure sets with Caffeine
            return getImports(ontology, new TreeSet<>()).stream();
        } finally {
            readLock.unlock();
        }
    }

    /**
     * A method that gets the imports of a given ontology.
     * 
     * @param ont The ontology whose (transitive) imports are to be retrieved.
     * @param result A place to store the result - the transitive closure of the imports will be
     *        stored in this result set.
     * @return modified result
     */
    private Set<OWLOntology> getImports(OWLOntology ont, Set<OWLOntology> result) {
        readLock.lock();
        try {
            directImports(ont).filter(result::add).forEach(o -> getImports(o, result));
            return result;
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Stream<OWLOntology> importsClosure(OWLOntology ontology) {
        readLock.lock();
        try {
            OWLOntologyID id = ontology.getOntologyID();
            return importsClosureCache
                .computeIfAbsent(id, i -> getImportsClosure(ontology, new LinkedHashSet<>()))
                .stream();
        } finally {
            readLock.unlock();
        }
    }

    /**
     * A recursive method that gets the reflexive transitive closure of the ontologies that are
     * imported by this ontology.
     * 
     * @param ontology The ontology whose reflexive transitive closure is to be retrieved
     * @param ontologies a place to store the result
     * @return modified ontologies
     */
    private Set<OWLOntology> getImportsClosure(OWLOntology ontology, Set<OWLOntology> ontologies) {
        readLock.lock();
        try {
            ontologies.add(ontology);
            directImports(ontology).filter(o -> !ontologies.contains(o))
                .forEach(o -> getImportsClosure(o, ontologies));
            return ontologies;
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public List<OWLOntology> getSortedImportsClosure(OWLOntology ontology) {
        readLock.lock();
        try {
            return ontology.importsClosure().sorted().toList();
        } finally {
            readLock.unlock();
        }
    }

    // Methods to create, load and reload ontologies
    @Override
    public void ontologyCreated(OWLOntology ontology) {
        // This method is called when a factory that we have asked to create or
        // load an ontology has created the ontology. We add the ontology to the
        // set of loaded ontologies.
        addOntology(ontology);
    }

    @Override
    public void setOntologyFormat(OWLOntology ontology, OWLDocumentFormat ontologyFormat) {
        writeLock.lock();
        try {
            OWLOntologyID ontologyID = ontology.getOntologyID();
            ontologyFormatsByOntology.put(ontologyID, ontologyFormat);
        } finally {
            writeLock.unlock();
        }
    }

    @Nullable
    @Override
    public OWLDocumentFormat getOntologyFormat(OWLOntology ontology) {
        readLock.lock();
        try {
            OWLOntologyID ontologyID = ontology.getOntologyID();
            return ontologyFormatsByOntology.get(ontologyID);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public OWLOntology createOntology(OWLOntologyID ontologyID)
        throws OWLOntologyCreationException {
        writeLock.lock();
        try {
            OWLOntology ontology = ontologiesByID.get(ontologyID);
            if (ontology != null) {
                throw new OWLOntologyAlreadyExistsException(ontologyID);
            }
            IRI documentIRI = computeDocumentIRI(ontologyID);
            if (documentIRIsByID.values().contains(documentIRI)) {
                throw new OWLOntologyDocumentAlreadyExistsException(documentIRI);
            }
            for (OWLOntologyFactory factory : ontologyFactories) {
                if (factory.canCreateFromDocumentIRI(documentIRI)) {
                    documentIRIsByID.put(ontologyID, documentIRI);
                    factory.setLock(lock);
                    return factory.createOWLOntology(this, ontologyID, documentIRI, this,
                        configProvider);
                }
            }
            throw new OWLOntologyFactoryNotFoundException(documentIRI.toString());
        } finally {
            writeLock.unlock();
        }
    }

    protected IRI computeDocumentIRI(OWLOntologyID ontologyID) {
        IRI documentIRI = getDocumentIRIFromMappers(ontologyID);
        if (documentIRI == null) {
            if (ontologyID.isNamed()) {
                documentIRI = ontologyID.getDefaultDocumentIRI().orElse(null);
            } else {
                documentIRI = dataFactory.generateDocumentIRI();
            }
        }
        return documentIRI;
    }

    @Override
    public OWLOntology createOntology(IRI ontologyIRI, Stream<OWLOntology> ontologies,
        boolean copyLogicalAxiomsOnly) throws OWLOntologyCreationException {
        writeLock.lock();
        try {
            if (contains(ontologyIRI)) {
                throw new OWLOntologyAlreadyExistsException(
                    dataFactory.getOWLOntologyID(ontologyIRI));
            }
            OWLOntology ont = createOntology(ontologyIRI);
            ont.addAxioms(
                ontologies.flatMap(o -> copyLogicalAxiomsOnly ? o.logicalAxioms() : o.axioms()));
            return ont;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public OWLOntology createOntology(Stream<OWLAxiom> axioms, IRI ontologyIRI)
        throws OWLOntologyCreationException {
        writeLock.lock();
        try {
            if (contains(ontologyIRI)) {
                throw new OWLOntologyAlreadyExistsException(
                    dataFactory.getOWLOntologyID(ontologyIRI));
            }
            OWLOntology ont = createOntology(ontologyIRI);
            ont.addAxioms(axioms);
            return ont;
        } finally {
            writeLock.unlock();
        }
    }

    private static class OntologyCopyWrapper implements OntologyCopy {
        private OntologyCopy delegate;

        public OntologyCopyWrapper(OntologyCopy delegate) {
            this.delegate = delegate;
        }

        @Override
        public boolean applyToAxiomsAndAnnotationsOnly() {
            return delegate.applyToAxiomsAndAnnotationsOnly();
        }

        @Override
        public boolean applyToImportsClosure() {
            return false;
        }

        @Override
        public boolean shouldMove() {
            return delegate.shouldMove();
        }
    }

    @Override
    public OWLOntology copyOntology(OWLOntology toCopy, OntologyCopy settings)
        throws OWLOntologyCreationException {
        checkNotNull(toCopy);
        checkNotNull(settings);
        // if the imports closure must be treated the same way, do so for all of the ontologies
        // contained - except this one
        if (settings.applyToImportsClosure()) {
            OntologyCopy newSettings = new OntologyCopyWrapper(settings);
            List<OWLOntology> importsToCopy =
                toCopy.importsClosure().filter(x -> !toCopy.equals(x)).toList();
            for (OWLOntology o : importsToCopy) {
                copyOntology(o, newSettings);
            }
        }
        writeLock.lock();
        try {
            OWLOntology toReturn = null;
            if (settings.shouldMove()) {
                toReturn = toCopy;
                ontologiesByID.put(toReturn.getOntologyID(), toReturn);
            } else {
                OWLOntology o = createOntology(toCopy.getOntologyID());
                AxiomType.axiomTypes().forEach(t -> o.addAxioms(toCopy.axioms(t)));
                toCopy.annotations().forEach(a -> o.applyChange(new AddOntologyAnnotation(o, a)));
                toCopy.importsDeclarations().forEach(a -> o.applyChange(new AddImport(o, a)));
                toReturn = o;
            }
            // toReturn now initialized
            OWLOntologyManager m = toCopy.getOWLOntologyManager();
            if (settings.shouldMove() || !settings.applyToAxiomsAndAnnotationsOnly()) {
                setOntologyDocumentIRI(toReturn, m.getOntologyDocumentIRI(toCopy));
                OWLDocumentFormat ontologyFormat = m.getOntologyFormat(toCopy);
                if (ontologyFormat != null) {
                    setOntologyFormat(toReturn, ontologyFormat);
                }
            }
            if (settings.shouldMove()) {
                m.removeOntology(toCopy);
                // at this point toReturn and toCopy are the same object
                // change the manager on the ontology
                toReturn.setOWLOntologyManager(this);
                // change the lock on the ontology
                if (toReturn instanceof OWLMutableOntology mo) {
                    mo.setLock(lock);
                }
            }
            return toReturn;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public OWLOntology loadOntology(IRI ontologyIRI) throws OWLOntologyCreationException {
        // if an ontology cyclically imports itself, the manager should not try
        // to download from the
        // same URL twice.
        Object value = new Object();
        if (!importedIRIs.containsKey(ontologyIRI)) {
            importedIRIs.put(ontologyIRI, value);
        }
        OWLOntology loadOntology = loadOntology(ontologyIRI, false, configProvider);
        importedIRIs.remove(ontologyIRI, value);
        return loadOntology;
    }

    protected OWLOntology loadOntology(IRI iri, boolean allowExists,
        OntologyConfigurator configuration) throws OWLOntologyCreationException {
        writeLock.lock();
        try {
            OWLOntology ontByID = null;
            // Check for matches on the ontology IRI first
            Optional<OWLOntologyID> findAny = ids().filter(o -> o.match(iri)).findAny();
            if (findAny.isPresent()) {
                ontByID = ontologiesByID.get(findAny.get());
            }
            if (ontByID != null) {
                return ontByID;
            }
            OWLOntologyID id = dataFactory.getOWLOntologyID(iri);
            IRI documentIRI = getDocumentIRIFromMappers(id);
            if (documentIRI != null) {
                if (documentIRIsByID.values().contains(documentIRI) && !allowExists) {
                    throw new OWLOntologyDocumentAlreadyExistsException(documentIRI);
                }
                // The ontology might be being loaded, but its IRI might
                // not have been set (as is probably the case with RDF/XML!)
                OWLOntology ontByDocumentIRI = loadOntologyByDocumentIRI(documentIRI);
                if (ontByDocumentIRI != null) {
                    return ontByDocumentIRI;
                }
            } else {
                // Nothing we can do here. We can't get a document IRI to load
                // the ontology from.
                throw new OntologyIRIMappingNotFoundException(iri);
            }
            if (documentIRIsByID.values().contains(documentIRI) && !allowExists) {
                throw new OWLOntologyDocumentAlreadyExistsException(documentIRI);
            }
            // The ontology might be being loaded, but its IRI might
            // not have been set (as is probably the case with RDF/XML!)
            OWLOntology ontByDocumentIRI = loadOntologyByDocumentIRI(documentIRI);
            if (ontByDocumentIRI != null) {
                return ontByDocumentIRI;
            }
            return loadOntology(iri, new IRIDocumentSource(documentIRI.toString(), null, null),
                configuration);
        } finally {
            writeLock.unlock();
        }
    }

    @Nullable
    private OWLOntology loadOntologyByDocumentIRI(IRI iri) {
        readLock.lock();
        try {
            Optional<Entry<OWLOntologyID, IRI>> findAny = documentIRIsByID.entrySet().stream()
                .filter(o -> iri.equals(o.getValue())).findAny();
            if (findAny.isPresent()) {
                return getOntology(findAny.get().getKey());
            }
            return null;
        } finally {
            readLock.unlock();
        }
    }

    @Nullable
    private OWLOntology getOntologyByDocumentIRI(IRI documentIRI) {
        readLock.lock();
        try {
            Optional<Entry<OWLOntologyID, IRI>> findAny = documentIRIsByID.entrySet().stream()
                .filter(o -> documentIRI.equals(o.getValue())).findAny();
            if (findAny.isPresent()) {
                return ontologiesByID.get(findAny.get().getKey());
            }
            return null;
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public OWLOntology loadOntologyFromOntologyDocument(IRI documentIRI)
        throws OWLOntologyCreationException {
        // Ontology URI not known in advance
        return loadOntology(null, new IRIDocumentSource(documentIRI.toString(), null, null),
            configProvider);
    }

    @Override
    public OWLOntology loadOntologyFromOntologyDocument(OWLOntologyDocumentSource documentSource)
        throws OWLOntologyCreationException {
        // Ontology URI not known in advance
        return loadOntology(null, documentSource, configProvider);
    }

    @Override
    public OWLOntology loadOntologyFromOntologyDocument(OWLOntologyDocumentSource documentSource,
        OntologyConfigurator conf) throws OWLOntologyCreationException {
        return loadOntology(null, documentSource, conf);
    }

    @Override
    public OWLOntology loadOntologyFromOntologyDocument(File file)
        throws OWLOntologyCreationException {
        return loadOntologyFromOntologyDocument(new FileDocumentSource(file));
    }

    @Override
    public OWLOntology loadOntologyFromOntologyDocument(InputStream inputStream)
        throws OWLOntologyCreationException {
        try (StreamDocumentSource in = new StreamDocumentSource(inputStream)) {
            return loadOntologyFromOntologyDocument(in);
        } catch (OWLOntologyCreationException e) {
            throw e;
        } catch (Exception e) {
            throw new OWLRuntimeException(e);
        }
    }

    /**
     * This is the method that all the other load method delegate to.
     * 
     * @param ontologyIRI The URI of the ontology to be loaded. This is only used to report to
     *        listeners and may be {@code null}
     * @param documentSource The input source that specifies where the ontology should be loaded
     *        from.
     * @param configuration load configuration
     * @return The ontology that was loaded.
     * @throws OWLOntologyCreationException If the ontology could not be loaded.
     */
    protected OWLOntology loadOntology(@Nullable IRI ontologyIRI,
        OWLOntologyDocumentSource documentSource, OntologyConfigurator configuration)
        throws OWLOntologyCreationException {
        writeLock.lock();
        try {
            if (loadCount.get() != importsLoadCount.get()) {
                LOGGER.warn(
                    "Runtime Warning: Parsers should load imported ontologies using the makeImportLoadRequest method.");
            }
            IRI documentIRI = dataFactory.getIRI(documentSource.getDocumentIRI());
            fireStartedLoadingEvent(getOWLDataFactory().getOWLOntologyID(ontologyIRI), documentIRI,
                loadCount.get() > 0);
            loadCount.incrementAndGet();
            broadcastChanges.set(false);
            Exception ex = null;
            OWLOntologyID idOfLoadedOntology = getOWLDataFactory().getOWLOntologyID();
            try {
                OWLOntology o = load(documentSource, configuration);
                if (o != null) {
                    idOfLoadedOntology = o.getOntologyID();
                    return o;
                }
            } catch (UnloadableImportException | OWLOntologyCreationException e) {
                ex = e;
                throw e;
            } catch (OWLRuntimeException e) {
                if (e.getCause() instanceof OWLOntologyCreationException crEx) {
                    ex = crEx;
                    throw crEx;
                }
                throw e;
            } finally {
                if (loadCount.decrementAndGet() == 0) {
                    broadcastChanges.set(true);
                    // Completed loading ontology and imports
                }
                fireFinishedLoadingEvent(idOfLoadedOntology, documentIRI, loadCount.get() > 0, ex);
            }
            throw new OWLOntologyFactoryNotFoundException(documentIRI.toString());
        } finally {
            writeLock.unlock();
        }
    }

    @Nullable
    protected OWLOntology load(OWLOntologyDocumentSource documentSource,
        OntologyConfigurator configuration) throws OWLOntologyCreationException {
        // Check if this is an IRI source and the IRI has already been loaded -
        // this will stop
        // ontologies
        // that cyclically import themselves from being loaded twice.
        if (documentSource instanceof IRIDocumentSource source) {
            Optional<Entry<OWLOntologyID, IRI>> findAny = documentIRIsByID.entrySet().stream()
                .filter(v -> Objects.equals(source.getDocumentIRI(), v.getValue().toString()))
                .findAny();
            if (findAny.isPresent()) {
                return getOntology(findAny.get().getKey());
            }
        }
        IRI documentIRI = dataFactory.getIRI(documentSource.getDocumentIRI());
        return attemptLoading(documentSource, configuration, documentIRI);
    }

    protected OWLOntology attemptLoading(OWLOntologyDocumentSource documentSource,
        OntologyConfigurator configuration, IRI documentIRI) throws OWLOntologyCreationException {
        for (OWLOntologyFactory factory : ontologyFactories) {
            if (factory.canAttemptLoading(documentSource)) {
                try {
                    // Note - there is no need to add the ontology here,
                    // because it will be added
                    // when the ontology is created.
                    factory.setLock(lock);
                    OWLOntology ontology =
                        factory.loadOWLOntology(this, documentSource, this, configuration);
                    if (configuration.shouldRepairIllegalPunnings()) {
                        fixIllegalPunnings(ontology);
                    }
                    // Store the ontology to the document IRI mapping
                    documentIRIsByID.put(ontology.getOntologyID(), documentIRI);
                    if (ontology instanceof HasTrimToSize toTrim
                        && configuration.shouldTrimToSize()) {
                        toTrim.trimToSize();
                    }
                    return ontology;
                } catch (OWLOntologyRenameException e) {
                    // We loaded an ontology from a document and the
                    // ontology turned out to have an IRI the same
                    // as a previously loaded ontology
                    throw new OWLOntologyAlreadyExistsException(e.getOntologyID(), e);
                }
            }
        }
        return null;
    }

    protected void fixIllegalPunnings(OWLOntology o) {
        Collection<IRI> illegals = o.determineIllegalPunnings(true);
        Map<IRI, Set<OWLDeclarationAxiom>> illegalDeclarations = new HashMap<>();
        o.axioms(AxiomType.DECLARATION, Imports.INCLUDED)
            .filter(d -> illegals.contains(d.getEntity().getIRI())).forEach(d -> illegalDeclarations
                .computeIfAbsent(d.getEntity().getIRI(), x -> new HashSet<>()).add(d));
        Map<OWLEntity, OWLEntity> replacementMap = new HashMap<>();
        for (Map.Entry<IRI, Set<OWLDeclarationAxiom>> e : illegalDeclarations.entrySet()) {
            if (e.getValue().size() == 1) {
                // One declaration only: illegal punning comes from use or from
                // defaulting of types
                OWLDeclarationAxiom correctDeclaration = e.getValue().iterator().next();
                // currently we only know how to fix the incorrect defaulting of
                // properties to annotation properties
                OWLEntity entity = correctDeclaration.getEntity();
                if (entity.isOWLDataProperty() || entity.isOWLObjectProperty()) {
                    OWLAnnotationProperty wrongProperty =
                        dataFactory.getOWLAnnotationProperty(entity.getIRI());
                    replacementMap.put(wrongProperty, entity);
                }
            } else {
                // Multiple declarations: bad data. Cannot be repaired
                // automatically.
                String errorMessage =
                    "Illegal redeclarations of entities: reuse of entity {} in punning not allowed {}";
                LOGGER.warn(errorMessage, e.getKey(), e.getValue());
            }
        }
        OWLAnnotationPropertyTransformer changer =
            new OWLAnnotationPropertyTransformer(replacementMap, dataFactory);
        List<OWLAxiomChange> list = new ArrayList<>();
        o.importsClosure().forEach(ont -> {
            for (OWLEntity e : replacementMap.keySet()) {
                // all axioms referring the annotation property
                // must be rebuilt.
                ont.referencingAxioms(e).forEach(ax -> {
                    list.add(new RemoveAxiom(ont, ax));
                    list.add(new AddAxiom(ont, changer.transformObject(ax)));
                });
            }
        });
        o.applyChanges(list);
    }

    @Override
    public void removeOntology(OWLOntology ontology) {
        // XXX check default
        writeLock.lock();
        try {
            removeOntology(ontology.getOntologyID());
            ontology.setOWLOntologyManager(null);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void removeOntology(OWLOntologyID ontologyID) {
        writeLock.lock();
        try {
            OWLOntology o = ontologiesByID.remove(ontologyID);
            ontologyFormatsByOntology.remove(ontologyID);
            documentIRIsByID.remove(ontologyID);
            removeValue(ontologyIDsByImportsDeclaration, ontologyID);
            removeValue(importedIRIs, ontologyID);
            if (o != null) {
                o.setOWLOntologyManager(null);
                resetImportsClosureCache();
            }
        } finally {
            writeLock.unlock();
        }
    }

    protected <Q, S> void removeValue(Map<Q, S> map, S id) {
        List<Q> keys = map.entrySet().stream().filter(e -> e.getValue().equals(id))
            .map(Map.Entry::getKey).toList();
        keys.forEach(map::remove);
    }

    private void addOntology(OWLOntology ont) {
        writeLock.lock();
        try {
            ontologiesByID.put(ont.getOntologyID(), ont);
            resetImportsClosureCache();
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public IRI getOntologyDocumentIRI(OWLOntology ontology) {
        readLock.lock();
        try {
            if (!contains(ontology)) {
                throw new UnknownOWLOntologyException(ontology.getOntologyID());
            }
            return verifyNotNull(documentIRIsByID.get(ontology.getOntologyID()));
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void setOntologyDocumentIRI(OWLOntology ontology, IRI documentIRI) {
        writeLock.lock();
        try {
            if (!ontologiesByID.containsKey(ontology.getOntologyID())) {
                throw new UnknownOWLOntologyException(ontology.getOntologyID());
            }
            documentIRIsByID.put(ontology.getOntologyID(), documentIRI);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Handles a rename of an ontology. This method should only be called *after* the change has
     * been applied
     * 
     * @param oldID The original ID of the ontology
     * @param newID The new ID of the ontology
     */
    private void renameOntology(OWLOntologyID oldID, OWLOntologyID newID) {
        OWLOntology ont = ontologiesByID.get(oldID);
        if (ont == null) {
            // Nothing to rename!
            return;
        }
        ontologiesByID.remove(oldID);
        ontologiesByID.put(newID, ont);
        if (ontologyFormatsByOntology.containsKey(oldID)) {
            ontologyFormatsByOntology.put(newID, ontologyFormatsByOntology.remove(oldID));
        }
        IRI documentIRI = documentIRIsByID.remove(oldID);
        if (documentIRI != null) {
            documentIRIsByID.put(newID, documentIRI);
        }
        resetImportsClosureCache();
    }

    protected void resetImportsClosureCache() {
        writeLock.lock();
        try {
            importsClosureCache.clear();
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public PriorityCollection<OWLStorerFactory> getOntologyStorers() {
        // Locking done by collection
        return ontologyStorers;
    }

    @Override
    @Inject
    public void setOntologyStorers(Set<OWLStorerFactory> storers) {
        // Locking done by collection
        ontologyStorers.set(storers);
    }

    @Override
    public PriorityCollection<OWLOntologyIRIMapper> getIRIMappers() {
        // Locking done by collection
        return documentMappers;
    }

    @Override
    @Inject
    public void setIRIMappers(Set<OWLOntologyIRIMapper> mappers) {
        // Locking done by collection
        documentMappers.set(mappers);
    }

    @Override
    public PriorityCollection<OWLParserFactory> getOntologyParsers() {
        // Locking done by collection
        return parserFactories;
    }

    @Override
    @Inject
    public void setOntologyParsers(Set<OWLParserFactory> parsers) {
        // Locking done by collection
        parserFactories.set(parsers);
    }

    @Override
    public PriorityCollection<OWLOntologyFactory> getOntologyFactories() {
        // Locking done by collection
        return ontologyFactories;
    }

    @Override
    @Inject
    public void setOntologyFactories(Set<OWLOntologyFactory> factories) {
        // Locking done by collection
        ontologyFactories.set(factories);
    }

    /**
     * Uses the mapper mechanism to obtain an ontology document IRI from an ontology IRI.
     * 
     * @param ontologyID The ontology ID for which a document IRI is to be retrieved
     * @return The document IRI that corresponds to the ontology IRI, or {@code null} if no physical
     *         URI can be found.
     */
    @Nullable
    private IRI getDocumentIRIFromMappers(OWLOntologyID ontologyID) {
        Optional<IRI> defIRI = ontologyID.getDefaultDocumentIRI();
        if (!defIRI.isPresent()) {
            return null;
        }
        for (OWLOntologyIRIMapper mapper : documentMappers) {
            IRI documentIRI = mapper.getDocumentIRI(defIRI.get());
            if (documentIRI != null) {
                return documentIRI;
            }
        }
        return defIRI.get();
    }

    // Listener stuff - methods to add/remove listeners
    private void readObject(java.io.ObjectInputStream stream)
        throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        configProvider = (OntologyConfigurator) stream.readObject();
        listenerMap = new ConcurrentHashMap<>();
        impendingChangeListenerMap = new ConcurrentHashMap<>();
        vetoListeners = new ArrayList<>();
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeObject(getOntologyConfigurator());
    }

    @Override
    public void addOntologyChangeListener(OWLOntologyChangeListener listener) {
        writeLock.lock();
        try {
            listenerMap.put(listener, defaultChangeBroadcastStrategy);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void broadcastChanges(Collection<? extends OWLOntologyChange> changes) {
        writeLock.lock();
        try {
            if (!broadcastChanges.get()) {
                return;
            }
            for (OWLOntologyChangeListener listener : new ArrayList<>(listenerMap.keySet())) {
                OWLOntologyChangeBroadcastStrategy strategy = listenerMap.get(listener);
                if (strategy == null) {
                    // This listener may have been removed during the broadcast
                    // of the changes, so when we attempt to retrieve it from
                    // the map it isn't there (because we iterate over a copy).
                    continue;
                }
                try {
                    // Handle exceptions on a per listener basis. If we have
                    // badly behaving listeners, we don't want one listener
                    // to prevent the other listeners from receiving events.
                    strategy.broadcastChanges(listener, changes);
                } catch (Exception e) {
                    LOGGER.warn(BADLISTENER, e.getMessage(), e);
                    listenerMap.remove(listener);
                }
            }
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void broadcastImpendingChanges(Collection<? extends OWLOntologyChange> changes) {
        writeLock.lock();
        try {
            if (!broadcastChanges.get()) {
                return;
            }
            for (ImpendingOWLOntologyChangeListener listener : new ArrayList<>(
                impendingChangeListenerMap.keySet())) {
                ImpendingOWLOntologyChangeBroadcastStrategy strategy =
                    impendingChangeListenerMap.get(listener);
                if (strategy != null) {
                    strategy.broadcastChanges(listener, changes);
                }
            }
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void setDefaultChangeBroadcastStrategy(OWLOntologyChangeBroadcastStrategy strategy) {
        writeLock.lock();
        try {
            defaultChangeBroadcastStrategy = strategy;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void addOntologyChangeListener(OWLOntologyChangeListener listener,
        OWLOntologyChangeBroadcastStrategy strategy) {
        writeLock.lock();
        try {
            listenerMap.put(listener, strategy);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void addImpendingOntologyChangeListener(ImpendingOWLOntologyChangeListener listener) {
        writeLock.lock();
        try {
            impendingChangeListenerMap.put(listener, defaultImpendingChangeBroadcastStrategy);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void removeImpendingOntologyChangeListener(ImpendingOWLOntologyChangeListener listener) {
        writeLock.lock();
        try {
            impendingChangeListenerMap.remove(listener);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void removeOntologyChangeListener(OWLOntologyChangeListener listener) {
        writeLock.lock();
        try {
            listenerMap.remove(listener);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void addOntologyChangesVetoedListener(OWLOntologyChangesVetoedListener listener) {
        writeLock.lock();
        try {
            vetoListeners.add(listener);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void removeOntologyChangesVetoedListener(OWLOntologyChangesVetoedListener listener) {
        writeLock.lock();
        try {
            vetoListeners.remove(listener);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void broadcastOntologyChangesVetoed(Collection<? extends OWLOntologyChange> changes,
        OWLOntologyChangeVetoException veto) {
        writeLock.lock();
        try {
            new ArrayList<>(vetoListeners).forEach(l -> l.ontologyChangesVetoed(changes, veto));
        } finally {
            writeLock.unlock();
        }
    }

    // Imports etc.
    @Nullable
    protected OWLOntology loadImports(OWLImportsDeclaration declaration,
        OntologyConfigurator configuration) throws OWLOntologyCreationException {
        writeLock.lock();
        try {
            importsLoadCount.incrementAndGet();
            OWLOntology ont = null;
            try {
                ont = loadOntology(declaration.getIRI(), true, configuration);
            } catch (OWLOntologyCreationException e) {
                if (configuration.getMissingImportHandlingStrategy().throwException()) {
                    throw e;
                } else {
                    // Silent
                    MissingImportEvent evt = new MissingImportEvent(declaration.getIRI(), e);
                    fireMissingImportEvent(evt);
                }
            } finally {
                importsLoadCount.decrementAndGet();
            }
            return ont;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void makeLoadImportRequest(OWLImportsDeclaration declaration) {
        // XXX check default
        writeLock.lock();
        try {
            makeLoadImportRequest(declaration, getOntologyConfigurator());
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void makeLoadImportRequest(OWLImportsDeclaration declaration,
        OntologyConfigurator configuration) {
        writeLock.lock();
        try {
            IRI iri = declaration.getIRI();
            if (!configuration.shouldDisableImportsLoading() && !configuration.isImportIgnored(iri)
                && !importedIRIs.containsKey(iri)) {
                // insert temporary value - we do not know the actual ID yet
                importedIRIs.put(iri, new Object());
                try {
                    OWLOntology ont = loadImports(declaration, configuration);
                    if (ont != null) {
                        ontologyIDsByImportsDeclaration.put(declaration, ont.getOntologyID());
                        importedIRIs.put(iri, ont.getOntologyID());
                    }
                } catch (OWLOntologyCreationException e) {
                    // Wrap as UnloadableImportException and throw
                    throw new UnloadableImportException(e, declaration);
                }
            }
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void addMissingImportListener(MissingImportListener listener) {
        writeLock.lock();
        try {
            missingImportsListeners.add(listener);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void removeMissingImportListener(MissingImportListener listener) {
        writeLock.lock();
        try {
            missingImportsListeners.remove(listener);
        } finally {
            writeLock.unlock();
        }
    }

    protected void fireMissingImportEvent(MissingImportEvent evt) {
        writeLock.lock();
        try {
            new ArrayList<>(missingImportsListeners).forEach(l -> l.importMissing(evt));
        } finally {
            writeLock.unlock();
        }
    }

    // Other listeners etc.
    @Override
    public void addOntologyLoaderListener(OWLOntologyLoaderListener listener) {
        writeLock.lock();
        try {
            loaderListeners.add(listener);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void removeOntologyLoaderListener(OWLOntologyLoaderListener listener) {
        writeLock.lock();
        try {
            loaderListeners.remove(listener);
        } finally {
            writeLock.unlock();
        }
    }

    protected void fireStartedLoadingEvent(OWLOntologyID ontologyID, IRI documentIRI,
        boolean imported) {
        writeLock.lock();
        try {
            for (OWLOntologyLoaderListener listener : new ArrayList<>(loaderListeners)) {
                listener.startedLoadingOntology(new OWLOntologyLoaderListener.LoadingStartedEvent(
                    ontologyID, documentIRI, imported));
            }
        } finally {
            writeLock.unlock();
        }
    }

    protected void fireFinishedLoadingEvent(OWLOntologyID ontologyID, IRI documentIRI,
        boolean imported, @Nullable Exception ex) {
        writeLock.lock();
        try {
            for (OWLOntologyLoaderListener listener : new ArrayList<>(loaderListeners)) {
                listener.finishedLoadingOntology(new OWLOntologyLoaderListener.LoadingFinishedEvent(
                    ontologyID, documentIRI, imported, ex));
            }
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void addOntologyChangeProgessListener(OWLOntologyChangeProgressListener listener) {
        writeLock.lock();
        try {
            progressListeners.add(listener);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void removeOntologyChangeProgessListener(OWLOntologyChangeProgressListener listener) {
        writeLock.lock();
        try {
            progressListeners.remove(listener);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void fireBeginChanges(int size) {
        writeLock.lock();
        try {
            if (!broadcastChanges.get()) {
                return;
            }
            for (OWLOntologyChangeProgressListener listener : progressListeners) {
                try {
                    listener.begin(size);
                } catch (Exception e) {
                    LOGGER.warn(BADLISTENER, e.getMessage(), e);
                    progressListeners.remove(listener);
                }
            }
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void fireEndChanges() {
        writeLock.lock();
        try {
            if (!broadcastChanges.get()) {
                return;
            }
            for (OWLOntologyChangeProgressListener listener : progressListeners) {
                try {
                    listener.end();
                } catch (Exception e) {
                    LOGGER.warn(BADLISTENER, e.getMessage(), e);
                    progressListeners.remove(listener);
                }
            }
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void fireChangeApplied(OWLOntologyChange change) {
        writeLock.lock();
        try {
            if (!broadcastChanges.get()) {
                return;
            }
            if (progressListeners.isEmpty()) {
                return;
            }
            for (OWLOntologyChangeProgressListener listener : progressListeners) {
                try {
                    listener.appliedChange(change);
                } catch (Exception e) {
                    LOGGER.warn(BADLISTENER, e.getMessage(), e);
                    progressListeners.remove(listener);
                }
            }
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void handleImportsChange(OWLOntologyChange change) {
        if (change.isImportChange()) {
            resetImportsClosureCache();
            if (change instanceof AddImport add) {
                OWLImportsDeclaration addImportDeclaration = add.getImportDeclaration();
                IRI iri = addImportDeclaration.getIRI();
                Optional<OWLOntologyID> findFirst =
                    ids().filter(o -> o.match(iri) || o.matchDocument(iri)).findFirst();
                findFirst
                    .ifPresent(o -> ontologyIDsByImportsDeclaration.put(addImportDeclaration, o));
                if (!findFirst.isPresent()) {
                    // then the import does not refer to a known IRI for
                    // ontologies; check for a document IRI to find the ontology
                    // id corresponding to the file location
                    documentIRIsByID.entrySet().stream().filter(o -> o.getValue().equals(iri))
                        .findAny().ifPresent(o -> ontologyIDsByImportsDeclaration
                            .put(addImportDeclaration, o.getKey()));
                }
            } else {
                // Remove the mapping from declaration to ontology
                OWLImportsDeclaration importDeclaration =
                    ((RemoveImport) change).getImportDeclaration();
                ontologyIDsByImportsDeclaration.remove(importDeclaration);
                importedIRIs.remove(importDeclaration.getIRI());
            }
        }
    }

    @Override
    public void handleOntologyIDChange(OWLOntologyChange change) {
        if (!(change instanceof SetOntologyID)) {
            return;
        }
        SetOntologyID setID = (SetOntologyID) change;
        OWLOntology existingOntology = ontologiesByID.get(setID.getNewOntologyID());
        OWLOntology o = setID.getOntology();
        if (existingOntology != null && !o.equals(existingOntology)
            && !o.equalAxioms(existingOntology)) {
            String location = "OWLOntologyManagerImpl.checkForOntologyIDChange()";
            LOGGER.error("{} existing:{}", location, existingOntology);
            LOGGER.error("{} new:{}", location, o);
            Set<OWLLogicalAxiom> diff1 = asUnorderedSet(o.logicalAxioms());
            Set<OWLLogicalAxiom> diff2 = asUnorderedSet(existingOntology.logicalAxioms());
            existingOntology.logicalAxioms().forEach(diff1::remove);
            o.logicalAxioms().forEach(diff2::remove);
            LOGGER.error("{} only in existing:{}", location, diff2);
            LOGGER.error("{} only in new:{}", location, diff1);
            throw new OWLOntologyRenameException(setID.getChangeData(), setID.getNewOntologyID());
        }
        renameOntology(setID.getOriginalOntologyID(), setID.getNewOntologyID());
        resetImportsClosureCache();
    }
}
