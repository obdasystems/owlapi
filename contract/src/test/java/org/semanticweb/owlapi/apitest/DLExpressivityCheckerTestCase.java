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
package org.semanticweb.owlapi.apitest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.semanticweb.owlapi.utility.Construct.CONCEPT_COMPLEX_NEGATION;
import static org.semanticweb.owlapi.utility.Construct.CONCEPT_INTERSECTION;
import static org.semanticweb.owlapi.utility.Construct.CONCEPT_UNION;
import static org.semanticweb.owlapi.utility.Construct.FULL_EXISTENTIAL;
import static org.semanticweb.owlapi.utility.Construct.N;
import static org.semanticweb.owlapi.utility.Construct.NOMINALS;
import static org.semanticweb.owlapi.utility.Construct.ROLE_COMPLEX;
import static org.semanticweb.owlapi.utility.Construct.ROLE_DOMAIN_RANGE;
import static org.semanticweb.owlapi.utility.Construct.ROLE_HIERARCHY;
import static org.semanticweb.owlapi.utility.Construct.ROLE_INVERSE;
import static org.semanticweb.owlapi.utility.Construct.ROLE_REFLEXIVITY_CHAINS;
import static org.semanticweb.owlapi.utility.Construct.ROLE_TRANSITIVE;
import static org.semanticweb.owlapi.utility.Construct.UNIVERSAL_RESTRICTION;
import static org.semanticweb.owlapi.utility.Languages.AL;
import static org.semanticweb.owlapi.utility.Languages.ALC;
import static org.semanticweb.owlapi.utility.Languages.ALCD;
import static org.semanticweb.owlapi.utility.Languages.ALCF;
import static org.semanticweb.owlapi.utility.Languages.ALCFD;
import static org.semanticweb.owlapi.utility.Languages.ALCH;
import static org.semanticweb.owlapi.utility.Languages.ALCHD;
import static org.semanticweb.owlapi.utility.Languages.ALCHF;
import static org.semanticweb.owlapi.utility.Languages.ALCHFD;
import static org.semanticweb.owlapi.utility.Languages.ALCHI;
import static org.semanticweb.owlapi.utility.Languages.ALCHID;
import static org.semanticweb.owlapi.utility.Languages.ALCHIF;
import static org.semanticweb.owlapi.utility.Languages.ALCHIFD;
import static org.semanticweb.owlapi.utility.Languages.ALCHIN;
import static org.semanticweb.owlapi.utility.Languages.ALCHIND;
import static org.semanticweb.owlapi.utility.Languages.ALCHIQ;
import static org.semanticweb.owlapi.utility.Languages.ALCHIQD;
import static org.semanticweb.owlapi.utility.Languages.ALCHN;
import static org.semanticweb.owlapi.utility.Languages.ALCHND;
import static org.semanticweb.owlapi.utility.Languages.ALCHO;
import static org.semanticweb.owlapi.utility.Languages.ALCHOD;
import static org.semanticweb.owlapi.utility.Languages.ALCHOF;
import static org.semanticweb.owlapi.utility.Languages.ALCHOFD;
import static org.semanticweb.owlapi.utility.Languages.ALCHOI;
import static org.semanticweb.owlapi.utility.Languages.ALCHOID;
import static org.semanticweb.owlapi.utility.Languages.ALCHOIF;
import static org.semanticweb.owlapi.utility.Languages.ALCHOIFD;
import static org.semanticweb.owlapi.utility.Languages.ALCHOIN;
import static org.semanticweb.owlapi.utility.Languages.ALCHOIND;
import static org.semanticweb.owlapi.utility.Languages.ALCHOIQ;
import static org.semanticweb.owlapi.utility.Languages.ALCHOIQD;
import static org.semanticweb.owlapi.utility.Languages.ALCHON;
import static org.semanticweb.owlapi.utility.Languages.ALCHOND;
import static org.semanticweb.owlapi.utility.Languages.ALCHOQ;
import static org.semanticweb.owlapi.utility.Languages.ALCHOQD;
import static org.semanticweb.owlapi.utility.Languages.ALCHQ;
import static org.semanticweb.owlapi.utility.Languages.ALCHQD;
import static org.semanticweb.owlapi.utility.Languages.ALCI;
import static org.semanticweb.owlapi.utility.Languages.ALCID;
import static org.semanticweb.owlapi.utility.Languages.ALCIF;
import static org.semanticweb.owlapi.utility.Languages.ALCIFD;
import static org.semanticweb.owlapi.utility.Languages.ALCIN;
import static org.semanticweb.owlapi.utility.Languages.ALCIND;
import static org.semanticweb.owlapi.utility.Languages.ALCIQ;
import static org.semanticweb.owlapi.utility.Languages.ALCIQD;
import static org.semanticweb.owlapi.utility.Languages.ALCN;
import static org.semanticweb.owlapi.utility.Languages.ALCND;
import static org.semanticweb.owlapi.utility.Languages.ALCO;
import static org.semanticweb.owlapi.utility.Languages.ALCOD;
import static org.semanticweb.owlapi.utility.Languages.ALCOF;
import static org.semanticweb.owlapi.utility.Languages.ALCOFD;
import static org.semanticweb.owlapi.utility.Languages.ALCOI;
import static org.semanticweb.owlapi.utility.Languages.ALCOID;
import static org.semanticweb.owlapi.utility.Languages.ALCOIF;
import static org.semanticweb.owlapi.utility.Languages.ALCOIFD;
import static org.semanticweb.owlapi.utility.Languages.ALCOIN;
import static org.semanticweb.owlapi.utility.Languages.ALCOIND;
import static org.semanticweb.owlapi.utility.Languages.ALCOIQ;
import static org.semanticweb.owlapi.utility.Languages.ALCOIQD;
import static org.semanticweb.owlapi.utility.Languages.ALCON;
import static org.semanticweb.owlapi.utility.Languages.ALCOND;
import static org.semanticweb.owlapi.utility.Languages.ALCOQ;
import static org.semanticweb.owlapi.utility.Languages.ALCOQD;
import static org.semanticweb.owlapi.utility.Languages.ALCQ;
import static org.semanticweb.owlapi.utility.Languages.ALCQD;
import static org.semanticweb.owlapi.utility.Languages.ALCR;
import static org.semanticweb.owlapi.utility.Languages.ALCRD;
import static org.semanticweb.owlapi.utility.Languages.ALCRF;
import static org.semanticweb.owlapi.utility.Languages.ALCRFD;
import static org.semanticweb.owlapi.utility.Languages.ALCRI;
import static org.semanticweb.owlapi.utility.Languages.ALCRID;
import static org.semanticweb.owlapi.utility.Languages.ALCRIF;
import static org.semanticweb.owlapi.utility.Languages.ALCRIFD;
import static org.semanticweb.owlapi.utility.Languages.ALCRIN;
import static org.semanticweb.owlapi.utility.Languages.ALCRIND;
import static org.semanticweb.owlapi.utility.Languages.ALCRIQ;
import static org.semanticweb.owlapi.utility.Languages.ALCRIQD;
import static org.semanticweb.owlapi.utility.Languages.ALCRN;
import static org.semanticweb.owlapi.utility.Languages.ALCRND;
import static org.semanticweb.owlapi.utility.Languages.ALCRO;
import static org.semanticweb.owlapi.utility.Languages.ALCROD;
import static org.semanticweb.owlapi.utility.Languages.ALCROF;
import static org.semanticweb.owlapi.utility.Languages.ALCROFD;
import static org.semanticweb.owlapi.utility.Languages.ALCROI;
import static org.semanticweb.owlapi.utility.Languages.ALCROID;
import static org.semanticweb.owlapi.utility.Languages.ALCROIF;
import static org.semanticweb.owlapi.utility.Languages.ALCROIFD;
import static org.semanticweb.owlapi.utility.Languages.ALCROIN;
import static org.semanticweb.owlapi.utility.Languages.ALCROIND;
import static org.semanticweb.owlapi.utility.Languages.ALCROIQ;
import static org.semanticweb.owlapi.utility.Languages.ALCROIQD;
import static org.semanticweb.owlapi.utility.Languages.ALCRON;
import static org.semanticweb.owlapi.utility.Languages.ALCROND;
import static org.semanticweb.owlapi.utility.Languages.ALCROQ;
import static org.semanticweb.owlapi.utility.Languages.ALCROQD;
import static org.semanticweb.owlapi.utility.Languages.ALCRQ;
import static org.semanticweb.owlapi.utility.Languages.ALCRQD;
import static org.semanticweb.owlapi.utility.Languages.ALCRr;
import static org.semanticweb.owlapi.utility.Languages.ALCRrD;
import static org.semanticweb.owlapi.utility.Languages.ALCRrF;
import static org.semanticweb.owlapi.utility.Languages.ALCRrFD;
import static org.semanticweb.owlapi.utility.Languages.ALCRrN;
import static org.semanticweb.owlapi.utility.Languages.ALCRrND;
import static org.semanticweb.owlapi.utility.Languages.ALCRrO;
import static org.semanticweb.owlapi.utility.Languages.ALCRrOD;
import static org.semanticweb.owlapi.utility.Languages.ALCRrOF;
import static org.semanticweb.owlapi.utility.Languages.ALCRrOFD;
import static org.semanticweb.owlapi.utility.Languages.ALCRrON;
import static org.semanticweb.owlapi.utility.Languages.ALCRrOND;
import static org.semanticweb.owlapi.utility.Languages.ALCRrOQ;
import static org.semanticweb.owlapi.utility.Languages.ALCRrOQD;
import static org.semanticweb.owlapi.utility.Languages.ALCRrQ;
import static org.semanticweb.owlapi.utility.Languages.ALCRrQD;
import static org.semanticweb.owlapi.utility.Languages.ALE;
import static org.semanticweb.owlapi.utility.Languages.EL;
import static org.semanticweb.owlapi.utility.Languages.ELPLUSPLUS;
import static org.semanticweb.owlapi.utility.Languages.FL;
import static org.semanticweb.owlapi.utility.Languages.FL0;
import static org.semanticweb.owlapi.utility.Languages.FLMINUS;
import static org.semanticweb.owlapi.utility.Languages.SD;
import static org.semanticweb.owlapi.utility.Languages.SF;
import static org.semanticweb.owlapi.utility.Languages.SFD;
import static org.semanticweb.owlapi.utility.Languages.SH;
import static org.semanticweb.owlapi.utility.Languages.SHD;
import static org.semanticweb.owlapi.utility.Languages.SHF;
import static org.semanticweb.owlapi.utility.Languages.SHFD;
import static org.semanticweb.owlapi.utility.Languages.SHI;
import static org.semanticweb.owlapi.utility.Languages.SHID;
import static org.semanticweb.owlapi.utility.Languages.SHIF;
import static org.semanticweb.owlapi.utility.Languages.SHIFD;
import static org.semanticweb.owlapi.utility.Languages.SHIN;
import static org.semanticweb.owlapi.utility.Languages.SHIND;
import static org.semanticweb.owlapi.utility.Languages.SHIQ;
import static org.semanticweb.owlapi.utility.Languages.SHIQD;
import static org.semanticweb.owlapi.utility.Languages.SHN;
import static org.semanticweb.owlapi.utility.Languages.SHND;
import static org.semanticweb.owlapi.utility.Languages.SHO;
import static org.semanticweb.owlapi.utility.Languages.SHOD;
import static org.semanticweb.owlapi.utility.Languages.SHOF;
import static org.semanticweb.owlapi.utility.Languages.SHOFD;
import static org.semanticweb.owlapi.utility.Languages.SHOI;
import static org.semanticweb.owlapi.utility.Languages.SHOID;
import static org.semanticweb.owlapi.utility.Languages.SHOIF;
import static org.semanticweb.owlapi.utility.Languages.SHOIFD;
import static org.semanticweb.owlapi.utility.Languages.SHOIN;
import static org.semanticweb.owlapi.utility.Languages.SHOIND;
import static org.semanticweb.owlapi.utility.Languages.SHOIQ;
import static org.semanticweb.owlapi.utility.Languages.SHOIQD;
import static org.semanticweb.owlapi.utility.Languages.SHON;
import static org.semanticweb.owlapi.utility.Languages.SHOND;
import static org.semanticweb.owlapi.utility.Languages.SHOQ;
import static org.semanticweb.owlapi.utility.Languages.SHOQD;
import static org.semanticweb.owlapi.utility.Languages.SHQ;
import static org.semanticweb.owlapi.utility.Languages.SHQD;
import static org.semanticweb.owlapi.utility.Languages.SI;
import static org.semanticweb.owlapi.utility.Languages.SID;
import static org.semanticweb.owlapi.utility.Languages.SIF;
import static org.semanticweb.owlapi.utility.Languages.SIFD;
import static org.semanticweb.owlapi.utility.Languages.SIN;
import static org.semanticweb.owlapi.utility.Languages.SIND;
import static org.semanticweb.owlapi.utility.Languages.SIQ;
import static org.semanticweb.owlapi.utility.Languages.SIQD;
import static org.semanticweb.owlapi.utility.Languages.SN;
import static org.semanticweb.owlapi.utility.Languages.SND;
import static org.semanticweb.owlapi.utility.Languages.SO;
import static org.semanticweb.owlapi.utility.Languages.SOD;
import static org.semanticweb.owlapi.utility.Languages.SOF;
import static org.semanticweb.owlapi.utility.Languages.SOFD;
import static org.semanticweb.owlapi.utility.Languages.SOI;
import static org.semanticweb.owlapi.utility.Languages.SOID;
import static org.semanticweb.owlapi.utility.Languages.SOIF;
import static org.semanticweb.owlapi.utility.Languages.SOIFD;
import static org.semanticweb.owlapi.utility.Languages.SOIN;
import static org.semanticweb.owlapi.utility.Languages.SOIND;
import static org.semanticweb.owlapi.utility.Languages.SOIQ;
import static org.semanticweb.owlapi.utility.Languages.SOIQD;
import static org.semanticweb.owlapi.utility.Languages.SON;
import static org.semanticweb.owlapi.utility.Languages.SOND;
import static org.semanticweb.owlapi.utility.Languages.SOQ;
import static org.semanticweb.owlapi.utility.Languages.SOQD;
import static org.semanticweb.owlapi.utility.Languages.SQ;
import static org.semanticweb.owlapi.utility.Languages.SQD;
import static org.semanticweb.owlapi.utility.Languages.SR;
import static org.semanticweb.owlapi.utility.Languages.SRD;
import static org.semanticweb.owlapi.utility.Languages.SRF;
import static org.semanticweb.owlapi.utility.Languages.SRFD;
import static org.semanticweb.owlapi.utility.Languages.SRI;
import static org.semanticweb.owlapi.utility.Languages.SRID;
import static org.semanticweb.owlapi.utility.Languages.SRIF;
import static org.semanticweb.owlapi.utility.Languages.SRIFD;
import static org.semanticweb.owlapi.utility.Languages.SRIN;
import static org.semanticweb.owlapi.utility.Languages.SRIND;
import static org.semanticweb.owlapi.utility.Languages.SRIQ;
import static org.semanticweb.owlapi.utility.Languages.SRIQD;
import static org.semanticweb.owlapi.utility.Languages.SRN;
import static org.semanticweb.owlapi.utility.Languages.SRND;
import static org.semanticweb.owlapi.utility.Languages.SRO;
import static org.semanticweb.owlapi.utility.Languages.SROD;
import static org.semanticweb.owlapi.utility.Languages.SROF;
import static org.semanticweb.owlapi.utility.Languages.SROFD;
import static org.semanticweb.owlapi.utility.Languages.SROI;
import static org.semanticweb.owlapi.utility.Languages.SROID;
import static org.semanticweb.owlapi.utility.Languages.SROIF;
import static org.semanticweb.owlapi.utility.Languages.SROIFD;
import static org.semanticweb.owlapi.utility.Languages.SROIN;
import static org.semanticweb.owlapi.utility.Languages.SROIND;
import static org.semanticweb.owlapi.utility.Languages.SROIQ;
import static org.semanticweb.owlapi.utility.Languages.SROIQD;
import static org.semanticweb.owlapi.utility.Languages.SRON;
import static org.semanticweb.owlapi.utility.Languages.SROND;
import static org.semanticweb.owlapi.utility.Languages.SROQ;
import static org.semanticweb.owlapi.utility.Languages.SROQD;
import static org.semanticweb.owlapi.utility.Languages.SRQ;
import static org.semanticweb.owlapi.utility.Languages.SRQD;
import static org.semanticweb.owlapi.utility.Languages.SRr;
import static org.semanticweb.owlapi.utility.Languages.SRrD;
import static org.semanticweb.owlapi.utility.Languages.SRrF;
import static org.semanticweb.owlapi.utility.Languages.SRrFD;
import static org.semanticweb.owlapi.utility.Languages.SRrN;
import static org.semanticweb.owlapi.utility.Languages.SRrND;
import static org.semanticweb.owlapi.utility.Languages.SRrO;
import static org.semanticweb.owlapi.utility.Languages.SRrOD;
import static org.semanticweb.owlapi.utility.Languages.SRrOF;
import static org.semanticweb.owlapi.utility.Languages.SRrOFD;
import static org.semanticweb.owlapi.utility.Languages.SRrON;
import static org.semanticweb.owlapi.utility.Languages.SRrOND;
import static org.semanticweb.owlapi.utility.Languages.SRrOQ;
import static org.semanticweb.owlapi.utility.Languages.SRrOQD;
import static org.semanticweb.owlapi.utility.Languages.SRrQ;
import static org.semanticweb.owlapi.utility.Languages.SRrQD;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.semanticweb.owlapi.apitest.baseclasses.Builder;
import org.semanticweb.owlapi.apitest.baseclasses.TestBase;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.utility.Construct;
import org.semanticweb.owlapi.utility.DLExpressivityChecker;
import org.semanticweb.owlapi.utility.Languages;

