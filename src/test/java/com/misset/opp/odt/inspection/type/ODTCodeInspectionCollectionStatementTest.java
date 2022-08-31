package com.misset.opp.odt.inspection.type;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.testCase.OMTInspectionTestCase;
import com.misset.opp.testCase.OMTOntologyTestCase;
import com.misset.opp.ttl.model.OppModel;
import org.apache.jena.ontology.OntClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

class ODTCodeInspectionCollectionStatementTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(ODTCodeInspectionCollectionStatement.class);
    }

    @BeforeEach
    protected void setUp() {
        super.setUp();
        OMTOntologyTestCase.initOntologyModel();
    }

    @Test
    void testHasWarningWhenAssigningIntegerToBoolean() {
        assertHasTypeWarning("/ont:ClassA / ^rdf:type / ont:booleanPredicate = 12;");
    }

    @Test
    void testHasNoWarningWhenAssigningBooleanToBoolean() {
        assertHasNoTypeWarning("/ont:ClassA / ^rdf:type / ont:booleanPredicate = true;");
    }

    @Test
    void testHasWarningWhenAssigningWrongClass() {
        assertHasTypeWarning("/ont:ClassA / ^rdf:type / ont:classPredicate = /ont:ClassA / ^rdf:type;");
    }

    @Test
    void testHasNoWarningWhenAssigningCorrectClass() {
        assertHasNoTypeWarning("/ont:ClassA / ^rdf:type / ont:classPredicate = /ont:ClassBSub / ^rdf:type;");
    }

    @Test
    void testHasWarningWhenAssigningSuperClass() {
        assertHasNoTypeWarning("/ont:ClassA / ^rdf:type / ont:classPredicate = /ont:ClassB / ^rdf:type;");
    }

    @Test
    void testHasNoWarningWhenAssigningSubClass() {
        final OntClass classBSub = OppModel.getInstance().getClass("http://ontology#ClassBSub");
        final OntClass subclassOfClassBSub = OppModel.getInstance().getModel()
                .createClass(classBSub.getNameSpace() + "#SubclassOfClassBSub");
        subclassOfClassBSub.addSuperClass(classBSub);
        assertHasNoTypeWarning("/ont:ClassA / ^rdf:type / ont:classPredicate = /ont:SubclassOfClassBSub / ^rdf:type;");
    }

    private void assertHasTypeWarning(String assignmentToTest) {
        setContent(assignmentToTest);
        assertHasWarning("Incompatible types");
    }

    private void assertHasNoTypeWarning(String assignmentToTest) {
        setContent(assignmentToTest);
        assertNoWarning("Incompatible types");
    }

    private void setContent(String assignmentToTest) {
        String content = insideProcedureRunWithPrefixes(assignmentToTest);
        configureByText(content);
    }
}
