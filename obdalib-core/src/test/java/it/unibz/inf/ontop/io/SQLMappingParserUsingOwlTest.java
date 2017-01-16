package it.unibz.inf.ontop.io;

/*
 * #%L
 * ontop-protege4
 * %%
 * Copyright (C) 2009 - 2013 KRDB Research Centre. Free University of Bozen Bolzano.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Injector;

import it.unibz.inf.ontop.injection.OBDACoreConfiguration;
import it.unibz.inf.ontop.model.*;
import it.unibz.inf.ontop.model.impl.SQLMappingFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import it.unibz.inf.ontop.exception.DuplicateMappingException;
import it.unibz.inf.ontop.exception.InvalidMappingException;
import it.unibz.inf.ontop.exception.InvalidMappingExceptionWithIndicator;
import it.unibz.inf.ontop.exception.InvalidPredicateDeclarationException;
import it.unibz.inf.ontop.injection.NativeQueryLanguageComponentFactory;
import it.unibz.inf.ontop.injection.OBDAFactoryWithException;
import it.unibz.inf.ontop.mapping.MappingParser;

import it.unibz.inf.ontop.parser.TurtleOBDASyntaxParser;

import static org.junit.Assert.assertEquals;

public class SQLMappingParserUsingOwlTest {

    private static final SQLMappingFactory MAPPING_FACTORY = SQLMappingFactoryImpl.getInstance();
    private final NativeQueryLanguageComponentFactory nativeQLFactory;
    private final OBDAFactoryWithException modelFactory;

    private TurtleOBDASyntaxParser parser;

    private String[][] mappings = {
            { "M1", "select id, fname, lname, age from student",
                    ":P{id} a :Student ; :firstName {fname} ; :lastName {lname} ; :age {age}^^xsd:int ." },
            { "M2", "select id, title, lecturer, description from course",
                    ":C{id} a :Course ; :title {title} ; :hasLecturer :L{id} ; :description {description}@en-US ." },
            { "M3", "select sid, cid from enrollment",
                    ":P{sid} :hasEnrollment :C{cid} ." },
                    
            { "M4", "select id, nome, cognome, eta from studenti",
                    ":P{id} a :Student ; :firstName {nome} ; :lastName {cognome} ; :age {eta}^^xsd:int ." },
            { "M5", "select id, titolo, professore, descrizione from corso",
                    ":C{id} a :Course ; :title {titolo} ; :hasLecturer :L{id} ; :description {decrizione}@it ." },
            { "M6", "select sid, cid from registrare", 
                    ":P{sid} :hasEnrollment :C{cid} ." }
    };
    private ImmutableMap<String, String> prefixes;

    public SQLMappingParserUsingOwlTest() {
        OBDACoreConfiguration configuration = OBDACoreConfiguration.defaultBuilder()
                .dbName("http://www.example.org/db/dummy/")
                .jdbcUrl("jdbc:postgresql://www.example.org/dummy")
                .dbUser("dummy")
                .dbPassword("dummy")
                .jdbcDriver("org.postgresql.Driver")
                .build();

        Injector injector = configuration.getInjector();
        nativeQLFactory = injector.getInstance(NativeQueryLanguageComponentFactory.class);
        modelFactory = injector.getInstance(OBDAFactoryWithException.class);
    }

    @Before
    public void setUp() throws Exception {
        PrefixManager prefixManager = setupPrefixManager();

        // Setting up the CQ parser
        prefixes = prefixManager.getPrefixMap();
        parser = new TurtleOBDASyntaxParser(prefixes);
    }

    @Test
    public void testRegularFile() throws Exception {
        saveRegularFile();
        loadRegularFile();
    }

    @Test(expected=InvalidMappingExceptionWithIndicator.class)
    public void testLoadWithBlankMappingId()
            throws DuplicateMappingException, InvalidDataSourceException, InvalidMappingException, IOException, InvalidPredicateDeclarationException {
        loadObdaFile("src/test/resources/it/unibz/inf/ontop/io/SchoolBadFile5.obda");
    }

    @Test(expected=InvalidMappingExceptionWithIndicator.class)
    public void testLoadWithBlankTargetQuery() throws DuplicateMappingException, InvalidMappingException, InvalidPredicateDeclarationException, InvalidDataSourceException, IOException {
        loadObdaFile("src/test/resources/it/unibz/inf/ontop/io/SchoolBadFile6.obda");
    }

    @Test(expected=InvalidMappingExceptionWithIndicator.class)
    public void testLoadWithBlankSourceQuery() throws DuplicateMappingException, InvalidMappingException, InvalidPredicateDeclarationException, InvalidDataSourceException, IOException {
        loadObdaFile("src/test/resources/it/unibz/inf/ontop/io/SchoolBadFile7.obda");
    }

    @Test(expected=IOException.class)
    public void testLoadWithBadTargetQuery() throws DuplicateMappingException, InvalidMappingException,
            InvalidPredicateDeclarationException, InvalidDataSourceException, IOException {
        loadObdaFile("src/test/resources/it/unibz/inf/ontop/io/SchoolBadFile8.obda");
    }

    @Test(expected=IOException.class)
    public void testLoadWithPredicateDeclarations() throws Exception {
        loadObdaFile("src/test/resources/it/unibz/inf/ontop/io/SchoolBadFile9.obda");
    }

    @Test(expected=InvalidMappingExceptionWithIndicator.class)
    public void testLoadWithAllMistakes() throws DuplicateMappingException, InvalidMappingException,
            InvalidPredicateDeclarationException, InvalidDataSourceException, IOException {
            loadObdaFile("src/test/resources/it/unibz/inf/ontop/io/SchoolBadFile10.obda");
    }
    
    /*
     * Test saving to a file
     */

    private void saveRegularFile() throws Exception {
        OBDAModel model = modelFactory.createOBDAModel(ImmutableList.of(),
                nativeQLFactory.create(ImmutableMap.of()));
        OntopNativeMappingSerializer writer = new OntopNativeMappingSerializer(model);
        writer.save(new File("src/test/java/it/unibz/inf/ontop/io/SchoolRegularFile.obda"));
    }

    /*
     * Test loading the file
     */

    private void loadRegularFile() throws Exception {
        OBDAModel model = loadObdaFile("src/test/java/it/unibz/inf/ontop/io/SchoolRegularFile.obda");

        // Check the content
        assertEquals(model.getPrefixManager().getPrefixMap().size(), 5);
        assertEquals(model.getMappings().size(), 0);
    }

    private void loadFileWithMultipleDataSources() throws Exception {
        OBDAModel model = loadObdaFile("src/test/java/it/unibz/inf/ontop/io/SchoolMultipleDataSources.obda");

        // Check the content
        assertEquals(model.getPrefixManager().getPrefixMap().size(), 6);
        assertEquals(model.getMappings().size(), 2);
    }

    private OBDAModel loadObdaFile(String fileLocation) throws IOException,
            InvalidPredicateDeclarationException, InvalidMappingException, DuplicateMappingException,
            InvalidDataSourceException {
        // Load the OBDA model
        MappingParser mappingParser = nativeQLFactory.create(new File(fileLocation));
        return mappingParser.getOBDAModel();
    }

    private PrefixManager setupPrefixManager() {
        // Setting up the prefixes
        PrefixManager prefixManager = new SimplePrefixManager(ImmutableMap.of(PrefixManager.DEFAULT_PREFIX,
                "http://www.semanticweb.org/ontologies/2012/5/Ontology1340973114537.owl#"));
        return prefixManager;
    }
    
    private Map<URI, ImmutableList<OBDAMappingAxiom>> addSampleMappings(URI sourceId) {
        Map<URI, ImmutableList<OBDAMappingAxiom>> mappingIndex = new HashMap<>();
        // Add some mappings
        try {
            mappingIndex.put(sourceId, ImmutableList.of(nativeQLFactory.create(mappings[0][0],
                    MAPPING_FACTORY.getSQLQuery(mappings[0][1]), parser.parse(mappings[0][2]))));
            mappingIndex.put(sourceId, ImmutableList.of(nativeQLFactory.create(mappings[1][0],
                    MAPPING_FACTORY.getSQLQuery(mappings[1][1]), parser.parse(mappings[1][2]))));
            mappingIndex.put(sourceId, ImmutableList.of(nativeQLFactory.create(mappings[2][0],
                    MAPPING_FACTORY.getSQLQuery(mappings[2][1]), parser.parse(mappings[2][2]))));
        } catch (Exception e) {
            // NO-OP
        }
        return mappingIndex;
    }
    
    private Map<URI, ImmutableList<OBDAMappingAxiom>> addMoreSampleMappings(
            Map<URI, ImmutableList<OBDAMappingAxiom>> mappingIndex, URI sourceId) {
        // Add some mappings
        try {
            mappingIndex.put(sourceId, ImmutableList.of(nativeQLFactory.create(mappings[3][0],
                    MAPPING_FACTORY.getSQLQuery(mappings[3][1]), parser.parse(mappings[3][2]))));
            mappingIndex.put(sourceId, ImmutableList.of(nativeQLFactory.create(mappings[4][0],
                    MAPPING_FACTORY.getSQLQuery(mappings[4][1]), parser.parse(mappings[4][2]))));
            mappingIndex.put(sourceId, ImmutableList.of(nativeQLFactory.create(mappings[5][0],
                    MAPPING_FACTORY.getSQLQuery(mappings[5][1]), parser.parse(mappings[5][2]))));
        } catch (Exception e) {
            // NO-OP
        }
        return mappingIndex;
    }
}
