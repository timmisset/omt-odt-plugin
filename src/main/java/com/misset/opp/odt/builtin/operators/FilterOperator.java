package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.odt.psi.ODTResolvableValue;
import com.misset.opp.odt.psi.ODTSignatureArgument;
import com.misset.opp.odt.psi.impl.resolvable.query.ODTResolvableQuery;
import com.misset.opp.resolvable.Resolvable;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class FilterOperator extends BuiltInCollectionOperator {
    // open issue: https://github.com/timmisset/omt-odt-plugin/issues/125
    private FilterOperator() {
    }

    public static final FilterOperator INSTANCE = new FilterOperator();
    private static final List<String> PARAMETER_NAMES = List.of("include");

    @Override
    public String getName() {
        return "FILTER";
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        Set<OntClass> acceptableTypes = Set.of(OppModelConstants.getXsdBoolean(), OppModelConstants.getXsdInteger());
        TTLValidationUtil.validateHasOntClass(call.resolveSignatureArgument(0), holder, call, acceptableTypes);
    }

    @Override
    public @Nullable Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        return Set.of(OppModelConstants.getXsdBooleanInstance(), OppModelConstants.getXsdIntegerInstance());
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }

    @Override
    protected Set<OntResource> resolveFrom(Set<OntResource> resources, PsiCall call) {
        return Optional.ofNullable(call.getCallSignatureArgumentElement(0))
                .filter(ODTSignatureArgument.class::isInstance)
                .map(ODTSignatureArgument.class::cast)
                .map(ODTSignatureArgument::getResolvableValue)
                .map(ODTResolvableValue::getQuery)
                .filter(ODTResolvableQuery.class::isInstance)
                .map(ODTResolvableQuery.class::cast)
                .filter(Resolvable::isBoolean)
                .map(query -> query.filter(resources))
                .orElse(resources);
    }
}