class DLExpressivityCheckerTestCase extends TestBase {

    static Collection<Object[]> getData() {
        return l(
        //@formatter:off
            new Object[] {"0 AL",        "UNIVRESTR" , l(UNIVERSAL_RESTRICTION),                              l(FL0),                 belowUniversal(),      l(FL0),                    l(Builder.assAll)},
            new Object[] {"1  AL",       ""          , l(),                                                   l(FL0, EL, ELPLUSPLUS), l(Languages.values()), l(FL0, EL, ELPLUSPLUS),    l(Builder.dDef)},
            new Object[] {"1  AL",       ""          , l(),                                                   l(FL0, EL, ELPLUSPLUS), l(Languages.values()), l(FL0, EL, ELPLUSPLUS),    l(Builder.decC)},
            new Object[] {"1  AL",       ""          , l(),                                                   l(FL0, EL, ELPLUSPLUS), l(Languages.values()), l(FL0, EL, ELPLUSPLUS),    l(Builder.decOp)},
            new Object[] {"1  AL",       ""          , l(),                                                   l(FL0, EL, ELPLUSPLUS), l(Languages.values()), l(FL0, EL, ELPLUSPLUS),    l(Builder.decDp)},
            new Object[] {"1  AL",       ""          , l(),                                                   l(FL0, EL, ELPLUSPLUS), l(Languages.values()), l(FL0, EL, ELPLUSPLUS),    l(Builder.decDt)},
            new Object[] {"1  AL",       ""          , l(),                                                   l(FL0, EL, ELPLUSPLUS), l(Languages.values()), l(FL0, EL, ELPLUSPLUS),    l(Builder.decAp)},
            new Object[] {"1  AL",       ""          , l(),                                                   l(FL0, EL, ELPLUSPLUS), l(Languages.values()), l(FL0, EL, ELPLUSPLUS),    l(Builder.decI)},
            new Object[] {"1  AL",       ""          , l(),                                                   l(FL0, EL, ELPLUSPLUS), l(Languages.values()), l(FL0, EL, ELPLUSPLUS),    l(Builder.ec)},
            new Object[] {"1  AL",       ""          , l(),                                                   l(FL0, EL, ELPLUSPLUS), l(Languages.values()), l(FL0, EL, ELPLUSPLUS),    l(Builder.nop)},
            new Object[] {"1  AL",       ""          , l(),                                                   l(FL0, EL, ELPLUSPLUS), l(Languages.values()), l(FL0, EL, ELPLUSPLUS),    l(Builder.opa)},
            new Object[] {"1  AL",       ""          , l(),                                                   l(FL0, EL, ELPLUSPLUS), l(Languages.values()), l(FL0, EL, ELPLUSPLUS),    l(Builder.subAnn)},
            new Object[] {"1  AL",       ""          , l(),                                                   l(FL0, EL, ELPLUSPLUS), l(Languages.values()), l(FL0, EL, ELPLUSPLUS),    l(Builder.subClass)},
            new Object[] {"1  AL",       ""          , l(),                                                   l(FL0, EL, ELPLUSPLUS), l(Languages.values()), l(FL0, EL, ELPLUSPLUS),    l(Builder.rule)},
            new Object[] {"1  AL",       ""          , l(),                                                   l(FL0, EL, ELPLUSPLUS), l(Languages.values()), l(FL0, EL, ELPLUSPLUS),    l(Builder.ann)},
            new Object[] {"1  AL",       ""          , l(),                                                   l(FL0, EL, ELPLUSPLUS), l(Languages.values()), l(FL0, EL, ELPLUSPLUS),    l(Builder.annDom)},
            new Object[] {"1  AL",       ""          , l(),                                                   l(FL0, EL, ELPLUSPLUS), l(Languages.values()), l(FL0, EL, ELPLUSPLUS),    l(Builder.annRange)},
            new Object[] {"1  AL",       ""          , l(),                                                   l(FL0, EL, ELPLUSPLUS), l(Languages.values()), l(FL0, EL, ELPLUSPLUS),    l(Builder.ass)},
            new Object[] {"2  ALCO",     "CUO"       , l(CONCEPT_COMPLEX_NEGATION, CONCEPT_UNION, NOMINALS),  l(ALCO),                belowCUO(),            l(ALCO),                   l(Builder.assDi)},
            new Object[] {"3  ALC",      "C"         , l(CONCEPT_COMPLEX_NEGATION),                           l(ALC),                 belowC(),              l(ALC),                    l(Builder.dc)},
            new Object[] {"3  ALC",      "C"         , l(CONCEPT_COMPLEX_NEGATION),                           l(ALC),                 belowC(),              l(ALC),                    l(Builder.assNot)},
            new Object[] {"3  ALC",      "C"         , l(CONCEPT_COMPLEX_NEGATION),                           l(ALC),                 belowC(),              l(ALC),                    l(Builder.assNotAnon)},
            new Object[] {"4  ALR",      "R"         , l(ROLE_COMPLEX),                                       expressR(),             belowR(),              expressR(),                l(Builder.dOp)},
            new Object[] {"4  ALR",      "R"         , l(ROLE_COMPLEX),                                       expressR(),             belowR(),              expressR(),                l(Builder.irr)},
            new Object[] {"4  ALR",      "R"         , l(ROLE_COMPLEX),                                       expressR(),             belowR(),              expressR(),                l(Builder.asymm)},
            new Object[] {"4  ALR",      "R"         , l(ROLE_COMPLEX),                                       expressR(),             belowR(),              expressR(),                l(Builder.assHasSelf)},
            new Object[] {"5  AL(D)",    "RRESTR(D)" , l(ROLE_DOMAIN_RANGE, Construct.D),                     l(ALCD),                belowALD(),            l(ALCD),                   l(Builder.dRange)},
            new Object[] {"5  AL(D)",    "RRESTR(D)" , l(ROLE_DOMAIN_RANGE, Construct.D),                     l(ALCD),                belowALD(),            l(ALCD),                   l(Builder.dRangeAnd)},
            new Object[] {"5  AL(D)",    "RRESTR(D)" , l(ROLE_DOMAIN_RANGE, Construct.D),                     l(ALCD),                belowALD(),            l(ALCD),                   l(Builder.dRangeOr)},
            new Object[] {"5  AL(D)",    "RRESTR(D)" , l(ROLE_DOMAIN_RANGE, Construct.D),                     l(ALCD),                belowALD(),            l(ALCD),                   l(Builder.dOneOf)},
            new Object[] {"5  AL(D)",    "RRESTR(D)" , l(ROLE_DOMAIN_RANGE, Construct.D),                     l(ALCD),                belowALD(),            l(ALCD),                   l(Builder.dNot)},
            new Object[] {"5  AL(D)",    "RRESTR(D)" , l(ROLE_DOMAIN_RANGE, Construct.D),                     l(ALCD),                belowALD(),            l(ALCD),                   l(Builder.dRangeRestrict)},
            new Object[] {"5  AL(D)",    "RRESTR(D)" , l(ROLE_DOMAIN_RANGE, Construct.D),                     l(ALCD),                belowALD(),            l(ALCD),                   l(Builder.dDom)},
            new Object[] {"6  ALC",      "CU"        , l(CONCEPT_COMPLEX_NEGATION, CONCEPT_UNION),            l(ALC),                 belowCU(),             l(ALC),                    l(Builder.du)},
            new Object[] {"7  ALH(D)",   "H(D)"      , l(ROLE_HIERARCHY, Construct.D),                        l(ALCHD),               belowHD(),             l(ALCHD),                  l(Builder.eDp)},
            new Object[] {"7  ALH(D)",   "H(D)"      , l(ROLE_HIERARCHY, Construct.D),                        l(ALCHD),               belowHD(),             l(ALCHD),                  l(Builder.subData)},
            new Object[] {"8  ALH",      "H"         , l(ROLE_HIERARCHY),                                     l(ALCH),                belowH(),              l(ALCH),                   l(Builder.eOp)},
            new Object[] {"8  ALH",      "H"         , l(ROLE_HIERARCHY),                                     l(ALCH),                belowH(),              l(ALCH),                   l(Builder.subObject)},
            new Object[] {"9 ALF(D)",    "F(D)"      , l(Construct.F, Construct.D),                           l(ALCFD),               belowFD(),             l(ALCFD),                  l(Builder.fdp)},
            new Object[] {"10 ALF",      "F"         , l(Construct.F),                                        l(ALCF),                belowF(),              l(ALCF),                   l(Builder.fop)},
            new Object[] {"11 ALIF",     "IF"        , l(ROLE_INVERSE, Construct.F),                          l(ALCIF),               belowIF(),             l(ALCIF),                  l(Builder.ifp)},
            new Object[] {"12 ALI",      "I"         , l(ROLE_INVERSE),                                       l(ALCI),                belowI(),              l(ALCI),                   l(Builder.iop)},
            new Object[] {"12 ALI",      "I"         , l(ROLE_INVERSE),                                       l(ALCI),                belowI(),              l(ALCI),                   l(Builder.opaInv)},
            new Object[] {"12 ALI",      "I"         , l(ROLE_INVERSE),                                       l(ALCI),                belowI(),              l(ALCI),                   l(Builder.opaInvj)},
            new Object[] {"12 ALI",      "I"         , l(ROLE_INVERSE),                                       l(ALCI),                belowI(),              l(ALCI),                   l(Builder.symm)},
            new Object[] {"13 AL(D)",    "(D)"       , l(Construct.D),                                        l(ALCD, ELPLUSPLUS),    belowD(),              l(ALCD, ELPLUSPLUS),       l(Builder.dDp)},
            new Object[] {"13 AL(D)",    "(D)"       , l(Construct.D),                                        l(ALCD, ELPLUSPLUS),    belowD(),              l(ALCD, ELPLUSPLUS),       l(Builder.ndp)},
            new Object[] {"28 AL(D)",  "UNIVRESTR(D)", l(UNIVERSAL_RESTRICTION, Construct.D),                 l(ALCD),                belowDU(),             l(ALCD),                   l(Builder.assDAll)},
            new Object[] {"13 AL(D)",    "(D)"       , l(Construct.D),                                        l(ALCD, ELPLUSPLUS),    belowD(),              l(ALCD, ELPLUSPLUS),       l(Builder.hasKey)},
            new Object[] {"13 AL(D)",    "(D)"       , l(Construct.D),                                        l(ALCD, ELPLUSPLUS),    belowD(),              l(ALCD, ELPLUSPLUS),       l(Builder.bigRule)},
            new Object[] {"13 AL(D)",    "(D)"       , l(Construct.D),                                        l(ALCD, ELPLUSPLUS),    belowD(),              l(ALCD, ELPLUSPLUS),       l(Builder.assDHas)},
            new Object[] {"13 AL(D)",    "(D)"       , l(Construct.D),                                        l(ALCD, ELPLUSPLUS),    belowD(),              l(ALCD, ELPLUSPLUS),       l(Builder.assD)},
            new Object[] {"13 AL(D)",    "(D)"       , l(Construct.D),                                        l(ALCD, ELPLUSPLUS),    belowD(),              l(ALCD, ELPLUSPLUS),       l(Builder.assDPlain)},
            new Object[] {"14 ALO",      "O"         , l(NOMINALS),                                           l(ALCO, ELPLUSPLUS),    belowO(),              l(ALCO,ELPLUSPLUS),        l(Builder.same)},
            new Object[] {"15 AL+",      "+"         , l(ROLE_TRANSITIVE),                                    l(Languages.S, ELPLUSPLUS), belowTRAN(),       l(Languages.S, ELPLUSPLUS),l(Builder.trans)},
            new Object[] {"16 AL",       "CINT"      , l(CONCEPT_INTERSECTION),                               l(FL0, EL, ELPLUSPLUS), l(Languages.values()), l(FL0, EL, ELPLUSPLUS),    l(Builder.assAnd)},
            new Object[] {"17 ALU",      "U"         , l(CONCEPT_UNION),                                      l(ALC),                 belowU(),              l(ALC),                    l(Builder.assOr)},
            new Object[] {"18 AL",       "RRESTR"    , l(ROLE_DOMAIN_RANGE),                                  l(FL, EL),              belowRRESTR(),         l(FL, EL),                 l(Builder.oDom)},
            new Object[] {"18 AL",       "RRESTR"    , l(ROLE_DOMAIN_RANGE),                                  l(FL, EL),              belowRRESTR(),         l(FL, EL),                 l(Builder.oRange)},
            new Object[] {"19 ALE",      "E"         , l(FULL_EXISTENTIAL),                                   l(EL,ALE, ELPLUSPLUS),  belowE(),              l(EL,ALE, ELPLUSPLUS),     l(Builder.assSome)},
            new Object[] {"20 ALEO",     "EO"        , l(FULL_EXISTENTIAL, NOMINALS),                         l(ALCO,ELPLUSPLUS),     belowEO(),             l(ALCO, ELPLUSPLUS),       l(Builder.assHas)},
            new Object[] {"21 ALQ",      "Q"         , l(Construct.Q),                                        l(ALCQ),                belowQ(),              l(ALCQ),                   l(Builder.assMin)},
            new Object[] {"21 ALQ",      "Q"         , l(Construct.Q),                                        l(ALCQ),                belowQ(),              l(ALCQ),                   l(Builder.assMax)},
            new Object[] {"21 ALQ",      "Q"         , l(Construct.Q),                                        l(ALCQ),                belowQ(),              l(ALCQ),                   l(Builder.assEq)},
            new Object[] {"22 ALUO",     "UO"        , l(CONCEPT_UNION, NOMINALS),                            l(ALCO),                belowUO(),             l(ALCO),                   l(Builder.assOneOf)},
            new Object[] {"23 ALE(D)",   "E(D)"      , l(FULL_EXISTENTIAL, Construct.D),                      l(ALCD,ELPLUSPLUS),     belowED(),             l(ALCD,ELPLUSPLUS),        l(Builder.assDSome)},
            new Object[] {"24 ALQ(D)",   "Q(D)"      , l(Construct.Q, Construct.D),                           l(ALCQD),               belowQD(),             l(ALCQD),                  l(Builder.assDMin)},
            new Object[] {"24 ALQ(D)",   "Q(D)"      , l(Construct.Q, Construct.D),                           l(ALCQD),               belowQD(),             l(ALCQD),                  l(Builder.assDMax)},
            new Object[] {"24 ALQ(D)",   "Q(D)"      , l(Construct.Q, Construct.D),                           l(ALCQD),               belowQD(),             l(ALCQD),                  l(Builder.assDEq)},
            new Object[] {"25 ALR",      "Rr"        , l(ROLE_REFLEXIVITY_CHAINS),                            expressRr(),            belowRr(),             expressRr(),               l(Builder.chain)},
            new Object[] {"25 ALR",      "Rr"        , l(ROLE_REFLEXIVITY_CHAINS),                            expressRr(),            belowRr(),             expressRr(),               l(Builder.ref)},
            new Object[] {"26 ALR",      "RIQ"       , l(ROLE_COMPLEX, ROLE_INVERSE, Construct.Q),            l(ALCRIQ),              belowRIQ(),            l(ALCRIQ),                 l(Builder.ref, Builder.trans, Builder.symm, Builder.subObject, Builder.fop, Builder.assMinTop, Builder.assMin) },
            new Object[] {"27 ALN",      "N"         , l(N),                                                  l(ALCN),                belowN(),              l(ALCN),                   l(Builder.assMinTop)}
            );
        //@formatter:on
    }

