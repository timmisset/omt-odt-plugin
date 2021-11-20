package com.misset.opp.omt;

import com.intellij.openapi.util.TextRange;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.YAMLElementGenerator;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;
import org.jetbrains.yaml.psi.impl.YAMLQuotedTextImpl;
import org.jetbrains.yaml.psi.impl.YAMLScalarElementManipulator;
import org.jetbrains.yaml.psi.impl.YAMLScalarImpl;

/**
 * This ElementManipulator is added for issue:
 * https://youtrack.jetbrains.com/issue/IDEA-282991
 */
public class OMTScalarElementManipulator extends YAMLScalarElementManipulator {
    @Override
    public YAMLScalarImpl handleContentChange(@NotNull YAMLScalarImpl element,
                                              @NotNull TextRange range,
                                              String newContent) throws IncorrectOperationException {
        final YAMLScalarImpl yamlScalar = super.handleContentChange(element, range, newContent);
        if (element instanceof YAMLPlainTextImpl && yamlScalar instanceof YAMLQuotedTextImpl) {
            // that's not what we want!
            final YAMLScalarImpl plainText = (YAMLScalarImpl) YAMLElementGenerator
                    .getInstance(element.getProject())
                    .createYamlKeyValue("foo", yamlScalar.getTextValue())
                    .getValue();
            if (plainText instanceof YAMLPlainTextImpl) {
                return (YAMLScalarImpl) yamlScalar.replace(plainText);
            }
        }
        return yamlScalar;
    }
}
