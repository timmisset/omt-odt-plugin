package com.misset.opp.omt.meta.model.scalars.queries;

import com.misset.opp.callable.Resolvable;
import com.misset.opp.callable.psi.PsiResolvable;
import com.misset.opp.omt.meta.OMTInjectable;
import com.misset.opp.omt.meta.model.SimpleInjectable;
import com.misset.opp.omt.meta.providers.util.OMTProviderUtil;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlScalarType;
import org.jetbrains.yaml.psi.YAMLScalar;

import java.util.Collections;
import java.util.Set;

@SimpleInjectable
public class OMTQueryMetaType extends YamlScalarType implements OMTInjectable {
    public OMTQueryMetaType() {
        super("Query");
    }

    protected Set<OntResource> resolve(@NotNull YAMLScalar scalarValue) {
        return OMTProviderUtil.getInjectedContent(scalarValue, PsiResolvable.class)
                .stream()
                .map(Resolvable::resolve)
                .findFirst()
                .orElse(Collections.emptySet());
    }
}