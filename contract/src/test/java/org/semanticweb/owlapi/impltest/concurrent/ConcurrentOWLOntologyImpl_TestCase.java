package org.semanticweb.owlapi.impltest.concurrent;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.semanticweb.owlapi.model.parameters.Imports.INCLUDED;

import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.semanticweb.owlapi.impl.concurrent.ConcurrentOWLOntologyImpl;
import org.semanticweb.owlapi.io.OWLOntologyDocumentTarget;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.ChangeReport;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLMutableOntology;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLPrimitive;
import org.semanticweb.owlapi.model.parameters.AxiomAnnotations;
import org.semanticweb.owlapi.utilities.OWLAxiomSearchFilter;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 03/04/15
 */
class ConcurrentOWLOntologyImpl_TestCase {

    private final ReentrantReadWriteLock readWriteLock = mock(ReentrantReadWriteLock.class);
    private final ReentrantReadWriteLock.ReadLock readLock = mock(ReentrantReadWriteLock.ReadLock.class);
    private final ReentrantReadWriteLock.WriteLock writeLock =
        mock(ReentrantReadWriteLock.WriteLock.class);
    private final OWLMutableOntology delegate = mock(OWLMutableOntology.class);
    private final IRI iri = mock(IRI.class);
    private ConcurrentOWLOntologyImpl ontology;

    interface TestConsumer {

        void consume(InOrder i) throws Exception;
    }

    @BeforeEach
    void setUp() {
        when(readWriteLock.readLock()).thenReturn(readLock);
        when(readWriteLock.writeLock()).thenReturn(writeLock);
        when(delegate.applyChanges(anyList()))
        .thenReturn(new ChangeReport(Collections.emptyList(), Collections.emptyList()));
        ontology = spy(new ConcurrentOWLOntologyImpl(delegate, readWriteLock));
    }

    private void readLock(TestConsumer f) {
        InOrder order = Mockito.inOrder(readLock, delegate, readLock);
        order.verify(readLock, times(1)).lock();
        try {
            f.consume(order);
        } catch (Exception e) {
            e.printStackTrace();
        }
        order.verify(readLock, times(1)).unlock();
        verify(writeLock, never()).lock();
        verify(writeLock, never()).unlock();
    }

    private void writeLock(TestConsumer f) {
        InOrder order = Mockito.inOrder(writeLock, delegate, writeLock);
        order.verify(writeLock, times(1)).lock();
        try {
            f.consume(order);
        } catch (Exception e) {
            e.printStackTrace();
        }
        order.verify(writeLock, times(1)).unlock();
        verify(readLock, never()).lock();
        verify(readLock, never()).unlock();
    }

    @Test
    void shouldDelegateTo_isEmpty_withReadLock() {
        ontology.isEmpty();
        readLock(i -> i.verify(delegate).isEmpty());
    }

    @Test
    void shouldDelegateTo_setOWLOntologyManager_withReadLock() {
        ontology.setOWLOntologyManager(manager);
        writeLock(i -> i.verify(delegate).setOWLOntologyManager(manager));
    }

    OWLOntologyManager manager = mock(OWLOntologyManager.class);

    @Test
    void shouldDelegateTo_getOntologyID_withReadLock() {
        ontology.getOntologyID();
        readLock(i -> i.verify(delegate).getOntologyID());
    }

    @Test
    void shouldDelegateTo_isAnonymous_withReadLock() {
        ontology.isAnonymous();
        readLock(i -> i.verify(delegate).isAnonymous());
    }

    OWLEntity entity = mock(OWLEntity.class);

    @Test
    void shouldDelegateTo_isDeclared_withReadLock() {
        ontology.isDeclared(entity, INCLUDED);
        readLock(i -> i.verify(delegate).isDeclared(entity, INCLUDED));
    }

    @Test
    void shouldDelegateTo_isDeclared_withReadLock_2() {
        ontology.isDeclared(entity);
        readLock(i -> i.verify(delegate).isDeclared(entity));
    }

