package com.misset.opp.indexing;

import com.misset.opp.testCase.OMTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class OMTPrefixIndexTest extends OMTTestCase {

    @Test
    void testRegistersPrefixes() {
        configureByText(insideActivityWithPrefixes(""));
        final List<String> xsd = PrefixIndex.getNamespaces("xsd");
        assertNotEmpty(xsd);
    }

    @Test
    void testOrdersByFrequency() {
        configureByText("prefixes:\n" +
                "   prefix: <http://b>\n" +
                "   prefix: <http://a>\n" +
                "   prefix: <http://a>\n"
        );
        final List<String> prefixes = PrefixIndex.getNamespaces("prefix");
        Assertions.assertEquals(2, prefixes.size());
        Assertions.assertEquals("http://a", prefixes.get(0));
        Assertions.assertEquals("http://b", prefixes.get(1));
    }

}
