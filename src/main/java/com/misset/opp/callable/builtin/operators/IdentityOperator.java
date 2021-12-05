package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class IdentityOperator extends BuiltInOperator {
    private IdentityOperator() { }
    public static final IdentityOperator INSTANCE = new IdentityOperator();

    @Override
    public String getName() {
        return "IDENTITY";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    protected String getShorthandSyntax() {
        return ".";
    }

    @Override
    protected Set<OntResource> resolveFrom(Set<OntResource> resources) {
        return resources;
    }
}
