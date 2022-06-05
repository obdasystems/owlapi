package org.semanticweb.owlapi.obolibrary.oboformat.parser;

import static org.semanticweb.owlapi.utilities.OWLAPIPreconditions.checkNotNull;
import static org.semanticweb.owlapi.utilities.OWLAPIPreconditions.verifyNotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.semanticweb.owlapi.obolibrary.oboformat.model.Clause;
import org.semanticweb.owlapi.obolibrary.oboformat.model.Frame;
import org.semanticweb.owlapi.obolibrary.oboformat.model.Frame.FrameType;
import org.semanticweb.owlapi.obolibrary.oboformat.model.FrameMergeException;
import org.semanticweb.owlapi.obolibrary.oboformat.model.OBODoc;
import org.semanticweb.owlapi.obolibrary.oboformat.model.Xref;
import org.semanticweb.owlapi.vocab.OBOFormatConstants.OboFormatTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Xref expander.
 */
public class XrefExpander {

    protected static final Logger LOG = LoggerFactory.getLogger(XrefExpander.class);
    protected Map<String, Rule> treatMap = new HashMap<>();
    protected Map<String, OBODoc> targetDocMap = new HashMap<>();
    OBODoc sourceOBODoc;
    @Nullable
    OBODoc targetOBODoc;
    @Nullable
    String targetBase;

    /**
     * @param src source
     */
    @SuppressWarnings("null")
    public XrefExpander(OBODoc src) {
        sourceOBODoc = src;
        Frame shf = checkNotNull(src.getHeaderFrame());
        String ontId = shf.getTagValue(OboFormatTag.TAG_ONTOLOGY, String.class);
        String tgtOntId = ontId + "/xref_expansions";
        targetOBODoc = new OBODoc();
        Frame thf = new Frame(FrameType.HEADER);
        thf.addClause(new Clause(OboFormatTag.TAG_ONTOLOGY, tgtOntId));
        targetOBODoc.setHeaderFrame(thf);
        sourceOBODoc.addImportedOBODoc(targetOBODoc);
        setUp();
    }

    /**
     * @param src source
     * @param targetBase target base
     */
    public XrefExpander(OBODoc src, String targetBase) {
        sourceOBODoc = src;
        this.targetBase = targetBase;
        setUp();
    }

    /**
     * @param src source
     * @param tgt target
     */
    public XrefExpander(OBODoc src, OBODoc tgt) {
        sourceOBODoc = src;
        targetOBODoc = tgt;
        setUp();
    }

    private static String getIDSpace(String x) {
        String[] parts = x.split(":", 2);
        return parts[0];
    }

    private static final List<String> XREF_EXPANDABLE =
        Arrays.asList(OboFormatTag.TAG_TREAT_XREFS_AS_EQUIVALENT.getTag(),
            OboFormatTag.TAG_TREAT_XREFS_AS_GENUS_DIFFERENTIA.getTag(),
            OboFormatTag.TAG_TREAT_XREFS_AS_REVERSE_GENUS_DIFFERENTIA.getTag(),
            OboFormatTag.TAG_TREAT_XREFS_AS_HAS_SUBCLASS.getTag(),
            OboFormatTag.TAG_TREAT_XREFS_AS_IS_A.getTag(),
            OboFormatTag.TAG_TREAT_XREFS_AS_RELATIONSHIP.getTag());

    /**
     * Setup the expander.
     */
    public final void setUp() {
        // required for translation of IDs
        Map<String, String> relationsUseByIdSpace = new HashMap<>();
        Frame headerFrame = sourceOBODoc.getHeaderFrame();
        if (headerFrame != null) {
            for (Clause c : headerFrame.getClauses()) {
                if (XREF_EXPANDABLE.contains(c.getTag())) {
                    expand(relationsUseByIdSpace, c, c.getTag());
                }
            }
        }
    }

