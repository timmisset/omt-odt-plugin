package com.misset.opp.odt.builtin.commands;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class AssertCommand extends AbstractBuiltInCommand {

    public static final List<String> PARAMETER_NAMES = List.of("assertion");

    private AssertCommand() {
    }

    public static final AssertCommand INSTANCE = new AssertCommand();

    @Override
    public String getName() {
        return "ASSERT";
    }

    @Override
    public int minNumberOfArguments() {
        return 1;
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }

    @Override
    protected Set<OntResource> resolveFrom(PsiCall call) {
        return Collections.singleton(OntologyModelConstants.getXsdBooleanInstance());
    }
}
