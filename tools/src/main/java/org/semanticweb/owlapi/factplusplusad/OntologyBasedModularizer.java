package org.semanticweb.owlapi.factplusplusad;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;

import org.semanticweb.owlapi.atomicdecomposition.AxiomWrapper;
import org.semanticweb.owlapi.atomicdecomposition.ModuleMethod;
import org.semanticweb.owlapi.atomicdecomposition.Signature;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.modularity.ModuleType;

/**
 * Ontology based modularizer.
 */
public class OntologyBasedModularizer {

    /**
     * ontology to work with
     */
    OWLOntology ontology;
    /**
     * pointer to a modularizer
     */
    Modularizer modularizer;

    /**
     * @param ontology ontology to modularise
     * @param moduleMethod modularisation method
     */
    public OntologyBasedModularizer(OWLOntology ontology, ModuleMethod moduleMethod) {
        this.ontology = ontology;
        modularizer = new Modularizer(moduleMethod);
        modularizer.preprocessOntology(ontology.axioms().map(AxiomWrapper::new).toList());
    }

    /**
     * Get module.
     *
     * @param from axioms to modularise
     * @param sig signature
     * @param type type of module
     * @return module
     */
    Collection<AxiomWrapper> getModule(Collection<AxiomWrapper> from, Signature sig,
        ModuleType type) {
        modularizer.extract(from, sig, type);
        return modularizer.getModule();
    }

    /**
     * Get module.
     *
     * @param sig signature
     * @param type type of module
     * @return module
     */
    Collection<AxiomWrapper> getModule(Signature sig, ModuleType type) {
        return getModule(ontology.axioms().map(AxiomWrapper::new).toList(), sig, type);
    }

    /**
     * @return the modularizer
     */
    Modularizer getModularizer() {
        return modularizer;
    }

    /**
     * @param entities signature
     * @param type module type
     * @return module
     */
    public Collection<OWLAxiom> getModule(Stream<OWLEntity> entities, ModuleType type) {
        return getModule(new Signature(entities), type).stream().map(AxiomWrapper::getAxiom)
            .filter(Objects::nonNull).toList();
    }
}
