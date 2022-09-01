package com.misset.opp.odt.psi;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.misset.opp.resolvable.Callable;
import com.misset.opp.resolvable.Variable;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.resolvable.psi.PsiCallable;
import com.misset.opp.resolvable.psi.PsiPrefix;
import com.misset.opp.resolvable.psi.PsiVariable;

import java.util.Collection;
import java.util.Map;

public interface ODTFile extends PsiFile {

    /**
     * Returns the prefix::namespaces that are available at the point of this Injected ODT fragment.
     * It does not provide the prefix declarations that are declared in the ODT file itself since those depend on the position
     * of the element that wants to use it
     */
    Map<String, String> getAvailableNamespaces();

    Collection<PsiPrefix> getPrefixes(String key);

    Collection<Variable> getVariables(String key);

    /**
     * Returns the Callables that have the specified callId.
     * It firs tries to return a PsiCallable and otherwise a non-psi Callable
     */
    Collection<Callable> getCallables(String callId);

    /**
     * Returns true if the source element can access the target element
     *
     * @see com.misset.opp.odt.psi.util.PsiRelationshipUtil
     */
    boolean isAccessible(PsiElement source, PsiElement target);

    Collection<PsiCallable> listPsiCallables();

    /**
     * Returns a list with available Callables that aren't part of the PsiTree
     * For example, Builtin commands
     */
    Collection<Callable> listCallables();

    /**
     * Returns both the Psi and non-Psi Callables
     */
    Collection<Callable> listAllCallables();

    Collection<PsiPrefix> listPrefixes();

    /**
     * Returns the variables that are declared as part of the PsiTree
     */
    Collection<PsiVariable> listPsiVariables();

    /**
     * Returns the variables that are not part of the PsiTree but are available
     * to the ODTFile. For example, Global Variables
     */
    Collection<Variable> listVariables();

    Collection<Variable> listAllVariables();

    /**
     * Return a collection of import quickfixes for the provided callable.
     * For standalone ODT files that don't support imports, returns an empty collection
     */
    Collection<LocalQuickFix> getRegisterImportQuickfixes(PsiCall callable);

    /**
     * Provides a quickfix to register a prefix
     */
    LocalQuickFix getRegisterPrefixQuickfix(String prefix, String namespace);

    /**
     * Returns true if the ODTFile is used as a statement to return a value
     */
    boolean isStatement();

    int getLineOffsetInParent();
}
