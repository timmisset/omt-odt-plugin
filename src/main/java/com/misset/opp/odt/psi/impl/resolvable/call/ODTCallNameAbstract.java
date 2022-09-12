package com.misset.opp.odt.psi.impl.resolvable.call;

import com.intellij.lang.ASTNode;
import com.intellij.lang.documentation.DocumentationMarkup;
import com.intellij.openapi.project.Project;
import com.misset.opp.documentation.DocumentationProvider;
import com.misset.opp.odt.builtin.AbstractBuiltin;
import com.misset.opp.odt.documentation.ODTDocumented;
import com.misset.opp.odt.psi.ODTCallName;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvableAbstract;
import com.misset.opp.odt.psi.resolvable.ODTResolvable;
import com.misset.opp.odt.psi.resolvable.call.ODTCall;
import com.misset.opp.resolvable.Callable;
import com.misset.opp.resolvable.ContextFactory;
import com.misset.opp.ttl.util.TTLResourceUtil;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class ODTCallNameAbstract extends ODTResolvableAbstract implements
        ODTCallName,
        ODTDocumented,
        ODTResolvable {
    protected ODTCallNameAbstract(@NotNull ASTNode node) {
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
        if (callable instanceof AbstractBuiltin) {
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
        sb.append(DocumentationProvider.getKeyValueSection("Type: ", callable.getType().getDescription()));
        List<String> params = new ArrayList<>();
        Map<Integer, String> parameterNames = callable.getParameterNames();
        HashMap<Integer, Set<OntResource>> parameterTypes = callable.getParameterTypes();
        parameterNames.keySet().forEach(
                i -> params.add(
                        callable.maxNumberOfArguments() == -1 && i == parameterNames.size() - 1 ? "..." : "" +
                                parameterNames.get(i) + " (" + TTLResourceUtil.describeUrisForLookupJoined(parameterTypes.get(i)) + ")")
        );
        if (!params.isEmpty()) {
            sb.append(DocumentationProvider.getKeyValueSection("Params: ", String.join("<br>", params)));
        }
        List<String> flags = callable.getFlags();
        if (!flags.isEmpty()) {
            sb.append(DocumentationProvider.getKeyValueSection("Flags: ", String.join("<br>", flags)));
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
            Set<OntResource> resolve = callable.resolve(ContextFactory.fromCall(call));
            sb.append(
                    DocumentationProvider.getKeyValueSection("Returns:",
                            resolve.isEmpty() ? "Unknown" : TTLResourceUtil.describeUrisForLookupJoined(resolve))
            );
            if (!callable.getSecondReturnArgument().isEmpty()) {
                sb.append(
                        DocumentationProvider.getKeyValueSection("Secondary return:",
                                TTLResourceUtil.describeUrisForLookupJoined(callable.getSecondReturnArgument())));
            }
        }
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        return getParent().resolve();
    }
}
