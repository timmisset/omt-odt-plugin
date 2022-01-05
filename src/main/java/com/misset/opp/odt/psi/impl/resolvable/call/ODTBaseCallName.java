package com.misset.opp.odt.psi.impl.resolvable.call;

import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.AnnotationHolder;
import com.misset.opp.odt.documentation.ODTDocumented;
import com.misset.opp.odt.psi.ODTCallName;
import com.misset.opp.odt.psi.impl.resolvable.ODTBaseResolvable;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvable;
import com.misset.opp.odt.psi.impl.resolvable.util.ODTResolvableUtil;
import com.misset.opp.resolvable.Callable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

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
    public String getDocumentation() {
        // resolve both the callable information and the call itself:
        ODTCall call = getParent();
        Callable callable = call.getCallable();
        if (callable == null) {
            return null;
        }
        return String.format("<h3>%s description</h3>%s<br><br><h3>Response</h3>" +
                        "The return type(s) for this call are:<br>%s",
                callable.getType(),
                callable.getDescription(null),
                ODTResolvableUtil.getDocumentation(call.resolve()));
    }

    @Override
    public void annotate(AnnotationHolder holder) {
        ODTResolvableUtil.annotateResolved(resolve(), holder, this, false);
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        return getParent().resolve();
    }
}
