package com.misset.opp.odt.psi.impl.variable;

import com.intellij.lang.ASTNode;
import com.intellij.lang.documentation.DocumentationMarkup;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.*;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.documentation.DocumentationProvider;
import com.misset.opp.odt.ODTElementGenerator;
import com.misset.opp.odt.documentation.ODTDocumented;
import com.misset.opp.odt.inspection.type.ODTCodeUntypedInspectionWarning;
import com.misset.opp.odt.psi.*;
import com.misset.opp.odt.psi.impl.ODTASTWrapperPsiElement;
import com.misset.opp.odt.psi.impl.resolvable.queryStep.ODTResolvableVariableStep;
import com.misset.opp.odt.psi.impl.variable.delegate.*;
import com.misset.opp.resolvable.Variable;
import com.misset.opp.resolvable.global.GlobalVariable;
import com.misset.opp.shared.refactoring.SupportsSafeDelete;
import com.misset.opp.ttl.model.OppModel;
import com.misset.opp.ttl.util.TTLResourceUtil;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;

/**
 * The behavior of variables is quite specific depending on them being a declaration, usage or (re)assignment
 * For this reason, the auto-generated ODTVariable class from the parser is decorated with a delegate specific
 * to their implementation.
 * Overlapping logic is confined in this base class
 */