    @Test
    void shouldDelegateTo_saveOntology_withReadLock() throws OWLOntologyStorageException {
        ontology.saveOntology(format, iri);
        readLock(i -> i.verify(delegate).saveOntology(format, iri));
    }

    @Test
    void shouldDelegateTo_saveOntology_withReadLock_2() throws OWLOntologyStorageException {
        ontology.saveOntology(format, outputStream);
        readLock(i -> i.verify(delegate).saveOntology(format, outputStream));
    }

    OutputStream outputStream = mock(OutputStream.class);
    OWLDocumentFormat format = mock(OWLDocumentFormat.class);

    @Test
    void shouldDelegateTo_saveOntology_withReadLock_3() throws OWLOntologyStorageException {
        ontology.saveOntology(target);
        readLock(i -> i.verify(delegate).saveOntology(target));
    }

    OWLOntologyDocumentTarget target = mock(OWLOntologyDocumentTarget.class);

    @Test
    void shouldDelegateTo_saveOntology_withReadLock_4() throws OWLOntologyStorageException {
        ontology.saveOntology(format, target);
        readLock(i -> i.verify(delegate).saveOntology(format, target));
    }

    @Test
    void shouldDelegateTo_saveOntology_withReadLock_5() throws OWLOntologyStorageException {
        ontology.saveOntology();
        readLock(i -> i.verify(delegate).saveOntology());
    }

    @Test
    void shouldDelegateTo_saveOntology_withReadLock_6() throws OWLOntologyStorageException {
        ontology.saveOntology(iri);
        readLock(i -> i.verify(delegate).saveOntology(iri));
    }

    @Test
    void shouldDelegateTo_saveOntology_withReadLock_7() throws OWLOntologyStorageException {
        ontology.saveOntology(outputStream);
        readLock(i -> i.verify(delegate).saveOntology(outputStream));
    }

    @Test
    void shouldDelegateTo_saveOntology_withReadLock_8() throws OWLOntologyStorageException {
        ontology.saveOntology(format);
        readLock(i -> i.verify(delegate).saveOntology(format));
    }

    @Test
    void shouldDelegateTo_isTopEntity_withReadLock() {
        ontology.isTopEntity();
        readLock(i -> i.verify(delegate).isTopEntity());
    }

    @Test
    void shouldDelegateTo_isBottomEntity_withReadLock() {
        ontology.isBottomEntity();
        readLock(i -> i.verify(delegate).isBottomEntity());
    }

    @Test
    void shouldDelegateTo_containsEntityInSignature_withReadLock() {
        ontology.containsEntityInSignature(entity);
        readLock(i -> i.verify(delegate).containsEntityInSignature(entity));
    }

    @Test
    void shouldDelegateTo_getDataPropertiesInSignature_withReadLock() {
        ontology.dataPropertiesInSignature();
        readLock(i -> i.verify(delegate).dataPropertiesInSignature());
    }

    OWLDatatype datatype = mock(OWLDatatype.class);

    @Test
    void shouldDelegateTo_getAxiomCount_withReadLock() {
        ontology.getAxiomCount(AxiomType.SUBCLASS_OF, INCLUDED);
        readLock(i -> i.verify(delegate).getAxiomCount(AxiomType.SUBCLASS_OF, INCLUDED));
    }

    @Test
    void shouldDelegateTo_getAxiomCount_withReadLock_2() {
        ontology.getAxiomCount(INCLUDED);
        readLock(i -> i.verify(delegate).getAxiomCount(INCLUDED));
    }

    @Test
    void shouldDelegateTo_getLogicalAxiomCount_withReadLock() {
        ontology.getLogicalAxiomCount(INCLUDED);
        readLock(i -> i.verify(delegate).getLogicalAxiomCount(INCLUDED));
    }

    @Test
    void shouldDelegateTo_containsAxiom_withReadLock() {
        ontology.containsAxiom(axiom, INCLUDED, AxiomAnnotations.IGNORE_AXIOM_ANNOTATIONS);
        readLock(i -> i.verify(delegate).containsAxiom(axiom, INCLUDED,
            AxiomAnnotations.IGNORE_AXIOM_ANNOTATIONS));
    }

