package com.misset.opp.omt.meta.providers.util;

import com.intellij.psi.PsiElement;
import com.misset.opp.odt.psi.impl.callables.ODTDefineStatement;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.HashMap;
import java.util.List;

import static com.misset.opp.util.CollectionUtil.addToGroupedMap;

/**
 * Util class for classes implementing OMTCallableProvider
 */
public class OMTCallableProviderUtil extends OMTProviderUtil {

    public static void addDefinedStatementsToMap(YAMLMapping mapping,
                                                 String key,
                                                 HashMap<String, List<PsiElement>> map) {
        final YAMLKeyValue callables = mapping.getKeyValueByKey(key);
        if (callables == null || callables.getValue() == null) {
            return;
        }
        getInjectedContent(callables.getValue(), ODTDefineStatement.class)
                .forEach(odtDefineStatement -> addToGroupedMap(odtDefineStatement.getName(), odtDefineStatement.getDefineName(), map));
    }

}
