package com.misset.opp.odt.builtin.commands;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.util.Pair;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Property;

import java.util.Set;

public class RemoveFromCommand extends BuiltInCommand {
    private RemoveFromCommand() { }
    public static final RemoveFromCommand INSTANCE = new RemoveFromCommand();

    @Override
    public String getName() {
        return "REMOVE_FROM";
    }

    @Override
    public int minNumberOfArguments() {
        return 2;
    }

    @Override
    protected Set<OntResource> resolveFrom(PsiCall call) {
        return call.resolveSignatureArgument(0);
    }

    @Override
    public void specificValidation(PsiCall call,
                                   ProblemsHolder holder) {
        // type compatibility is not really an issue when removing an item

        // validate modality
        final Pair<Set<OntResource>, Property> signatureLeadingInformation = call.getSignatureLeadingInformation(0);
        if (call.getCallSignatureElement() != null && signatureLeadingInformation != null) {
            TTLValidationUtil.validateCardinalityMultiple(signatureLeadingInformation.getFirst(),
                    signatureLeadingInformation.getSecond(),
                    holder,
                    call.getCallSignatureElement());
        }
    }
}