    OWLAxiom axiom = mock(OWLAxiom.class);
    OWLPrimitive primitive = mock(OWLPrimitive.class);

    @Test
    void shouldDelegateTo_containsAxiom_withReadLock_2() {
        ontology.containsAxiom(axiom);
        readLock(i -> i.verify(delegate).containsAxiom(axiom));
    }

    OWLDataProperty dataProperty = mock(OWLDataProperty.class);
    OWLIndividual individual = mock(OWLIndividual.class);
    OWLObjectPropertyExpression objectProperty = mock(OWLObjectPropertyExpression.class);

    @Test
    void shouldDelegateTo_getAxiomCount_withReadLock_5() {
        ontology.getAxiomCount();
        readLock(i -> i.verify(delegate).getAxiomCount());
    }

    @Test
    void shouldDelegateTo_getAxiomCount_withReadLock_6() {
        ontology.getAxiomCount(AxiomType.SUBCLASS_OF);
        readLock(i -> i.verify(delegate).getAxiomCount(AxiomType.SUBCLASS_OF));
    }

    @Test
    void shouldDelegateTo_getLogicalAxiomCount_withReadLock_3() {
        ontology.getLogicalAxiomCount();
        readLock(i -> i.verify(delegate).getLogicalAxiomCount());
    }

    @Test
    void shouldDelegateTo_containsAxiomIgnoreAnnotations_withReadLock_2() {
        ontology.containsAxiomIgnoreAnnotations(axiom);
        readLock(i -> i.verify(delegate).containsAxiomIgnoreAnnotations(axiom));
    }

    @Test
    void shouldDelegateTo_containsEntityInSignature_withReadLock_2() {
        ontology.containsEntityInSignature(iri, INCLUDED);
        readLock(i -> i.verify(delegate).containsEntityInSignature(iri, INCLUDED));
    }

    @Test
    void shouldDelegateTo_containsEntityInSignature_withReadLock_3() {
        ontology.containsEntityInSignature(iri);
        readLock(i -> i.verify(delegate).containsEntityInSignature(iri));
    }

    @Test
    void shouldDelegateTo_containsEntityInSignature_withReadLock_4() {
        ontology.containsEntityInSignature(entity, INCLUDED);
        readLock(i -> i.verify(delegate).containsEntityInSignature(entity, INCLUDED));
    }

    @Test
    void shouldDelegateTo_containsClassInSignature_withReadLock() {
        ontology.containsClassInSignature(iri);
        readLock(i -> i.verify(delegate).containsClassInSignature(iri));
    }

    @Test
    void shouldDelegateTo_containsClassInSignature_withReadLock_2() {
        ontology.containsClassInSignature(iri, INCLUDED);
        readLock(i -> i.verify(delegate).containsClassInSignature(iri, INCLUDED));
    }

    @Test
    void shouldDelegateTo_containsObjectPropertyInSignature_withReadLock() {
        ontology.containsObjectPropertyInSignature(iri);
        readLock(i -> i.verify(delegate).containsObjectPropertyInSignature(iri));
    }

    @Test
    void shouldDelegateTo_containsObjectPropertyInSignature_withReadLock_2() {
        ontology.containsObjectPropertyInSignature(iri, INCLUDED);
        readLock(i -> i.verify(delegate).containsObjectPropertyInSignature(iri, INCLUDED));
    }

    @Test
    void shouldDelegateTo_containsDataPropertyInSignature_withReadLock() {
        ontology.containsDataPropertyInSignature(iri, INCLUDED);
        readLock(i -> i.verify(delegate).containsDataPropertyInSignature(iri, INCLUDED));
    }

    @Test
    void shouldDelegateTo_containsDataPropertyInSignature_withReadLock_2() {
        ontology.containsDataPropertyInSignature(iri);
        readLock(i -> i.verify(delegate).containsDataPropertyInSignature(iri));
    }

