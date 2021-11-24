package com.misset.opp.omt.meta.model.scalars.queries;

import com.misset.opp.callable.PsiResolvable;
import com.misset.opp.callable.Resolvable;
import com.misset.opp.omt.meta.ODTInjectable;
import com.misset.opp.omt.meta.model.ODTSimpleInjectable;
import com.misset.opp.omt.meta.providers.util.OMTProviderUtil;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlScalarType;
import org.jetbrains.yaml.psi.YAMLScalar;

import java.util.Collections;
import java.util.Set;

@ODTSimpleInjectable
public class ODTQueryMetaType extends YamlScalarType implements ODTInjectable {
    public ODTQueryMetaType() {
        super("ODT Query");
    }

    protected Set<OntResource> resolve(@NotNull YAMLScalar scalarValue) {
        return OMTProviderUtil.getInjectedContent(scalarValue, PsiResolvable.class)
                .stream()
                .map(Resolvable::resolve)
                .findFirst()
                .orElse(Collections.emptySet());
    }
}
