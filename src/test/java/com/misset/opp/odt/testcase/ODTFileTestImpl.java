package com.misset.opp.odt.testcase;

import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.misset.opp.odt.psi.impl.ODTFileImpl;
import com.misset.opp.resolvable.Callable;
import com.misset.opp.resolvable.Variable;
import com.misset.opp.resolvable.psi.PsiPrefix;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ODTFileTestImpl extends ODTFileImpl {

    HashMap<String, Collection<Callable>> mockCallables = new HashMap<>();
    HashMap<String, Collection<Variable>> mockVariables = new HashMap<>();

    HashMap<String, Collection<PsiPrefix>> mockPrefixes = new HashMap<>();
    private boolean isStatement = false;

    public ODTFileTestImpl(@NotNull FileViewProvider provider) {
        super(provider);
    }

    @Override
    public boolean isStatement() {
        return isStatement;
    }

    public void setIsStatement(boolean isStatement) {
        this.isStatement = isStatement;
    }

    @Override
    public Collection<Callable> listCallables() {
        return Stream.concat(super.listCallables().stream(), mockCallables.values().stream().flatMap(Collection::stream)).collect(Collectors.toList());
    }

    @Override
    public Collection<Variable> listVariables() {
        return Stream.concat(super.listVariables().stream(), mockVariables.values().stream().flatMap(Collection::stream)).collect(Collectors.toList());
    }

    @Override
    public Collection<PsiPrefix> listPrefixes() {
        return Stream.concat(super.listPrefixes().stream(), mockPrefixes.values().stream().flatMap(Collection::stream)).collect(Collectors.toList());
    }

    /**
     * Add a mock callable to the context of the ODT File
     */
    public void addCallable(Callable callable) {
        mockCallables.computeIfAbsent(callable.getCallId(), s -> new ArrayList<>()).add(callable);
    }

    public void addVariable(Variable variable) {
        mockVariables.computeIfAbsent(variable.getName(), s -> new ArrayList<>()).add(variable);
    }

    public void addPrefix(PsiPrefix prefix) {
        mockPrefixes.computeIfAbsent(prefix.getName(), s -> new ArrayList<>()).add(prefix);
    }

    @Override
    public boolean isAccessible(PsiElement source, PsiElement target) {
        return target.getClass().getName().contains("MockitoMock") || super.isAccessible(source, target);
    }
}
