package com.misset.opp.omt.meta.scalars.queries;

import com.misset.opp.omt.meta.OMTMetaInjectable;
import com.misset.opp.omt.meta.model.SimpleInjectable;
import com.misset.opp.omt.meta.providers.util.OMTProviderUtil;
import com.misset.opp.resolvable.Resolvable;
import com.misset.opp.resolvable.psi.PsiResolvableQuery;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlScalarType;
import org.jetbrains.yaml.psi.YAMLScalar;

import java.util.Collections;
import java.util.Set;

@SimpleInjectable
public class OMTQueryMetaType extends YamlScalarType implements OMTMetaInjectable {
    public OMTQueryMetaType() {
        super("Query");
    }

    protected Set<OntResource> resolve(@NotNull YAMLScalar scalarValue) {
        return OMTProviderUtil.getInjectedContent(scalarValue, PsiResolvableQuery.class)
                .stream()
                .map(Resolvable::resolve)
                .findFirst()
                .orElse(Collections.emptySet());
    }
}
