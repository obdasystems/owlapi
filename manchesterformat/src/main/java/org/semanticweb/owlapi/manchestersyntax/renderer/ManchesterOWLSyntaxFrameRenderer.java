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
package org.semanticweb.owlapi.manchestersyntax.renderer;

import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.ANNOTATIONS;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.ANNOTATION_PROPERTY;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.ASYMMETRIC;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.CHARACTERISTICS;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.CLASS;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.DATATYPE;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.DATA_PROPERTY;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.DIFFERENT_FROM;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.DIFFERENT_INDIVIDUALS;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.DISJOINT_CLASSES;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.DISJOINT_PROPERTIES;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.DISJOINT_UNION_OF;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.DISJOINT_WITH;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.DOMAIN;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.EQUIVALENT_CLASSES;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.EQUIVALENT_PROPERTIES;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.EQUIVALENT_TO;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.FACTS;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.FUNCTIONAL;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.HAS_KEY;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.IMPORT;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.INDIVIDUAL;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.INDIVIDUALS;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.INVERSE_FUNCTIONAL;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.INVERSE_OF;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.IRREFLEXIVE;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.NOT;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.OBJECT_PROPERTY;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.ONTOLOGY;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.PREFIX;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.RANGE;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.REFLEXIVE;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.RULE;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.SAME_AS;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.SAME_INDIVIDUAL;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.SUBCLASS_OF;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.SUB_PROPERTY_CHAIN;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.SUB_PROPERTY_OF;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.SUPERCLASS_OF;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.SUPER_PROPERTY_OF;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.SYMMETRIC;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.TRANSITIVE;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.TYPES;
import static org.semanticweb.owlapi.utilities.OWLAPIStreamUtils.add;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import org.semanticweb.owlapi.io.OWLStorerParameters;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.HasAnnotations;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;
import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLEntityVisitor;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNaryPropertyAxiom;
import org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLRuntimeException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.utilities.OWLAxiomFilter;
import org.semanticweb.owlapi.utility.CollectionFactory;
import org.semanticweb.owlapi.utility.OWLObjectComparator;
import org.semanticweb.owlapi.utility.OntologyIRIShortFormProvider;

/**
 * The Class ManchesterOWLSyntaxFrameRenderer.
 *
 * @author Matthew Horridge, The University Of Manchester, Bio-Health Informatics Group
 * @since 2.0.0
 */
