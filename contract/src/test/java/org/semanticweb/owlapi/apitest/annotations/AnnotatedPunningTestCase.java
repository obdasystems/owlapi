package org.semanticweb.owlapi.apitest.annotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.of;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.semanticweb.owlapi.apitest.baseclasses.TestBase;
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormatFactory;
import org.semanticweb.owlapi.formats.ManchesterSyntaxDocumentFormatFactory;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormatFactory;
import org.semanticweb.owlapi.formats.TurtleDocumentFormatFactory;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLDocumentFormatFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * Created by ses on 5/13/14.
 */
class AnnotatedPunningTestCase extends TestBase {

    OWLOntology makeOwlOntologyWithDeclarationsAndAnnotationAssertions(
        OWLAnnotationProperty annotationProperty, List<OWLEntity> entities) {
        List<OWLAxiom> axioms = new ArrayList<>();
        axioms.add(Declaration(annotationProperty));
        for (OWLEntity entity : entities) {
            axioms.add(
                AnnotationAssertion(annotationProperty, entity.getIRI(), AnonymousIndividual()));
            axioms.add(Declaration(entity));
        }
        return o(axioms);
    }

    String saveForRereading(OWLOntology o, OWLDocumentFormat format) {
        return saveOntology(o, format).toString();
    }

    static List<Arguments> allTests() {
        List<? extends OWLEntity> entities = l(CLASSES.A, Datatype(CLASSES.A.getIRI()),
            AnnotationProperty(CLASSES.A.getIRI()), DataProperty(CLASSES.A.getIRI()),
            ObjectProperty(CLASSES.A.getIRI()), NamedIndividual(CLASSES.A.getIRI()));
        return l(of(new RDFXMLDocumentFormatFactory(), entities), of(new TurtleDocumentFormatFactory(), entities),
            of(new FunctionalSyntaxDocumentFormatFactory(), entities),
            of(new ManchesterSyntaxDocumentFormatFactory(), entities));
    }

    @ParameterizedTest
    @MethodSource("allTests")
    void runTestForAnnotationsOnPunnedEntitiesForFormat(OWLDocumentFormatFactory formatFactory,
        List<OWLEntity> entities) {
        OWLOntology o =
            makeOwlOntologyWithDeclarationsAndAnnotationAssertions(ANNPROPS.AP, entities);
        for (int counter = 0; counter < 10; counter++) {
            String in = saveForRereading(o, formatFactory.createFormat());
            m.removeOntology(o);
            o = loadFrom(in);
        }
        assertEquals(entities.size(), o.axioms(AxiomType.ANNOTATION_ASSERTION).count());
    }
}
