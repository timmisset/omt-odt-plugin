package com.misset.opp.odt.psi.impl.variable;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.odt.ODTElementGenerator;
import com.misset.opp.odt.psi.ODTDeclareVariable;
import com.misset.opp.odt.psi.ODTDefineParam;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.ODTVariableAssignment;
import com.misset.opp.odt.psi.ODTVariableValue;
import com.misset.opp.odt.psi.impl.variable.delegate.ODTDeclaredVariableDelegate;
import com.misset.opp.odt.psi.impl.variable.delegate.ODTUsedVariableDelegate;
import com.misset.opp.odt.psi.impl.variable.delegate.ODTVariableAssignmentDelegate;
import com.misset.opp.odt.psi.impl.variable.delegate.ODTVariableDelegate;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * The behavior of variables is quite specific depending on them being a declaration, usage or (re)assignment
 * For this reason, the auto-generated ODTVariable class from the parser is decorated with a delegate specific
 * to their implementation.
 * Overlapping logic is confined in this base class
 */
public abstract class ODTBaseVariable extends ASTWrapperPsiElement implements ODTVariable {
    private final ODTVariableDelegate delegate;

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
                return new ODTDeclaredVariableDelegate(this);
            } else {
                return new ODTUsedVariableDelegate(this);
            }
        });
    }

    public boolean isDeclaredVariable() {
        return delegate.isDeclaredVariable();
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
}