    @Test
    void shouldDelegateTo_containsAnnotationPropertyInSignature_withReadLock() {
        ontology.containsAnnotationPropertyInSignature(iri);
        readLock(i -> i.verify(delegate).containsAnnotationPropertyInSignature(iri));
    }

    @Test
    void shouldDelegateTo_containsAnnotationPropertyInSignature_withReadLock_2() {
        ontology.containsAnnotationPropertyInSignature(iri, INCLUDED);
        readLock(i -> i.verify(delegate).containsAnnotationPropertyInSignature(iri, INCLUDED));
    }

    @Test
    void shouldDelegateTo_containsDatatypeInSignature_withReadLock() {
        ontology.containsDatatypeInSignature(iri, INCLUDED);
        readLock(i -> i.verify(delegate).containsDatatypeInSignature(iri, INCLUDED));
    }

    @Test
    void shouldDelegateTo_containsDatatypeInSignature_withReadLock_2() {
        ontology.containsDatatypeInSignature(iri);
        readLock(i -> i.verify(delegate).containsDatatypeInSignature(iri));
    }

    @Test
    void shouldDelegateTo_containsIndividualInSignature_withReadLock() {
        ontology.containsIndividualInSignature(iri);
        readLock(i -> i.verify(delegate).containsIndividualInSignature(iri));
    }

    @Test
    void shouldDelegateTo_containsIndividualInSignature_withReadLock_2() {
        ontology.containsIndividualInSignature(iri, INCLUDED);
        readLock(i -> i.verify(delegate).containsIndividualInSignature(iri, INCLUDED));
    }

    @Test
    void shouldDelegateTo_getPunnedIRIs_withReadLock() {
        ontology.getPunnedIRIs(INCLUDED);
        readLock(i -> i.verify(delegate).getPunnedIRIs(INCLUDED));
    }

    @Test
    void shouldDelegateTo_contains_withReadLock() {
        Object arg1 = new Object();
        ontology.contains(searchFilter, arg1, INCLUDED);
        readLock(i -> i.verify(delegate).contains(searchFilter, arg1, INCLUDED));
    }

    OWLAxiomSearchFilter searchFilter = mock(OWLAxiomSearchFilter.class);
    OWLObject object = mock(OWLObject.class);
    OWLAnnotationProperty annotationProperty = mock(OWLAnnotationProperty.class);
    OWLAnnotationSubject subject = mock(OWLAnnotationSubject.class);
    OWLClass owlClass = mock(OWLClass.class);
    OWLDataPropertyExpression dataPropertyExpression = mock(OWLDataPropertyExpression.class);
    OWLClassExpression classExpression = mock(OWLClassExpression.class);

    @Test
    void shouldDelegateTo_getOWLOntologyManager_withReadLock() {
        ontology.getOWLOntologyManager();
        readLock(i -> i.verify(delegate).getOWLOntologyManager());
    }

    @Test
    void shouldDelegateTo_getOWLOntologyManager_withWriteLock() {
        ontology.setOWLOntologyManager(manager);
        writeLock(i -> i.verify(delegate).setOWLOntologyManager(manager));
    }

    @Test
    void shouldDelegateTo_applyChange_withWriteLock() {
        ontology.applyChange(change);
        writeLock(i -> i.verify(delegate).applyChange(change));
    }

    OWLOntologyChange change = mock(OWLOntologyChange.class);

    @Test
    void shouldDelegateTo_applyChanges_withWriteLock() {
        ontology.applyChanges(list);
        writeLock(i -> i.verify(delegate).applyChanges(list));
    }

    List<OWLOntologyChange> list = mock(List.class);

    @Test
    void shouldDelegateTo_addAxioms_withWriteLock() {
        ontology.addAxioms(set);
        writeLock(i -> i.verify(delegate).addAxioms(set));
    }

    Set<OWLAxiom> set = mock(Set.class);

    @Test
    void shouldDelegateTo_addAxiom_withWriteLock() {
        ontology.addAxiom(axiom);
        writeLock(i -> i.verify(delegate).addAxiom(axiom));
    }
}
