package org.semanticweb.owlapi.rdf.rdfxml.parser;

import static org.semanticweb.owlapi.rdf.rdfxml.parser.HandlerFunction.NO_OP_IRI;
import static org.semanticweb.owlapi.rdf.rdfxml.parser.HandlerFunction.NO_OP_LIT;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.OWL_ALL_VALUES_FROM;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.OWL_ANNOTATED_PROPERTY;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.OWL_ANNOTATED_SOURCE;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.OWL_ANNOTATED_TARGET;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.OWL_COMPLEMENT_OF;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.OWL_DATATYPE_COMPLEMENT_OF;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.OWL_DECLARED_AS;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.OWL_DIFFERENT_FROM;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.OWL_DISJOINT_UNION_OF;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.OWL_DISJOINT_WITH;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.OWL_EQUIVALENT_CLASS;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.OWL_EQUIVALENT_PROPERTY;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.OWL_HAS_KEY;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.OWL_HAS_VALUE;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.OWL_IMPORTS;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.OWL_INTERSECTION_OF;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.OWL_INVERSE_OF;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.OWL_ONE_OF;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.OWL_ON_CLASS;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.OWL_ON_DATA_RANGE;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.OWL_ON_PROPERTY;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.OWL_PROPERTY_CHAIN_AXIOM;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.OWL_PROPERTY_DISJOINT_WITH;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.OWL_SAME_AS;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.OWL_SOME_VALUES_FROM;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.OWL_UNION_OF;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.OWL_VERSION_IRI;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.RDFS_DOMAIN;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.RDFS_RANGE;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.RDFS_SUBCLASS_OF;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.RDFS_SUB_PROPERTY_OF;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.RDF_FIRST;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.RDF_REST;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.RDF_TYPE;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import org.semanticweb.owlapi.model.HasIRI;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Matthew Horridge, The University Of Manchester, Bio-Health Informatics Group
 * @since 2.0.0
 */
