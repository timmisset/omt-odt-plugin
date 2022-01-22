package com.misset.opp.omt.meta.model.modelitems;

import com.intellij.openapi.application.ReadAction;
import com.misset.opp.testCase.OMTCompletionTestCase;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

class OMTActivityMetaTypeTest extends OMTCompletionTestCase {

    @Test
    void testComputeKeyCompletions() {

        String content = "model:\n" +
                "   Activiteit: !Activity\n" +
                "       title: title\n" +
                "       <caret>\n" +
                "\n";
        configureByText(content);
        final List<String> completionList = getLookupStrings();
        new OMTActivityMetaType().getFeatures()
                .keySet()
                .stream()
                .filter(s -> !s.equals("title"))
                .forEach(s -> Assertions.assertTrue(completionList.contains(s)));
    }


    @Test
    void testWithReturnValueReturnsIfResolvable() {

        String content = "model:\n" +
                "   <caret>Activiteit: !Activity\n" +
                "       returns: true\n" +
                "\n";
        configureByText(content);
        ReadAction.run(() -> {
            YAMLMapping value = (YAMLMapping) ((YAMLKeyValue) myFixture.getElementAtCaret()).getValue();
            Set<OntResource> resolve = new OMTActivityMetaType().resolve(value, null);
            Assertions.assertTrue(resolve.stream().anyMatch(OppModel.INSTANCE.XSD_BOOLEAN_INSTANCE::equals));
        });
    }

    @Test
    void testWithReturnValueReturnsOwlThingIfNotResolvable() {

        String content = "model:\n" +
                "   <caret>Activiteit: !Activity\n" +
                "       returns: $test\n" +
                "\n";
        configureByText(content);
        ReadAction.run(() -> {
            YAMLMapping value = (YAMLMapping) ((YAMLKeyValue) myFixture.getElementAtCaret()).getValue();
            Set<OntResource> resolve = new OMTActivityMetaType().resolve(value, null);
            Assertions.assertTrue(resolve.stream().anyMatch(OppModel.INSTANCE.OWL_THING_INSTANCE::equals));
        });
    }

    @Test
    void testWithoutReturnValueReturnsEmptyCollection() {

        String content = "model:\n" +
                "   <caret>Activiteit: !Activity\n" +
                "       onStart: $test\n" +
                "\n";
        configureByText(content);
        ReadAction.run(() -> {
            YAMLMapping value = (YAMLMapping) ((YAMLKeyValue) myFixture.getElementAtCaret()).getValue();
            Set<OntResource> resolve = new OMTActivityMetaType().resolve(value, null);
            Assertions.assertTrue(resolve.isEmpty());
        });
    }
}
