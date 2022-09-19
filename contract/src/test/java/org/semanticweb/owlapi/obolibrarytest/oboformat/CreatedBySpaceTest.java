package org.semanticweb.owlapi.obolibrarytest.oboformat;

import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.formats.OBODocumentFormat;

public class CreatedBySpaceTest extends OboFormatTestBasics {

    @Test
    public void testCreatedByWithSpace() {
        String input = "ontology: test\n[Typedef]\nid: R:1\nname: r1\ncreated_by: John Doe";
        loadOntologyFromString(input, df.generateDocumentIRI(), new OBODocumentFormat());
    }
}
