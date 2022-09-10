package com.misset.opp.odt.annotation;

import com.misset.opp.odt.testcase.ODTFileTestImpl;
import com.misset.opp.odt.testcase.ODTTestCase;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static com.misset.opp.odt.annotation.ODTSemicolonAnnotator.SEMICOLON_ILLEGAL;
import static com.misset.opp.odt.annotation.ODTSemicolonAnnotator.SEMICOLON_REQUIRED;

@RunWith(MockitoJUnitRunner.class)
class ODTSemicolonAnnotatorTest extends ODTTestCase {

    @Test
    void testHasErrorWhenNotStatementWithoutSemicolon() {
        ODTFileTestImpl odtFile = configureByText("/ont:ClassA");
        odtFile.setIsStatement(false);
        inspection.assertHasError(SEMICOLON_REQUIRED);
    }

    @Test
    void testHasNoErrorWhenNotStatementWithSemicolon() {
        ODTFileTestImpl odtFile = configureByText("/ont:ClassA;");
        odtFile.setIsStatement(false);
        inspection.assertNoError(SEMICOLON_REQUIRED);
        inspection.assertNoError(SEMICOLON_ILLEGAL);
    }

    @Test
    void testHasErrorWhenStatementWithSemicolon() {
        ODTFileTestImpl odtFile = configureByText("/ont:ClassA;");
        odtFile.setIsStatement(true);
        inspection.assertHasError(SEMICOLON_ILLEGAL);
    }

    @Test
    void testHasWarningUnnecessarySemicolon() {
        configureByText("/ont:ClassA;;");
        inspection.assertHasError(SEMICOLON_ILLEGAL);
    }

    @Test
    void testHasErrorWhenInterpolationWithSemicolon() {
        configureByText("`${/ont:ClassA;}`;");
        inspection.assertHasError(SEMICOLON_ILLEGAL);
    }

    @Test
    void testNoErrorWhenInterpolationWithoutSemicolon() {
        configureByText("`${/ont:ClassA}`;");
        inspection.assertNoError(SEMICOLON_REQUIRED);
        inspection.assertNoError(SEMICOLON_ILLEGAL);
    }
}
