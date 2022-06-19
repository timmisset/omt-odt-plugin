package com.misset.opp.omt.meta.model;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.misset.opp.omt.meta.OMTMetaMapType;
import com.misset.opp.omt.meta.model.modelitems.OMTModelItemMetaType;
import com.misset.opp.omt.meta.providers.OMTCallableProvider;
import com.misset.opp.resolvable.psi.PsiCallable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.misset.opp.omt.meta.providers.util.OMTCallableProviderUtil.addModelItemsToMap;

public class OMTModelMetaType extends OMTMetaMapType implements OMTCallableProvider {

    private static final OMTModelMetaType INSTANCE = new OMTModelMetaType();

    public static OMTModelMetaType getInstance() {
        return INSTANCE;
    }

    private OMTModelMetaType() {
        super("Model");
    }

    @Override
    protected YamlMetaType getMapEntryType(String name) {
        return OMTModelItemMetaType.getInstance();
    }

    @Override
    public @NotNull HashMap<String, List<PsiCallable>> getCallableMap(YAMLMapping yamlMapping,
                                                                      PsiLanguageInjectionHost host) {
        final HashMap<String, List<PsiCallable>> map = new HashMap<>();
        addModelItemsToMap(yamlMapping, map);
        return map;
    }

    @Override
    public void validateValue(@NotNull YAMLValue value, @NotNull ProblemsHolder problemsHolder) {
        if (!(value instanceof YAMLMapping)) {
            return;
        }
        YAMLMapping mapping = (YAMLMapping) value;
        ArrayList<YAMLKeyValue> yamlKeyValues = new ArrayList<>(mapping.getKeyValues());
        for (YAMLKeyValue keyValue : yamlKeyValues) {
            YAMLValue yamlValue = keyValue.getValue();
            if (yamlValue == null) {
                problemsHolder.registerProblem(keyValue, "Empty model item", ProblemHighlightType.ERROR);
            } else {
                PsiElement tag = yamlValue.getTag();
                if (tag == null) {
                    problemsHolder.registerProblem(keyValue, "Model items require a tag identifier", ProblemHighlightType.ERROR);
                } else {
                    if (!OMTModelItemMetaType.getTypes().contains(tag.getText())) {
                        problemsHolder.registerProblem(tag, "Unknown tag", ProblemHighlightType.ERROR);
                    }
                }
            }
        }
    }
}
