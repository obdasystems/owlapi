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
package org.semanticweb.owlapi.manchestersyntax.parser;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import org.semanticweb.owlapi.io.OWLParserException;

/**
 * The Class ParserException.
 *
 * @author Matthew Horridge, The University Of Manchester, Bio-Health Informatics Group
 * @since 2.2.0
 */
public class ParserException extends OWLParserException {

    private final String currentToken;
    private final int lineNumber;
    private final int columnNumber;
    private final List<String> tokenSequence;
    private final Set<String> expectedKeywords = new LinkedHashSet<>();
    private final int startPos;
    private final boolean classNameExpected;
    private final boolean objectPropertyNameExpected;
    private final boolean dataPropertyNameExpected;
    private final boolean individualNameExpected;
    private final boolean datatypeNameExpected;
    private final boolean annotationPropertyExpected;
    private final boolean integerExpected;
    private final boolean ontologyNameExpected;

    /**
     * @param message          the message
     * @param tokenSequence    the token sequence
     * @param startPos         the start pos
     * @param lineNumber       the line number
     * @param columnNumber     the column number
     * @param expectedKeywords the expected keywords
     * @param flags            error flags
     */
    public ParserException(String message, List<String> tokenSequence, int startPos, int lineNumber,
        int columnNumber, @Nullable Set<String> expectedKeywords, boolean... flags) {
        super(message);
        currentToken = tokenSequence.iterator().next();
        this.tokenSequence = tokenSequence;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
        if (expectedKeywords != null) {
            this.expectedKeywords.addAll(expectedKeywords);
        }
        this.startPos = startPos;
        classNameExpected = flags[0];
        objectPropertyNameExpected = flags[1];
        dataPropertyNameExpected = flags[2];
        individualNameExpected = flags[3];
        datatypeNameExpected = flags[4];
        annotationPropertyExpected = flags[5];
        ontologyNameExpected = flags[6];
        integerExpected = flags[7];
    }

    /**
     * @return the token sequence
     */
    public List<String> getTokenSequence() {
        return tokenSequence;
    }

    /**
     * @return the start pos
     */
    public int getStartPos() {
        return startPos;
    }

    /**
     * @return true, if is class name expected
     */
    public boolean isClassNameExpected() {
        return classNameExpected;
    }

    /**
     * @return true, if is object property name expected
     */
    public boolean isObjectPropertyNameExpected() {
        return objectPropertyNameExpected;
    }

    /**
     * @return true, if is data property name expected
     */
    public boolean isDataPropertyNameExpected() {
        return dataPropertyNameExpected;
    }

    /**
     * @return true, if is individual name expected
     */
    public boolean isIndividualNameExpected() {
        return individualNameExpected;
    }

    /**
     * @return true, if is datatype name expected
     */
    public boolean isDatatypeNameExpected() {
        return datatypeNameExpected;
    }

    /**
     * @return true, if is annotation property name expected
     */
    public boolean isAnnotationPropertyNameExpected() {
        return annotationPropertyExpected;
    }

    /**
     * @return true, if is ontology name expected
     */
    public boolean isOntologyNameExpected() {
        return ontologyNameExpected;
    }

    /**
     * @return the expected keywords
     */
    public Set<String> getExpectedKeywords() {
        return expectedKeywords;
    }

    /**
     * @return the current token
     */
    public String getCurrentToken() {
        return currentToken;
    }

    @Override
    public int getLineNumber() {
        return lineNumber;
    }

    @Override
    public int getColumnNumber() {
        return columnNumber;
    }

    /**
     * @return true, if is integer expected
     */
    public boolean isIntegerExpected() {
        return integerExpected;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder(1000);
        String topMessage = super.getMessage();
        if (topMessage != null) {
            sb.append(topMessage).append("\n");
        }
        sb.append("Encountered ");
        sb.append(currentToken);
        sb.append(" at line ");
        sb.append(lineNumber);
        sb.append(" column ");
        sb.append(columnNumber);
        sb.append(". Expected one of:\n");
        if (ontologyNameExpected) {
            sb.append("\tOntology name\n");
        }
        if (classNameExpected) {
            sb.append("\tClass name\n");
        }
        if (objectPropertyNameExpected) {
            sb.append("\tObject property name\n");
        }
        if (dataPropertyNameExpected) {
            sb.append("\tData property name\n");
        }
        if (individualNameExpected) {
            sb.append("\tIndividual name\n");
        }
        if (datatypeNameExpected) {
            sb.append("\tDatatype name\n");
        }
        if (annotationPropertyExpected) {
            sb.append("\tAnnotation property\n");
        }
        if (integerExpected) {
            sb.append("\tInteger\n");
        }
        expectedKeywords.forEach(kw -> sb.append('\t').append(kw).append('\n'));
        return sb.toString();
    }
}
