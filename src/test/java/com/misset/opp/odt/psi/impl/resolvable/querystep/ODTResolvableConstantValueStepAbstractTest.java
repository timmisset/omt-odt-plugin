package com.misset.opp.odt.psi.impl.resolvable.querystep;

import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.psi.ODTConstantValue;
import com.misset.opp.odt.testcase.ODTFileTestImpl;
import com.misset.opp.odt.testcase.ODTTestCase;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Literal;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

class ODTResolvableConstantValueStepAbstractTest extends ODTTestCase {

    private static Stream<Arguments> getExpectedLiterals() {
        OntModel model = initOntologyModel().getModel();
        return Stream.of(
                Arguments.of("true", OntologyModelConstants.getXsdBooleanTrue()),
                Arguments.of("false", OntologyModelConstants.getXsdBooleanFalse()),
                Arguments.of("'string'", model.createTypedLiteral("string")),
                Arguments.of("1", model.createTypedLiteral(1)),
                Arguments.of("1.0", model.createTypedLiteral(1.0)),
                Arguments.of("null", null)
        );
    }

    private static Stream<Arguments> getExpectedTypes() {
        return Stream.of(
                Arguments.of("true", OntologyModelConstants.getXsdBooleanInstance()),
                Arguments.of("false", OntologyModelConstants.getXsdBooleanInstance()),
                Arguments.of("'string'", OntologyModelConstants.getXsdStringInstance()),
                Arguments.of("1", OntologyModelConstants.getXsdIntegerInstance()),
                Arguments.of("1.0", OntologyModelConstants.getXsdDecimalInstance()),
                Arguments.of("`an interpolated string`", OntologyModelConstants.getXsdStringInstance()),
                Arguments.of("null", null),
                Arguments.of("string", OntologyModelConstants.getXsdString())
        );
    }

    @ParameterizedTest
    @MethodSource(value = "getExpectedTypes")
    void testHasExpectedValue(String content, OntResource type) {
        ODTFileTestImpl odtFileTest = configureByText(content);
        ReadAction.run(() -> {
            ODTConstantValue constantValue = PsiTreeUtil.findChildOfType(odtFileTest, ODTConstantValue.class);
            Set<OntResource> resolve = constantValue.resolve();
            if (type == null) {
                assertTrue(resolve.isEmpty());
            } else {
                assertEquals(1, resolve.size());
                assertEquals(type, resolve.iterator().next());
            }
        });
    }

    @ParameterizedTest
    @MethodSource(value = "getExpectedLiterals")
    void testHasExpectedLiteral(String content, Literal type) {
        ODTFileTestImpl odtFileTest = configureByText(content);
        ReadAction.run(() -> {
            ODTConstantValue constantValue = PsiTreeUtil.findChildOfType(odtFileTest, ODTConstantValue.class);
            List<Literal> literals = constantValue.resolveLiteral();
            if (type == null) {
                assertTrue(literals.isEmpty());
            } else {
                assertEquals(1, literals.size());
                assertEquals(type, literals.iterator().next());
            }
        });
    }

}