@SuppressWarnings({"null"})
enum AbstractTriplePH implements TriplePredicateHandler, LiteralTripleHandler {
    //@formatter:off
    FIRSTLITERALHANDLER             (                           
        (c, s, p, o) -> RDF_FIRST.getIRI().equals(p), 
        (c, s, p, o) -> RDF_FIRST.getIRI().equals(p), 
        (c, s, p, o) -> c.addFirst(s, p, o)), 
    ANNOTATIONLITERALHANDLER        (                           
        (c, s, p, o) -> c.canHandleAnnotationLiteral(s, p), 
        (c, s, p, o) -> false, 
        (c, s, p, o) -> c.handleAnnotationLiteralTriple(s, p, o)), 
    ANNOTATIONRESOURCETRIPLEHANDLER (null,                      
        (c, s, p, o) -> c.canHandleAnnotationResource(s, p), 
        (c, s, p, o) -> false, 
        (c, s, p, o) -> c.handleAnnotationResourceTriple(s, p, o)), 
    DATAPROPERTYASSERTIONHANDLER    (                           
        (c, s, p, o) -> c.canHandleDataAssertion(p), 
        NO_OP_LIT, 
        (c, s, p, o) -> c.handleDataAssertionTriple(s, p, o)), 
    OBJECTPROPERTYASSERTIONHANDLER  (null,                      
        (c, s, p, o) -> c.canHandleObjectAssertion(p), 
        NO_OP_IRI, 
        (c, s, p, o) -> c.handleObjectAssertionTriple(s, p, o)), 
    GTPLITERALTRIPLEHANDLER         (                           
        (c, s, p, o) -> c.canHandleLiteral(p),
        (c, s, p, o) -> c.canHandleLiteralStreaming(p), 
        NO_OP_LIT), 
    COMPLEMENTOFHANDLER             (OWL_COMPLEMENT_OF,         
        (c, s, p, o) -> defaultCanHandle(OWL_COMPLEMENT_OF, c, s, p, o) && c.canHandleEquivalentClass(s),
        (c, s, p, o) -> c.canHandleComplementOfStreaming(s, o), 
        (c, s, p, o) -> c.handleNamedEquivalentComplementOfTriple(s, p, o)), 
    DECLAREDASHANDLER               (OWL_DECLARED_AS,           
        (c, s, p, o) -> defaultCanHandle(OWL_DECLARED_AS, c, s, p, o), 
        (c, s, p, o) -> true,
        (c, s, p, o) -> c.handleDeclarationTriple(s, p, o)), 
    DIFFERENTFROMHANDLER            (OWL_DIFFERENT_FROM,        
        (c, s, p, o) -> defaultCanHandle(OWL_DIFFERENT_FROM, c, s, p, o),
        (c, s, p, o) -> true, 
        (c, s, p, o) -> c.handleDifferentFromTriple(s, p, o)), 
    DISJOINTUNIONHANDLER            (OWL_DISJOINT_UNION_OF,     
        (c, s, p, o) -> defaultCanHandle(OWL_DISJOINT_UNION_OF, c, s, p, o) && c.canHandleDisjointUnion(s), 
        (c, s, p, o) -> c.canHandleDisjointUnionStreaming(s),
        (c, s, p, o) -> c.handleDisjointUnionTriple(s, p, o)), 
    DISJOINTWITHHANDLER             (OWL_DISJOINT_WITH,         
        (c, s, p, o) -> defaultCanHandle(OWL_DISJOINT_WITH, c, s, p, o) && c.iris.bothCe(s, o),
        (c, s, p, o) -> c.canHandleDisjointWithStreaming(s, o), 
        (c, s, p, o) -> c.handleDisjointwithTriple(s, p, o)), 
    EQUIVALENTCLASSHANDLER          (OWL_EQUIVALENT_CLASS,      
        (c, s, p, o) -> defaultCanHandle(OWL_EQUIVALENT_CLASS, c, s, p, o) && c.iris.bothClassOrDataRange(s, o), 
        (c, s, p, o) -> c.canHandleEquivalentClassesStreaming(s, o),
        (c, s, p, o) -> c.handleEquivalentClassTriple(s, p, o)), 
    FIRSTRESOURCEHANDLER            (RDF_FIRST,                 
        (c, s, p, o) -> defaultCanHandle(RDF_FIRST, c, s, p, o), 
        (c, s, p, o) -> true,
        (c, s, p, o) -> c.addFirst(s, p, o)), 
    HASKEYHANDLER                   (OWL_HAS_KEY,               
        (c, s, p, o) -> defaultCanHandle(OWL_HAS_KEY, c, s, p, o),
        (c, s, p, o) -> c.canHandleHasKeyStreaming(s),
        (c, s, p, o) -> c.handleHasKeyTriple(s, p, o, Translators.getListTranslator(c))), 
    PROPERTYCHAINAH                 (OWL_PROPERTY_CHAIN_AXIOM,  
        (c, s, p, o) -> defaultCanHandle(OWL_PROPERTY_CHAIN_AXIOM, c, s, p, o),
        (c, s, p, o) -> c.canHandlePropertyChainStreaming(o),
        (c, s, p, o) -> c.handlePropertyChainTriple(s, p, o)), 
    PROPERTYDISJOINTWITHHANDLER     (OWL_PROPERTY_DISJOINT_WITH,
        (c, s, p, o) -> defaultCanHandle(OWL_PROPERTY_DISJOINT_WITH, c, s, p, o) && c.canHandlePropertyDisjoint(s, o), 
        (c, s, p, o) -> defaultCanHandle(OWL_PROPERTY_DISJOINT_WITH, c, s, p, o) && c.canHandlePropertyDisjoint(s, o),
        (c, s, p, o) -> c.handlePropertyDisjointTriple(s, p, o)), 
    PROPERTYRANGEHANDLER            (RDFS_RANGE,                
        (c, s, p, o) -> defaultCanHandle(RDFS_RANGE, c, s, p, o),
        (c, s, p, o) -> c.canHandlePropertyRangeStreaming(s, o), 
        (c, s, p, o) -> c.handlePropertyRangeTriple(s, p, o)), 
    RESTHANDLER                     (RDF_REST,                  
        (c, s, p, o) -> defaultCanHandle(RDF_REST, c, s, p, o),
        (c, s, p, o) -> true, 
        (c, s, p, o) -> c.handleRestTriple(s, p, o)), 
    SAMEASHANDLER                   (OWL_SAME_AS,               
        (c, s, p, o) -> defaultCanHandle(OWL_SAME_AS, c, s, p, o),
        (c, s, p, o) -> true, 
        (c, s, p, o) -> c.handleSameHastriple(s, p, o)), 
    SOMEVALUESFROMHANDLER           (OWL_SOME_VALUES_FROM,      
        (c, s, p, o) -> defaultCanHandle(OWL_SOME_VALUES_FROM, c, s, p, o), 
        (c, s, p, o) -> c.handleSameValuesFromTriple(s, p, o), 
        (c, s, p, o) -> c.handleSameValuesFromTriple(s, p, o)), 
    IMPORTSHANDLER                  (OWL_IMPORTS,               
        (c, s, p, o) -> defaultCanHandle(OWL_IMPORTS, c, s, p, o), 
        (c, s, p, o) -> true, 
        (c, s, p, o) -> c.handleImportsTriple(s, p, o)), 
    /**
     * A handler for top level intersection classes.
     */
    INTERSECTIONOFHANDLER           (OWL_INTERSECTION_OF,       
        (c, s, p, o) -> defaultCanHandle(OWL_INTERSECTION_OF, c, s, p, o) && c.canHandleEquivalentClass(s), 
        (c, s, p, o) -> c.canHandleIntersectionStreaming(s, o), 
        (c, s, p, o) -> c.handleNamedEquivalentIntersectionTriple(s, p, o)), 
    /**
     * owl:inverseOf is used in both property expressions AND axioms.
     */
    INVERSEOFHANDLER                (OWL_INVERSE_OF,            
        (c, s, p, o) -> defaultCanHandle(OWL_INVERSE_OF, c, s, p, o) && c.canHandleInverseOf(s, o), 
        (c, s, p, o) -> c.canHandleInverseOfStreaming(s, o), 
        (c, s, p, o) -> c.handleInverseOfTriple(s, p, o)), 
    /**
     * Handles rdfs:subClassOf triples. If handling is set to strict then the triple is only
     * consumed if the s and o are typed as classes.
     */
    SUBCLASSOFHANDLER               (RDFS_SUBCLASS_OF,          
        (c, s, p, o) -> defaultCanHandle(RDFS_SUBCLASS_OF, c, s, p, o) && c.iris.isTyped(s, o), 
        (c, s, p, o) -> c.canHandleSubClassStreaming(s, o), 
        (c, s, p, o) -> c.handleSubClassTriple(s, p, o)), 
    SUBPROPERTYOFHANDLER            (RDFS_SUB_PROPERTY_OF,      
        (c, s, p, o) -> defaultCanHandle(RDFS_SUB_PROPERTY_OF, c, s, p, o),
        (c, s, p, o) -> c.canHandleSubPropertyStreaming(s, o), 
        (c, s, p, o) -> c.handleSubPropertyTriple(s, p, o)), 
    TYPEHANDLER                     (RDF_TYPE,                  
        (c, s, p, o) -> defaultCanHandle(RDF_TYPE, c, s, p, o), 
        (c, s, p, o) -> c.canHandleTypeStreaming(o), 
        (c, s, p, o) -> c.handleTypeAssertionTriple(s, p, o)), 
    EQUIVALENTPROPERTYHANDLER       (OWL_EQUIVALENT_PROPERTY,   
        (c, s, p, o) -> defaultCanHandle(OWL_EQUIVALENT_PROPERTY, c, s, p, o), 
        NO_OP_IRI, 
        (c, s, p, o) -> c.handleEquivalentPropertyTriple(s, p, o)), 
    HASVALUEHANDLER                 (OWL_HAS_VALUE,             
        (c, s, p, o) -> defaultCanHandle(OWL_HAS_VALUE, c, s, p, o),
        (c, s, p, o) -> c.canHandleHasValueStreaming(s), 
        NO_OP_IRI), 
    ALLVALUESFROMHANDLER            (OWL_ALL_VALUES_FROM,       
        (c, s, p, o) -> defaultCanHandle(OWL_ALL_VALUES_FROM, c, s, p, o),
        (c, s, p, o) -> c.canHandleAllValuesStreaming(s, p, o), 
        NO_OP_IRI), 
    ANNOTATEDPROPERTYHANDLER        (OWL_ANNOTATED_PROPERTY,    
        (c, s, p, o) -> defaultCanHandle(OWL_ANNOTATED_PROPERTY, c, s, p, o),
        (c, s, p, o) -> c.canHandleAnnotatedPropertyStreaming(s, o), 
        NO_OP_IRI), 
    ANNOTATEDSOURCEHANDLER          (OWL_ANNOTATED_SOURCE,      
        (c, s, p, o) -> defaultCanHandle(OWL_ANNOTATED_SOURCE, c, s, p, o),
        (c, s, p, o) -> c.canHandleannotatedSourceStreaming(s, o), 
        NO_OP_IRI), 
    ANNOTATEDTARGETHANDLER          (OWL_ANNOTATED_TARGET,      
        (c, s, p, o) -> defaultCanHandle(OWL_ANNOTATED_TARGET, c, s, p, o),
        (c, s, p, o) -> c.canHandleAnnotatedTargetStreaming(s, o), 
        NO_OP_IRI), 
    DATATYPECOMPLEMENTOFHANDLER     (OWL_DATATYPE_COMPLEMENT_OF,
        NO_OP_IRI, (c, s, p, o) -> c.canHandleDatatypeComplementOfStreaming(s, o), NO_OP_IRI), 
    ONCLASSHANDLER                  (OWL_ON_CLASS,              
        NO_OP_IRI, (c, s, p, o) -> c.canHandleOnClassStreamng(o), NO_OP_IRI), 
    ONDATARANGEHANDLER              (OWL_ON_DATA_RANGE,         
        NO_OP_IRI, (c, s, p, o) -> c.canHandleOnDataStreaming(o), NO_OP_IRI), 
    ONPROPERTYHANDLER               (OWL_ON_PROPERTY,           
        (c, s, p, o) -> defaultCanHandle(OWL_ON_PROPERTY, c, s, p, o), 
        (c, s, p, o) -> c.canHandleOnPropertyStreaming(s), 
        NO_OP_IRI), 
    ONEOFHANDLER                    (OWL_ONE_OF,                
        (c, s, p, o) -> defaultCanHandle(OWL_ONE_OF, c, s, p, o) && c.canHandleEquivalentClass(s), 
        NO_OP_IRI, 
        (c, s, p, o) -> c.handleNamedEquivalentOneOfTriple(s, p, o)), 
    PROPERTYDOMAINHANDLER           (RDFS_DOMAIN,               
        (c, s, p, o) -> defaultCanHandle(RDFS_DOMAIN, c, s, p, o), 
        NO_OP_IRI, 
        (c, s, p, o) -> c.handlePropertyDomainTriple(s, p, o)), 
    VERSIONIRIHANDLER               (OWL_VERSION_IRI,           
        (c, s, p, o) -> OWL_VERSION_IRI.getIRI().equals(p), 
        NO_OP_IRI, 
        (c, s, p, o) -> c.handleVersionTriple(s, p, o)), 
    UNIONOFHANDLER                  (OWL_UNION_OF,              
        (c, s, p, o) -> defaultCanHandle(OWL_UNION_OF, c, s, p, o) && c.canHandleEquivalentClass(s), 
        NO_OP_IRI, 
        (c, s, p, o) -> c .handleNamedEquivalentUnionTriple( s, p, o));
    //@formatter:on
    static final Logger LOGGER = LoggerFactory.getLogger(AbstractTriplePH.class);
    HasIRI predicateIRI;
    HandlerFunction<IRI> canHandle;
    HandlerFunction<IRI> canHandleStreaming;
    HandlerFunction<IRI> handle;
    HandlerFunction<OWLLiteral> canHandleLit;
    HandlerFunction<OWLLiteral> canHandleStreamingLit;
    HandlerFunction<OWLLiteral> handleLit;

