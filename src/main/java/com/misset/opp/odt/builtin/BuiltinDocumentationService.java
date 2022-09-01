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

    public String getDocumentation(AbstractBuiltin builtin) {
        ODTApiDocumentationService documentationService = ODTApiDocumentationService.getInstance(project);

        String level1 = builtin.isCommand() ? "Commands" : "Operators";
        String path = String.format("%s/%s", level1, builtin.getCallId());

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

        sb.append(DocumentationProvider.getKeyValueSection("Type:", builtin.isCommand() ? "Command" : "Operator"));
        sb.append(DocumentationProvider.getKeyValueSection("Min parameters:", String.valueOf(builtin.minNumberOfArguments())));
        sb.append(DocumentationProvider.getKeyValueSection("Max parameters:", String.valueOf(builtin.maxNumberOfArguments())));

        if (!builtin.getFlags().isEmpty()) {
            sb.append(DocumentationProvider.getKeyValueSection("Flags:", Strings.join(builtin.getFlags(), ", ")));
        }

        String examples = documentationService.readApiDocumentation(path + "/Examples");
        if (examples != null) {
            sb.append(DocumentationProvider.getKeyValueSection("Example(s):", examples));
        }

        return sb.toString();
    }

}
