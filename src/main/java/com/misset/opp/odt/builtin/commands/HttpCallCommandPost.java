package com.misset.opp.odt.builtin.commands;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class HttpCallCommandPost extends BuiltInHttpCommand {
    private HttpCallCommandPost() {
    }

    public static final HttpCallCommandPost INSTANCE = new HttpCallCommandPost();

    @Override
    public String getName() {
        return "HTTP_POST";
    }

    @Override
    public boolean isVoid() {
        return false;
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
        validateStringArgument(0, call, holder);
        validateJSONArgument(1, call, holder);
        validateBooleanArgument(2, call, holder);
        validateJSONArgument(3, call, holder);
    }

    @Override
    protected @Nullable Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        if (index == 0) {
            return Set.of(OppModelConstants.XSD_STRING_INSTANCE);
        } else if (index == 1) {
            return Set.of(OppModelConstants.JSON_OBJECT);
        } else if (index == 2) {
            return Set.of(OppModelConstants.XSD_BOOLEAN_INSTANCE);
        } else if (index == 3) {
            return Set.of(OppModelConstants.JSON_OBJECT);
        }
        return null;
    }
}
