package com.misset.opp.odt.psi.impl.variable;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.ModificationTracker;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.odt.ODTElementGenerator;
import com.misset.opp.odt.ODTMultiHostInjector;
import com.misset.opp.odt.psi.ODTDeclareVariable;
import com.misset.opp.odt.psi.ODTDefineParam;
import com.misset.opp.odt.psi.ODTScript;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.ODTVariableAssignment;
import com.misset.opp.odt.psi.ODTVariableValue;
import com.misset.opp.odt.psi.impl.variable.delegate.ODTDeclaredVariableDelegate;
import com.misset.opp.odt.psi.impl.variable.delegate.ODTDefineInputParamDelegate;
import com.misset.opp.odt.psi.impl.variable.delegate.ODTUsedVariableDelegate;
import com.misset.opp.odt.psi.impl.variable.delegate.ODTVariableAssignmentDelegate;
import com.misset.opp.odt.psi.impl.variable.delegate.ODTVariableDelegate;
import com.misset.opp.omt.meta.providers.OMTVariableProvider;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.Optional;
import java.util.Set;

/**
 * The behavior of variables is quite specific depending on them being a declaration, usage or (re)assignment
 * For this reason, the auto-generated ODTVariable class from the parser is decorated with a delegate specific
 * to their implementation.
 * Overlapping logic is confined in this base class
 */
public abstract class ODTBaseVariable extends ASTWrapperPsiElement implements ODTVariable {
    private final ODTVariableDelegate delegate;
    private static final Key<CachedValue<SearchScope>> USAGE_SEARCH_SCOPE = new Key<>("USAGE_SEARCH_SCOPE");
    protected static final Key<CachedValue<Boolean>> IS_DECLARED_VARIABLE = new Key<>("IS_DECLARED_VARIABLE");

    public ODTBaseVariable(@NotNull ASTNode node) {
        super(node);
        this.delegate = getDelegate();
    }

    private ODTVariableDelegate getDelegate() {
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
                return new ODTUsedVariableDelegate(this);
            }
        });
    }

    public boolean isDeclaredVariable() {
        return CachedValuesManager.getCachedValue(this,
                IS_DECLARED_VARIABLE,
                () -> new CachedValueProvider.Result<>(delegate.isDeclaredVariable(), ModificationTracker.NEVER_CHANGED));
    }

    public boolean canBeDeclaredVariable(ODTVariable variable) {
        return delegate.canBeDeclaredVariable(variable);
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
    public PsiReference getReference() {
        return delegate.getReference();
    }

    @Override
    public Set<OntResource> getType() {
        return delegate.getType();
    }

    public boolean isOMTVariableProvider() {
        return delegate.isOMTVariableProvider();
    }

    @Override
    public @NotNull SearchScope getUseScope() {
        return CachedValuesManager.getCachedValue(this, USAGE_SEARCH_SCOPE, () -> {
            final SearchScope searchScope = isOMTVariableProvider() ? getOMTUseScope() : getODTUseScope();
            return new CachedValueProvider.Result<>(searchScope, getContainingFile());
        });
    }

    private SearchScope getODTUseScope() {
        PsiElement useScope = PsiTreeUtil.getTopmostParentOfType(this, ODTScript.class);
        if(useScope != null && useScope.getParent() != null){
            // use the parent, sometimes there are javadocs with references to this element
            // which are not in the same script
            useScope = useScope.getParent();
        }
        return new LocalSearchScope(useScope != null ? useScope : getContainingFile());
    }
    private SearchScope getOMTUseScope() {
        final Optional<Pair<YAMLMapping, OMTVariableProvider>> closestProvider = ODTMultiHostInjector.getClosestProvider(
                this,
                YAMLMapping.class,
                OMTVariableProvider.class);
        if(closestProvider.isPresent()) {
            final YAMLMapping first = closestProvider.get().getFirst();
            return new LocalSearchScope(first);
        } else {
            return GlobalSearchScope.fileScope(getContainingFile());
        }
    }
}
