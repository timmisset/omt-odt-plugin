package com.misset.opp.odt.builtin.commands;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.odt.builtin.ArgumentValidator;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class HttpCallCommandGet extends BuiltInHttpCommand {

    private static final List<String> PARAMETER_NAMES = List.of("url", "queryParams", "throwsOnError");

    private HttpCallCommandGet() {
    }

    public static final HttpCallCommandGet INSTANCE = new HttpCallCommandGet();

    @Override
    public String getName() {
        return "HTTP_GET";
    }

    @Override
    public boolean isVoid() {
        return false;
    }

    @Override
    public int minNumberOfArguments() {
        return 1;
    }

    @Override
    public int maxNumberOfArguments() {
        return 3;
    }

    @Override
    protected void specificValidation(PsiCall call,
                                      ProblemsHolder holder) {
        ArgumentValidator.validateStringArgument(0, call, holder);
        ArgumentValidator.validateJSONArgument(1, call, holder);
        ArgumentValidator.validateBooleanArgument(2, call, holder);
    }

    @Override
    protected @Nullable Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        if (index == 0) {
            return Set.of(OppModelConstants.XSD_STRING_INSTANCE);
        } else if (index == 1) {
            return Set.of(OppModelConstants.JSON_OBJECT);
        } else if (index == 2) {
            return Set.of(OppModelConstants.XSD_BOOLEAN_INSTANCE);
        }
        return null;
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
