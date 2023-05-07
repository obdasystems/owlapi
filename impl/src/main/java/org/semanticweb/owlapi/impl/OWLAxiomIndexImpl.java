package org.semanticweb.owlapi.impl;

import java.util.stream.Stream;

import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLAxiomIndex;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDatatypeDefinitionAxiom;
import org.semanticweb.owlapi.model.OWLSubAnnotationPropertyOfAxiom;
import org.semanticweb.owlapi.search.Filters;

/**
 * @author ignazio
 * @since 4.0.0
 */
public abstract class OWLAxiomIndexImpl extends OWLObjectImpl
    implements OWLAxiomIndex, HasTrimToSize {

    protected final Internals ints = new Internals();

    @Override
    public void trimToSize() {
        // XXX no operation
    }

    @Override
    public Stream<OWLDatatypeDefinitionAxiom> datatypeDefinitions(OWLDatatype datatype) {
        // XXX stream better?
        return ints.filterAxioms(Filters.datatypeDefFilter, datatype).stream()
            .map(OWLDatatypeDefinitionAxiom.class::cast);
    }

    @Override
    public Stream<OWLSubAnnotationPropertyOfAxiom> subAnnotationPropertyOfAxioms(
        OWLAnnotationProperty subProperty) {
        return ints.filterAxioms(Filters.subAnnotationWithSub, subProperty).stream()
            .map(OWLSubAnnotationPropertyOfAxiom.class::cast);
    }

    @Override
    public Stream<OWLAnnotationPropertyDomainAxiom> annotationPropertyDomainAxioms(
        OWLAnnotationProperty property) {
        return ints.filterAxioms(Filters.apDomainFilter, property).stream()
            .map(OWLAnnotationPropertyDomainAxiom.class::cast);
    }

    @Override
    public Stream<OWLAnnotationPropertyRangeAxiom> annotationPropertyRangeAxioms(
        OWLAnnotationProperty property) {
        return ints.filterAxioms(Filters.apRangeFilter, property).stream()
            .map(OWLAnnotationPropertyRangeAxiom.class::cast);
    }
}
