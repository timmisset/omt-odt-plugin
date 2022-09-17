package com.misset.opp.odt.psi.impl.resolvable.querystep;

import com.intellij.lang.ASTNode;
import com.intellij.lang.documentation.DocumentationMarkup;
import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.IElementType;
import com.misset.opp.model.OntologyModel;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyResourceUtil;
import com.misset.opp.model.util.OntologyValueParserUtil;
import com.misset.opp.odt.psi.ODTConstantValue;
import com.misset.opp.odt.psi.ODTTypes;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

public abstract class ODTResolvableConstantValueStepAbstract extends ODTResolvableQueryStepAbstract implements ODTConstantValue {
    protected ODTResolvableConstantValueStepAbstract(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        final IElementType elementType = getNode().getFirstChildNode().getElementType();

        final OntResource result;
        if (elementType == ODTTypes.BOOLEAN) {
            result = OntologyModelConstants.getXsdBooleanInstance();
        } else if (elementType == ODTTypes.STRING) {
            result = OntologyModelConstants.getXsdStringInstance();
        } else if (elementType == ODTTypes.INTEGER) {
            result = OntologyModelConstants.getXsdIntegerInstance();
        } else if (elementType == ODTTypes.DECIMAL) {
            result = OntologyModelConstants.getXsdDecimalInstance();
        } else if (elementType == ODTTypes.INTERPOLATED_STRING) {
            result = OntologyModelConstants.getXsdStringInstance();
        } else if (elementType == ODTTypes.NULL) {
            result = null; // returns an emptySet which is the ODT equivalent of null
        } else if (elementType == ODTTypes.PRIMITIVE) {
            final Individual individual = OntologyValueParserUtil.parsePrimitive(getText());
            result = individual != null ? OntologyModel.getInstance().toClass(individual) : null;
        } else {
            result = OntologyModelConstants.getOwlThingInstance();
        }

        // a constant value, like a string, boolean, number etc is considered
        // an instance of the specified data-type
        return result == null ? Collections.emptySet() : Set.of(result);
    }

    @Override
    public String getDocumentation(Project project) {
        StringBuilder sb = new StringBuilder();
        sb.append(DocumentationMarkup.DEFINITION_START);
        sb.append("Constant<br>");
        sb.append(OntologyResourceUtil.describeUrisJoined(resolve(), "<br>", false));
        sb.append(DocumentationMarkup.DEFINITION_END);
        return sb.toString();
    }
}
