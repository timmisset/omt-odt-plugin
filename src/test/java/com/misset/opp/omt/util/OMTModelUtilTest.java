package com.misset.opp.omt.util;

import com.google.gson.JsonObject;
import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.testCase.OMTTestCase;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLSequenceItem;
import org.jetbrains.yaml.psi.YAMLValue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OMTModelUtilTest extends OMTTestCase {

    @Test
    void getJsonForElement() {
        String content = "model:";
        configureByText(content);
        final JsonObject model = getKeyJson("model");
        Assertions.assertTrue(model.has(OMTModelUtil.TYPE));
    }

    @Test
    void getJsonForActivity() {
        String content = "model:\n" +
                "   Activiteit: !Activity\n";
        configureByText(content);
        final JsonObject model = getKeyJson("Activiteit");
        Assertions.assertEquals("Activity", model.get(OMTModelUtil.NAME).getAsString());
    }

    @Test
    void getJsonForActivityEntry() {
        String content = "model:\n" +
                "   Activiteit: !Activity\n" +
                "       onStart:";
        configureByText(content);
        final JsonObject model = getKeyJson("onStart");
        Assertions.assertTrue(model.has(OMTModelUtil.NODE));
    }

    @Test
    void getJsonForSequenceItem() {
        String content = "model:\n" +
                "   Activiteit: !Activity\n" +
                "       variables:\n" +
                "       - $variable";
        configureByText(content);
        Assertions.assertEquals("Variable",
                getValueJson("$variable").get(OMTModelUtil.NAME)
                        .getAsString());
    }
    @Test
    void getJsonForSequence() {
        String content = "model:\n" +
                "   Activiteit: !Activity\n" +
                "       variables:\n" +
                "       - $variable";
        configureByText(content);
        Assertions.assertEquals("sequence",
                getKeyJson("variables").get(OMTModelUtil.NODE)
                        .getAsString());
    }

    @Test
    void getJsonForSequenceItemEntry() {
        String content = "model:\n" +
                "   Activiteit: !Activity\n" +
                "       variables:\n" +
                "       - name: $variable";
        configureByText(content);
        Assertions.assertTrue(getKeyJson("name").has(OMTModelUtil.NODE));
    }

    private JsonObject getKeyJson(String key) {
        return ReadAction.compute(() -> PsiTreeUtil.findChildrenOfType(getFile(), YAMLKeyValue.class)
                .stream()
                .filter(
                        keyValue -> keyValue.getKeyText()
                                .equals(key))
                .map(OMTModelUtil::getJson)
                .findFirst()
                .orElse(null));
    }
    private JsonObject getValueJson(String value) {
        return ReadAction.compute(() -> PsiTreeUtil.findChildrenOfType(getFile(), YAMLValue.class)
                .stream()
                .filter(
                        keyValue -> keyValue.getText()
                                .equals(value))
                .map(OMTModelUtil::getJson)
                .findFirst()
                .orElse(null));
    }

}
