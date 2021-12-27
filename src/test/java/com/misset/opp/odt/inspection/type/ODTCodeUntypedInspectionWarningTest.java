package com.misset.opp.odt.inspection.type;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.testCase.OMTInspectionTestCase;
import com.misset.opp.testCase.OMTOntologyTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Set;

class ODTCodeUntypedInspectionWarningTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Set.of(ODTCodeUntypedInspectionWarning.class);
    }

    @BeforeEach
    public void setUp() {
        super.setUp();
        OMTOntologyTestCase.initOntologyModel();
    }

    @Test
    void testHasWarningWhenNotAnnotated() {
        configureByText("queries: |\n" +
                "   DEFINE QUERY query($param) => '';\n");
        assertHasWarning(ODTCodeUntypedInspectionWarning.ANNOTATE_PARAMETER_WITH_TYPE);
    }

    @Test
    void testNoWarningWhenAnnotated() {
        configureByText("queries: |\n" +
                "   /**\n" +
                "    * @param $param (string)\n" +
                "    */\n" +
                "   DEFINE QUERY query($param) => '';\n");
        assertNoWarning(ODTCodeUntypedInspectionWarning.ANNOTATE_PARAMETER_WITH_TYPE);
    }

    @Test
    void testNoWarningWhenMultipleAnnotated() {
        configureByText("queries: |\n" +
                "   /**\n" +
                "    * @param $one (string)\n" +
                "    * @param $two (string)\n" +
                "    */\n" +
                "   DEFINE QUERY query($one, $two) => '';\n");
        assertNoWarning(ODTCodeUntypedInspectionWarning.ANNOTATE_PARAMETER_WITH_TYPE);
    }

    @Test
    void testNoWarningWhenWithCurie() {
        configureByText(withPrefixes("queries: |\n" +
                "   /**\n" +
                "    * @param $one (ont:ClassA)\n" +
                "    * @param $two (ont:ClassB)\n" +
                "    */\n" +
                "   DEFINE QUERY query($one, $two) => '';\n"));
        assertNoWarning(ODTCodeUntypedInspectionWarning.ANNOTATE_PARAMETER_WITH_TYPE);
    }

    @Test
    void testAddsJavaDoc() {
        configureByText("queries:\n" +
                "   DEFINE QUERY query($param) => '';\n");
        invokeQuickFixIntention(ODTCodeUntypedInspectionWarning.ANNOTATE_PARAMETER_WITH_TYPE);
        Assertions.assertEquals("queries:\n" +
                "  /**\n" +
                "   * @param $param (TypeOrClass)\n" +
                "   */\n" +
                "  DEFINE QUERY query($param) => '';\n", getFile().getText());
    }

    @Test
    void testAddsJavaDocPersistComment() {
        configureByText("queries:\n" +
                "  /**\n" +
                "   * There's something about query\n" +
                "   */\n" +
                "   DEFINE QUERY query($param) => '';\n");
        invokeQuickFixIntention(ODTCodeUntypedInspectionWarning.ANNOTATE_PARAMETER_WITH_TYPE);
        Assertions.assertEquals("queries:\n" +
                "  /**\n" +
                "   * There's something about query\n" +
                "   * @param $param (TypeOrClass)\n" +
                "   */\n" +
                "  DEFINE QUERY query($param) => '';\n", getFile().getText());
    }

    @Test
    void testAddsJavaDocAppendToExistingBlock() {
        configureByText("queries:\n" +
                "  /**\n" +
                "   * @param $param (TypeOrClass)\n" +
                "   */\n" +
                "  DEFINE QUERY query($param2, $param) => '';\n");
        invokeQuickFixIntention(ODTCodeUntypedInspectionWarning.ANNOTATE_PARAMETER_WITH_TYPE);
        Assertions.assertEquals("queries:\n" +
                "  /**\n" +
                "   * @param $param2 (TypeOrClass)\n" + // <-- ordered according to DefineParam ordering
                "   * @param $param (TypeOrClass)\n" +
                "   */\n" +
                "  DEFINE QUERY query($param2, $param) => '';\n", getFile().getText());
    }

    @Test
    void testHasBaseWarningWhenNotAnnotatedAndStartsWithIndentifierStep() {
        configureByText(insideQueryWithPrefixes("."));
        assertHasWarning(ODTCodeUntypedInspectionWarning.ANNOTATE_BASE_WITH_TYPE);
    }

    @Test
    void testHasNoBaseWarningWhenAnnotatedAndStartsWithIndentifierStep() {
        configureByText(withPrefixes("queries:\n" +
                "  /**\n" +
                "   * @base (string)\n" +
                "   */\n" +
                "  DEFINE QUERY query => ont:booleanPredicate;\n"));
        assertNoWarning(ODTCodeUntypedInspectionWarning.ANNOTATE_BASE_WITH_TYPE);
    }

    @Test
    void testHasNoBaseWarningWhenAnnotatedAndStartsWithIndentifierStepWithCurieType() {
        configureByText(withPrefixes("queries:\n" +
                "  /**\n" +
                "   * @base (ont:ClassA)\n" +
                "   */\n" +
                "  DEFINE QUERY query => ont:booleanPredicate;\n"));
        assertNoWarning(ODTCodeUntypedInspectionWarning.ANNOTATE_BASE_WITH_TYPE);
    }

    @Test
    void testHasBaseWarningWhenNotAnnotatedAndStartsWithCurie() {
        configureByText(insideQueryWithPrefixes("ont:someProperty"));
        assertHasWarning(ODTCodeUntypedInspectionWarning.ANNOTATE_BASE_WITH_TYPE);
    }

    @Test
    void testHasBaseWarningWhenNotAnnotatedAndStartsWithReverseCurie() {
        configureByText(insideQueryWithPrefixes("^ont:someProperty"));
        assertHasWarning(ODTCodeUntypedInspectionWarning.ANNOTATE_BASE_WITH_TYPE);
    }

    @Test
    void testHasNoBaseWarningWhenNotAnnotatedAndStartsWithRootOperator() {
        configureByText(insideQueryWithPrefixes("/ont:ClassA"));
        assertNoWarning(ODTCodeUntypedInspectionWarning.ANNOTATE_BASE_WITH_TYPE);
    }

    @Test
    void testHasNoBaseWarningWhenNotAnnotatedAndStartsWithVariable() {
        configureByText(insideQueryWithPrefixes("$variable"));
        assertNoWarning(ODTCodeUntypedInspectionWarning.ANNOTATE_BASE_WITH_TYPE);
    }

    @Test
    void testHasNoBaseWarningWhenNotAnnotatedAndStartsWithConstant() {
        configureByText(insideQueryWithPrefixes("true"));
        assertNoWarning(ODTCodeUntypedInspectionWarning.ANNOTATE_BASE_WITH_TYPE);
    }

    @Test
    void testHasNoBaseWarningWhenNotAnnotatedAndStartsWithNonStaticResolvableCallable() {
        // no need to annotate with a base, since the outcome of the first step is
        // always a boolean
        configureByText(insideQueryWithPrefixes("EQUALS(1)"));
        assertNoWarning(ODTCodeUntypedInspectionWarning.ANNOTATE_BASE_WITH_TYPE);
    }

    @Test
    void testHasBaseWarningWhenNotAnnotatedAndStartsWithNonStaticUnresolvableCallable() {
        // PICK requires information about the previous step to determine the output type
        configureByText(insideQueryWithPrefixes("PICK(1)"));
        assertHasWarning(ODTCodeUntypedInspectionWarning.ANNOTATE_BASE_WITH_TYPE);
    }

    @Test
    void testHasNoBaseWarningWhenNotAnnotatedAndStartsWithStaticCallable() {
        configureByText(insideQueryWithPrefixes("IIF(true, 'true', 'false')"));
        assertNoWarning(ODTCodeUntypedInspectionWarning.ANNOTATE_BASE_WITH_TYPE);
    }

    @Test
    void testAddsJavaDocForBase() {
        configureByText("queries:\n" +
                "   DEFINE QUERY query => ont:booleanPredicate;\n");
        invokeQuickFixIntention(ODTCodeUntypedInspectionWarning.ANNOTATE_BASE_WITH_TYPE);
        Assertions.assertEquals("queries:\n" +
                "  /**\n" +
                "   * @base (TypeOrClass)\n" +
                "   */\n" +
                "  DEFINE QUERY query => ont:booleanPredicate;\n", getFile().getText());
    }

    @Test
    void testSortsJavaDocForBase() {
        configureByText("queries:\n" +
                "  /**\n" +
                "   * @param $param2 (TypeOrClass)\n" +
                "   * @param $param (TypeOrClass)\n" +
                "   */\n" +
                "  DEFINE QUERY query($param2, $param) => ont:booleanPredicate;\n");
        invokeQuickFixIntention(ODTCodeUntypedInspectionWarning.ANNOTATE_BASE_WITH_TYPE);
        Assertions.assertEquals("queries:\n" +
                "  /**\n" +
                "   * @base (TypeOrClass)\n" +
                "   * @param $param2 (TypeOrClass)\n" +
                "   * @param $param (TypeOrClass)\n" +
                "   */\n" +
                "  DEFINE QUERY query($param2, $param) => ont:booleanPredicate;\n", getFile().getText());
    }
}
