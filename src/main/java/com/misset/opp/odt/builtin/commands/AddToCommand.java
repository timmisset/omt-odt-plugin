package com.misset.opp.odt.builtin.commands;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.util.Pair;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Property;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class AddToCommand extends AbstractBuiltInCommand {
    private AddToCommand() {
    }

    public static final AddToCommand INSTANCE = new AddToCommand();
    private static final List<String> PARAMETER_NAMES = List.of("collection", "addition");

    @Override
    public String getName() {
        return "ADD_TO";
    }

    @Override
    public int minNumberOfArguments() {
        return 2;
    }

    @Override
    protected Set<OntResource> resolveFrom(PsiCall call) {
        return combineArgumentResources(call);
    }

    @Override
    @Nullable
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
            TTLValidationUtil.validateCardinalityMultiple(signatureLeadingInformation.getFirst(),
                    signatureLeadingInformation.getSecond(),
                    holder,
                    call.getCallSignatureElement());
        }
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
