package com.misset.opp.omt.util;

import com.google.gson.JsonObject;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.omt.psi.OMTVariable;
import com.misset.opp.omt.psi.model.modelitems.OMTModelItem;
import com.misset.opp.omt.psi.wrapper.OMTModelItemFactory;
import com.misset.opp.util.resources.JsonModelLoader;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.misset.opp.omt.util.OMTModelUtil.NAME;

public class OMTVariableUtil {

    /**
     * Upward tree-walking, gathering all variables defined in OMT entries
     *
     * @return
     */
    public static List<OMTVariable> getAccessibleVariables(YAMLValue value) {
        List<OMTVariable> variables = new ArrayList<>();
        YAMLKeyValue keyValue = PsiTreeUtil.getParentOfType(value, YAMLKeyValue.class);
        while (keyValue != null) {
            final JsonObject json = OMTModelUtil.getJson(keyValue);
            if (JsonModelLoader.hasValue(json, NAME, "ModelItem")) {
                // a model item key:value pair, parse accordingly:
                final OMTModelItem modelItem = OMTModelItemFactory.fromKeyValue(keyValue);
                final List<OMTVariable> variableList = modelItem.getDeclaredVariables()
                        .stream()
                        .filter(newVariable -> isNewVariable(variables, newVariable))
                        .collect(Collectors.toList());
                variables.addAll(variableList);
            }
            keyValue = PsiTreeUtil.getParentOfType(keyValue, YAMLKeyValue.class);
        }
        return variables;
    }

    private static boolean isNewVariable(List<OMTVariable> variables,
                                  OMTVariable newVariable) {
        return variables.stream()
                .noneMatch(
                        existingVariable -> newVariable.getName() != null && newVariable.getName()
                                .equals(existingVariable.getName()));
    }

}
