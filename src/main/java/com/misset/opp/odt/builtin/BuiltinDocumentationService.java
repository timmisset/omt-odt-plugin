package com.misset.opp.odt.builtin;

import com.intellij.lang.documentation.DocumentationMarkup;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.Strings;
import com.misset.opp.documentation.DocumentationProvider;
import com.misset.opp.odt.documentation.ODTApiDocumentationService;
import org.jetbrains.annotations.NotNull;

@Service
public final class BuiltinDocumentationService {
    private final Project project;

    public static BuiltinDocumentationService getInstance(@NotNull Project project) {
        return project.getService(BuiltinDocumentationService.class);
    }

    public BuiltinDocumentationService(Project project) {
        this.project = project;
    }

    public String getDocumentation(Builtin builtin) {
        ODTApiDocumentationService documentationService = ODTApiDocumentationService.getInstance(project);

        String level1 = builtin.isCommand() ? "Commands" : "Operators";
        String path = level1 + "/" + builtin.getCallId();

        StringBuilder sb = new StringBuilder();
        sb.append(DocumentationMarkup.DEFINITION_START);
        sb.append(builtin.getName());
        sb.append(DocumentationMarkup.DEFINITION_END);

        String description = documentationService.readApiDocumentation(path);
        if (description != null) {
            sb.append(DocumentationMarkup.CONTENT_START);
            sb.append(description);
            sb.append(DocumentationMarkup.CONTENT_END);
        }

        DocumentationProvider.addKeyValueSection("Type:", builtin.isCommand() ? "Command" : "Operator", sb);
        DocumentationProvider.addKeyValueSection("Min parameters:", String.valueOf(builtin.minNumberOfArguments()), sb);
        DocumentationProvider.addKeyValueSection("Max parameters:", String.valueOf(builtin.maxNumberOfArguments()), sb);

        if (!builtin.getFlags().isEmpty()) {
            DocumentationProvider.addKeyValueSection("Flags:", Strings.join(builtin.getFlags(), ", "), sb);
        }

        String examples = documentationService.readApiDocumentation(path + "/Examples");
        if (examples != null) {
            DocumentationProvider.addKeyValueSection("Example(s):", examples, sb);
        }

        return sb.toString();
    }

}
