package com.misset.opp.odt.psi.impl.resolvable.querystep;

import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.psi.ODTConstantValue;
import com.misset.opp.odt.testcase.ODTFileTestImpl;
import com.misset.opp.odt.testcase.ODTTestCase;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

class ODTResolvableConstantValueStepAbstractTest extends ODTTestCase {

    private static Stream<Arguments> getExpectedTypes() {
        return Stream.of(
                Arguments.of("true", OntologyModelConstants.getXsdTrue()),
                Arguments.of("false", OntologyModelConstants.getXsdFalse()),
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

}
