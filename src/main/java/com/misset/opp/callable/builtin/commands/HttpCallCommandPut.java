package com.misset.opp.callable.builtin.commands;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.Call;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class HttpCallCommandPut extends BuiltInHttpCommand {
    private HttpCallCommandPut() {
    }

    public static final HttpCallCommandPut INSTANCE = new HttpCallCommandPut();

    @Override
    public String getName() {
        return "HTTP_PUT";
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
    protected @Nullable Set<OntResource> getAcceptableArgumentTypeWithContext(int index, Call call) {
        if (index == 0) {
            return Set.of(OppModel.INSTANCE.XSD_STRING_INSTANCE);
        } else if (index == 1) {
            return Set.of(OppModel.INSTANCE.JSON_OBJECT);
        } else if (index == 2) {
            return Set.of(OppModel.INSTANCE.XSD_BOOLEAN_INSTANCE);
        } else if (index == 3) {
            return Set.of(OppModel.INSTANCE.JSON_OBJECT);
        }
        return null;
    }
}
