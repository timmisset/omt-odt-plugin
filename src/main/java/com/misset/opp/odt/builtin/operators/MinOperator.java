package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntResource;

import java.util.List;
import java.util.Set;

public class MinOperator extends BuiltInCollectionOperator {
    private MinOperator() { }

    public static final MinOperator INSTANCE = new MinOperator();
    private static final List<String> PARAMETER_NAMES = List.of("compareBy");

    @Override
    public String getName() {
        return "MIN";
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
        Set<OntClass> acceptableTypes = Set.of(OppModelConstants.XSD_NUMBER, OppModelConstants.XSD_DATETIME);
        TTLValidationUtil.validateHasOntClass(call.resolvePreviousStep(),
                holder,
                call,
                acceptableTypes);
        TTLValidationUtil.validateHasOntClass(call.resolveSignatureArgument(0),
                holder,
                call.getCallSignatureArgumentElement(0),
                acceptableTypes);
    }


    @Override
    public Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        if (index == 0) {
            return Set.of(OppModelConstants.XSD_NUMBER_INSTANCE, OppModelConstants.XSD_DATETIME_INSTANCE);
        }
        return null;
    }

    @Override
    public Set<OntResource> getAcceptableInputType() {
        return Set.of(OppModelConstants.XSD_NUMBER_INSTANCE, OppModelConstants.XSD_DATETIME_INSTANCE);
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