  //@formatter:off
    protected static List<Languages> belowN()         { return l(ALCRND, SHND, SHIND, ALCRN, ALCROIN, ALCHND, SRND, SROIN, ALCOND, SHIN, ALCHIN, ALCRON, SHOND, ALCON, ALCND, SOIN, SRON, ALCRrOND, ALCIN, SRN, SOIND, ALCRIND, SND, SHN, SRIND, SRrOND, ALCOIN, SIN, ALCHIND, SN, ALCIND, ALCRrND, ALCHOIN, ALCHN, SOND, ALCHOIND, SHOIN, SRrN, SRrON, SON, SHOIND, ALCOIND, ALCROND, SROIND, ALCROIND, SRrND, ALCHOND, ALCRrON, ALCN, SHON, ALCRIN, ALCRrN, SRIN, SROND, SIND, ALCHON); }
    protected static List<Languages> belowRIQ()       { return l(ALCRIQD, SROIQ, SROIQD, SRIQD, ALCROIQD, SRIQ, ALCROIQ, ALCRIQ); }
    protected static List<Languages> belowRRESTR()    { return l(FL, SROIQ, SROIQD, ALCHOID, ALCHIQ, ALCROF, SHQ, ALCIFD, SROD, ALCN, ALCRrF, SRD, ALCOID, ALCHOI, ALCHOIF, SHIQD, ALCHFD, ALCHN, SRIQD, ALCROID, ALCHOF, SRQD, SHIFD, ALCROQD, SRrN, SROID, ALCROIQ, ALCRQ, SI, SHOD, SHOIFD, ALCHOQ, ALCRrQ, ALCRrOD, SRIQ, ALCHID, SHQD, ALCHOIQD, ALCIN, ALCIQ, ALCOI, ALCOIQD, ALCRrD, ALCRD, SRrOQ, ALCROND, SIQD, SHIQ, ALCRr, ALCOQD, SRND, ALCRrOND, ALCRrOQ, ALCQ, SHN, SRrOF, SHO, SHD, ALCOIQ, SOIQ, SROND, ALCO, SIFD, SHOIND, ALCHO, SIND, SOI, SRQ, ALCHQD, SROIND, ALCIQD, SHOF, ALCROFD, ALCH, ALCOQ, ALCRON, ALCRrO, SHOIN, SRrOQD, EL, SN, ALCI, ALCRIQ, SOQ, SHF, ALCRO, SRN, SOQD, ALCHIFD, ALCOIN, SHON, SROF, SRr, SRID, ALCOFD, ALCOIFD, ALCHIND, SHOQD, SROQD, SQD, SHOFD, ALCHOIFD, ALCRFD, SND, ALCRrFD, SHFD, SFD, ALCHQ, SHIND, ALCRrOF, SHIN, SHOIQD, ALCRN, SHOQ, Languages.S, ALCOND, SOID, SRrOND, ALCOF, ALCRID, SRrF, ALCOD, ALCHON, SIF, ALCHD, SRrOFD, SHND, ALCRF, ALCRIQD, SRrD, SHOND, SRrON, SRIND, SROFD, SRON, SHID, ALCIND, SRrO, SRrND, SRO, ALCHOIN, SRI, SOIFD, ALCHOD, ALCIF, SROIFD, ALCHF, SID, ALCRrON, ALCD, SO, SROIF, SIN, SRF, ALCROIFD, ALCROIF, ALCHOND, SF, SOIND, ALCND, SRIN, ALCROI, ALCRND, SHI, ALCROIQD, ALC, ALCFD, SROIN, ALCRrQD, SHOIF, ALCID, ALCHOQD, ALCHIQD, ALCRIF, SOIN, ALCROIND, ALCRIND, ALCROQ, SH, SRIF, SD, SRFD, SRrQ, ALCR, ALCOIND, ALCHIF, ALCRrND, ALCF, ALCROIN, ALCHIN, ALCHOFD, ALCROD, SRrOD, SRrQD, SR, SOIF, ALCHOIQ, ALCON, ALCHND, ALCQD, SIQ, ALCRI, SRrFD, SQ, SOF, ALCHOIND, ALCHI, SROQ, SON, SOND, SOFD, SRIFD, ALCRIFD, SHOIQ, SOIQD, ALCRQD, SOD, SHOI, SROI, ALCRrOQD, SHIF, ALCRrN, ALCOIF, ALCRIN, SHOID, ALCRrOFD); }
    protected static List<Languages> belowALD()       { return l(ALCHD, SRD, ALCQD, SID, ALCFD, ALCHOIND, SROFD, SQD, SHND, ALCHQD, ALCHOD, SIND, SHIFD, SRID, SRND, ALCRND, ALCROIQD, SRrOFD, ALCIND, ALCRrFD, SRrD, SHQD, ALCOID, SROIQD, ALCRFD, SOIFD, SRIQD, ALCOND, SRrFD, SOID, SROIFD, ALCOIQD, ALCRIFD, SIQD, SRIND, SROIND, SOQD, SHOND, ALCRrOQD, ALCND, ALCRD, ALCHIND, ALCHOND, ALCROIFD, ALCROQD, ALCROIND, ALCROND, SRrQD, ALCOIND, ALCHOID, SHOQD, SRQD, ALCOQD, SROID, SOND, ALCHFD, ALCHOQD, ALCRrOD, SROQD, ALCROID, ALCHID, ALCRrOFD, SHOFD, SRIFD, ALCIQD, SOIND, SHD, ALCIFD, SHOIQD, SHOID, ALCRQD, SND, SRrND, SHIQD, SROD, SOD, SRFD, ALCHND, ALCRrQD, ALCHOIQD, ALCRIQD, SRrOD, SOIQD, ALCROD, SFD, ALCID, ALCHOFD, ALCHOIFD, ALCRIND, ALCOIFD, ALCRID, SHOIND, SHIND, ALCHIFD, SHFD, ALCOFD, ALCOD, ALCRrD, SHOD, ALCROFD, SIFD, ALCRrND, SRrOND, ALCRrOND, SD, SRrOQD, SROND, SHOIFD, SOFD, ALCHIQD, SHID, ALCD); }
    protected static List<Languages> belowFD()        { return l(SHFD, ALCROIFD, SRIFD, ALCHOFD, SHOIFD, ALCHFD, SOFD, ALCHIFD, SFD, ALCRIFD, SROIFD, ALCIFD, ALCOIFD, SOIFD, ALCHOIFD, ALCROFD, SHIFD, SROFD, ALCFD, SHOFD, SRFD, ALCRFD, ALCOFD, SIFD, SRrFD, SRrOFD, ALCRrFD, ALCRrOFD); }
    protected static List<Languages> belowE()         { return l(EL, ALE, ALC, ALCD, ALCQ, ALCN, ALCF, ALCI, ALCO, ALCOI, ALCOF, ALCIF, ALCIN, ALCON, ALCOQ, ALCIQ, ALCOIQ, ALCOIN, ALCOIF, Languages.S, SI, SO, SF, SN, SQ, SOF, SIF, SON, SIN, SOQ, SIQ, SOI, SOIF, SOIN, SOIQ, SH, SHF, SHN, SHO, SHI, SHOF, SHIF, SHON, SHQ, SHOQ, SHIQ, SHOI, SHOIF, SHOIN, SHOIQ, SHIN, SHIND, SROIQ, SROIQD, ELPLUSPLUS, SOIFD, ALCHOND, ALCHOIF, SQD, ALCIFD, ALCHIN, SHOFD, SRIF, SND, SHOQD, SHID, ALCOID, ALCHOIFD, SHND, ALCROIQD, SD, ALCROQ, ALCH, ALCOD, SROIN, ALCHIND, SROIND, SRON, ALCHI, ALCID, ALCHIF, SRO, SROID, ALCRIF, ALCHOIQD, ALCRIN, ALCROQD, ALCHF, ALCROF, ALCHIQD, SRFD, ALCHN, SIQD, ALCRID, SHIQD, ALCOIND, SOQD, ALCRON, SHD, SR, ALCIND, ALCHID, ALCHOD, ALCOIFD, SRQ, SOIND, ALCRIFD, ALCHFD, SOD, ALCOQD, SRIND, ALCOFD, SRIFD, ALCRI, SHOND, SRID, ALCROIF, SRIQD, ALCRF, SOFD, SHFD, ALCROIN, ALCND, ALCHIQ, SHOIND, SID, SRIN, ALCHOID, ALCHOIN, ALCHOIND, ALCRQ, ALCHOI, ALCR, ALCROIND, SROQD, ALCHOF, ALCROND, ALCHQ, ALCQD, SROD, ALCROID, ALCOIQD, SROQ, ALCOND, ALCHND, ALCROD, SROFD, ALCHOQD, SHOIFD, ALCRIQ, SRF, SIND, ALCRIND, SHIFD, ALCRND, SRD, SROI, SROND, ALCHO, SHOD, ALCHD, ALCRFD, ALCHOIQ, SOND, SHQD, ALCRD, SRND, ALCRIQD, SOIQD, SFD, SROF, SROIF, SOID, SRN, ALCHIFD, SROIFD, SIFD, SRQD, SHOID, SRIQ, SHOIQD, ALCHON, ALCROFD, ALCHOQ, ALCRQD, ALCIQD, ALCROIQ, ALCHOFD, ALCROIFD, ALCHQD, ALCROI, SRI, ALCFD, ALCRN, ALCRO, ALCRrF, SRrN, ALCRrQ, ALCRrOD, ALCRrD, SRrOQ, ALCRr, ALCRrOND, ALCRrOQ, SRrOF, ALCRrO, SRrOQD, SRr, ALCRrFD, ALCRrOF, SRrOND, SRrF, SRrOFD, SRrD, SRrON, SRrO, SRrND, ALCRrON, ALCRrQD, SRrQ, ALCRrND, SRrOD, SRrQD, SRrFD, ALCRrOQD, ALCRrN, ALCRrOFD); }
    protected static List<Languages> belowU()         { return l(ALC, ALCD, ALCQ, ALCN, ALCF, ALCI, ALCO, ALCOI, ALCOF, ALCIF, ALCIN, ALCON, ALCOQ, ALCIQ, ALCOIQ, ALCOIN, ALCOIF, Languages.S, SI, SO, SF, SN, SQ, SOF, SIF, SON, SIN, SOQ, SIQ, SOI, SOIF, SOIN, SOIQ, SH, SHF, SHN, SHO, SHI, SHOF, SHIF, SHON, SHQ, SHOQ, SHIQ, SHOI, SHOIF, SHOIN, SHOIQ, SHIN, SHIND, SROIQ, SROIQD, SHOQD, SIQD, ALCRIFD, ALCOD, ALCHID, SOIFD, SRIFD, SRF, ALCHND, ALCOIND, ALCHFD, SROIND, ALCHOIN, ALCROID, SRFD, ALCRIND, SND, ALCHOIFD, ALCROIN, SOND, ALCHOI, SHOIFD, SROIFD, ALCOIQD, ALCROQ, SHIFD, ALCHIQ, SOFD, SOID, ALCROQD, SRIF, SFD, ALCIND, ALCHOFD, SRID, ALCROF, ALCRIQ, ALCHOIQD, SOIQD, SROID, ALCHOID, ALCHIND, ALCRQD, SHIQD, SHOND, ALCROIFD, ALCRQ, ALCHO, SRD, SRIND, ALCROIQD, SOQD, SHOFD, ALCRIQD, ALCOID, ALCHI, ALCHOD, ALCHON, ALCIQD, ALCROIF, ALCHIQD, ALCHOQ, SROF, SROD, SROIF, ALCRO, SHD, SROIN, ALCRF, ALCHOIQ, ALCROI, ALCRN, ALCRON, SHID, ALCHIFD, SIFD, SRI, ALCND, ALCID, ALCHQ, SIND, SOIND, ALCOQD, SD, SHND, SHOID, ALCRIN, ALCOIFD, ALCROD, ALCHIN, ALCFD, ALCROIQ, SROND, SHQD, SOD, SRON, SHOIND, ALCHIF, SROQD, SHOD, ALCHF, ALCRIF, SQD, ALCRI, ALCRD, SRN, ALCHD, ALCHOF, ALCROND, SRND, ALCHOIND, SRQD, SID, ALCH, SRIQ, SRO, ALCHOND, ALCOND, SROFD, ALCROFD, SRIQD, ALCRFD, SHOIQD, SHFD, SROQ, ALCRND, ALCRID, ALCROIND, SRQ, ALCIFD, ALCR, SROI, SR, ALCHN, ALCHQD, SRIN, ALCQD, ALCHOIF, ALCOFD, ALCHOQD, SRrD, ALCRrOFD, ALCRr, SRrOND, ALCRrQ, SRrON, SRrOF, ALCRrN, SRrND, ALCRrO, ALCRrD, SRrQD, SRrN, SRrOD, SRrOQD, SRrF, ALCRrOND, SRrOFD, ALCRrFD, ALCRrOQ, ALCRrQD, ALCRrOQD, SRrQ, ALCRrF, SRrOQ, SRrO, ALCRrOD, ALCRrON, SRrFD, ALCRrOF, SRr, ALCRrND); }
    protected static List<Languages> belowTRAN()      { return l(Languages.S, SI, SO, SF, SN, SQ, SOF, SIF, SON, SIN, SOQ, SIQ, SOI, SOIF, SOIN, SOIQ, SH, SHF, SHN, SHO, SHI, SHOF, SHIF, SHON, SHQ, SHOQ, SHIQ, SHOI, SHOIF, SHOIN, SHOIQ, SHIN, SHIND, SROIQ, SROIQD, SHOQD, SHD, SROIN, SIQD, SOIFD, SHID, SRIFD, SRF, SIFD, SRI, SROIND, SRFD, SIND, SOIND, SND, SD, SHND, SHOID, SOND, SHOIFD, SROIFD, SROND, SHQD, SOD, SRON, SHOIND, SHIFD, SOFD, SROQD, SHOD, SQD, SRN, SOID, SRIF, SFD, SRND, SRQD, SID, SRID, SRIQ, SRO, SROFD, SRIQD, SOIQD, SHOIQD, SHFD, SROID, SHIQD, SHOND, SROQ, SRD, SRIND, SOQD, SHOFD, SRQ, SROI, SR, SROF, SRIN, SROD, SROIF, SRrO, SRr, SRrOD, SRrD, SRrN, SRrQD, SRrOF, SRrQ, SRrON, SRrOND, SRrOQD, SRrOFD, ELPLUSPLUS, SRrOQ, SRrFD, SRrND, SRrF); }
    protected static List<Languages> belowED()        { return l(ALCD, SHIND, SROIQD, ALCROIND, SROQD, SOIFD, ALCHOND, ALCROND, SQD, ALCQD, ALCIFD, SHOFD, SND, SHOQD, SHID, ALCOID, SROD, ALCROID, ALCOIQD, ALCHOIFD, SHND, ALCROIQD, SD, ALCOND, ALCHND, ALCOD, ALCROD, SROFD, ALCHOQD, ALCHIND, SHOIFD, SROIND, SIND, ALCRIND, SHIFD, ALCID, ALCRND, SRD, SROID, ALCHOIQD, SROND, ALCROQD, SHOD, ALCHD, ALCHIQD, SRFD, ALCRFD, SIQD, ALCRID, SHIQD, ALCOIND, SOND, SHQD, SOQD, ALCRD, SRND, SHD, ALCIND, ALCRIQD, ALCHID, ALCHOD, SOIQD, ALCOIFD, SFD, SOIND, ALCRIFD, SOID, ALCHFD, ALCHIFD, SOD, SROIFD, ALCOQD, SRIND, SIFD, ALCOFD, SRIFD, SRQD, SHOID, SHOND, SHOIQD, SRID, ALCROFD, ALCRQD, ALCIQD, SRIQD, SOFD, SHFD, ALCND, ALCHOFD, ALCROIFD, SHOIND, SID, ALCHQD, ALCHOID, ALCHOIND, ALCFD, SRrOFD, SRrD, ALCRrFD, ALCRrOFD, ELPLUSPLUS, ALCRrQD, ALCRrOQD, SRrOND, SRrND, ALCRrD, SRrQD, ALCRrOD, SRrOD, SRrOQD, SRrFD, ALCRrND, ALCRrOND); }
    protected static List<Languages> belowUO()        { return l(ALCO, ALCOI, ALCOF, ALCON, ALCOQ, ALCOIQ, ALCOIN, ALCOIF, SO, SOF, SON, SOQ, SOI, SOIF, SOIN, SOIQ, SHO, SHOF, SHON, SHOQ, SHOI, SHOIF, SHOIN, SHOIQ, SROIQ, SROIQD, ALCROIND, SROQD, ALCHOF, SOIFD, ALCHOND, ALCHOIF, ALCROND, SHOFD, SHOQD, ALCOID, SROD, ALCROID, ALCOIQD, SROQ, ALCHOIFD, ALCROIQD, ALCROQ, ALCOND, ALCOD, ALCROD, SROFD, SROIN, ALCHOQD, SHOIFD, SROIND, SRON, SROI, SRO, SROID, ALCHOIQD, SROND, ALCROQD, ALCHO, SHOD, ALCROF, ALCHOIQ, ALCOIND, SOND, SOQD, ALCRON, ALCHOD, SOIQD, ALCOIFD, SOIND, SROF, SROIF, SOID, SOD, SROIFD, ALCOQD, ALCOFD, SHOID, SHOND, SHOIQD, ALCROIF, ALCHON, ALCROFD, ALCHOQ, SOFD, ALCROIQ, ALCROIN, ALCHOFD, ALCROIFD, SHOIND, ALCROI, ALCHOID, ALCHOIN, ALCHOIND, ALCHOI, ALCRO, SRrOFD, ALCRrOQ, ALCRrOFD, ALCRrOQD, SRrOND, SRrON, SRrOF, SRrOQ, SRrO, ALCRrO, ALCRrOD, SRrOD, ALCRrON, SRrOQD, ALCRrOF, ALCRrOND); }
    protected static List<Languages> belowEO()        { return l(ALCO, ALCOI, ALCOF, ALCON, ALCOQ, ALCOIQ, ALCOIN, ALCOIF, SO, SOF, SON, SOQ, SOI, SOIF, SOIN, SOIQ, SHO, SHOF, SHON, SHOQ, SHOI, SHOIF, SHOIN, SHOIQ, SROIQ, SROIQD, ELPLUSPLUS, ALCROIND, SROQD, ALCHOF, SOIFD, ALCHOND, ALCHOIF, ALCROND, SHOFD, SHOQD, ALCOID, SROD, ALCROID, ALCOIQD, SROQ, ALCHOIFD, ALCROIQD, ALCROQ, ALCOND, ALCOD, ALCROD, SROFD, SROIN, ALCHOQD, SHOIFD, SROIND, SRON, SROI, SRO, SROID, ALCHOIQD, SROND, ALCROQD, ALCHO, SHOD, ALCROF, ALCHOIQ, ALCOIND, SOND, SOQD, ALCRON, ALCHOD, SOIQD, ALCOIFD, SOIND, SROF, SROIF, SOID, SOD, SROIFD, ALCOQD, ALCOFD, SHOID, SHOND, SHOIQD, ALCROIF, ALCHON, ALCROFD, ALCHOQ, SOFD, ALCROIQ, ALCROIN, ALCHOFD, ALCROIFD, SHOIND, ALCROI, ALCHOID, ALCHOIN, ALCHOIND, ALCHOI, ALCRO, ALCRrOF, SRrOND, SRrOFD, SRrON, SRrO, ALCRrON, ALCRrOD, SRrOQ, ALCRrOND, ALCRrOQ, SRrOF, SRrOD, ALCRrO, SRrOQD, ALCRrOQD, ALCRrOFD); }
    protected static List<Languages> belowO()         { return l(ALCO, ALCOI, ALCOF, ALCON, ALCOQ, ALCOIQ, ALCOIN, ALCOIF, SO, SOF, SON, SOQ, SOI, SOIF, SOIN, SOIQ, SHO, SHOF, SHON, SHOQ, SHOI, SHOIF, SHOIN, SHOIQ, SROIQ, SROIQD, ELPLUSPLUS, ALCROF, SOND, ALCOQD, SHOIQD, ALCROI, ALCROND, ALCROIQD, SROIF, ALCHOD, ALCHOIN, ALCHOFD, SHOND, ALCHOI, ALCOFD, SROND, SHOID, ALCHON, ALCOIQD, SOQD, SOIQD, ALCHO, ALCRON, ALCROIQ, ALCROQD, ALCROFD, ALCOIFD, SHOD, ALCOID, ALCRO, SROFD, SROID, SHOIFD, ALCHOIQ, ALCROIN, ALCOND, ALCOD, SOIND, ALCHOF, ALCHOID, SROIN, ALCROIND, ALCROQ, ALCHOQD, SROQ, ALCOIND, ALCHOIF, ALCROID, ALCHOQ, ALCROIF, SROQD, ALCHOIQD, ALCHOIFD, SHOQD, ALCHOND, SROF, SOIFD, SOD, SROIFD, SHOFD, ALCROD, SRO, SOID, ALCROIFD, SHOIND, SROD, SRON, SROI, SROIND, ALCHOIND, SOFD, ALCRrOFD, ALCRrOD, SRrOD, SRrOF, SRrOFD, SRrOQ, SRrON, ALCRrOQ, SRrO, ALCRrOF, ALCRrOND, ALCRrON, SRrOQD, ALCRrOQD, ALCRrO, SRrOND); }
    protected static List<Languages> belowCUO()       { return l(ALCO, ALCOI, ALCOF, ALCON, ALCOQ, ALCOIQ, ALCOIN, ALCOIF, SO, SOF, SON, SOQ, SOI, SOIF, SOIN, SOIQ, SHO, SHOF, SHON, SHOQ, SHOI, SHOIF, SHOIN, SHOIQ, SROIQ, SROIQD, ALCHOIND, ALCROI, SOIQD, ALCHOIQD, SHOIND, ALCROIFD, ALCHOQ, SOFD, ALCROFD, ALCOIND, SOND, SHOID, SROI, SHOD, ALCROIN, SROF, SHOFD, ALCROD, SOQD, SRO, ALCOFD, ALCROND, ALCHOI, ALCRON, SROQD, SROQ, ALCHON, ALCHOIQ, SOIFD, ALCHOD, SHOQD, ALCROID, SROIF, SROIN, ALCHOIF, SROIFD, SROD, ALCOID, ALCHOQD, SHOIQD, ALCRO, ALCROF, ALCROIQ, SHOIFD, SOID, SROFD, SROIND, ALCHOIN, SOD, ALCHOF, SHOND, SROID, ALCROIF, SRON, ALCROQ, SROND, ALCHOND, ALCHOID, ALCROQD, SOIND, ALCHO, ALCOIFD, ALCHOIFD, ALCROIQD, ALCOIQD, ALCOQD, ALCOD, ALCOND, ALCROIND, ALCHOFD, SRrOD, ALCRrOF, ALCRrOFD, SRrOQD, ALCRrOD, ALCRrOND, SRrOND, ALCRrO, SRrON, ALCRrOQ, SRrOFD, SRrOQ, SRrOF, SRrO, ALCRrON, ALCRrOQD); }
    protected static List<Languages> belowIF()        { return l(ALCIF, ALCOIF, SIF, SOIF, SHIF, SHOIF, SOIFD, ALCRIFD, SRIF, ALCRIF, SROIFD, ALCROIF, SHOIFD, SROIF, ALCHIFD, ALCROIFD, ALCOIFD, SIFD, ALCIFD, ALCHOIFD, ALCHIF, ALCHOIF, SHIFD, SRIFD); }
    protected static List<Languages> belowF()         { return l(ALCF, ALCOF, ALCIF, ALCOIF, SF, SOF, SIF, SOIF, SHF, SHOF, SHIF, SHOIF, SRF, ALCHOFD, ALCHIFD, ALCHIF, SROF, ALCHOIF, ALCRIFD, SHFD, SRFD, ALCRFD, SHIFD, SOIFD, SROIFD, ALCOIFD, ALCOFD, SRIFD, SFD, SRIF, ALCRIF, SROFD, SIFD, SHOFD, ALCROIF, ALCIFD, SHOIFD, ALCHOIFD, ALCROIFD, SOFD, ALCFD, ALCROF, ALCRF, SROIF, ALCHF, ALCROFD, ALCHFD, ALCHOF, SRrOFD, ALCRrFD, SRrFD, ALCRrOFD, SRrOF, ALCRrF, SRrF, ALCRrOF); }
    protected static List<Languages> belowCU()        { return l(ALC, ALCD, ALCQ, ALCN, ALCF, ALCI, ALCO, ALCOI, ALCOF, ALCIF, ALCIN, ALCON, ALCOQ, ALCIQ, ALCOIQ, ALCOIN, ALCOIF, Languages.S, SI, SO, SF, SN, SQ, SOF, SIF, SON, SIN, SOQ, SIQ, SOI, SOIF, SOIN, SOIQ, SH, SHF, SHN, SHO, SHI, SHOF, SHIF, SHON, SHQ, SHOQ, SHIQ, SHOI, SHOIF, SHOIN, SHOIQ, SHIN, SHIND, SROIQ, SROIQD, SRIQD, SRQD, SHIQD, ALCROQ, ALCH, ALCRIQD, SROQD, SROIF, ALCROD, ALCOD, SIQD, ALCFD, ALCHOD, ALCID, ALCROIQD, ALCROIF, SROIN, SHOFD, ALCRD, SRN, SRIN, SROD, ALCND, SRO, ALCRQD, SHND, SROFD, SD, ALCRIQ, ALCRO, SRQ, ALCROID, SHOIFD, SRIND, ALCHIND, ALCHOIND, SOND, SHD, ALCRIFD, SROIND, ALCHD, SOIQD, SROND, ALCHOQD, SROF, ALCOID, SND, SRFD, SRD, SIFD, ALCRIF, SHID, SROQ, SROI, ALCOIND, ALCROF, SHIFD, ALCHI, ALCHOF, ALCHOID, ALCROIQ, ALCHOIQ, ALCIQD, ALCROI, SRIF, ALCROND, ALCIFD, ALCHIQD, SOD, SHQD, ALCHQD, SFD, ALCHQ, ALCHOI, SHOIQD, SRND, ALCOND, ALCOFD, ALCROIFD, ALCHOIQD, SHOIND, SOIFD, SROID, ALCHOND, ALCOQD, ALCHID, ALCRFD, ALCRI, ALCHIFD, SRIFD, ALCRID, SRF, ALCQD, ALCRND, ALCHN, ALCRN, ALCHFD, ALCHOIFD, ALCRON, ALCIND, SID, SQD, ALCHF, SHFD, ALCHIF, ALCR, ALCRIN, SIND, SRON, SHOQD, ALCHIN, ALCROFD, ALCHON, ALCOIFD, ALCHIQ, ALCHOQ, ALCRF, SRIQ, ALCROQD, ALCROIN, SR, ALCHOFD, SROIFD, ALCRIND, SOID, SOIND, SHOND, SRID, SRI, ALCHOIN, SHOID, SOFD, ALCHND, ALCHOIF, ALCRQ, ALCROIND, ALCHO, SOQD, SHOD, ALCOIQD, ALCRrQD, ALCRrOFD, ALCRrOD, SRrND, SRr, ALCRrOND, ALCRrQ, ALCRrO, ALCRrOQ, ALCRrF, SRrOFD, SRrN, SRrOQ, SRrOF, ALCRrFD, SRrOD, ALCRrOF, SRrQD, SRrOQD, SRrQ, ALCRr, ALCRrND, SRrFD, SRrOND, ALCRrN, SRrON, SRrF, SRrO, ALCRrON, ALCRrD, ALCRrOQD, SRrD); }
    protected static List<Languages> belowUniversal() { return l(FL0, FLMINUS, FL, AL, ALE, ALC, ALCD, ALCQ, ALCN, ALCF, ALCI, ALCO, ALCOI, ALCOF, ALCIF, ALCIN, ALCON, ALCOQ, ALCIQ, ALCOIQ, ALCOIN, ALCOIF, Languages.S, SI, SO, SF, SN, SQ, SOF, SIF, SON, SIN, SOQ, SIQ, SOI, SOIF, SOIN, SOIQ, SH, SHF, SHN, SHO, SHI, SHOF, SHIF, SHON, SHQ, SHOQ, SHIQ, SHOI, SHOIF, SHOIN, SHOIQ, SHIN, SHIND, SROIQ, SROIQD, SOFD, ALCROIND, ALCHOIQD, SOND, SRIQD, ALCQD, ALCIFD, ALCOFD, SIND, SRIFD, SHND, SIQD, ALCHID, SROI, SRI, ALCHOQ, SRND, SRIQ, SROQ, SRIND, ALCHN, ALCHIF, ALCHO, ALCHOIND, ALCRO, SRO, ALCROND, SRID, SRQ, ALCRIN, SOIND, SHIFD, SROND, ALCOD, SROD, ALCHOID, SHOID, SRIN, ALCRIF, ALCROQ, ALCRIQD, SRON, ALCROQD, SHOD, ALCHI, ALCFD, SROFD, ALCROIQD, SROIFD, SD, ALCHOIFD, SRQD, SHOIQD, ALCHOD, SOIQD, SOD, SROID, ALCHOQD, ALCHIFD, SHFD, ALCHOI, SHQD, ALCIND, ALCHOIN, SROIF, ALCROID, SOQD, SROIN, ALCOID, ALCHF, ALCHOND, ALCRIQ, ALCROD, ALCR, ALCROIN, ALCHOF, ALCHND, ALCOIND, ALCRF, SQD, ALCHIND, ALCHIQD, SHOND, SFD, ALCRI, ALCRON, ALCROI, ALCHFD, SHIQD, ALCHOIF, ALCROIQ, ALCOQD, ALCOIQD, ALCROIFD, SIFD, ALCND, ALCRFD, ALCRQD, SRF, SHD, ALCHIQ, ALCHD, SHOQD, SRD, ALCHOIQ, SRIF, ALCRND, SHID, ALCRIND, SHOFD, ALCRD, SRN, SROF, ALCROIF, SOID, ALCRQ, SRFD, ALCOIFD, ALCHON, ALCHQD, ALCRIFD, ALCROF, SHOIFD, ALCRID, ALCHIN, ALCHOFD, ALCOND, SROQD, SHOIND, ALCRN, SND, ALCHQ, SID, ALCIQD, SOIFD, ALCH, SROIND, SR, ALCROFD, ALCID, ALCRrQD, ALCRrOFD, ALCRrOD, SRrND, SRr, ALCRrOND, ALCRrQ, ALCRrO, ALCRrOQ, ALCRrF, SRrOFD, SRrN, SRrOQ, SRrOF, ALCRrFD, SRrOD, ALCRrOF, SRrQD, SRrOQD, SRrQ, ALCRr, ALCRrND, SRrFD, SRrOND, ALCRrN, SRrON, SRrF, SRrO, ALCRrON, ALCRrD, ALCRrOQD, SRrD); }
    protected static List<Languages> belowQD()        { return l(ALCRIQD, SOQD, SRIQD, ALCHIQD, ALCIQD, SHIQD, SRQD, SQD, SHOQD, SIQD, SHOIQD, ALCHOQD, ALCHOIQD, SHQD, ALCROIQD, ALCOQD, SOIQD, SROQD, ALCHQD, ALCOIQD, SROIQD, ALCRQD, ALCROQD, ALCQD, SRrOQD, ALCRrOQD, ALCRrQD, SRrQD); }
    protected static List<Languages> belowQ()         { return l(SRrQ, ALCHOIQD, SOIQ, SRIQ, SHQD, SRIQD, ALCHOQ, SRQD, ALCROIQ, ALCHQ, ALCRrOQ, SHOIQ, SOQD, SROQ, SIQ, ALCROQ, SOQ, ALCIQ, SHIQ, ALCHIQ, ALCRIQD, SHOQD, ALCHOQD, SRrOQD, ALCROIQD, ALCOQD, SHQ, ALCRrQD, SOIQD, SRrQD, ALCOQ, ALCRQ, ALCRrOQD, SQ, ALCHIQD, ALCRIQ, SROQD, SIQD, SHOIQD, ALCHQD, SROIQD, SHOQ, SQD, SRQ, ALCQD, ALCOIQ, SROIQ, ALCIQD, SHIQD, ALCHOIQ, ALCROQD, ALCRQD, SRrOQ, ALCRrQ, ALCQ, ALCOIQD); }
    protected static List<Languages> belowD()         { return l(ALCD, SHIND, SROIQD, ALCROIQD, ALCHOQD, SHOID, ALCRND, ALCROND, SROND, SOFD, ALCHID, ALCRID, ALCROQD, SHOIFD, ALCQD, ALCRFD, SQD, SHOD, SOQD, ALCOND, ALCIQD, ALCROIND, SHOIQD, ALCOQD, SHOFD, ALCHFD, SND, SOIQD, ALCRIQD, ALCHOND, ALCRIND, ALCHQD, SOIFD, ALCHOD, ALCHND, SHD, SHIFD, SROFD, ALCOIQD, ALCHOID, SHID, ALCOID, ALCHOFD, ALCROIFD, ALCOIND, ALCHIND, SRQD, ALCIFD, SD, ALCHOIND, SHIQD, SROQD, SROIND, ALCROFD, SHOQD, SID, SOD, ALCOIFD, ALCFD, ALCRQD, SHOIND, SRFD, SRND, SRIND, ALCHIQD, SROD, SROIFD, SHQD, ALCROD, ALCID, SOIND, ALCHIFD, ALCIND, SHFD, ALCRD, SOND, ALCND, ALCOD, SIND, SOID, SHND, SIQD, SRD, SHOND, ALCOFD, ALCHOIQD, ALCROID, SFD, SROID, ALCHD, ALCHOIFD, SIFD, SRIFD, SRIQD, ALCRIFD, SRID, SRrOQD, ALCRrOQD, ALCRrQD, SRrND, ELPLUSPLUS, SRrQD, SRrOFD, SRrOND, ALCRrND, ALCRrD, SRrOD, ALCRrOFD, SRrD, ALCRrOND, ALCRrOD, ALCRrFD, SRrFD); }
    protected static List<Languages> belowDU()        { return l(ALCD, SHIND, SROIQD, ALCROIQD, ALCHOQD, SHOID, ALCRND, ALCROND, SROND, SOFD, ALCHID, ALCRID, ALCROQD, SHOIFD, ALCQD, ALCRFD, SQD, SHOD, SOQD, ALCOND, ALCIQD, ALCROIND, SHOIQD, ALCOQD, SHOFD, ALCHFD, SND, SOIQD, ALCRIQD, ALCHOND, ALCRIND, ALCHQD, SOIFD, ALCHOD, ALCHND, SHD, SHIFD, SROFD, ALCOIQD, ALCHOID, SHID, ALCOID, ALCHOFD, ALCROIFD, ALCOIND, ALCHIND, SRQD, ALCIFD, SD, ALCHOIND, SHIQD, SROQD, SROIND, ALCROFD, SHOQD, SID, SOD, ALCOIFD, ALCFD, ALCRQD, SHOIND, SRFD, SRND, SRIND, ALCHIQD, SROD, SROIFD, SHQD, ALCROD, ALCID, SOIND, ALCHIFD, ALCIND, SHFD, ALCRD, SOND, ALCND, ALCOD, SIND, SOID, SHND, SIQD, SRD, SHOND, ALCOFD, ALCHOIQD, ALCROID, SFD, SROID, ALCHD, ALCHOIFD, SIFD, SRIFD, SRIQD, ALCRIFD, SRID, SRrOQD, ALCRrOQD, ALCRrQD, SRrND, SRrQD, SRrOFD, SRrOND, ALCRrND, ALCRrD, SRrOD, ALCRrOFD, SRrD, ALCRrOND, ALCRrOD, ALCRrFD, SRrFD); }
    protected static List<Languages> belowH()         { return l(SH, SHF, SHN, SHO, SHI, SHOF, SHIF, SHON, SHQ, SHOQ, SHIQ, SHOI, SHOIF, SHOIN, SHOIQ, SHIN, SHIND, ALCHOF, SHIQD, SHOFD, ALCHQ, ALCHOD, ALCHIQ, ALCHOND, ALCHIND, ALCHI, ALCHD, SHOIFD, ALCHOIN, ALCHIFD, ALCHOQD, ALCHOIQ, SHOND, ALCHON, ALCHOIFD, SHID, ALCHIF, ALCHND, ALCHOIND, ALCHIN, ALCHOIQD, ALCHIQD, ALCHOFD, ALCHQD, SHFD, ALCHOQ, ALCHO, ALCHFD, SHOQD, SHND, ALCHOI, SHOIND, SHQD, SHOID, ALCHOID, ALCHN, ALCHID, SHOD, ALCH, SHOIQD, ALCHF, ALCHOIF, SHD, SHIFD); }
    protected static List<Languages> belowHD()        { return l(SHIND, ALCHOIND, SHOQD, ALCHOD, ALCHD, ALCHOFD, SHIQD, SHND, ALCHND, SHOIFD, ALCHOIQD, SHID, SHOD, SHOND, ALCHIQD, ALCHOND, ALCHIFD, ALCHOIFD, SHQD, SHFD, SHIFD, ALCHOID, SHD, SHOID, ALCHIND, ALCHOQD, SHOFD, SHOIND, ALCHQD, ALCHFD, ALCHID, SHOIQD); }
    protected static List<Languages> belowI()         { return l(ALCI, ALCOI, ALCIF, ALCIN, ALCIQ, ALCOIQ, ALCOIN, ALCOIF, SI, SIF, SIN, SIQ, SOI, SOIF, SOIN, SOIQ, SHI, SHIF, SHIQ, SHOI, SHOIF, SHOIN, SHOIQ, SHIN, SHIND, SROIQ, SROIQD, SIQD, ALCRID, SRI, SRIQD, ALCOIND, ALCHOIF, SROIND, ALCHOID, SOID, ALCIND, SRIQ, ALCRI, SOIND, ALCRIND, SRIF, SHIQD, ALCID, ALCRIN, SRIN, ALCIQD, ALCRIQ, ALCROIF, ALCHIN, ALCHOIQ, ALCHIQD, ALCHI, ALCHID, SHOIQD, ALCRIQD, SRID, SRIND, ALCHIFD, SROID, SHOID, ALCOIQD, ALCHIF, ALCROIQD, ALCRIFD, SIND, ALCROID, SHID, SHIFD, SOIFD, ALCHIND, SROIFD, SROI, ALCOIFD, SHOIND, SRIFD, SROIN, ALCROIND, ALCHOIN, ALCRIF, ALCHIQ, SIFD, SOIQD, ALCHOI, ALCROI, ALCROIQ, ALCOID, ALCIFD, SHOIFD, ALCHOIFD, SID, ALCROIFD, ALCHOIND, SROIF, ALCHOIQD, ALCROIN); }
    protected static List<Languages> belowC()         { return l(ALC, ALCD, ALCQ, ALCN, ALCF, ALCI, ALCO, ALCOI, ALCOF, ALCIF, ALCIN, ALCON, ALCOQ, ALCIQ, ALCOIQ, ALCOIN, ALCOIF, Languages.S, SI, SO, SF, SN, SQ, SOF, SIF, SON, SIN, SOQ, SIQ, SOI, SOIF, SOIN, SOIQ, SH, SHF, SHN, SHO, SHI, SHOF, SHIF, SHON, SHQ, SHOQ, SHIQ, SHOI, SHOIF, SHOIN, SHOIQ, SHIN, SHIND, SROIQ, SROIQD, ALCROIND, ALCOFD, ALCHOIQ, ALCRF, SR, ALCRIQ, ALCRD, SRIFD, ALCRO, SQD, SRI, ALCID, ALCH, SND, SOQD, SHOND, ALCRI, ALCHIND, ALCOND, ALCHIFD, SRIN, SOIND, SROIFD, SRO, SRF, ALCHOID, SROI, SROIN, ALCOID, ALCHIQD, SHND, ALCHOI, SROF, SHOIND, SHOD, SIQD, ALCQD, SHIQD, SOD, ALCHI, ALCHND, ALCHOIQD, ALCOIQD, ALCROF, SROIND, ALCRID, SFD, SHFD, ALCRFD, SROQD, SHOIFD, ALCROI, SHOQD, ALCRIQD, ALCFD, ALCHOD, ALCHF, ALCROIQD, SROFD, ALCHOIF, ALCRON, ALCROIF, ALCIND, ALCHIQ, SROD, ALCRIN, ALCHOQ, SID, ALCHQ, ALCHOND, ALCOQD, ALCRIFD, ALCRIF, ALCROFD, SRIQ, SRQ, ALCROD, ALCHD, ALCHN, SIFD, SRN, SRQD, SROND, ALCRIND, ALCHOF, SHOIQD, ALCHOFD, SHQD, ALCR, SD, SROID, SOID, ALCROIN, SRON, SRD, ALCRN, ALCRND, SOFD, ALCRQ, SRIND, ALCHOQD, ALCOIFD, ALCOD, SHIFD, ALCOIND, SIND, ALCHQD, ALCROQ, SOIQD, SHOFD, ALCHFD, ALCROQD, ALCHOIND, ALCHOIN, SROIF, ALCROID, ALCROIFD, ALCHIN, SROQ, SHID, SHD, ALCROND, ALCIFD, ALCIQD, ALCHOIFD, SRID, ALCHON, ALCRQD, SRFD, ALCROIQ, ALCHID, ALCND, SOIFD, SRND, SRIF, ALCHO, SHOID, ALCHIF, SOND, SRIQD, ALCRrQD, ALCRrOFD, ALCRrOD, SRrND, SRr, ALCRrOND, ALCRrQ, ALCRrO, ALCRrOQ, ALCRrF, SRrOFD, SRrN, SRrOQ, SRrOF, ALCRrFD, SRrOD, ALCRrOF, SRrQD, SRrOQD, SRrQ, ALCRr, ALCRrND, SRrFD, SRrOND, ALCRrN, SRrON, SRrF, SRrO, ALCRrON, ALCRrD, ALCRrOQD, SRrD); }
    protected static List<Languages> belowR()         { return l(SROQD, SRIQD, ALCROQD, SROF, SRI, SROID, ALCROQ, SRIN, ALCROIFD, ALCRF, ALCROIND, SRF, ELPLUSPLUS, SRID, SROIQ, ALCRND, ALCROIQD, ALCRI, ALCROIF, ALCRD, ALCRON, SROD, ALCROIQ, ALCRO, ALCROF, SROIF, SROND, ALCRIF, SRQD, SRIFD, SRIND, SROFD, SROIND, ALCRQ, SROQ, ALCRFD, ALCROID, SRND, SROIN, ALCROND, ALCRIN, SRD, ALCR, ALCRQD, ALCRIQD, SROI, SRON, ALCROI, ALCRIFD, ALCRID, SRFD, ALCRN, SRIF, SR, SRN, SRO, SROIFD, ALCROFD, ALCRIND, ALCROIN, SRQ, ALCROD, SRIQ, SROIQD, ALCRIQ); }
    protected static List<Languages> belowRr()        { return l(ALCRr, ALCRrON, SRrQD, SRrD, ALCRrO, ALCRrFD, ALCRrOQ, SRrOF, ALCRrOQD, ALCRrF, SRrQ, ALCRrN, ALCRrOFD, SRrON, SRrFD, ALCRrQ, SRrO, ALCRrQD, SRrND, ALCRrOND, SRrOND, ALCRrOF, SRrOQD, ALCRrD, SRrF, ALCRrND, SRrOD, SRrN, ALCRrOD, SRrOFD, SRrOQ, SRr); }
    protected static List<Languages> expressR()       { return l(ALCR, ELPLUSPLUS); }
    protected static List<Languages> expressRr()      { return l(ALCRr); }
  //@formatter:on

