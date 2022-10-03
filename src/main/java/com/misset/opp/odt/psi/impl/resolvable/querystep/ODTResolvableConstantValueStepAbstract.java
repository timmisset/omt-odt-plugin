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
import com.misset.opp.odt.psi.ODTInterpolatedString;
import com.misset.opp.odt.psi.ODTTypes;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Literal;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public abstract class ODTResolvableConstantValueStepAbstract extends ODTResolvableQueryStepAbstract implements ODTConstantValue {

    private static final Set<String> TRUES = Set.of("true", "True", "TRUE");

    protected ODTResolvableConstantValueStepAbstract(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        final IElementType elementType = getNode().getFirstChildNode().getElementType();

        final OntResource result;
        if (elementType == ODTTypes.BOOLEAN) {
            result = getBooleanInstance();
        } else if (elementType == ODTTypes.STRING) {
            result = OntologyModelConstants.getXsdStringInstance();
        } else if (elementType == ODTTypes.INTEGER) {
            result = OntologyModelConstants.getXsdIntegerInstance();
        } else if (elementType == ODTTypes.DECIMAL) {
            result = OntologyModelConstants.getXsdDecimalInstance();
        } else if (this instanceof ODTInterpolatedString) {
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
        return DocumentationMarkup.DEFINITION_START +
                "Constant<br>" +
                OntologyResourceUtil.describeUrisJoined(resolve(), "<br>", false) +
                DocumentationMarkup.DEFINITION_END;
    }

    private OntResource getBooleanInstance() {
        return TRUES.contains(getText()) ?
                OntologyModelConstants.getXsdTrue() :
                OntologyModelConstants.getXsdFalse();

    }

    @Override
    public @NotNull List<Literal> resolveLiteral() {
        final IElementType elementType = getNode().getFirstChildNode().getElementType();
        OntModel ontModel = OntologyModel.getInstance().getModel();
        final Literal result;
        if (elementType == ODTTypes.BOOLEAN) {
            result = TRUES.contains(getText()) ? ontModel.createTypedLiteral(true) : ontModel.createTypedLiteral(false);
        } else if (elementType == ODTTypes.STRING) {
            String unwrapped = StringUtils.unwrap(getText(), '\'');
            unwrapped = StringUtils.unwrap(unwrapped, '"');
            result = ontModel.createTypedLiteral(unwrapped);
        } else if (elementType == ODTTypes.INTEGER) {
            result = ontModel.createTypedLiteral(Integer.parseInt(getText()));
        } else if (elementType == ODTTypes.DECIMAL) {
            result = ontModel.createTypedLiteral(Double.parseDouble(getText()));
        } else {
            result = null; // returns an emptySet which is the ODT equivalent of null
        }
        return result == null ? Collections.emptyList() : List.of(result);
    }
}