    AbstractTriplePH(HasIRI i, HandlerFunction<IRI> canHandle,
        HandlerFunction<IRI> canHandleStreaming, HandlerFunction<IRI> handle) {
        this(i, canHandle, canHandleStreaming, handle, NO_OP_LIT, NO_OP_LIT, NO_OP_LIT);
    }

    AbstractTriplePH(HandlerFunction<OWLLiteral> canHandle,
        HandlerFunction<OWLLiteral> canHandleStreaming, HandlerFunction<OWLLiteral> handle) {
        this(null, NO_OP_IRI, NO_OP_IRI, NO_OP_IRI, canHandle, canHandleStreaming, handle);
    }

    AbstractTriplePH(@Nullable HasIRI i, HandlerFunction<IRI> canHandle,
        HandlerFunction<IRI> canHandleStreaming, HandlerFunction<IRI> handle,
        HandlerFunction<OWLLiteral> canHandleLit, HandlerFunction<OWLLiteral> canHandleStreamingLit,
        HandlerFunction<OWLLiteral> handleLit) {
        predicateIRI = i;
        this.canHandle = canHandle;
        this.canHandleStreaming = canHandleStreaming;
        this.handle = handle;
        this.canHandleLit = canHandleLit;
        this.canHandleStreamingLit = canHandleStreamingLit;
        this.handleLit = handleLit;
    }

