package com.misset.opp.omt.meta.arrays;

import com.intellij.codeInspection.*;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiParserFacade;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.YAMLElementGenerator;
import org.jetbrains.yaml.meta.model.YamlArrayType;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLSequence;
import org.jetbrains.yaml.psi.YAMLSequenceItem;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OMTSortedArrayMetaType extends YamlArrayType {
    protected static final String UNSORTED_IMPORT_MEMBERS = "Unsorted import members";
    protected static final String SORT_MEMBERS = "Sort members";

    public OMTSortedArrayMetaType(@NotNull YamlMetaType elementType) {
        super(elementType);
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
}
