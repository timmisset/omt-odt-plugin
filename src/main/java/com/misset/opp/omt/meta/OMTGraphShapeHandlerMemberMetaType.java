package com.misset.opp.omt.meta;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlStringType;
import org.jetbrains.yaml.psi.YAMLScalar;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

import java.util.Optional;
import java.util.function.UnaryOperator;

public class OMTGraphShapeHandlerMemberMetaType extends YamlStringType {
    protected static final UnaryOperator<String> NOT_AN_KNOWN_HANDLER = s -> String.format("%s is not a known handler", s);

    private static final OMTGraphShapeHandlerMemberMetaType INSTANCE = new OMTGraphShapeHandlerMemberMetaType();

    public static OMTGraphShapeHandlerMemberMetaType getInstance() {
        return INSTANCE;
    }

    private OMTGraphShapeHandlerMemberMetaType() {
    }

    @Override
    protected void validateScalarValue(@NotNull YAMLScalar scalarValue,
                                       @NotNull ProblemsHolder holder) {

        if (scalarValue instanceof YAMLPlainTextImpl) {
            Optional.ofNullable(scalarValue.getReference())
                    .filter(psiReference -> psiReference.resolve() == null)
                    .ifPresent(psiReference -> holder.registerProblem(
                            psiReference.getElement(),
                            NOT_AN_KNOWN_HANDLER.apply(psiReference.getElement().getText()),
                            ProblemHighlightType.ERROR)
                    );
        }
    }
}
