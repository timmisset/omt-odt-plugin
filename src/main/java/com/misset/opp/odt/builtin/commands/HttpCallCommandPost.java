package com.misset.opp.odt.builtin.commands;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.odt.builtin.ArgumentValidator;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class HttpCallCommandPost extends BuiltInHttpCommand {

    private static final List<String> PARAMETER_NAMES = List.of("url", "body", "queryParams", "throwsOnError");

    private HttpCallCommandPost() {
    }

    public static final HttpCallCommandPost INSTANCE = new HttpCallCommandPost();

    @Override
    public String getName() {
        return "HTTP_POST";
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
            return Set.of(OppModelConstants.getXsdStringInstance());
        } else if (index == 1) {
            return Set.of(OppModelConstants.getJsonObject());
        } else if (index == 2) {
            return Set.of(OppModelConstants.getXsdBooleanInstance());
        } else if (index == 3) {
            return Set.of(OppModelConstants.getJsonObject());
        }
        return null;
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
