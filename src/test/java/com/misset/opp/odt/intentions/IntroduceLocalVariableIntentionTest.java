package com.misset.opp.odt.intentions;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.misset.opp.testCase.ODTTestCase;
import com.misset.opp.testCase.OMTOntologyTestCase;
import com.misset.opp.ttl.model.OppModelConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

class IntroduceLocalVariableIntentionTest extends ODTTestCase {

    @Test
    void testIsAvailableForQuery() {
        configureByText("<caret>/ont:ClassA / ^rdf:type;");
        assertHasIntention();
    }

    @Test
    @Disabled("Unit-test doesn't work due to the in-place refactoring mechanism")
    void testInvokeHasClassName() {
        OMTOntologyTestCase.initOntologyModel();
        configureByText(String.format("<caret>/<http://ontology#ClassA> / ^<%s>;", OppModelConstants.RDF_TYPE.getURI()));
        IntentionAction singleIntention = myFixture.findSingleIntention(IntroduceLocalVariableIntention.TEXT);
        WriteCommandAction.runWriteCommandAction(getProject(), () -> singleIntention.invoke(getProject(), getEditor(), getFile()));
        String text = ReadAction.compute(() -> getFile().getText());
        Assertions.assertEquals("VAR $classA = /<http://ontology#ClassA> / ^<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>;", text);
    }

    @Test
    @Disabled("Unit-test doesn't work due to the in-place refactoring mechanism")
    void testInvokeHasPrimitiveName() {
        OMTOntologyTestCase.initOntologyModel();
        configureByText("<caret>'test';");
        IntentionAction singleIntention = myFixture.findSingleIntention(IntroduceLocalVariableIntention.TEXT);
        WriteCommandAction.runWriteCommandAction(getProject(), () -> singleIntention.invoke(getProject(), getEditor(), getFile()));
        String text = ReadAction.compute(() -> getFile().getText());
        Assertions.assertEquals("VAR $string = 'test';", text);
    }

    @Test
    void testIsAvailableForCommand() {
        configureByText("<caret>@Call();");
        assertHasIntention();
    }

    @Test
    void testIsAvailableAtEndOfStatement() {
        configureByText("/ont:ClassA / ^rdf:type<caret>;");
        assertHasIntention();
    }

    @Test
    void testIsNotAvailableWhenAlreadyAssigned() {
        configureByText("VAR $variable = /ont:ClassA / ^rdf:type<caret>;");
        assertHasNoIntention();
    }

    @Test
    void testIsNotAvailableInsideDefineStatement() {
        configureByText("DEFINE QUERY query => /ont:ClassA / ^rdf:type<caret>;");
        assertHasNoIntention();
    }

    private void assertHasIntention() {
        List<IntentionAction> availableIntentions = myFixture.getAvailableIntentions();
        Assertions.assertTrue(availableIntentions.stream().anyMatch(intentionAction -> intentionAction.getText().equals(IntroduceLocalVariableIntention.TEXT)));
    }

    private void assertHasNoIntention() {
        List<IntentionAction> availableIntentions = myFixture.getAvailableIntentions();
        Assertions.assertFalse(availableIntentions.stream().anyMatch(intentionAction -> intentionAction.getText().equals(IntroduceLocalVariableIntention.TEXT)));
    }

}