public abstract class ODTBaseVariable
        extends ODTASTWrapperPsiElement
        implements ODTVariable,
        ODTVariableWrapper,
        PsiNameIdentifierOwner,
        ODTDocumented,
        SupportsSafeDelete {
    private final ODTVariableDelegate delegate;
    protected static final Key<CachedValue<Boolean>> IS_DECLARED_VARIABLE = new Key<>("IS_DECLARED_VARIABLE");
    protected static final Key<CachedValue<Set<OntResource>>> VARIABLE_TYPE = new Key<>("VARIABLE_TYPE");

    public ODTBaseVariable(@NotNull ASTNode node) {
        super(node);
        this.delegate = getDelegate();
    }

    @Override
    public String getDescription() {
        return getName();
    }

    @Override
    public ODTVariable getElement() {
        return this;
    }

    @Override
    public ODTVariableDelegate getDelegate() {
        return ReadAction.compute(() -> {
            /*
                VAR $variable;                          // wrapped in DECLARE_VARIABLE
                VAR $variable = 'Hello world';          // wrapped first in VARIABLE_ASSIGNMENT
                $variable = 'Hello brave new world';    // wrapped in VARIABLE_ASSIGNMENT
                VAR $newVariable = $variable            // wrapped in VARIABLE_VALUE
                @LOG($variable);                        // only VARIABLE
            */
            PsiElement parentType = PsiTreeUtil.getParentOfType(this,
                    ODTVariableValue.class,
                    ODTVariableAssignment.class,
                    ODTDefineParam.class,
                    ODTDeclareVariable.class);
            if (parentType instanceof ODTDeclareVariable) {
                return new ODTDeclaredVariableDelegate(this);
            } else if (parentType instanceof ODTVariableAssignment) {
                return new ODTVariableAssignmentDelegate(this);
            } else if (parentType instanceof ODTDefineParam) {
                return new ODTDefineInputParamDelegate(this);
            } else {
                return new ODTUsageVariableDelegate(this);
            }
        });
    }

    public boolean isDeclaredVariable() {
        return CachedValuesManager.getCachedValue(this,
                IS_DECLARED_VARIABLE,
                () -> new CachedValueProvider.Result<>(delegate.isDeclaredVariable(),
                        getODTFile(),
                        PsiModificationTracker.MODIFICATION_COUNT));
    }

    public boolean canBeDeclaredVariable(ODTVariable variable) {
        return delegate.canBeDeclaredVariable(variable);
    }

    @Override
    public boolean isAssignedVariable() {
        return delegate.isAssignedVariable();
    }

    @Override
    public boolean canBeAnnotated() {
        return getDelegate() instanceof ODTDefineInputParamDelegate;
    }

    @Override
    public PsiElement setName(@NlsSafe @NotNull String name) throws IncorrectOperationException {
        if (!name.startsWith("$")) {
            name = "$" + name;
        }
        final ODTVariable variable = ODTElementGenerator.getInstance(getProject()).createVariable(name);
        return variable != null ? replace(variable) : this;
    }

    @Override
    @NotNull
    public String getName() {
        return getText();
    }

    public boolean sameNameAs(ODTVariable variable) {
        return Optional.ofNullable(variable)
                .map(PsiNamedElement::getName)
                .map(s -> s.equals(getName()))
                .orElse(false);
    }

    @Override
    public boolean isParameter() {
        return getDelegate() instanceof ODTDefineInputParamDelegate;
    }

    @Override
    public PsiReference getReference() {
        return delegate.getReference();
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        return CachedValuesManager.getCachedValue(this, VARIABLE_TYPE, () -> {
            final Set<OntResource> type = delegate.resolve();
            return new CachedValueProvider.Result<>(type,
                    getODTFile(),
                    PsiModificationTracker.MODIFICATION_COUNT,
                    ODTCodeUntypedInspectionWarning.PARAM_ANNOTATION,
                    OppModel.ONTOLOGY_MODEL_MODIFICATION_TRACKER);
        });
    }

    @Override
    public String getDocumentation(Project project) {
        Variable declared = getDeclared();
        if (declared == null) {
            return null;
        }

        ODTResolvableVariableStep variableStep = null;
        if (getParent() instanceof ODTVariableStep) {
            variableStep = (ODTResolvableVariableStep) getParent();
        }

        StringBuilder sb = new StringBuilder();
        sb.append(DocumentationMarkup.DEFINITION_START);
        sb.append(getName());
        sb.append(DocumentationMarkup.DEFINITION_END);

        String description = declared.getDescription();
        if (!description.equals(getName())) {
            sb.append(DocumentationMarkup.CONTENT_START);
            sb.append(description);
            sb.append(DocumentationMarkup.CONTENT_END);
        }

        Set<OntResource> unfiltered = resolve();
        Set<OntResource> filtered = variableStep != null ? variableStep.getResolvableParent().filter(unfiltered) : unfiltered;
        sb.append(DocumentationMarkup.SECTIONS_START);
        String typeLabel = filtered.size() == 1 ? "Type:" : "Types:";
        DocumentationProvider.addKeyValueSection(typeLabel, filtered.isEmpty() ? "Unknown" : TTLResourceUtil.describeUrisForLookupJoined(filtered), sb);
        if (!unfiltered.equals(filtered)) {
            DocumentationProvider.addKeyValueSection("Unfiltered:", TTLResourceUtil.describeUrisForLookupJoined(unfiltered, "<br>"), sb);
        }
        DocumentationProvider.addKeyValueSection("Scope:", isGlobal() ? "Global" : "Local", sb);
        DocumentationProvider.addKeyValueSection("Readonly:", isGlobal() || isReadonly() ? "Yes" : "No", sb);
        sb.append(DocumentationMarkup.SECTIONS_END);
        return sb.toString();
    }

    @Override
    public boolean isGlobal() {
        return GlobalVariable.getVariable(getName()) != null;
    }

    @Override
    public Variable getDeclared() {
        return delegate.getDeclared();
    }

    @Override
    public boolean isReadonly() {
        return Optional.ofNullable(getDeclared())
                .filter(variable -> variable != this)
                .map(Variable::isReadonly)
                .orElse(false);
    }

    @Override
    public @NotNull SearchScope getUseScope() {
        return new LocalSearchScope(getODTFile());
    }

    @Override
    public boolean isUnused() {
        return isDeclaredVariable() &&
                !"$_".equals(getName()) &&
                ReferencesSearch.search(this, getUseScope()).findFirst() == null;
    }

    @Override
    public void delete() throws IncorrectOperationException {
        getDelegate().delete();
    }

    @Override
    public @Nullable PsiElement getNameIdentifier() {
        return this;
    }

    @Override
    public String getSource() {
        return delegate.getSource();
    }

    @Override
    public Scope getScope() {
        return Scope.LOCAL;
    }
}
