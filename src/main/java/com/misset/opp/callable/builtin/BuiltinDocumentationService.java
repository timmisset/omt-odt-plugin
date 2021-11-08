package com.misset.opp.callable.builtin;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.progress.impl.BackgroundableProcessIndicator;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FilenameIndex;
import org.commonmark.html.HtmlRenderer;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Optional;

@Service
public final class BuiltinDocumentationService {
    private final Project project;
    private static final HashMap<String, String> documents = new HashMap<>();

    public static BuiltinDocumentationService getInstance(@NotNull Project project) {
        return project.getService(BuiltinDocumentationService.class);
    }

    public BuiltinDocumentationService(Project project) {
        this.project = project;
    }

    public static String getDocumentation(Builtin builtin) {
        return documents.getOrDefault(builtin.getClass().getSimpleName(),
                "Documentation not loaded yet, probably indexing");
    }

    public Task.Backgroundable getTask() {
        return new Task.Backgroundable(project,
                "Load Markdown descriptions for Builtin operators & commands") {
            private final FileDocumentManager documentManager = FileDocumentManager.getInstance();
            private final Parser parser = Parser.builder().build();
            private final HtmlRenderer renderer = HtmlRenderer.builder().build();

            @Override
            public void run(@Nullable ProgressIndicator indicator) {
                documents.clear();
                ReadAction.run(() -> FilenameIndex.getAllFilesByExt(project, "md")
                        .forEach(this::addToContents));
            }

            private void addToContents(VirtualFile virtualFile) {
                documents.put(virtualFile.getNameWithoutExtension(), parseMarkdownToHtml(virtualFile));
            }

            private String parseMarkdownToHtml(VirtualFile virtualFile) {
                String text = Optional.ofNullable(documentManager.getDocument(virtualFile))
                        .map(Document::getText)
                        .orElse(null);
                if(text == null) { return null; }

                Node parse = parser.parse(text);
                return renderer.render(parse);
            }
        };
    }

    public void loadDocuments() {
        if (DumbService.isDumb(project)) {
            return;
        }
        DumbService.getInstance(project).runWhenSmart(() -> {
            final Task.Backgroundable task = getTask();

            ProgressManager.getInstance().runProcessWithProgressAsynchronously(
                    getTask(), new BackgroundableProcessIndicator(task)
            );
        });

    }
}
