package com.misset.opp.omt.meta.scalars.queries;

import com.misset.opp.odt.psi.ODTQuery;
import com.misset.opp.omt.injection.OMTODTInjectionUtil;
import com.misset.opp.omt.meta.OMTMetaInjectable;
import com.misset.opp.omt.meta.model.SimpleInjectable;
import com.misset.opp.resolvable.Resolvable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlScalarType;
import org.jetbrains.yaml.psi.YAMLScalar;

import java.util.Collections;
import java.util.Set;

@SimpleInjectable
public class OMTQueryMetaType extends YamlScalarType implements OMTMetaInjectable {
    private static final OMTQueryMetaType INSTANCE = new OMTQueryMetaType();

    public static OMTQueryMetaType getInstance() {
        return INSTANCE;
    }

    protected OMTQueryMetaType() {
        super("Query");
    }

    protected Set<OntResource> resolve(@NotNull YAMLScalar scalarValue) {
        return OMTODTInjectionUtil.getInjectedContent(scalarValue, ODTQuery.class)
                .stream()
                .map(Resolvable::resolve)
                .findFirst()
                .orElse(Collections.emptySet());
    }
}