    protected void expand(Map<String, String> relationsUseByIdSpace, Clause c,
        @Nullable String tag) {
        String v = c.getValue().toString();
        String[] parts = v.split("\\s");
        String idSpace = parts[0];
        assert tag != null;
        String relation = null;
        if (tag.equals(OboFormatTag.TAG_TREAT_XREFS_AS_EQUIVALENT.getTag())) {
            addRule(parts[0], new EquivalenceExpansion());
        } else if (tag.equals(OboFormatTag.TAG_TREAT_XREFS_AS_GENUS_DIFFERENTIA.getTag())) {
            addRule(idSpace, new GenusDifferentiaExpansion(parts[1], parts[2]));
            relationsUseByIdSpace.put(idSpace, parts[1]);
            relation = parts[1];
        } else if (tag.equals(OboFormatTag.TAG_TREAT_XREFS_AS_REVERSE_GENUS_DIFFERENTIA.getTag())) {
            addRule(idSpace, new ReverseGenusDifferentiaExpansion(parts[1], parts[2]));
            relationsUseByIdSpace.put(idSpace, parts[1]);
            relation = parts[1];
        } else if (tag.equals(OboFormatTag.TAG_TREAT_XREFS_AS_HAS_SUBCLASS.getTag())) {
            addRule(idSpace, new HasSubClassExpansion());
        } else if (tag.equals(OboFormatTag.TAG_TREAT_XREFS_AS_IS_A.getTag())) {
            addRule(idSpace, new IsaExpansion());
        } else if (tag.equals(OboFormatTag.TAG_TREAT_XREFS_AS_RELATIONSHIP.getTag())) {
            addRule(idSpace, new RelationshipExpansion(parts[1]));
            relationsUseByIdSpace.put(idSpace, parts[1]);
            relation = parts[1];
        }
        if (targetBase != null) {
            bridgeOntology(idSpace, relation);
        }
    }

    protected void bridgeOntology(String idSpace, @Nullable String relation) {
        // create a new bridge ontology for every expansion macro
        OBODoc tgt = new OBODoc();
        Frame thf = new Frame(FrameType.HEADER);
        thf.addClause(
            new Clause(OboFormatTag.TAG_ONTOLOGY, targetBase + "-" + idSpace.toLowerCase()));
        tgt.setHeaderFrame(thf);
        targetDocMap.put(idSpace, tgt);
        sourceOBODoc.addImportedOBODoc(tgt);
        if (relation != null) {
            // See 4.4.2
            // "In addition, any Typedef frames for relations used
            // in a header macro are also copied into the
            // corresponding bridge ontology
            Frame tdf = sourceOBODoc.getTypedefFrame(relation);
            if (tdf != null) {
                try {
                    tgt.addTypedefFrame(tdf);
                } catch (FrameMergeException e) {
                    LOG.debug("frame merge failed", e);
                }
            }
        }
    }

    /**
     * @param idSpace id space
     * @return target doc
     */
    public OBODoc getTargetDoc(String idSpace) {
        if (targetOBODoc != null) {
            return targetOBODoc;
        }
        return targetDocMap.get(idSpace);
    }

    private void addRule(String db, Rule rule) {
        if (treatMap.containsKey(db)) {
            throw new InvalidXrefMapException(db);
        }
        rule.idSpace = db;
        treatMap.put(db, rule);
    }

    /**
     * Expand xref values.
     */
    public void expandXrefs() {
        for (Frame f : sourceOBODoc.getTermFrames()) {
            String id = checkNotNull(f.getTagValue(OboFormatTag.TAG_ID, String.class));
            Collection<Clause> clauses = f.getClauses(OboFormatTag.TAG_XREF);
            for (Clause c : clauses) {
                Xref x = c.getValue(Xref.class);
                String xid = x.getIdref();
                String s = getIDSpace(xid);
                if (treatMap.containsKey(s)) {
                    treatMap.get(s).expand(f, id, xid);
                }
            }
        }
    }

