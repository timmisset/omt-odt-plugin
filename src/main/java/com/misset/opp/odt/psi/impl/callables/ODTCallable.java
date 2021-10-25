package com.misset.opp.odt.psi.impl.callables;

public interface ODTCallable {

    String getName();
    boolean isLocalCommand();
    boolean isBuiltinCommand();
    boolean hasPsiElement();

}