    @ParameterizedTest
    @MethodSource("getData")
    void testAssertion(@SuppressWarnings("unused") String name, String expected,
        List<Construct> constructs, List<Languages> expressible, List<Languages> within,
        List<Languages> minimal, List<OWLAxiom> objects) {
        Set<OWLOntology> ont = ont(objects);
        DLExpressivityChecker testsubject = new DLExpressivityChecker(ont);
        List<Construct> constructsFound = testsubject.getConstructs();
        List<Languages> below = new ArrayList<>();
        List<Languages> minimalLanguages = new ArrayList<>();
        for (Languages lang : Languages.values()) {
            if (testsubject.isWithin(lang)) {
                below.add(lang);
            }
            if (testsubject.minimal(lang)) {
                minimalLanguages.add(lang);
            }
        }
        Collection<Languages> expressibleInLanguages = testsubject.expressibleInLanguages();
        assertEquals(expressible, expressibleInLanguages,
            expected + delta("expressible", expressible, expressibleInLanguages));
        assertEquals(constructs, constructsFound, expected);
        assertEquals(new HashSet<>(within), new HashSet<>(below),
            expected + delta("below", within, below));
        assertEquals(expected, testsubject.getDescriptionLogicName());
        assertEquals(new HashSet<>(minimal), new HashSet<>(minimalLanguages),
            expected + delta("minimal", minimal, minimalLanguages));
    }

    private static String delta(String prefix, Collection<Languages> within2,
        Collection<Languages> below) {
        Set<Languages> onlyFirst = new HashSet<>(within2);
        onlyFirst.removeAll(below);
        Set<Languages> onlySecond = new HashSet<>(below);
        onlySecond.removeAll(within2);
        return prefix + "Only in first list: " + onlyFirst + "    Only in second list: \n"
            + onlySecond.stream().map(Languages::name).collect(Collectors.joining(", ")) + "\n\n";
    }

    Set<OWLOntology> ont(Collection<OWLAxiom> objects) {
        return set(o(IRIS.iriTest, objects));
    }
}
