package com.misset.opp.odt.psi.impl.variable;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.odt.ODTElementGenerator;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.impl.variable.delegate.ODTDeclaredVariableDelegate;
import com.misset.opp.odt.psi.impl.variable.delegate.ODTUsedVariableDelegate;
import com.misset.opp.odt.psi.impl.variable.delegate.ODTVariableAssignmentDelegate;
import com.misset.opp.odt.psi.impl.variable.delegate.ODTVariableDelegate;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static com.misset.opp.odt.psi.ODTTypes.DECLARE_VARIABLE;
import static com.misset.opp.odt.psi.ODTTypes.VARIABLE_ASSIGNMENT;
import static com.misset.opp.odt.psi.ODTTypes.VARIABLE_VALUE;
import static com.misset.opp.util.ASTNodeUtil.getParentType;

/**
 * The behavior of variables is quite specific depending on them being a declaration, usage or (re)assignment
 * For this reason, the auto-generated ODTVariable class from the parser is decorated with a delegate specific
 * to their implementation.
 * Overlapping logic is confined in this base class
 */
public abstract class ODTBaseVariable extends ASTWrapperPsiElement implements ODTVariable {
    private static final TokenSet ALL_VARIABLE_CONTAINERS = TokenSet.create(VARIABLE_ASSIGNMENT, VARIABLE_VALUE, DECLARE_VARIABLE);
    private final ODTVariableDelegate delegate;

    public ODTBaseVariable(@NotNull ASTNode node) {
        super(node);
        this.delegate = getDelegate();
    }
    private ODTVariableDelegate getDelegate() {
        /*
            VAR $variable;                          // wrapped in DECLARE_VARIABLE
            VAR $variable = 'Hello world';          // wrapped first in VARIABLE_ASSIGNMENT
            $variable = 'Hello brave new world';    // wrapped in VARIABLE_ASSIGNMENT
            VAR $newVariable = $variable            // wrapped in VARIABLE_VALUE
            @LOG($variable);                        // only VARIABLE
         */
        final IElementType parentType = getParentType(getNode(), ALL_VARIABLE_CONTAINERS);
        if(parentType == DECLARE_VARIABLE) {
            return new ODTDeclaredVariableDelegate(this);
        } else if(parentType == VARIABLE_ASSIGNMENT) {
            return new ODTVariableAssignmentDelegate(this);
        } else {
            return new ODTUsedVariableDelegate(this);
        }
    }

    public boolean isDeclaredVariable() {
        return delegate.isDeclaredVariable();
    }
    public boolean canBeDeclaredVariable(ODTVariable variable) {
        return delegate.canBeDeclaredVariable(variable);
    }

    @Override
    public PsiElement setName(@NlsSafe @NotNull String name) throws IncorrectOperationException {
        if(!name.startsWith("$")) {
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