    /**
     * Rule.
     */
    public abstract class Rule {

        @Nullable
        protected String xref;
        /**
         * Id space.
         */
        @Nullable
        protected String idSpace;

        /**
         * @param sf source frame
         * @param id id
         * @param xRef xref
         */
        public abstract void expand(Frame sf, String id, String xRef);

        protected Frame getTargetFrame(String id) {
            OBODoc targetDoc = getTargetDoc(verifyNotNull(idSpace, "idSpace not set yet"));
            Frame f = targetDoc.getTermFrame(id);
            if (f == null) {
                f = new Frame();
                f.setId(id);
                try {
                    targetDoc.addTermFrame(f);
                } catch (FrameMergeException e) {
                    // this should be impossible
                    LOG.error("Frame merge exceptions should not be possible", e);
                }
            }
            return f;
        }
    }

    /**
     * Equivalence expansion.
     */
    public class EquivalenceExpansion extends Rule {

        @Override
        public void expand(Frame sf, String id, String xRef) {
            Clause c = new Clause(OboFormatTag.TAG_EQUIVALENT_TO, xRef);
            sf.addClause(c);
        }
    }

    /**
     * Subclass expansion.
     */
    public class HasSubClassExpansion extends Rule {

        @Override
        public void expand(Frame sf, String id, String xRef) {
            Clause c = new Clause(OboFormatTag.TAG_IS_A, id);
            getTargetFrame(xRef).addClause(c);
        }
    }

    /**
     * Genus diff expansion.
     */
    public class GenusDifferentiaExpansion extends Rule {

        protected final String rel;
        protected final String tgt;

        /**
         * @param rel relation
         * @param tgt target
         */
        public GenusDifferentiaExpansion(String rel, String tgt) {
            this.rel = rel;
            this.tgt = tgt;
        }

        @Override
        public void expand(Frame sf, String id, String xRef) {
            Clause gc = new Clause(OboFormatTag.TAG_INTERSECTION_OF, xRef);
            Clause dc = new Clause(OboFormatTag.TAG_INTERSECTION_OF);
            dc.setValue(rel);
            dc.addValue(tgt);
            getTargetFrame(id).addClause(gc);
            getTargetFrame(id).addClause(dc);
        }
    }

    /**
     * Reverse genus differentia expansion.
     */
    public class ReverseGenusDifferentiaExpansion extends Rule {

        protected final String rel;
        protected final String tgt;

        /**
         * @param rel relation
         * @param tgt target
         */
        public ReverseGenusDifferentiaExpansion(String rel, String tgt) {
            this.rel = rel;
            this.tgt = tgt;
        }

        @Override
        public void expand(Frame sf, String id, String xRef) {
            Clause gc = new Clause(OboFormatTag.TAG_INTERSECTION_OF, id);
            Clause dc = new Clause(OboFormatTag.TAG_INTERSECTION_OF);
            dc.setValue(rel);
            dc.addValue(tgt);
            getTargetFrame(xRef).addClause(gc);
            getTargetFrame(xRef).addClause(dc);
        }
    }

    /**
     * Is a expansion.
     */
    public class IsaExpansion extends Rule {

        @Override
        public void expand(Frame sf, String id, String xRef) {
            Clause c = new Clause(OboFormatTag.TAG_IS_A, xRef);
            getTargetFrame(id).addClause(c);
        }
    }

    /**
     * Relationship expansion.
     */
    public class RelationshipExpansion extends Rule {

        protected final String rel;

        /**
         * @param rel relation
         */
        public RelationshipExpansion(String rel) {
            this.rel = rel;
        }

        @Override
        public void expand(Frame sf, String id, String xRef) {
            Clause c = new Clause(OboFormatTag.TAG_RELATIONSHIP, rel);
            c.addValue(xRef);
            getTargetFrame(id).addClause(c);
        }
    }
}
