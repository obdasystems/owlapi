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
package org.semanticweb.owlapi.change;

import static org.semanticweb.owlapi.utilities.OWLAPIPreconditions.checkNotNull;

import java.io.Serializable;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.UnknownOWLOntologyException;

/**
 * Associates an {@link OWLOntologyID} with ontology-less change data.<br>
 * An {@link OWLOntologyChangeRecord} captures information about an {@link OWLOntologyChange} in a
 * way that does not require a reference to an {@link OWLOntology} object. It does this by
 * referencing an {@link OWLOntologyID} instead of referencing an {@link OWLOntology}. The primary
 * reason for doing this is so that changes can be serialized and logged more easily. It should be
 * kept in mind that {@link OWLOntologyChangeRecord} objects can represent changes for which there
 * might be no in memory representation of a specific {@link OWLOntology}. This is also true if an
 * {@link OWLOntology} object has its {@link OWLOntologyID} changed.<br>
 * An {@link OWLOntologyChange} object contains two important pieces of information:
 * <ol>
 * <li>The {@link OWLOntologyID} that identifies the ontology that the change pertains to.</li>
 * <li>The {@link OWLOntologyChangeData} that describes the change specific data. For each kind of
 * {@link OWLOntologyChange} there is a corresponding {@link OWLOntologyChangeData} class which
 * captures the essential details that pertain to the change. The reason for this separation is that
 * it allows change information to be captured where the context of the change (the ontology) is
 * known via some other mechanism.</li>
 * </ol>
 * {@code OWLOntologyChangeRecord} objects are immutable.
 *
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group
 * @since 3.3
 * @param getOntologyID The {@link OWLOntologyID} which identifies the ontology that the change was
 *        applied to.
 * @param getData The {@link OWLOntologyChangeData} that describes the particular details of the
 *        change.
 */
public record OWLOntologyChangeRecord(OWLOntologyID getOntologyID, OWLOntologyChangeData getData)
    implements Serializable {

    /**
     * A convenience method that creates an {@code OWLOntologyChangeRecord} by deriving data from an
     * {@link OWLOntologyChange} object.
     *
     * @param change The {@link OWLOntologyChange} object.
     * @return instance of {@code OWLOntologyChangeRecord}
     */
    public static OWLOntologyChangeRecord createFromOWLOntologyChange(OWLOntologyChange change) {
        checkNotNull(change, "change must not be null");
        OWLOntologyID ontologyId = change.getOntology().getOntologyID();
        OWLOntologyChangeData data = change.getChangeData();
        return new OWLOntologyChangeRecord(ontologyId, data);
    }

    /**
     * Creates an {@link OWLOntologyChange} from the {@link OWLOntologyID} and
     * {@link OWLOntologyChangeData} associated with this {@code OWLOntologyChangeRecord} object.
     * The {@link OWLOntology} that is the target of the resulting {@link OWLOntologyChange} is
     * derived from an {@link OWLOntologyManager}. The manager <i>must</i> contain an ontology that
     * has an {@link OWLOntologyID} which is equal to the {@link OWLOntologyID} associated with this
     * {@code OWLOntologyChangeRecord} object.
     *
     * @param manager The manager which will be used to obtain a reference to an {@link OWLOntology}
     *        object having the same {@link OWLOntologyID} as the {@link OWLOntologyID} associated
     *        with this {@code OWLOntologyChangeRecord}.
     * @return The {@link OWLOntologyChange} object that is derived from this record's
     *         {@link OWLOntologyID} and {@link OWLOntologyChangeData}. The specific concrete
     *         subclass of the returned {@link OWLOntologyChange} will depend upon the specific
     *         concrete subclass of the {@link OWLOntologyChangeData} associated with this
     *         {@code OWLOntologyChangeRecord}.
     * @throws UnknownOWLOntologyException if the specified manager does not contain an ontology
     *         which has an {@link OWLOntologyID} equal to the {@link OWLOntologyID} associated with
     *         this {@code OWLOntologyChangeRecord}.
     */
    public OWLOntologyChange createOntologyChange(OWLOntologyManager manager) {
        OWLOntology ontology = manager.getOntology(getOntologyID);
        if (ontology == null) {
            throw new UnknownOWLOntologyException(getOntologyID);
        }
        return getData.createOntologyChange(ontology);
    }

    @Override
    public String toString() {
        return getName() + '(' + getOntologyID + ' ' + getData + ')';
    }

    /**
     * @return a name for the object class
     */
    public String getName() {
        return getClass().getSimpleName();
    }
}