    /**
     * General literal triples - i.e. triples which have a predicate that is not a built in IRI.
     * Annotation properties get precedence over data properties, so that if we have the
     * statement<br>
     * a:A a:foo a:B<br>
     * and a:foo is typed as both an annotation and data property then the statement will be
     * translated as an annotation on a:A
     *
     * @return handlers
     */
    public static List<LiteralTripleHandler> getLiteralTripleHandlers() {
        return Arrays.asList(DATAPROPERTYASSERTIONHANDLER, FIRSTLITERALHANDLER,
            ANNOTATIONLITERALHANDLER);
    }

    public static Map<IRI, TriplePredicateHandler> getPredicateHandlers() {
        return Stream.of(DIFFERENTFROMHANDLER, DISJOINTUNIONHANDLER, DISJOINTWITHHANDLER,
            EQUIVALENTCLASSHANDLER, EQUIVALENTPROPERTYHANDLER, PROPERTYDOMAINHANDLER,
            PROPERTYRANGEHANDLER, SAMEASHANDLER, SUBCLASSOFHANDLER, SUBPROPERTYOFHANDLER,
            TYPEHANDLER, IMPORTSHANDLER, INTERSECTIONOFHANDLER, UNIONOFHANDLER, COMPLEMENTOFHANDLER,
            ONEOFHANDLER, HASVALUEHANDLER, SOMEVALUESFROMHANDLER, ALLVALUESFROMHANDLER, RESTHANDLER,
            FIRSTRESOURCEHANDLER, DECLAREDASHANDLER, HASKEYHANDLER, VERSIONIRIHANDLER,
            PROPERTYCHAINAH, ANNOTATEDSOURCEHANDLER, ANNOTATEDPROPERTYHANDLER,
            ANNOTATEDTARGETHANDLER, PROPERTYDISJOINTWITHHANDLER, INVERSEOFHANDLER,
            ONPROPERTYHANDLER, ONCLASSHANDLER, ONDATARANGEHANDLER, DATATYPECOMPLEMENTOFHANDLER)
            .collect(Collectors.toUnmodifiableMap(TriplePredicateHandler::getPredicateIRI,
                Function.identity()));
    }

