package com.misset.opp.omt.documentation;

import com.intellij.lang.documentation.AbstractDocumentationProvider;
import com.intellij.lang.documentation.DocumentationMarkup;
import com.intellij.lang.documentation.DocumentationProvider;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.YAMLTokenTypes;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;

public class OMTDocumentationProvider extends AbstractDocumentationProvider implements DocumentationProvider {

    @Override
    public @Nullable @Nls String generateDoc(PsiElement element, @Nullable PsiElement originalElement) {
        OMTMetaTypeProvider metaTypeProvider = OMTMetaTypeProvider.getInstance(element.getProject());
        final YamlMetaType metaType;
        if (element instanceof YAMLKeyValue) {
            metaType = metaTypeProvider.getResolvedKeyValueMetaTypeMeta((YAMLKeyValue) element);
        } else if (element instanceof YAMLMapping) {
            metaType = metaTypeProvider.getResolvedMetaType(element);
        } else {
            metaType = null;
        }
        if (metaType instanceof OMTDocumented) {
            // documented class
            return OMTDocumentationProvider.getClassDocumentation(element.getProject(), (OMTDocumented) metaType);
        } else {
            if (element instanceof YAMLKeyValue) {
                // might be a documented attribute
                return getDocumentedAttribute(metaTypeProvider, (YAMLKeyValue) element);
            }
        }
        return null;
    }

    private YamlMetaType getMetaTypeParent(OMTMetaTypeProvider metaTypeProvider, PsiElement element) {
        YAMLMapping parentMapping = PsiTreeUtil.getParentOfType(element, YAMLMapping.class, true);
        if (parentMapping != null) {
            YamlMetaType resolvedMetaType = metaTypeProvider.getResolvedMetaType(parentMapping);
            if (resolvedMetaType instanceof OMTDocumented) {
                return resolvedMetaType;
            }
        }
        YAMLKeyValue parentKeyValue = PsiTreeUtil.getParentOfType(element, YAMLKeyValue.class, true);
        if (parentKeyValue == null) {
            return null;
        } else {
            return metaTypeProvider.getResolvedKeyValueMetaTypeMeta(parentKeyValue);
        }
    }

    private String getDocumentedAttribute(OMTMetaTypeProvider metaTypeProvider, YAMLKeyValue element) {
        YamlMetaType parentMetaType = getMetaTypeParent(metaTypeProvider, element);
        if (parentMetaType instanceof OMTDocumented) {
            OMTDocumented documented = (OMTDocumented) parentMetaType;
            String level1Header = documented.getLevel1Header();
            String type = documented.getDocumentationClass();
            String attribute = element.getKeyText();
            ApiDocumentationService documentationService = ApiDocumentationService.getInstance(element.getProject());
            String description = documentationService.readOMTApiDocumentation(level1Header + "/" + type + "/" + attribute);
            String example = documentationService.readOMTApiDocumentation(level1Header + "/" + type + "/" + attribute + "/Example");

            StringBuilder sb = new StringBuilder();
            sb.append(DocumentationMarkup.DEFINITION_START);
            sb.append(attribute);
            sb.append(DocumentationMarkup.DEFINITION_END);
            sb.append(DocumentationMarkup.CONTENT_START);
            sb.append(description != null ? description : "Could not find description");
            sb.append(DocumentationMarkup.CONTENT_END);
            if (example != null) {
                OMTDocumentationProvider.addKeyValueSection("Example", example, sb);
            }

            return sb.toString();
        }
        return null;
    }

    @Override
    public @Nullable PsiElement getCustomDocumentationElement(@NotNull Editor editor, @NotNull PsiFile file, @Nullable PsiElement contextElement, int targetOffset) {
        IElementType elementType = PsiUtilCore.getElementType(contextElement);
        if (elementType == YAMLTokenTypes.SCALAR_KEY || elementType == YAMLTokenTypes.TAG) {
            // a scalar key, retrieve the key:value to obtain the MetaType information
            return contextElement.getParent();
        }
        return contextElement;
    }

    /**
     * Creates a key/value row for the rendered documentation.
     */
    public static void addKeyValueSection(String key, String value, StringBuilder sb) {
        sb.append(DocumentationMarkup.SECTION_HEADER_START);
        sb.append(key);
        sb.append(DocumentationMarkup.SECTION_SEPARATOR);
        sb.append("<p>");
        sb.append(value);
        sb.append(DocumentationMarkup.SECTION_END);
    }

    /**
     * Generic documentation retrieval for OMT Classes
     * Looks for Description, Example, Local Commands
     */
    public static String getClassDocumentation(Project project, OMTDocumented omtDocumented) {
        return getClassDocumentationSB(project, omtDocumented).toString();
    }

    /**
     * Generic documentation retrieval for OMT Classes
     * Looks for Description, Example, Local Commands.
     * <p>
     * Returns the stringbuilder to add additional segments
     * Use addKeyValueSection to add additional sections
     */
    public static StringBuilder getClassDocumentationSB(Project project, OMTDocumented omtDocumented) {
        ApiDocumentationService instance = ApiDocumentationService.getInstance(project);

        String type = omtDocumented.getDocumentationClass();
        StringBuilder sb = new StringBuilder();
        sb.append(DocumentationMarkup.DEFINITION_START);
        sb.append(type);
        sb.append(DocumentationMarkup.DEFINITION_END);
        sb.append(DocumentationMarkup.CONTENT_START);
        String description = instance.readOMTApiDocumentation(
                "Classes/" + type + "/Description"
        );
        sb.append(description != null ? description : "Could not find description");
        sb.append(DocumentationMarkup.CONTENT_END);

        // Some API documentation sections have additional description headers that should be included
        // as separate documentation segments
        for (String header : omtDocumented.getAdditionalDescriptionHeaders()) {
            String additionalHeader = instance.readOMTApiDocumentation(
                    "Classes/" + type + "/Description/" + header
            );
            if (additionalHeader != null) {
                OMTDocumentationProvider.addKeyValueSection(header, additionalHeader, sb);
            }
        }


        String example = instance.readOMTApiDocumentation(
                "Classes/" + type + "/Description/Example"
        );
        if (example != null) {
            OMTDocumentationProvider.addKeyValueSection("Example", example, sb);
        }

        return sb;
    }
}
