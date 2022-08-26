package com.misset.opp.resolvable;

/**
 * Resolvable Variable
 * Global and Local variables that are not part of the PsiTree should implement this interface
 */
public interface Variable extends Resolvable {

    /**
     * Returns the source where this variable was declared
     * For example, 'ODT Script', 'Global', 'from Command ...' etc
     */
    String getSource();

    String getName();

    String getDescription();

    default boolean isParameter() {
        return false;
    }

    default boolean isReadonly() {
        return false;
    }

    default boolean isGlobal() {
        return false;
    }

    Scope getScope();

    enum Scope {
        GLOBAL, LOCAL
    }

}
