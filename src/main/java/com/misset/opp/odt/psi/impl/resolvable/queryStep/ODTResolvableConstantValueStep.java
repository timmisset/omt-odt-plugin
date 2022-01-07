package com.misset.opp.odt.psi.impl.resolvable.queryStep;

import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.documentation.DocumentationMarkup;
import com.intellij.psi.tree.IElementType;
import com.misset.opp.odt.psi.ODTConstantValue;
import com.misset.opp.odt.psi.ODTTypes;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.util.TTLResourceUtil;
import com.misset.opp.ttl.util.TTLValueParserUtil;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

public abstract class ODTResolvableConstantValueStep extends ODTResolvableQueryStepBase implements ODTConstantValue {
    public ODTResolvableConstantValueStep(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        final OppModel oppModel = OppModel.INSTANCE;
        final IElementType elementType = getNode().getFirstChildNode().getElementType();

        final OntResource result;
        if (elementType == ODTTypes.BOOLEAN) {
            result = oppModel.XSD_BOOLEAN_INSTANCE;
        } else if (elementType == ODTTypes.STRING) {
            result = oppModel.XSD_STRING_INSTANCE;
        } else if (elementType == ODTTypes.INTEGER) {
            result = oppModel.XSD_INTEGER_INSTANCE;
        } else if (elementType == ODTTypes.DECIMAL) {
            result = oppModel.XSD_DECIMAL_INSTANCE;
        } else if (elementType == ODTTypes.INTERPOLATED_STRING) {
            result = oppModel.XSD_STRING_INSTANCE;
        } else if (elementType == ODTTypes.NULL) {
            result = null; // returns an emptySet which is the ODT equivalent of null
        } else if (elementType == ODTTypes.PRIMITIVE) {
            final Individual individual = TTLValueParserUtil.parsePrimitive(getText());
            result = individual != null ? individual.getOntClass() : null;
        } else {
            result = oppModel.OWL_THING_INSTANCE;
        }

        // a constant value, like a string, boolean, number etc is considered
        // an instance of the specified data-type
        return result == null ? Collections.emptySet() : Set.of(result);
    }

    @Override
    public String getDocumentation() {
        StringBuilder sb = new StringBuilder();
        sb.append(DocumentationMarkup.DEFINITION_START);
        sb.append("Constant<br>");
        sb.append(TTLResourceUtil.describeUrisJoined(resolve(), "<br>", false));
        sb.append(DocumentationMarkup.DEFINITION_END);
        return sb.toString();
    }

    @Override
    protected boolean applyTextAttributes() {
        return false;
    }

    @Override
    public void annotate(AnnotationHolder holder) {
        if (getNode().getFirstChildNode().getElementType() == ODTTypes.INTERPOLATED_STRING) {
            // don't annotate the interpolated string, it probably contains child elements
            // that should be annotated instead
            return;
        }
        super.annotate(holder);
    }
}
