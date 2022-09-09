package com.misset.opp.odt.inspection.redundancy;

import com.misset.opp.odt.ODTTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static com.misset.opp.odt.inspection.redundancy.ODTStyleInspectionUnnecessaryWrappingSubquery.UNNECESSARY_WRAPPING_OF_SUBQUERY;

class ODTStyleInspectionUnnecessaryWrappingSubqueryTest extends ODTTestCase {

    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Collections.singleton(ODTStyleInspectionUnnecessaryWrappingSubquery.class));
    }

    @Test
    void testWrappedSubquery() {
        configureByText("('' == '') AND ('' == '')");
        inspection.assertHasWarning(UNNECESSARY_WRAPPING_OF_SUBQUERY);
    }

    @Test
    void testWrappedSubqueryNoWarning() {
        configureByText("('' == '' OR '' == '') AND ('' == '' AND '' == '')");
        inspection.assertNoWarning(UNNECESSARY_WRAPPING_OF_SUBQUERY);
    }

    @Test
    void testDecoratedWrappedSubqueryNoWarning() {
        configureByText("(/ont:ClassA / ^rdfs:subclassOf)*");
        inspection.assertNoWarning(UNNECESSARY_WRAPPING_OF_SUBQUERY);
    }

    @Test
    void testWrappedSubqueryInIFBlock() {
        configureByText("IF ('' == '') { @LOG('test'); }");
        inspection.assertHasWarning(UNNECESSARY_WRAPPING_OF_SUBQUERY);
    }

    @Test
    void testUnwrapSubquery() {
        configureByText("('' == '')");
        inspection.invokeQuickFixIntention("Unwrap");
        assertEquals("'' == ''", getFile().getText());
    }
}
