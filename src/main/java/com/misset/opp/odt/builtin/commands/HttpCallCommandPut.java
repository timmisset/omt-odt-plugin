package com.misset.opp.odt.builtin.commands;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.ArgumentValidator;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class HttpCallCommandPut extends BuiltInHttpCommand {

    private static final List<String> PARAMETER_NAMES = List.of("url", "body", "queryParams", "throwsOnError");

    private HttpCallCommandPut() {
    }

    public static final HttpCallCommandPut INSTANCE = new HttpCallCommandPut();

    @Override
    public String getName() {
        return "HTTP_PUT";
    }

    @Override
    public int minNumberOfArguments() {
        return 2;
    }

    @Override
    public int maxNumberOfArguments() {
        return 4;
    }

    @Override
    protected void specificValidation(PsiCall call,
                                      ProblemsHolder holder) {
        ArgumentValidator.validateStringArgument(0, call, holder);
        ArgumentValidator.validateJSONArgument(1, call, holder);
        ArgumentValidator.validateBooleanArgument(2, call, holder);
        ArgumentValidator.validateJSONArgument(3, call, holder);
    }

    @Override
    protected @Nullable Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        if (index == 0) {
            return Set.of(OntologyModelConstants.getXsdStringInstance());
        } else if (index == 1) {
            return Set.of(OntologyModelConstants.getJsonObject());
        } else if (index == 2) {
            return Set.of(OntologyModelConstants.getXsdBooleanInstance());
        } else if (index == 3) {
            return Set.of(OntologyModelConstants.getJsonObject());
        }
        return null;
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
