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
package org.semanticweb.owlapi.rdf.model;

import static org.semanticweb.owlapi.utilities.OWLAPIPreconditions.checkNotNull;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nullable;

import org.semanticweb.owlapi.documents.RDFLiteral;
import org.semanticweb.owlapi.documents.RDFNode;
import org.semanticweb.owlapi.documents.RDFResource;
import org.semanticweb.owlapi.documents.RDFResourceBlankNode;
import org.semanticweb.owlapi.documents.RDFResourceIRI;
import org.semanticweb.owlapi.documents.RDFTriple;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.NodeID;
import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.rdf.AbsoluteIRIHelper;
import org.semanticweb.owlapi.utility.AxiomAppearance;
import org.semanticweb.owlapi.utility.IndividualAppearance;

/**
 * @author Matthew Horridge, The University Of Manchester, Bio-Health Informatics Group
 * @since 2.0.0
 */
public class RDFTranslator
    extends AbstractTranslator<RDFNode, RDFResource, RDFResourceIRI, RDFLiteral> {

    protected AxiomAppearance axiomOccurrences;
    private AtomicInteger nextBlankNodeId;
    private Map<Object, Integer> blankNodeMap;

    /**
     * @param ontology the ontology
     * @param format target format
     * @param useStrongTyping true if strong typing is required
     * @param occurrences will tell whether anonymous individuals need an id or not
     * @param translatedAxioms translated axioms
     */
    public RDFTranslator(OWLOntology ontology, @Nullable OWLDocumentFormat format,
        boolean useStrongTyping, IndividualAppearance occurrences, Set<OWLAxiom> translatedAxioms) {
        super(ontology.getOWLOntologyManager(), ontology, format, useStrongTyping, occurrences,
            translatedAxioms);
    }

    /**
     * @param axiomOccurs axiom occurrences
     * @param counter counter for blank nodes
     * @param bnodeMap map for blank node renaming
     * @return this modified object
     */
    public RDFTranslator withOccurrences(AxiomAppearance axiomOccurs, AtomicInteger counter,
        Map<Object, Integer> bnodeMap) {
        axiomOccurrences = axiomOccurs;
        nextBlankNodeId = counter;
        blankNodeMap = bnodeMap;
        return this;
    }

    @Override
    protected void addTriple(RDFResource subject, RDFResourceIRI pred, RDFNode object) {
        graph.addTriple(new RDFTriple(subject, pred, object));
    }

    @Override
    protected RDFResourceBlankNode getAnonymousNode(Object key) {
        checkNotNull(key, "key cannot be null");
        boolean isIndividual = key instanceof OWLAnonymousIndividual;
        boolean isAxiom = false;
        boolean needId = false;
        if (isIndividual) {
            OWLAnonymousIndividual anonymousIndividual = (OWLAnonymousIndividual) key;
            needId = multipleOccurrences.appearsMultipleTimes(anonymousIndividual);
            return getBlankNodeFor(anonymousIndividual.getID().getID(), isIndividual, isAxiom,
                needId);
        } else if (key instanceof OWLAxiom k) {
            isIndividual = false;
            isAxiom = true;
            needId = axiomOccurrences.appearsMultipleTimes(k);
        }
        return getBlankNodeFor(key, isIndividual, isAxiom, needId);
    }

    protected RDFResourceBlankNode getBlankNodeFor(Object key, boolean isIndividual,
        boolean isAxiom, boolean needId) {
        Integer id = blankNodeMap.get(key);
        if (id == null) {
            id = Integer.valueOf(nextBlankNodeId.getAndIncrement());
            blankNodeMap.put(key, id);
        }
        return new RDFResourceBlankNode(NodeID.nodeId(id.intValue(), manager.getOWLDataFactory()),
            isIndividual, needId, isAxiom);
    }

    @Override
    protected RDFLiteral getLiteralNode(OWLLiteral literal) {
        return new RDFLiteral(literal);
    }

    @Override
    protected RDFResourceIRI getPredicateNode(IRI iri) {
        return new RDFResourceIRI(AbsoluteIRIHelper.verifyAbsolute(iri, format, ont));
    }

    @Override
    protected RDFResourceIRI getResourceNode(IRI iri) {
        return new RDFResourceIRI(AbsoluteIRIHelper.verifyAbsolute(iri, format, ont));
    }
}
