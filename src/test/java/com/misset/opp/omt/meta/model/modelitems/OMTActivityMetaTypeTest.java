package com.misset.opp.omt.meta.model.modelitems;

import com.intellij.openapi.application.ReadAction;
import com.misset.opp.model.OntologyModel;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.omt.inspection.structure.OMTValueInspection;
import com.misset.opp.omt.meta.arrays.OMTHandlersArrayMetaType;
import com.misset.opp.omt.testcase.OMTTestCase;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;

class OMTActivityMetaTypeTest extends OMTTestCase {

    @Test
    void testComputeKeyCompletions() {

        String content = "model:\n" +
                "   Activiteit: !Activity\n" +
                "       title: title\n" +
                "       <caret>\n" +
                "\n";
        configureByText(content);
        final List<String> completionList = completion.getLookupStrings();
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
            Assertions.assertTrue(resolve.stream().allMatch(
                    resource -> OntologyModel.getInstance(getProject()).isInstanceOf(resource, OntologyModelConstants.getXsdBoolean())
            ));
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
            Assertions.assertTrue(resolve.stream().anyMatch(OntologyModelConstants.getOwlThingInstance()::equals));
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

    @Test
    void testHasWarningWhenCombiningOverwriteAllWithOtherHandlers() {
        myFixture.enableInspections(Collections.singleton(OMTValueInspection.class));
        String content = "model:\n" +
                "   MyActivity: !Activity\n" +
                "       handlers:\n" +
                "       -  !OverwriteAll {} \n" +
                "       -  !MergePredicates\n" +
                "          subjects: /a:b\n" +
                "          predicates: /a:b\n" +
                "          from: parent\n" +
                "\n";
        configureByText(content);
        inspection.assertHasWarning(OMTHandlersArrayMetaType.OVERWRITE_ALL_SHOULD_NOT_BE_COMBINED_WITH_OTHER_HANDLERS);
    }
}
