package com.misset.opp.callable.builtin.commands;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.util.Pair;
import com.misset.opp.callable.Call;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Property;

import java.util.HashMap;
import java.util.Set;

public class AddToCommand extends BuiltInCommand {
    private AddToCommand() {
    }

    public static final AddToCommand INSTANCE = new AddToCommand();

    @Override
    public String getName() {
        return "ADD_TO";
    }

    @Override
    public int minNumberOfArguments() {
        return 2;
    }

    @Override
    protected Set<OntResource> resolveFrom(Call call) {
        return combineArgumentResources(call);
    }

    @Override
    public HashMap<Integer, Set<OntResource>> getParameterTypes() {
        return super.getParameterTypes();
    }

    @Override
    public Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        if (index == 1) {
            return call.resolveSignatureArgument(0);
        }
        return null;
    }

    @Override
    public void specificValidation(PsiCall call,
                                   ProblemsHolder holder) {
        // validate type compatibility
        TTLValidationUtil.validateCompatibleTypes(call.resolveSignatureArgument(0),
                call.resolveSignatureArgument(1),
                holder,
                call.getCallSignatureElement()
        );

        // validate modality
        final Pair<Set<OntResource>, Property> signatureLeadingInformation = call.getSignatureLeadingInformation(0);
        if (call.getCallSignatureElement() != null && signatureLeadingInformation != null) {
            TTLValidationUtil.validateModularityMultiple(signatureLeadingInformation.getFirst(),
                    signatureLeadingInformation.getSecond(),
                    holder,
                    call.getCallSignatureElement());
        }
    }
}
