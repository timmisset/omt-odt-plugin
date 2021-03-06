package com.misset.opp.omt.psi.impl.yaml;

import com.intellij.openapi.util.TextRange;
import com.misset.opp.omt.meta.OMTMetaInjectable;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.shared.InjectableContentType;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.psi.impl.YAMLScalarImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class YAMLInjectableUtil {

    public static List<TextRange> getTextRanges(YAMLScalarImpl yamlScalar) {
        YamlMetaTypeProvider.MetaTypeProxy metaType = OMTMetaTypeProvider.getInstance(yamlScalar.getProject()).getValueMetaType(yamlScalar);
        return Optional.ofNullable(metaType)
                .map(YamlMetaTypeProvider.MetaTypeProxy::getMetaType)
                .filter(OMTMetaInjectable.class::isInstance)
                .map(OMTMetaInjectable.class::cast)
                .map(omtMetaInjectable -> omtMetaInjectable.getTextRanges(yamlScalar))
                .orElse(Collections.emptyList());
    }

    public static InjectableContentType getContentType(YAMLScalarImpl yamlScalar) {
        YamlMetaTypeProvider.MetaTypeProxy metaType = OMTMetaTypeProvider.getInstance(yamlScalar.getProject()).getValueMetaType(yamlScalar);
        return Optional.ofNullable(metaType)
                .map(YamlMetaTypeProvider.MetaTypeProxy::getMetaType)
                .filter(OMTMetaInjectable.class::isInstance)
                .map(OMTMetaInjectable.class::cast)
                .map(OMTMetaInjectable::getInjectableContentType)
                .orElse(InjectableContentType.None);
    }

}
