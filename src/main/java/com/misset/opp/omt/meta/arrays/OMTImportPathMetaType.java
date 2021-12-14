package com.misset.opp.omt.meta.arrays;

import com.intellij.codeInspection.*;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiParserFacade;
import com.misset.opp.omt.meta.OMTImportMemberMetaType;
import com.misset.opp.omt.meta.OMTImportMetaType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.YAMLElementGenerator;
import org.jetbrains.yaml.meta.model.YamlArrayType;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLSequence;
import org.jetbrains.yaml.psi.YAMLSequenceItem;
import org.jetbrains.yaml.psi.YAMLValue;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OMTImportPathMetaType extends YamlArrayType {
    protected static final String UNSORTED_IMPORT_MEMBERS = "Unsorted import members";
    protected static final String SORT_MEMBERS = "Sort members";

    public OMTImportPathMetaType(String path) {
        super(new OMTImportMemberMetaType());
    }

    @Override
    public void validateValue(@NotNull YAMLValue value,
                              @NotNull ProblemsHolder problemsHolder) {
        // require YAMLSequence validation
        super.validateValue(value, problemsHolder);

        // check if sorted:
        if (value instanceof YAMLSequence) {
            final YAMLSequence sequence = (YAMLSequence) value;
            List<YAMLSequenceItem> items = getSortedList(sequence);
            if (!items.equals(sequence.getItems())) {
                // not the same sort order, so must be unsorted:
                final InspectionManager manager = problemsHolder.getManager();
                // the process is started from the Import Mapping, but the fix is for a specific path / sequence list
                // issue a new (more) specific ProblemDescriptor
                final ProblemDescriptor unsortedImports = manager.createProblemDescriptor(value,
                        UNSORTED_IMPORT_MEMBERS,
                        problemsHolder.isOnTheFly(),
                        new LocalQuickFix[]{getSortingQuickFix()},
                        ProblemHighlightType.WEAK_WARNING);
                problemsHolder.registerProblem(unsortedImports);
            }
        }
    }

    private List<YAMLSequenceItem> getSortedList(YAMLSequence sequence) {
        final List<YAMLSequenceItem> items = new ArrayList<>(sequence.getItems());
        List<String> values = items.stream()
                .map(sequenceItem -> sequenceItem.getValue() != null ? sequenceItem.getValue().getText() : "")
                .sorted(String.CASE_INSENSITIVE_ORDER
                        .thenComparing(Comparator.naturalOrder()))
                .collect(Collectors.toCollection(ArrayList::new));

        items.sort(Comparator.comparing(o -> Optional.ofNullable(o.getValue())
                .map(PsiElement::getText)
                .map(values::indexOf)
                .orElse(-1)));
        return items;
    }

    private LocalQuickFix getSortingQuickFix() {
        return new LocalQuickFix() {
            @Override
            public @IntentionFamilyName @NotNull String getFamilyName() {
                return SORT_MEMBERS;
            }

            @Override
            public void applyFix(@NotNull Project project,
                                 @NotNull ProblemDescriptor descriptor) {
                final PsiElement psiElement = descriptor.getPsiElement();
                if (psiElement instanceof YAMLSequence) {
                    final YAMLSequence yamlSequence = (YAMLSequence) psiElement;
                    List<YAMLSequenceItem> items = getSortedList(yamlSequence);

                    final YAMLElementGenerator generator = YAMLElementGenerator.getInstance(project);
                    PsiParserFacade parserFacade = PsiParserFacade.SERVICE.getInstance(project);
                    final YAMLSequence newSequence = generator.createEmptySequence();
                    items.forEach(sequenceItem -> {
                        newSequence.add(sequenceItem);
                        if (newSequence.getItems().size() < items.size()) {
                            newSequence.add(parserFacade.createWhiteSpaceFromText("\n"));
                        }
                    });
                    yamlSequence.replace(newSequence);
                }
            }
        };
    }

    @Override
    public void validateKey(@NotNull YAMLKeyValue keyValue,
                            @NotNull ProblemsHolder problemsHolder) {
        if (keyValue.getKey() == null) {
            return;
        }
        // validate that the key can be resolved to file:
        // resolve to the virtual file instead of the Psi to prevent the file from being loaded without cause
        final boolean resolvable = Optional.ofNullable(OMTImportMetaType.resolveToPath(keyValue))
                .filter(s -> !s.startsWith("temp:///")) // <-- unittests use a fictitious path that is not valid for Path
                .map(Path::of)
                .map(VirtualFileManager.getInstance()::findFileByNioPath)
                .map(VirtualFile::exists)
                .orElse(false);
        if (!resolvable) {
            problemsHolder.registerProblem(keyValue.getKey(), "Cannot find file", ProblemHighlightType.ERROR);
        }

        // validate the sequence itself, check if it's ordered:
        Optional.ofNullable(keyValue.getValue())
                .ifPresent(value -> new OMTImportPathMetaType(keyValue.getName()).validateValue(value, problemsHolder));
    }
}
