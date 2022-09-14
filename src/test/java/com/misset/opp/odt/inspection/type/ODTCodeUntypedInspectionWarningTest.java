package com.misset.opp.odt.inspection.type;

import com.misset.opp.odt.testcase.ODTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class ODTCodeUntypedInspectionWarningTest extends ODTTestCase {

    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Collections.singleton(ODTCodeUntypedInspectionWarning.class));
    }

    @Test
    void testHasWarningWhenNotAnnotated() {
        configureByText("DEFINE QUERY query($param) => '';\n");
        inspection.assertHasWarning(ODTCodeUntypedInspectionWarning.ANNOTATE_PARAMETER_WITH_TYPE);
    }

    @Test
    void testNoWarningWhenAnnotated() {
        configureByText("/**\n" +
                " * @param $param (string)\n" +
                " */\n" +
                "DEFINE QUERY query($param) => '';\n");
        inspection.assertNoWarning(ODTCodeUntypedInspectionWarning.ANNOTATE_PARAMETER_WITH_TYPE);
    }

    @Test
    void testNoWarningWhenMultipleAnnotated() {
        configureByText("/**\n" +
                " * @param $one (string)\n" +
                " * @param $two (string)\n" +
                " */\n" +
                "DEFINE QUERY query($one, $two) => '';\n");
        inspection.assertNoWarning(ODTCodeUntypedInspectionWarning.ANNOTATE_PARAMETER_WITH_TYPE);
    }

    @Test
    void testNoWarningWhenWithCurie() {
        configureByText(withPrefixes("/**\n" +
                " * @param $one (ont:ClassA)\n" +
                " * @param $two (ont:ClassB)\n" +
                " */\n" +
                "DEFINE QUERY query($one, $two) => '';\n"));
        inspection.assertNoWarning(ODTCodeUntypedInspectionWarning.ANNOTATE_PARAMETER_WITH_TYPE);
    }

    @Test
    void testAddsJavaDoc() {
        configureByText("DEFINE QUERY query($param) => '';\n");
        inspection.invokeQuickFixIntention(ODTCodeUntypedInspectionWarning.ANNOTATE_PARAMETER_WITH_TYPE);
        Assertions.assertEquals("" +
                "/**\n" +
                " * @param $param (TypeOrClass)\n" +
                " */\n" +
                "DEFINE QUERY query($param) => '';\n", getFile().getText());
    }

    @Test
    void testAddsJavaDocForScalarList() {
        configureByText("DEFINE QUERY query($param) => '';\n");
        inspection.invokeQuickFixIntention(ODTCodeUntypedInspectionWarning.ANNOTATE_PARAMETER_WITH_TYPE);
        Assertions.assertEquals("" +
                "/**\n" +
                " * @param $param (TypeOrClass)\n" +
                " */\n" +
                "DEFINE QUERY query($param) => '';\n", getFile().getText());
    }

    @Test
    void testAddsJavaDocForScalarListWithOtherContent() {
        configureByText("" +
                "/**\n" +
                " * @param $param (string)\n" +
                " */\n" +
                "DEFINE QUERY aQuery($param) => '';\n" +
                "DEFINE QUERY query($param) => '';\n"
        );
        inspection.invokeQuickFixIntention(ODTCodeUntypedInspectionWarning.ANNOTATE_PARAMETER_WITH_TYPE);
        Assertions.assertEquals("" +
                "/**\n" +
                " * @param $param (string)\n" +
                " */\n" +
                "DEFINE QUERY aQuery($param) => '';\n" +
                "/**\n" +
                " * @param $param (TypeOrClass)\n" +
                " */\n" +
                "DEFINE QUERY query($param) => '';\n", getFile().getText());
    }

    @Test
    void testAddsJavaDocPersistComment() {
        configureByText("" +
                "/**\n" +
                " * There's something about query\n" +
                " */\n" +
                "DEFINE QUERY query($param) => '';\n");
        inspection.invokeQuickFixIntention(ODTCodeUntypedInspectionWarning.ANNOTATE_PARAMETER_WITH_TYPE);
        Assertions.assertEquals("" +
                "/**\n" +
                " * There's something about query\n" +
                " * @param $param (TypeOrClass)\n" +
                " */\n" +
                "DEFINE QUERY query($param) => '';\n", getFile().getText());
    }

    @Test
    void testAddsJavaDocAppendToExistingBlock() {
        configureByText("" +
                "/**\n" +
                " * @param $param (TypeOrClass)\n" +
                " */\n" +
                "DEFINE QUERY query($param2, $param) => '';\n");
        inspection.invokeQuickFixIntention(ODTCodeUntypedInspectionWarning.ANNOTATE_PARAMETER_WITH_TYPE);
        Assertions.assertEquals("" +
                "/**\n" +
                " * @param $param2 (TypeOrClass)\n" + // <-- ordered according to DefineParam ordering
                " * @param $param (TypeOrClass)\n" +
                " */\n" +
                "DEFINE QUERY query($param2, $param) => '';\n", getFile().getText());
    }

    @Test
    void testHasBaseWarningWhenNotAnnotatedAndStartsWithIndentifierStep() {
        configureByText("DEFINE QUERY query => .");
        inspection.assertHasWarning(ODTCodeUntypedInspectionWarning.ANNOTATE_BASE_WITH_TYPE);
    }

    @Test
    void testHasNoBaseWarningWhenAnnotatedAndStartsWithIndentifierStep() {
        configureByText(withPrefixes("" +
                "/**\n" +
                " * @base (string)\n" +
                " */\n" +
                "DEFINE QUERY query => ont:booleanPredicate;\n"));
        inspection.assertNoWarning(ODTCodeUntypedInspectionWarning.ANNOTATE_BASE_WITH_TYPE);
    }

    @Test
    void testHasNoBaseWarningWhenAnnotatedAndStartsWithIndentifierStepWithCurieType() {
        configureByText(withPrefixes("" +
                "/**\n" +
                " * @base (ont:ClassA)\n" +
                " */\n" +
                "DEFINE QUERY query => ont:booleanPredicate;\n"));
        inspection.assertNoWarning(ODTCodeUntypedInspectionWarning.ANNOTATE_BASE_WITH_TYPE);
    }

    @Test
    void testHasBaseWarningWhenNotAnnotatedAndStartsWithCurie() {
        configureByText(withPrefixes("DEFINE QUERY query => ont:someProperty"));
        inspection.assertHasWarning(ODTCodeUntypedInspectionWarning.ANNOTATE_BASE_WITH_TYPE);
    }

    @Test
    void testHasBaseWarningWhenNotAnnotatedAndStartsWithReverseCurie() {
        configureByText(withPrefixes("DEFINE QUERY query => ^ont:someProperty"));
        inspection.assertHasWarning(ODTCodeUntypedInspectionWarning.ANNOTATE_BASE_WITH_TYPE);
    }

    @Test
    void testHasNoBaseWarningWhenNotAnnotatedAndStartsWithRootOperator() {
        configureByText(withPrefixes("DEFINE QUERY query => /ont:ClassA"));
        inspection.assertNoWarning(ODTCodeUntypedInspectionWarning.ANNOTATE_BASE_WITH_TYPE);
    }

    @Test
    void testHasNoBaseWarningWhenNotAnnotatedAndStartsWithVariable() {
        configureByText(withPrefixes("DEFINE QUERY query => $variable"));
        inspection.assertNoWarning(ODTCodeUntypedInspectionWarning.ANNOTATE_BASE_WITH_TYPE);
    }

    @Test
    void testHasNoBaseWarningWhenNotAnnotatedAndStartsWithConstant() {
        configureByText(withPrefixes("DEFINE QUERY query => true"));
        inspection.assertNoWarning(ODTCodeUntypedInspectionWarning.ANNOTATE_BASE_WITH_TYPE);
    }

    @Test
    void testHasNoBaseWarningWhenNotAnnotatedAndStartsWithNonStaticResolvableCallable() {
        // no need to annotate with a base, since the outcome of the first step is
        // always a boolean
        configureByText(withPrefixes("DEFINE QUERY query => EQUALS(1)"));
        inspection.assertNoWarning(ODTCodeUntypedInspectionWarning.ANNOTATE_BASE_WITH_TYPE);
    }

    @Test
    void testHasBaseWarningWhenNotAnnotatedAndStartsWithNonStaticUnresolvableCallable() {
        // PICK requires information about the previous step to determine the output type
        configureByText(withPrefixes("DEFINE QUERY query => PICK(1)"));
        inspection.assertHasWarning(ODTCodeUntypedInspectionWarning.ANNOTATE_BASE_WITH_TYPE);
    }

    @Test
    void testHasNoBaseWarningWhenNotAnnotatedAndStartsWithStaticCallable() {
        configureByText(withPrefixes("DEFINE QUERY query => IIF(true, 'true', 'false')"));
        inspection.assertNoWarning(ODTCodeUntypedInspectionWarning.ANNOTATE_BASE_WITH_TYPE);
    }

    @Test
    void testAddsJavaDocForBase() {
        configureByText("DEFINE QUERY query => ont:booleanPredicate;\n");
        inspection.invokeQuickFixIntention(ODTCodeUntypedInspectionWarning.ANNOTATE_BASE_WITH_TYPE);
        Assertions.assertEquals("" +
                "/**\n" +
                " * @base (TypeOrClass)\n" +
                " */\n" +
                "DEFINE QUERY query => ont:booleanPredicate;\n", getFile().getText());
    }

    @Test
    void testSortsJavaDocForBase() {
        configureByText("" +
                "/**\n" +
                " * @param $param2 (TypeOrClass)\n" +
                " * @param $param (TypeOrClass)\n" +
                " */\n" +
                "DEFINE QUERY query($param2, $param) => ont:booleanPredicate;\n");
        inspection.invokeQuickFixIntention(ODTCodeUntypedInspectionWarning.ANNOTATE_BASE_WITH_TYPE);
        Assertions.assertEquals("" +
                "/**\n" +
                " * @base (TypeOrClass)\n" +
                " * @param $param2 (TypeOrClass)\n" +
                " * @param $param (TypeOrClass)\n" +
                " */\n" +
                "DEFINE QUERY query($param2, $param) => ont:booleanPredicate;\n", getFile().getText());
    }
}
