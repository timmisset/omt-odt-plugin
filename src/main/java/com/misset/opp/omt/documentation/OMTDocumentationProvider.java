package com.misset.opp.omt.documentation;

import com.intellij.lang.documentation.AbstractDocumentationProvider;
import com.intellij.lang.documentation.DocumentationMarkup;
import com.intellij.lang.documentation.DocumentationProvider;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.Strings;
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

import java.util.List;

import static com.misset.opp.documentation.DocumentationProvider.addKeyValueSection;

public class OMTDocumentationProvider extends AbstractDocumentationProvider implements DocumentationProvider {

    public static final String CLASSES = "Classes";
    public static final String DESCRIPTION = "Description";
    public static final String EXAMPLE = "Example";

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

    /**
     * Generic documentation retrieval for OMT Classes
     * Looks for Description, Example, Local Commands.
     * <p>
     * Returns the stringbuilder to add additional segments
     * Use addKeyValueSection to add additional sections
     */
    public static StringBuilder getClassDocumentationSB(Project project, OMTDocumented omtDocumented) {
        OMTApiDocumentationService documentationService = OMTApiDocumentationService.getInstance(project);

        String type = omtDocumented.getDocumentationClass();
        StringBuilder sb = new StringBuilder();
        sb.append(DocumentationMarkup.DEFINITION_START);
        sb.append(type);
        sb.append(DocumentationMarkup.DEFINITION_END);
        sb.append(DocumentationMarkup.CONTENT_START);
        String description = documentationService.readApiDocumentation(
                documentPath(CLASSES, type, DESCRIPTION)
        );
        sb.append(description != null ? description : "Could not find description");
        sb.append(DocumentationMarkup.CONTENT_END);

        // Some API documentation sections have additional headers that should be included
        // as separate documentation segments
        omtDocumented.getAdditionalHeaders()
                .forEach(header -> addAdditionalField(
                        header,
                        documentPath(CLASSES, type, header),
                        sb,
                        documentationService)
                );

        // Some API documentation sections have additional description headers that should be included
        // as separate documentation segments
        omtDocumented.getAdditionalDescriptionHeaders()
                .forEach(header -> addAdditionalField(
                        header,
                        documentPath(CLASSES, type, DESCRIPTION, header),
                        sb,
                        documentationService)
                );

        String example = documentationService.readApiDocumentation(
                documentPath(CLASSES, type, DESCRIPTION, EXAMPLE)
        );
        if (example != null) {
            addKeyValueSection(EXAMPLE, example, sb);
        }

        return sb;
    }

    private static String documentPath(String... parts) {
        return Strings.join(List.of(parts), "/");
    }

    /**
     * Generic documentation retrieval for OMT Classes
     * Looks for Description, Example, Local Commands
     */
    public static String getClassDocumentation(Project project, OMTDocumented omtDocumented) {
        return getClassDocumentationSB(project, omtDocumented).toString();
    }

    private String getDocumentedAttribute(OMTMetaTypeProvider metaTypeProvider, YAMLKeyValue element) {
        YamlMetaType parentMetaType = getMetaTypeParent(metaTypeProvider, element);
        if (parentMetaType instanceof OMTDocumented) {
            OMTDocumented documented = (OMTDocumented) parentMetaType;
            String level1Header = documented.getLevel1Header();
            String type = documented.getDocumentationClass();
            String attribute = element.getKeyText();
            OMTApiDocumentationService documentationService = OMTApiDocumentationService.getInstance(element.getProject());
            String description = documentationService.readApiDocumentation(
                    documentPath(level1Header, type, attribute));
            String example = documentationService.readApiDocumentation(
                    documentPath(level1Header, type, attribute, EXAMPLE));

            StringBuilder sb = new StringBuilder();
            sb.append(DocumentationMarkup.DEFINITION_START);
            sb.append(attribute);
            sb.append(DocumentationMarkup.DEFINITION_END);
            sb.append(DocumentationMarkup.CONTENT_START);
            sb.append(description != null ? description : "Could not find description");
            sb.append(DocumentationMarkup.CONTENT_END);
            if (example != null) {
                addKeyValueSection(EXAMPLE, example, sb);
            }

            return sb.toString();
        }
        return null;
    }

    private static void addAdditionalField(String header,
                                           String path,
                                           StringBuilder sb,
                                           OMTApiDocumentationService documentationService) {
        String content = documentationService.readApiDocumentation(path);
        if (content != null) {
            addKeyValueSection(header, content, sb);
        }
    }

    @Override
    public @Nullable PsiElement getCustomDocumentationElement(@NotNull Editor editor,
                                                              @NotNull PsiFile file,
                                                              @Nullable PsiElement contextElement,
                                                              int targetOffset) {
        IElementType elementType = PsiUtilCore.getElementType(contextElement);
        if (contextElement != null && (elementType == YAMLTokenTypes.SCALAR_KEY || elementType == YAMLTokenTypes.TAG)) {
            // a scalar key, retrieve the key:value to obtain the MetaType information
            return contextElement.getParent();
        }
        return contextElement;
    }
}
