package com.misset.opp.omt.indexing;

import com.misset.opp.testCase.OMTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class OMTPrefixIndexTest extends OMTTestCase {

    @Test
    void testRegistersPrefixes() {
        configureByText(insideActivityWithPrefixes(""));
        OMTPrefixIndex.getIndexTask(getProject()).run(null);
        final List<String> xsd = OMTPrefixIndex.getNamespaces("xsd");
        assertNotEmpty(xsd);
    }

    @Test
    void testOrdersByFrequency() {
        configureByText("prefixes:\n" +
                "   prefix: <http://b>\n" +
                "   prefix: <http://a>\n" +
                "   prefix: <http://a>\n"
        );
        OMTPrefixIndex.getIndexTask(getProject()).run(null);
        final List<String> prefixes = OMTPrefixIndex.getNamespaces("prefix");
        Assertions.assertEquals(2, prefixes.size());
        Assertions.assertEquals("http://a", prefixes.get(0));
        Assertions.assertEquals("http://b", prefixes.get(1));
    }

}