public class ManchesterOWLSyntaxFrameRenderer extends ManchesterOWLSyntaxObjectRenderer
    implements OWLEntityVisitor {

    protected final OWLObjectComparator ooc;
    private final OWLOntology o;
    private final List<AxiomType<?>> filteredAxiomTypes = Arrays.asList(AxiomType.SWRL_RULE);
    private final List<RendererListener> listeners = new ArrayList<>();
    private final Predicate<OWLAxiom> props =
        ax -> ((OWLNaryPropertyAxiom<?>) ax).getOperandsAsList().size() == 2;
    private OntologyIRIShortFormProvider shortFormProvider = new OntologyIRIShortFormProvider();
    private boolean renderExtensions = false;
    private boolean explicitXsdStrings;
    private OWLAxiomFilter axiomFilter = axiom -> true;
    private RenderingDirector renderingDirector = (a, b) -> false;
    /**
     * The event.
     */
    @Nullable
    private RendererEvent event;

    /**
     * Instantiates a new manchester owl syntax frame renderer.
     *
     * @param ontology the ontology
     * @param parameters storer parameters
     * @param writer the writer
     */
    public ManchesterOWLSyntaxFrameRenderer(OWLOntology ontology, OWLStorerParameters parameters,
        Writer writer) {
        super(writer, parameters, ontology.getPrefixManager());
        o = ontology;
        ooc = new OWLObjectComparator(ontology.getPrefixManager());
        explicitXsdStrings = Boolean.parseBoolean(
            parameters.getParameter("force xsd:string on literals", Boolean.FALSE).toString());
    }

    /**
     * Instantiates a new manchester owl syntax frame renderer.
     *
     * @param ontologies the ontologies
     * @param writer the writer
     * @param parameters storer parameters
     * @param entityShortFormProvider the entity short form provider
     */
    public ManchesterOWLSyntaxFrameRenderer(Collection<OWLOntology> ontologies,
        OWLStorerParameters parameters, Writer writer, PrefixManager entityShortFormProvider) {
        super(writer, parameters, entityShortFormProvider);
        if (ontologies.size() != 1) {
            throw new OWLRuntimeException("Can only render one ontology");
        }
        o = ontologies.iterator().next();
        ooc = new OWLObjectComparator(entityShortFormProvider);
        explicitXsdStrings = Boolean.parseBoolean(
            parameters.getParameter("force xsd:string on literals", Boolean.FALSE).toString());
    }

    static <E> Collection<E> sortedSet() {
        return new TreeSet<>();
    }

    /**
     * Sets the rendering director.
     *
     * @param renderingDirector the new rendering director
     */
    public void setRenderingDirector(RenderingDirector renderingDirector) {
        this.renderingDirector = renderingDirector;
    }

    /**
     * @param shortFormProvider short form provider to be used
     */
    public void setOntologyIRIShortFormProvider(OntologyIRIShortFormProvider shortFormProvider) {
        this.shortFormProvider = shortFormProvider;
    }

    /**
     * Adds the renderer listener.
     *
     * @param listener the listener
     */
    public void addRendererListener(RendererListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes the renderer listener.
     *
     * @param listener the listener
     */
    public void removeRendererListener(RendererListener listener) {
        listeners.remove(listener);
    }

    /**
     * Sets the axiom filter.
     *
     * @param axiomFilter the new axiom filter
     */
    public void setAxiomFilter(OWLAxiomFilter axiomFilter) {
        this.axiomFilter = axiomFilter;
    }

    /**
     * Clear filtered axiom types.
     */
    public void clearFilteredAxiomTypes() {
        filteredAxiomTypes.clear();
    }

    /**
     * Adds the filtered axiom type.
     *
     * @param axiomType the axiom type
     */
    public void addFilteredAxiomType(AxiomType<?> axiomType) {
        filteredAxiomTypes.add(axiomType);
    }

    /**
     * Sets the render extensions.
     *
     * @param renderExtensions the new render extensions
     */
    public void setRenderExtensions(boolean renderExtensions) {
        this.renderExtensions = renderExtensions;
    }

    /**
     * Write ontology.
     *
     * @throws OWLOntologyStorageException renderer exception
     */
    public void writeOntology() throws OWLOntologyStorageException {
        writePrefixMap();
        writeNewLine();
        writeOntologyHeader();
        o.annotationPropertiesInSignature().sorted(ooc).forEach(this::write);
        o.datatypesInSignature().sorted(ooc).forEach(this::write);
        o.objectPropertiesInSignature().sorted(ooc).forEach(prop -> {
            write(prop);
            OWLObjectPropertyExpression invProp = prop.getInverseProperty();
            if (o.axioms(invProp).count() > 0) {
                write(invProp);
            }
        });
        o.dataPropertiesInSignature().sorted(ooc).forEach(this::write);
        o.classesInSignature().sorted(ooc).forEach(this::write);
        o.individualsInSignature().sorted(ooc).forEach(this::write);
        o.referencedAnonymousIndividuals().sorted(ooc).forEach(this::write);
        // Nary disjoint classes axioms
        event = new RendererEvent(this, o);
        o.axioms(AxiomType.DISJOINT_CLASSES).sorted(ooc)
            .forEach(ax -> writeMoreThanTwo(ax, ax.getOperandsAsList(), DISJOINT_CLASSES));
        // Nary equivalent classes axioms
        o.axioms(AxiomType.EQUIVALENT_CLASSES).sorted(ooc)
            .forEach(ax -> writeMoreThanTwo(ax, ax.getOperandsAsList(), EQUIVALENT_CLASSES));
        // Nary disjoint properties
        o.axioms(AxiomType.DISJOINT_OBJECT_PROPERTIES).sorted(ooc)
            .forEach(ax -> writeMoreThanTwo(ax, ax.getOperandsAsList(), DISJOINT_PROPERTIES));
        // Nary equivalent properties
        o.axioms(AxiomType.EQUIVALENT_OBJECT_PROPERTIES).sorted(ooc)
            .forEach(ax -> writeMoreThanTwo(ax, ax.getOperandsAsList(), EQUIVALENT_PROPERTIES));
        // Nary disjoint properties
        o.axioms(AxiomType.DISJOINT_DATA_PROPERTIES).sorted(ooc)
            .forEach(ax -> writeMoreThanTwo(ax, ax.getOperandsAsList(), DISJOINT_PROPERTIES));
        // Nary equivalent properties
        o.axioms(AxiomType.EQUIVALENT_DATA_PROPERTIES).sorted(ooc)
            .forEach(ax -> writeMoreThanTwo(ax, ax.getOperandsAsList(), EQUIVALENT_PROPERTIES));
        // Nary different individuals
        o.axioms(AxiomType.DIFFERENT_INDIVIDUALS).sorted(ooc).forEach(
            ax -> writeMoreThanTwo(ax, ax.getOperandsAsList(), DIFFERENT_INDIVIDUALS, true));
        // Nary same individuals
        o.axioms(AxiomType.SAME_INDIVIDUAL).sorted(ooc)
            .forEach(ax -> writeMoreThanTwo(ax, ax.getOperandsAsList(), SAME_INDIVIDUAL, true));
        o.axioms(AxiomType.SWRL_RULE).sorted(ooc).forEach(
            rule -> writeSection(RULE, Collections.singleton(rule).iterator(), ", ", false));
        filtersort(o.axioms(AxiomType.SUBCLASS_OF), a -> ((OWLSubClassOfAxiom) a).isGCI())
                .collect(Collectors.groupingBy(OWLSubClassOfAxiom::getSubClass))
                .forEach(this::write);
        flush();
    }

    protected <T> void writeMoreThanTwo(OWLAxiom ax, List<T> individuals,
        ManchesterOWLSyntax section, boolean writeTwoIfAnnotated) {
        if (individuals.size() > 2 || writeTwoIfAnnotated && individuals.size() == 2
            && !ax.annotationsAsList().isEmpty()) {
            SectionMap<Object, OWLAxiom> map = new SectionMap<>();
            map.put(individuals, ax);
            writeSection(section, map, ",", false);
        }
    }

    protected <T> void writeMoreThanTwo(OWLAxiom ax, List<T> individuals,
        ManchesterOWLSyntax section) {
        if (individuals.size() > 2) {
            SectionMap<Object, OWLAxiom> map = new SectionMap<>();
            map.put(individuals, ax);
            writeSection(section, map, ",", false);
        }
    }

    /**
     * Write ontology header.
     */
    public void writeOntologyHeader() {
        event = new RendererEvent(this, o);
        fireFrameRenderingPrepared(ONTOLOGY.toString());
        write(ONTOLOGY.toString());
        write(":");
        writeSpace();
        if (o.isNamed()) {
            int indent = getIndent();
            write("<").write(o.getOntologyID().getOntologyIRI().map(Object::toString).orElse(""))
                .write(">").writeNewLine();
            pushTab(indent);
            o.getOntologyID().getVersionIRI()
                .ifPresent(v -> write("<").write(v.toString()).write(">"));
            popTab();
        }
        fireFrameRenderingStarted(ONTOLOGY.toString());
        writeNewLine();
        o.importsDeclarations().sorted().forEach(this::writeImports);
        writeNewLine();
        writeSection(ANNOTATIONS, o.annotations().iterator(), ",", true);
        fireFrameRenderingFinished(ONTOLOGY.toString());
    }

    protected void writeImports(OWLImportsDeclaration decl) {
        fireSectionItemPrepared(IMPORT.toString());
        write(IMPORT.toString()).write(": ");
        fireSectionRenderingStarted(IMPORT.toString());
        write("<").write(decl.getIRI().toString()).write(">").writeNewLine();
        fireSectionRenderingFinished(IMPORT.toString());
    }

    /**
     * Write prefix map.
     */
    public void writePrefixMap() {
        Map<String, String> prefixMap = prefixManager.getPrefixName2PrefixMap();
        prefixMap.entrySet().stream().sorted((o1, o2) -> o1.getKey().compareTo(o2.getKey()))
            .forEach(value -> write(PREFIX.toString()).write(": ").write(value.getKey()).write(" ")
                .write("<").write(value.getValue()).write(">").writeNewLine());
        if (!prefixMap.containsKey(":")) {
            write(PREFIX.toString());
            write(": : ");
            write(o.getOntologyID().getOntologyIRI().map(IRI::toQuotedString)
                .orElse(o.getOntologyID().getDefaultDocumentIRI().map(IRI::toQuotedString)
                    .orElse("<urn:absoluteiri:defaultvalue#>")));
            writeNewLine();
        }
        if (!prefixMap.isEmpty()) {
            writeNewLine().writeNewLine();
        }
    }

    /**
     * Checks if is filtered.
     *
     * @param axiomType the axiom type
     * @return true, if is filtered
     */
    public boolean isFiltered(AxiomType<?> axiomType) {
        return filteredAxiomTypes.contains(axiomType);
    }

    /**
     * Checks if is displayed.
     *
     * @param axiom the axiom
     * @return true, if is displayed
     */
    public boolean isDisplayed(@Nullable OWLAxiom axiom) {
        return axiom != null && axiomFilter.passes(axiom);
    }

    /**
     * Write frame.
     *
     * @param entity the entity
     * @return the sets the
     */
    public Collection<OWLAxiom> writeFrame(OWLEntity entity) {
        if (entity.isOWLClass()) {
            return write(entity.asOWLClass());
        }
        if (entity.isOWLObjectProperty()) {
            return write(entity.asOWLObjectProperty());
        }
        if (entity.isOWLDataProperty()) {
            return write(entity.asOWLDataProperty());
        }
        if (entity.isOWLNamedIndividual()) {
            return write(entity.asOWLNamedIndividual());
        }
        if (entity.isOWLAnnotationProperty()) {
            return write(entity.asOWLAnnotationProperty());
        }
        if (entity.isOWLDatatype()) {
            return write(entity.asOWLDatatype());
        }
        return Collections.emptySet();
    }

    protected <T extends OWLAxiom> Stream<T> filtersort(Stream<T> s) {
        return s.filter(this::isDisplayed).sorted(ooc);
    }

    protected <T extends OWLAxiom> Stream<T> filtersort(Stream<T> s, Predicate<OWLAxiom> extra) {
        return s.filter(this::isDisplayed).filter(extra).sorted(ooc);
    }


    private void write(OWLClassExpression superClass, List<OWLSubClassOfAxiom> axs) {
        writeEntityStart(CLASS, superClass);

        if (!isFiltered(AxiomType.SUBCLASS_OF)) {
            SectionMap<Object, OWLAxiom> superclasses = new SectionMap<>();
            filtersort(axs.stream()).forEach(ax -> superclasses.put(ax.getSuperClass(), ax));
            writeSection(SUBCLASS_OF, superclasses, ",", true);
        }
    }

    /**
     * @param cls the class
     * @return the sets the
     */
    public Collection<OWLAxiom> write(OWLClass cls) {
        List<OWLAxiom> axioms = new ArrayList<>();
        axioms.addAll(writeEntityStart(CLASS, cls));
        if (!isFiltered(AxiomType.EQUIVALENT_CLASSES)) {
            renderEquivalent(cls, axioms);
        }
        if (!isFiltered(AxiomType.SUBCLASS_OF)) {
            renderSubclass(cls, axioms);
        }
        if (!isFiltered(AxiomType.DISJOINT_UNION)) {
            renderDisjointUnion(cls, axioms);
        }
        if (!isFiltered(AxiomType.DISJOINT_CLASSES)) {
            renderDisjointClasses(cls, axioms);
        }
        if (!isFiltered(AxiomType.HAS_KEY)) {
            renderHasKey(cls);
        }
        if (!isFiltered(AxiomType.CLASS_ASSERTION)) {
            renderClassAssertion(cls, axioms);
        }
        if (!isFiltered(AxiomType.SWRL_RULE)) {
            // XXX used at all?
            renderRule(cls);
        }
        writeEntitySectionEnd(CLASS.toString());
        return axioms;
    }

    protected void renderRule(OWLClass cls) {
        Set<OWLAxiom> rules = new HashSet<>();
        filtersort(o.axioms(AxiomType.SWRL_RULE)).forEach(rule -> {
            for (SWRLAtom atom : rule.headList()) {
                if (atom.getPredicate().equals(cls)) {
                    writeSection(RULE, rules.iterator(), ", ", true);
                    break;
                }
            }
        });
    }

    protected void renderClassAssertion(OWLClass cls, List<OWLAxiom> axioms) {
        SectionMap<Object, OWLAxiom> individuals = new SectionMap<>();
        filtersort(o.classAssertionAxioms(cls), ax -> renderExtensions).forEach(ax -> {
            individuals.put(ax.getIndividual(), ax);
            axioms.add(ax);
        });
        writeSection(INDIVIDUALS, individuals, ",", true);
    }

    protected void renderHasKey(OWLClass cls) {
        filtersort(o.hasKeyAxioms(cls)).forEach(ax -> {
            SectionMap<Object, OWLAxiom> map = new SectionMap<>();
            map.put(ax.getOperandsAsList(), ax);
            writeSection(HAS_KEY, map, ", ", true);
        });
    }

    protected void renderDisjointClasses(OWLClass cls, List<OWLAxiom> axioms) {
        SectionMap<Object, OWLAxiom> disjointClasses = new SectionMap<>();
        filtersort(o.disjointClassesAxioms(cls)).forEach(ax -> {
            if (ax.getOperandsAsList().size() == 2) {
                OWLClassExpression disjointWith =
                    ax.getClassExpressionsMinus(cls).iterator().next();
                disjointClasses.put(disjointWith, ax);
            }
            axioms.add(ax);
        });
        writeSection(DISJOINT_WITH, disjointClasses, ", ", false);
        if (renderExtensions) {
            // Handling of nary in frame style
            filtersort(o.disjointClassesAxioms(cls)).forEach(ax -> {
                if (ax.getOperandsAsList().size() > 2) {
                    axioms.add(ax);
                    writeSection(DISJOINT_CLASSES, ax.classExpressions().iterator(), ", ", false);
                }
            });
        }
    }

    protected void renderDisjointUnion(OWLClass cls, List<OWLAxiom> axioms) {
        // Handling of nary in frame style
        filtersort(o.disjointUnionAxioms(cls)).forEach(ax -> {
            axioms.add(ax);
            writeSection(DISJOINT_UNION_OF, ax.classExpressions().iterator(), ", ", false);
        });
    }

    protected void renderSubclass(OWLClass cls, List<OWLAxiom> axioms) {
        SectionMap<Object, OWLAxiom> superclasses = new SectionMap<>();
        filtersort(o.subClassAxiomsForSubClass(cls)).forEach(ax -> {
            superclasses.put(ax.getSuperClass(), ax);
            axioms.add(ax);
        });
        writeSection(SUBCLASS_OF, superclasses, ",", true);
        if (renderExtensions) {
            SectionMap<Object, OWLAxiom> subClasses = new SectionMap<>();
            filtersort(o.subClassAxiomsForSuperClass(cls))
                .forEach(ax -> addSubClasses(axioms, subClasses, ax));
            writeSection(SUPERCLASS_OF, subClasses, ",", true);
        }
    }

    protected void renderEquivalent(OWLClass cls, List<OWLAxiom> axioms) {
        SectionMap<Object, OWLAxiom> equivalentClasses = new SectionMap<>();
        filtersort(o.equivalentClassesAxioms(cls), this::twoOperands)
            .forEach(ax -> addEquivalent(cls, axioms, equivalentClasses, ax));
        equivalentClasses.remove(cls);
        writeSection(EQUIVALENT_TO, equivalentClasses, ",", true);
    }

    protected void addSubClasses(List<OWLAxiom> axioms, SectionMap<Object, OWLAxiom> subClasses,
        OWLSubClassOfAxiom ax) {
        subClasses.put(ax.getSubClass(), ax);
        axioms.add(ax);
    }

    protected boolean twoOperands(OWLAxiom ax) {
        return ((OWLEquivalentClassesAxiom) ax).getOperandsAsList().size() == 2;
    }

    protected void addEquivalent(OWLClass cls, List<OWLAxiom> axioms,
        SectionMap<Object, OWLAxiom> equivalentClasses, OWLEquivalentClassesAxiom ax) {
        ax.getClassExpressionsMinus(cls).forEach(c -> equivalentClasses.put(c, ax));
        axioms.add(ax);
    }

    /**
     * Write entity section end.
     *
     * @param type the type
     */
    protected void writeEntitySectionEnd(String type) {
        fireFrameRenderingFinished(type);
        popTab();
        writeNewLine();
    }

    /**
     * @param property the property
     * @return the sets the
     */
    public Collection<OWLAxiom> write(OWLObjectPropertyExpression property) {
        Collection<OWLAxiom> axioms = new ArrayList<>();
        axioms.addAll(writeEntityStart(OBJECT_PROPERTY, property));
        if (!isFiltered(AxiomType.SUB_OBJECT_PROPERTY)) {
            renderSubObjectProperty(property, axioms);
        }
        if (!isFiltered(AxiomType.EQUIVALENT_OBJECT_PROPERTIES)) {
            renderEquivalentObjectProperties(property, axioms);
        }
        if (!isFiltered(AxiomType.DISJOINT_OBJECT_PROPERTIES)) {
            renderDisjointObjectProperties(property, axioms);
        }
        if (!isFiltered(AxiomType.SUB_PROPERTY_CHAIN_OF)) {
            renderSubProperttyChain(property, axioms);
        }
        SectionMap<Object, OWLAxiom> characteristics = new SectionMap<>();
        if (!isFiltered(AxiomType.FUNCTIONAL_OBJECT_PROPERTY)) {
            filtersort(o.functionalObjectPropertyAxioms(property)).forEach(
                ax -> addCharacteristic(FUNCTIONAL.toString(), axioms, characteristics, ax));
        }
        if (!isFiltered(AxiomType.INVERSE_FUNCTIONAL_OBJECT_PROPERTY)) {
            filtersort(o.inverseFunctionalObjectPropertyAxioms(property))
                .forEach(ax -> addCharacteristic(INVERSE_FUNCTIONAL.toString(), axioms,
                    characteristics, ax));
        }
        if (!isFiltered(AxiomType.SYMMETRIC_OBJECT_PROPERTY)) {
            filtersort(o.symmetricObjectPropertyAxioms(property)).forEach(
                ax -> addCharacteristic(SYMMETRIC.toString(), axioms, characteristics, ax));
        }
        if (!isFiltered(AxiomType.TRANSITIVE_OBJECT_PROPERTY)) {
            filtersort(o.transitiveObjectPropertyAxioms(property)).forEach(
                ax -> addCharacteristic(TRANSITIVE.toString(), axioms, characteristics, ax));
        }
        if (!isFiltered(AxiomType.REFLEXIVE_OBJECT_PROPERTY)) {
            filtersort(o.reflexiveObjectPropertyAxioms(property)).forEach(
                ax -> addCharacteristic(REFLEXIVE.toString(), axioms, characteristics, ax));
        }
        if (!isFiltered(AxiomType.IRREFLEXIVE_OBJECT_PROPERTY)) {
            filtersort(o.irreflexiveObjectPropertyAxioms(property)).forEach(
                ax -> addCharacteristic(IRREFLEXIVE.toString(), axioms, characteristics, ax));
        }
        if (!isFiltered(AxiomType.ASYMMETRIC_OBJECT_PROPERTY)) {
            filtersort(o.asymmetricObjectPropertyAxioms(property)).forEach(
                ax -> addCharacteristic(ASYMMETRIC.toString(), axioms, characteristics, ax));
        }
        writeSection(CHARACTERISTICS, characteristics, ",", true);
        if (!isFiltered(AxiomType.OBJECT_PROPERTY_DOMAIN)) {
            renderObjectPropertyDomain(property, axioms);
        }
        if (!isFiltered(AxiomType.OBJECT_PROPERTY_RANGE)) {
            renderObjectPropertyRange(property, axioms);
        }
        if (!isFiltered(AxiomType.INVERSE_OBJECT_PROPERTIES)) {
            renderInverseProperties(property, axioms);
        }
        if (!isFiltered(AxiomType.SWRL_RULE)) {
            renderSWRLRule(property);
        }
        writeEntitySectionEnd(OBJECT_PROPERTY.toString());
        return axioms;
    }

    protected void renderSWRLRule(OWLObjectPropertyExpression property) {
        Collection<OWLAxiom> rules = sortedCollection();
        filtersort(o.axioms(AxiomType.SWRL_RULE))
            .forEach(rule -> renderRule(property, rules, rule));
    }

    protected void renderRule(OWLObjectPropertyExpression property, Collection<OWLAxiom> rules,
        SWRLRule rule) {
        if (rule.head().anyMatch(a -> a.getPredicate().equals(property))) {
            rules.add(rule);
            writeSection(RULE, rules.iterator(), ",", true);
        }
    }

    protected void renderInverseProperties(OWLObjectPropertyExpression property,
        Collection<OWLAxiom> axioms) {
        Collection<OWLObjectPropertyExpression> properties = sortedCollection();
        filtersort(o.inverseObjectPropertyAxioms(property)).forEach(ax -> {
            if (ax.getFirstProperty().equals(property)) {
                properties.add(ax.getSecondProperty());
            } else {
                properties.add(ax.getFirstProperty());
            }
            axioms.add(ax);
        });
        writeSection(INVERSE_OF, properties.iterator(), ",", true);
    }

    protected void renderObjectPropertyRange(OWLObjectPropertyExpression property,
        Collection<OWLAxiom> axioms) {
        SectionMap<Object, OWLAxiom> expressions = new SectionMap<>();
        filtersort(o.objectPropertyRangeAxioms(property)).forEach(ax -> {
            expressions.put(ax.getRange(), ax);
            axioms.add(ax);
        });
        writeSection(RANGE, expressions, ",", true);
    }

    protected void renderObjectPropertyDomain(OWLObjectPropertyExpression property,
        Collection<OWLAxiom> axioms) {
        SectionMap<Object, OWLAxiom> expressions = new SectionMap<>();
        filtersort(o.objectPropertyDomainAxioms(property)).forEach(ax -> {
            expressions.put(ax.getDomain(), ax);
            axioms.add(ax);
        });
        writeSection(DOMAIN, expressions, ",", true);
    }

    protected void renderSubProperttyChain(OWLObjectPropertyExpression property,
        Collection<OWLAxiom> axioms) {
        filtersort(o.axioms(AxiomType.SUB_PROPERTY_CHAIN_OF),
            ax -> ((OWLSubPropertyChainOfAxiom) ax).getSuperProperty().equals(property))
                .forEach(ax -> {
                    SectionMap<Object, OWLAxiom> map = new SectionMap<>();
                    map.put(ax.getPropertyChain(), ax);
                    writeSection(SUB_PROPERTY_CHAIN, map, " o ", false);
                    axioms.add(ax);
                });
    }

    protected void renderDisjointObjectProperties(OWLObjectPropertyExpression property,
        Collection<OWLAxiom> axioms) {
        SectionMap<Object, OWLAxiom> properties = new SectionMap<>();
        filtersort(o.disjointObjectPropertiesAxioms(property), props).forEach(ax -> {
            properties.put(ax.getPropertiesMinus(property).iterator().next(), ax);
            axioms.add(ax);
        });
        writeSection(DISJOINT_WITH, properties, ",", true);
    }

    protected void renderEquivalentObjectProperties(OWLObjectPropertyExpression property,
        Collection<OWLAxiom> axioms) {
        SectionMap<Object, OWLAxiom> properties = new SectionMap<>();
        filtersort(o.equivalentObjectPropertiesAxioms(property), props).forEach(ax -> {
            properties.put(ax.getPropertiesMinus(property).iterator().next(), ax);
            axioms.add(ax);
        });
        writeSection(EQUIVALENT_TO, properties, ",", true);
    }

    protected void renderSubObjectProperty(OWLObjectPropertyExpression property,
        Collection<OWLAxiom> axioms) {
        SectionMap<Object, OWLAxiom> properties = new SectionMap<>();
        filtersort(o.objectSubPropertyAxiomsForSubProperty(property)).forEach(ax -> {
            properties.put(ax.getSuperProperty(), ax);
            axioms.add(ax);
        });
        writeSection(SUB_PROPERTY_OF, properties, ",", true);
        if (renderExtensions) {
            SectionMap<Object, OWLAxiom> extproperties = new SectionMap<>();
            filtersort(o.objectSubPropertyAxiomsForSuperProperty(property)).forEach(ax -> {
                extproperties.put(ax.getSubProperty(), ax);
                axioms.add(ax);
            });
            writeSection(SUPER_PROPERTY_OF, extproperties, ",", true);
        }
    }

    protected void addCharacteristic(String value, Collection<OWLAxiom> axioms,
        SectionMap<Object, OWLAxiom> characteristics, OWLAxiom ax) {
        characteristics.put(value, ax);
        axioms.add(ax);
    }

    /**
     * @param property the property
     * @return the sets the
     */
    public Collection<OWLAxiom> write(OWLDataProperty property) {
        List<OWLAxiom> axioms = new ArrayList<>();
        axioms.addAll(writeEntityStart(DATA_PROPERTY, property));
        if (!isFiltered(AxiomType.FUNCTIONAL_DATA_PROPERTY)) {
            SectionMap<Object, OWLAxiom> characteristics = new SectionMap<>();
            filtersort(o.functionalDataPropertyAxioms(property)).forEach(ax -> {
                characteristics.put(FUNCTIONAL.toString(), ax);
                axioms.add(ax);
            });
            writeSection(CHARACTERISTICS, characteristics, ",", true);
        }
        if (!isFiltered(AxiomType.DATA_PROPERTY_DOMAIN)) {
            SectionMap<Object, OWLAxiom> domains = new SectionMap<>();
            filtersort(o.dataPropertyDomainAxioms(property)).forEach(ax -> {
                domains.put(ax.getDomain(), ax);
                axioms.add(ax);
            });
            writeSection(DOMAIN, domains, ",", true);
        }
        if (!isFiltered(AxiomType.DATA_PROPERTY_RANGE)) {
            SectionMap<Object, OWLAxiom> ranges = new SectionMap<>();
            filtersort(o.dataPropertyRangeAxioms(property)).forEach(ax -> {
                ranges.put(ax.getRange(), ax);
                axioms.add(ax);
            });
            writeSection(RANGE, ranges, ",", true);
        }
        if (!isFiltered(AxiomType.SUB_DATA_PROPERTY)) {
            SectionMap<Object, OWLAxiom> supers = new SectionMap<>();
            filtersort(o.dataSubPropertyAxiomsForSubProperty(property)).forEach(ax -> {
                supers.put(ax.getSuperProperty(), ax);
                axioms.add(ax);
            });
            writeSection(SUB_PROPERTY_OF, supers, ",", true);
        }
        if (!isFiltered(AxiomType.EQUIVALENT_DATA_PROPERTIES)) {
            SectionMap<Object, OWLAxiom> properties = new SectionMap<>();
            filtersort(o.equivalentDataPropertiesAxioms(property), props).forEach(ax -> {
                properties.put(ax.getPropertiesMinus(property).iterator().next(), ax);
                axioms.add(ax);
            });
            writeSection(EQUIVALENT_TO, properties, ",", true);
        }
        if (!isFiltered(AxiomType.DISJOINT_DATA_PROPERTIES)) {
            SectionMap<Object, OWLAxiom> properties = new SectionMap<>();
            filtersort(o.disjointDataPropertiesAxioms(property), props).forEach(ax -> {
                properties.put(ax.getPropertiesMinus(property).iterator().next(), ax);
                axioms.add(ax);
            });
            properties.remove(property);
            writeSection(DISJOINT_WITH, properties, ",", true);
        }
        if (!isFiltered(AxiomType.SWRL_RULE)) {
            // XXX is rules used?
            List<OWLAxiom> rules = new ArrayList<>();
            filtersort(o.axioms(AxiomType.SWRL_RULE)).forEach(rule -> {
                for (SWRLAtom atom : rule.headList()) {
                    if (atom.getPredicate().equals(property)) {
                        writeSection(RULE, rules.iterator(), "", true);
                        break;
                    }
                }
            });
        }
        writeEntitySectionEnd(DATA_PROPERTY.toString());
        return axioms;
    }

    /**
     * @param individual the individual
     * @return the sets the
     */
    public Collection<OWLAxiom> write(OWLIndividual individual) {
        List<OWLAxiom> axioms = new ArrayList<>();
        axioms.addAll(writeEntityStart(INDIVIDUAL, individual));
        if (!isFiltered(AxiomType.CLASS_ASSERTION)) {
            SectionMap<Object, OWLAxiom> expressions = new SectionMap<>();
            filtersort(o.classAssertionAxioms(individual)).forEach(ax -> {
                expressions.put(ax.getClassExpression(), ax);
                axioms.add(ax);
            });
            writeSection(TYPES, expressions, ",", true);
        }
        Stream<Stream<? extends OWLPropertyAssertionAxiom<?, ?>>> stream =
            Stream.of(o.objectPropertyAssertionAxioms(individual),
                o.negativeObjectPropertyAssertionAxioms(individual),
                o.dataPropertyAssertionAxioms(individual),
                o.negativeDataPropertyAssertionAxioms(individual));
        List<OWLPropertyAssertionAxiom<?, ?>> assertions =
            stream.flatMap(Function.identity()).sorted(ooc).toList();
        if (!assertions.isEmpty()) {
            handleAssertions(assertions);
        }
        if (!isFiltered(AxiomType.SAME_INDIVIDUAL)) {
            Collection<OWLIndividual> inds = sortedCollection();
            filtersort(o.sameIndividualAxioms(individual)).forEach(ax -> {
                if (ax.getOperandsAsList().size() == 2 && ax.annotationsAsList().isEmpty()) {
                    add(inds, ax.individuals());
                    axioms.add(ax);
                }
            });
            inds.remove(individual);
            writeSection(SAME_AS, inds.iterator(), ",", true);
        }
        if (!isFiltered(AxiomType.DIFFERENT_INDIVIDUALS)) {
            Collection<OWLIndividual> inds = sortedCollection();
            Collection<OWLDifferentIndividualsAxiom> nary = sortedCollection();
            filtersort(o.differentIndividualAxioms(individual)).forEach(ax -> {
                if (ax.getOperandsAsList().size() == 2 && ax.annotationsAsList().isEmpty()) {
                    add(inds, ax.individuals());
                    axioms.add(ax);
                } else {
                    nary.add(ax);
                }
            });
            inds.remove(individual);
            writeSection(DIFFERENT_FROM, inds.iterator(), ",", true);
            if (renderExtensions) {
                nary.forEach(ax -> writeSection(DIFFERENT_INDIVIDUALS, ax.individuals().iterator(),
                    ", ", false));
            }
        }
        writeEntitySectionEnd(INDIVIDUAL.toString());
        return axioms;
    }

    protected void handleAssertions(List<OWLPropertyAssertionAxiom<?, ?>> assertions) {
        fireSectionRenderingPrepared(FACTS.toString());
        writeSection(FACTS);
        writeSpace();
        writeOntologiesList(o);
        incrementTab(1);
        writeNewLine();
        fireSectionRenderingStarted(FACTS.toString());
        for (Iterator<OWLPropertyAssertionAxiom<?, ?>> it = assertions.iterator(); it.hasNext();) {
            OWLPropertyAssertionAxiom<?, ?> ax = it.next();
            fireSectionItemPrepared(FACTS.toString());
            Iterator<OWLAnnotation> annos = ax.annotations().iterator();
            boolean isNotEmpty = annos.hasNext();
            if (isNotEmpty) {
                writeAnnotations(annos);
                pushTab(getIndent() + 1);
            }
            if (ax instanceof OWLNegativeDataPropertyAssertionAxiom
                || ax instanceof OWLNegativeObjectPropertyAssertionAxiom) {
                write(NOT).writeSpace();
            }
            accept(ax.getProperty()).writeSpace().writeSpace().accept(ax.getObject());
            if (isNotEmpty) {
                popTab();
            }
            fireSectionItemFinished(FACTS.toString());
            if (it.hasNext()) {
                write(",").writeNewLine();
            }
        }
        popTab();
        writeNewLine().writeNewLine();
    }

    /**
     * @param datatype the datatype
     * @return the sets the
     */
    public Collection<OWLAxiom> write(OWLDatatype datatype) {
        List<OWLAxiom> axioms = new ArrayList<>();
        axioms.addAll(writeEntityStart(DATATYPE, datatype));
        if (!isFiltered(AxiomType.DATATYPE_DEFINITION)) {
            Collection<OWLDataRange> dataRanges = sortedCollection();
            o.datatypeDefinitions(datatype).filter(this::isDisplayed).forEach(ax -> {
                axioms.add(ax);
                dataRanges.add(ax.getDataRange());
            });
            writeSection(EQUIVALENT_TO, dataRanges.iterator(), ",", true);
        }
        writeEntitySectionEnd(DATATYPE.toString());
        return axioms;
    }

    /**
     * @param rule the rule
     * @return written axioms
     */
    public Collection<OWLAxiom> write(SWRLRule rule) {
        List<OWLAxiom> axioms = new ArrayList<>(1);
        if (o.containsAxiom(rule)) {
            writeSection(RULE, CollectionFactory.createSet(rule).iterator(), "", true);
            axioms.add(rule);
        }
        return axioms;
    }

    /**
     * @param property the property
     * @return written axioms
     */
    public Collection<OWLAxiom> write(OWLAnnotationProperty property) {
        List<OWLAxiom> axioms = new ArrayList<>();
        axioms.addAll(writeEntityStart(ANNOTATION_PROPERTY, property));
        if (!isFiltered(AxiomType.SUB_ANNOTATION_PROPERTY_OF)) {
            Collection<OWLAnnotationProperty> properties = sortedCollection();
            o.subAnnotationPropertyOfAxioms(property).filter(this::isDisplayed)
                .forEach(ax -> properties.add(ax.getSuperProperty()));
            writeSection(SUB_PROPERTY_OF, properties.iterator(), ",", true);
        }
        if (!isFiltered(AxiomType.ANNOTATION_PROPERTY_DOMAIN)) {
            Collection<IRI> iris = sortedCollection();
            o.annotationPropertyDomainAxioms(property).filter(this::isDisplayed)
                .forEach(ax -> iris.add(ax.getDomain()));
            writeSection(DOMAIN, iris.iterator(), ",", true);
        }
        if (!isFiltered(AxiomType.ANNOTATION_PROPERTY_RANGE)) {
            Collection<IRI> iris = sortedCollection();
            o.annotationPropertyRangeAxioms(property).filter(this::isDisplayed)
                .forEach(ax -> iris.add(ax.getRange()));
            writeSection(RANGE, iris.iterator(), ",", true);
        }
        writeEntitySectionEnd(ANNOTATION_PROPERTY.toString());
        return axioms;
    }

    /**
     * Write entity start.
     *
     * @param keyword the keyword
     * @param entity the entity
     * @return written axioms
     */
    private Collection<OWLAnnotationAssertionAxiom> writeEntityStart(ManchesterOWLSyntax keyword,
        OWLObject entity) {
        event = new RendererEvent(this, entity);
        String kw = keyword.toString();
        fireFrameRenderingPrepared(kw);
        writeSection(keyword);

        boolean resetTab = false;
        if(entity instanceof OWLEntity) {
            Set<OWLAnnotation> annotations = o.declarationAxioms((OWLEntity) entity)
                    .flatMap(HasAnnotations::annotations)
                    .sorted()
                    .collect(Collectors.toSet());

            if (!annotations.isEmpty()) {
                incrementTab(4);
                writeNewLine();
                write(ManchesterOWLSyntax.ANNOTATIONS.toString());
                write(": ");
                pushTab(getIndent() + 1);
                for (Iterator<OWLAnnotation> annoIt = annotations.iterator(); annoIt.hasNext();) {
                    annoIt.next().accept(this);
                    if (annoIt.hasNext()) {
                        write(", ");
                        writeNewLine();
                    }
                }
                popTab();
                popTab();
                incrementTab(2);
                writeNewLine();
                resetTab = true;
            }
        }

        entity.accept(this);
        if(resetTab) {
            popTab();
        }
        fireFrameRenderingStarted(kw);
        writeNewLine();
        incrementTab(4);
        writeNewLine();
        return switch (entity) {
            case OWLEntity e -> writeAnnotations(e.getIRI());
            case OWLAnonymousIndividual i -> writeAnnotations(i);
            default -> Collections.emptySet();
        };
    }

    /**
     * Write annotations.
     *
     * @param subject the subject
     * @return written axioms
     */
    public Collection<OWLAnnotationAssertionAxiom> writeAnnotations(OWLAnnotationSubject subject) {
        Collection<OWLAnnotationAssertionAxiom> axioms = sortedCollection();
        if (!isFiltered(AxiomType.ANNOTATION_ASSERTION)) {
            SectionMap<Object, OWLAxiom> sectionMap = new SectionMap<>();
            filtersort(o.annotationAssertionAxioms(subject)).forEach(ax -> {
                axioms.add(ax);
                sectionMap.put(ax.getAnnotation(), ax);
            });
            writeSection(ANNOTATIONS, sectionMap, ",", true);
        }
        return axioms;
    }

    protected ManchesterOWLSyntaxObjectRenderer writeSection(ManchesterOWLSyntax keyword) {
        write("", keyword, "");
        write(":");
        return writeSpace();
    }

    private void writeSection(ManchesterOWLSyntax keyword, SectionMap<Object, OWLAxiom> content,
        String delimiter, boolean newline) {
        String sec = keyword.toString();
        if (content.isNotEmpty() || renderingDirector.renderEmptyFrameSection(keyword, o)) {
            fireSectionRenderingPrepared(sec);
            writeSection(keyword);
            writeOntologiesList(o);
            incrementTab(4);
            writeNewLine();
            fireSectionRenderingStarted(sec);
            sectionObjects(content, delimiter, newline, sec);
            fireSectionRenderingFinished(sec);
            popTab();
            writeNewLine().writeNewLine();
        }
    }

    protected void sectionObjects(SectionMap<Object, OWLAxiom> content, String delimiter,
        boolean newline, String sec) {
        for (Iterator<Object> it = content.getSectionObjects().iterator(); it.hasNext();) {
            Object obj = it.next();
            iterate(content.getAnnotationsForSectionObject(obj).iterator(),
                x -> handleAnnotationSet(delimiter, newline, sec, obj, x), () -> write(",\n"));
            if (it.hasNext()) {
                write(delimiter);
                fireSectionItemFinished(sec);
                if (newline) {
                    writeNewLine();
                }
            } else {
                fireSectionItemFinished(sec);
            }
        }
    }

    protected void handleAnnotationSet(String delimiter, boolean newline, String sec, Object obj,
        Collection<OWLAnnotation> annos) {
        fireSectionItemPrepared(sec);
        handleAnnotations(annos);
        // Write actual object
        handleObject(delimiter, newline, obj);
    }

    protected void handleAnnotations(Collection<OWLAnnotation> annos) {
        if (!annos.isEmpty()) {
            incrementTab(4);
            writeNewLine().write(ManchesterOWLSyntax.ANNOTATIONS.toString()).write(": ");
            pushTab(getIndent() + 1);
            iterate(annos.iterator());
            popTab();
            popTab();
            writeNewLine();
        }
    }

    protected void handleObject(String delimiter, boolean newline, Object obj) {
        if (obj instanceof OWLObject) {
            accept((OWLObject) obj);
        } else if (obj instanceof Collection) {
            iterate(((Collection<?>) obj).iterator(), this::handleCollectionElement,
                () -> divider(delimiter, newline));
        } else {
            write(obj.toString());
        }
    }

    protected void handleCollectionElement(Object obj) {
        if (obj instanceof OWLObject ob) {
            ob.accept(this);
        } else {
            write(obj.toString());
        }
    }

    /**
     * Write section.
     *
     * @param keyword the keyword
     * @param content the content
     * @param delimiter the delimiter
     * @param newline the newline
     */
    public void writeSection(ManchesterOWLSyntax keyword, Iterator<?> content, String delimiter,
        boolean newline) {
        String sec = keyword.toString();
        if (content.hasNext() || renderingDirector.renderEmptyFrameSection(keyword, o)) {
            fireSectionRenderingPrepared(sec);
            writeSection(keyword);
            writeOntologiesList(o);
            incrementTab(4);
            writeNewLine();
            fireSectionRenderingStarted(sec);
            while (content.hasNext()) {
                nextContentElement(content, delimiter, newline, sec);
            }
            fireSectionRenderingFinished(sec);
            popTab();
            writeNewLine().writeNewLine();
        }
    }

    protected void nextContentElement(Iterator<?> content, String delimiter, boolean newline,
        String sec) {
        Object obj = content.next();
        fireSectionItemPrepared(sec);
        if (obj instanceof OWLObject) {
            ((OWLObject) obj).accept(this);
        } else {
            write(obj.toString());
        }
        if (content.hasNext()) {
            write(delimiter);
            fireSectionItemFinished(sec);
            if (newline) {
                writeNewLine();
            }
        } else {
            fireSectionItemFinished(sec);
        }
    }

    /**
     * Write comment.
     *
     * @param comment the comment
     * @param placeOnNewline the place on newline
     */
    public void writeComment(String comment, boolean placeOnNewline) {
        writeComment("#", comment, placeOnNewline);
    }

    /**
     * @param commentDelim the comment delim
     * @param comment the comment
     * @param placeOnNewline the place on newline
     */
    public void writeComment(String commentDelim, String comment, boolean placeOnNewline) {
        if (placeOnNewline) {
            writeNewLine();
        }
        write(commentDelim).write(comment).writeNewLine();
    }

    /**
     * Write ontologies list.
     *
     * @param ontologiesList the ontologies list
     */
    private void writeOntologiesList(OWLOntology... ontologiesList) {
        if (!renderExtensions) {
            return;
        }
        if (ontologiesList.length == 0) {
            return;
        }
        write("[in ");
        int count = 0;
        for (OWLOntology ont : ontologiesList) {
            write(shortFormProvider.getShortForm(ont));
            count++;
            if (count < ontologiesList.length) {
                write(", ");
            }
        }
        write("]");
    }

    /**
     * Fire frame rendering prepared.
     *
     * @param section the section
     */
    private void fireFrameRenderingPrepared(String section) {
        listeners.forEach(l -> l.frameRenderingPrepared(section, event));
    }

    /**
     * Fire frame rendering started.
     *
     * @param section the section
     */
    private void fireFrameRenderingStarted(String section) {
        listeners.forEach(l -> l.frameRenderingStarted(section, event));
    }

    /**
     * Fire frame rendering finished.
     *
     * @param section the section
     */
    private void fireFrameRenderingFinished(String section) {
        listeners.forEach(l -> l.frameRenderingFinished(section, event));
    }

    /**
     * Fire section rendering prepared.
     *
     * @param section the section
     */
    private void fireSectionRenderingPrepared(String section) {
        listeners.forEach(l -> l.sectionRenderingPrepared(section, event));
    }

    /**
     * Fire section rendering started.
     *
     * @param section the section
     */
    private void fireSectionRenderingStarted(String section) {
        listeners.forEach(l -> l.sectionRenderingStarted(section, event));
    }

    /**
     * Fire section rendering finished.
     *
     * @param section the section
     */
    private void fireSectionRenderingFinished(String section) {
        listeners.forEach(l -> l.sectionRenderingFinished(section, event));
    }

    /**
     * Fire section item prepared.
     *
     * @param section the section
     */
    private void fireSectionItemPrepared(String section) {
        listeners.forEach(l -> l.sectionItemPrepared(section, event));
    }

    /**
     * Fire section item finished.
     *
     * @param section the section
     */
    private void fireSectionItemFinished(String section) {
        listeners.forEach(l -> l.sectionItemFinished(section, event));
    }

    <E extends OWLObject> Collection<E> sortedCollection() {
        return new TreeSet<>(ooc);
    }

    private class SectionMap<O, V extends OWLAxiom> {

        private final Map<O, Collection<V>> object2Axioms = new LinkedHashMap<>();

        SectionMap() {}

        boolean isNotEmpty() {
            return !object2Axioms.isEmpty();
        }

        void put(O obj, V forAxiom) {
            object2Axioms.computeIfAbsent(obj, x -> sortedCollection()).add(forAxiom);
        }

        void remove(O obj) {
            object2Axioms.remove(obj);
        }

        Collection<O> getSectionObjects() {
            return object2Axioms.keySet();
        }

        Collection<List<OWLAnnotation>> getAnnotationsForSectionObject(Object sectionObject) {
            Collection<V> axioms = object2Axioms.get(sectionObject);
            if (axioms == null) {
                return sortedSet();
            }
            Collection<List<OWLAnnotation>> annos = new ArrayList<>();
            axioms.forEach(ax -> annos.add(ax.annotationsAsList()));
            return annos;
        }
    }
}
