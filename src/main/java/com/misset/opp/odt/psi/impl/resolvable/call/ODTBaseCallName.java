package com.misset.opp.odt.psi.impl.resolvable.call;

import com.intellij.lang.ASTNode;
import com.intellij.lang.documentation.DocumentationMarkup;
import com.intellij.openapi.project.Project;
import com.misset.opp.documentation.DocumentationProvider;
import com.misset.opp.odt.builtin.Builtin;
import com.misset.opp.odt.documentation.ODTDocumented;
import com.misset.opp.odt.psi.ODTCallName;
import com.misset.opp.odt.psi.impl.resolvable.ODTBaseResolvable;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvable;
import com.misset.opp.resolvable.Callable;
import com.misset.opp.resolvable.Context;
import com.misset.opp.ttl.util.TTLResourceUtil;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class ODTBaseCallName extends ODTBaseResolvable implements
        ODTCallName,
        ODTDocumented,
        ODTResolvable {
    public ODTBaseCallName(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    @NotNull
    public ODTCall getParent() {
        return (ODTCall) super.getParent();
    }

    @Override
    public String getDocumentation(Project project) {
        // resolve both the callable information and the call itself:
        ODTCall call = getParent();
        Callable callable = call.getCallable();
        if (callable == null) {
            return null;
        }
        if (callable instanceof Builtin) {
            return getBuiltinDocumentation(project, callable, call);
        }

        StringBuilder sb = new StringBuilder();
        sb.append(DocumentationMarkup.DEFINITION_START);
        sb.append(callable.getName());
        sb.append(DocumentationMarkup.DEFINITION_END);

        String description = callable.getDescription(project);
        if (description != null) {
            sb.append(DocumentationMarkup.CONTENT_START);
            sb.append(description);
            sb.append(DocumentationMarkup.CONTENT_END);
        }

        sb.append(DocumentationMarkup.SECTIONS_START);
        DocumentationProvider.addKeyValueSection("Type: ", callable.getType(), sb);
        List<String> params = new ArrayList<>();
        Map<Integer, String> parameterNames = callable.getParameterNames();
        HashMap<Integer, Set<OntResource>> parameterTypes = callable.getParameterTypes();
        parameterNames.keySet().forEach(
                i -> params.add(
                        callable.maxNumberOfArguments() == -1 && i == parameterNames.size() - 1 ? "..." : "" +
                                parameterNames.get(i) + " (" + TTLResourceUtil.describeUrisForLookupJoined(parameterTypes.get(i)) + ")")
        );
        if (!params.isEmpty()) {
            DocumentationProvider.addKeyValueSection("Params: ", String.join("<br>", params), sb);
        }
        List<String> flags = callable.getFlags();
        if (!flags.isEmpty()) {
            DocumentationProvider.addKeyValueSection("Flags: ", String.join("<br>", flags), sb);
        }

        appendReturnInformation(callable, call, sb);

        sb.append(DocumentationMarkup.SECTIONS_END);

        return sb.toString();
    }

    private String getBuiltinDocumentation(Project project, Callable callable, ODTCall call) {
        // static documentation from the API.md
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(callable.getDescription(project));
        appendReturnInformation(callable, call, stringBuilder);
        return stringBuilder.toString();
    }

    private void appendReturnInformation(Callable callable, ODTCall call, StringBuilder sb) {
        if (!callable.isVoid()) {
            Set<OntResource> resolve = callable.resolve(Context.fromCall(call));
            DocumentationProvider.addKeyValueSection("Returns:", resolve.isEmpty() ? "Unknown" : TTLResourceUtil.describeUrisForLookupJoined(resolve), sb);
            if (!callable.getSecondReturnArgument().isEmpty()) {
                DocumentationProvider.addKeyValueSection("Secondary return:", TTLResourceUtil.describeUrisForLookupJoined(callable.getSecondReturnArgument()), sb);
            }
        }
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        return getParent().resolve();
    }
}
