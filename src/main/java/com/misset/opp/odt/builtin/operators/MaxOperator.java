package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class MaxOperator extends BuiltInCollectionOperator {
    private MaxOperator() { }

    public static final MaxOperator INSTANCE = new MaxOperator();
    private static final List<String> PARAMETER_NAMES = List.of("compareBy");

    @Override
    public String getName() {
        return "MAX";
    }

    @Override
    public int maxNumberOfArguments() {
        return 1;
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        Set<OntClass> acceptableTypes = Set.of(OntologyModelConstants.getXsdNumber(), OntologyModelConstants.getXsdDatetime());
        OntologyValidationUtil.validateHasOntClass(call.resolvePreviousStep(),
                holder,
                call,
                acceptableTypes);
        OntologyValidationUtil.validateHasOntClass(call.resolveSignatureArgument(0),
                holder,
                call.getCallSignatureArgumentElement(0),
                acceptableTypes);
    }

    @Override
    public @Nullable Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        if (index == 0) {
            return Set.of(OntologyModelConstants.getXsdNumberInstance(), OntologyModelConstants.getXsdDatetimeInstance());
        }
        return null;
    }

    @Override
    public Set<OntResource> getAcceptableInputType() {
        return Set.of(OntologyModelConstants.getXsdNumberInstance(), OntologyModelConstants.getXsdDatetimeInstance());
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