    public static boolean defaultCanHandle(HasIRI iri, OWLRDFConsumer c, IRI s, IRI p, IRI o) {
        c.iris.inferTypes(s, o);
        return iri.getIRI().equals(p);
    }

    @Override
    public final boolean canHandle(OWLRDFConsumer c, IRI subject, IRI predicate, IRI object) {
        return canHandle.handle(c, subject, predicate, object);
    }

    @Override
    public final boolean canHandleStreaming(OWLRDFConsumer c, IRI subject, IRI predicate,
        IRI object) {
        return canHandleStreaming.handle(c, subject, predicate, object);
    }

    @Override
    public final boolean handleTriple(OWLRDFConsumer c, IRI subject, IRI predicate, IRI object) {
        return handle.handle(c, subject, predicate, object);
    }

    @Override
    public final boolean canHandle(OWLRDFConsumer c, IRI subject, IRI predicate,
        OWLLiteral object) {
        return canHandleLit.handle(c, subject, predicate, object);
    }

    @Override
    public final boolean canHandleStreaming(OWLRDFConsumer c, IRI subject, IRI predicate,
        OWLLiteral object) {
        return canHandleStreamingLit.handle(c, subject, predicate, object);
    }

    @Override
    public final boolean handleTriple(OWLRDFConsumer c, IRI subject, IRI predicate,
        OWLLiteral object) {
        return handleLit.handle(c, subject, predicate, object);
    }

    @Override
    public IRI getPredicateIRI() {
        return predicateIRI.getIRI();
    }
}
