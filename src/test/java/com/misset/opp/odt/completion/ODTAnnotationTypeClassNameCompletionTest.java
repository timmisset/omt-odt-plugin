package com.misset.opp.odt.completion;

import com.misset.opp.odt.testcase.ODTTestCase;
import org.junit.jupiter.api.Test;

import java.util.List;

class ODTAnnotationTypeClassNameCompletionTest extends ODTTestCase {

    @Test
    void testShowsCompletionForClassesInAnnotation() {
        String content = withPrefixes("/**\n" +
                " * @param $param (<caret>classOrType)\n" +
                " */\n" +
                "DEFINE QUERY query($param) => true");
        configureByText(content);
        List<String> lookupStrings = completion.getLookupStrings();
        assertContainsElements(lookupStrings, "ont:ClassA", "ont:ClassB");
    }

    @Test
    void testShowsNoCompletionForClassesInAnnotationOutsideParenthesis() {
        String content = withPrefixes("/**\n" +
                " * @param $param <caret>(classOrType)\n" +
                " */\n" +
                "DEFINE QUERY query($param) => true");
        configureByText(content);
        List<String> lookupStrings = completion.getLookupStrings();
        assertDoesntContain(lookupStrings, "ont:ClassA", "ont:ClassB");
    }

}
