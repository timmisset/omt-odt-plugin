package com.misset.opp.documentation;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ApiMarkdownReaderTest {

    Path path = Path.of("src/test/resources/ODT-API.md");
    ApiMarkdownReader apiMarkdownReader;

    ApiMarkdownReaderTest() throws IOException {
        apiMarkdownReader = new ApiMarkdownReader(path);
    }

    @Test
    void testGetDescription() {
        assertNotNull(apiMarkdownReader.getDescription("Operators/OPERATOR"));
        assertNotNull(apiMarkdownReader.getDescription("Operators/OPERATOR/Examples"));
    }

    @Test
    void testGetDescriptionReturnsDescriptionOfTheOperator() {
        assertTrue(
                apiMarkdownReader.getDescription("Operators/OPERATOR")
                        .contains("This is some information about the operator 'Operator'")
        );
    }

    @Test
    void testGetDescriptionReturnsExampleOfTheOperator() {
        assertTrue(
                apiMarkdownReader.getDescription("Operators/OPERATOR/Examples")
                        .contains("VAR $variable = OPERATOR")
        );
    }

    @Test
    void testReturnsNullWhenNoContent() throws IOException {
        Path path = Path.of("src/test/resources/empty-API.md");
        apiMarkdownReader = new ApiMarkdownReader(path);
        assertNull(apiMarkdownReader.getDescription("Operators/OPERATOR"));
    }

    @Test
    void testReturnsNullWhenNoContentAtTheSpecifiedPath() throws IOException {
        assertNull(apiMarkdownReader.getDescription("notValid"));
        assertNull(apiMarkdownReader.getDescription("Operators/notValid"));
        assertNull(apiMarkdownReader.getDescription("Operators/OPERATOR/notValid"));
    }
}
