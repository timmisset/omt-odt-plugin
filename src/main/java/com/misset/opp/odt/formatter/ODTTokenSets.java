package com.misset.opp.odt.formatter;

import com.intellij.psi.tree.TokenSet;
import com.misset.opp.odt.psi.ODTTypes;

public final class ODTTokenSets {

    public static final TokenSet QUERY_TYPES = TokenSet.create(
            ODTTypes.QUERY_PATH, ODTTypes.QUERY_ARRAY, ODTTypes.BOOLEAN_STATEMENT, ODTTypes.EQUATION_STATEMENT
    );
    public static final TokenSet ASSIGNMENT_OPERATORS = TokenSet.create(
            ODTTypes.EQUALS, ODTTypes.ADD, ODTTypes.REMOVE
    );
    public static final TokenSet RELATIONAL_OPERATORS = TokenSet.orSet(ASSIGNMENT_OPERATORS, TokenSet.create(ODTTypes.CONDITIONAL_OPERATOR));
    public static final TokenSet CHOOSE_BLOCKS = TokenSet.create(ODTTypes.CHOOSE_BLOCK, ODTTypes.WHEN_PATH,
            ODTTypes.OTHERWISE_PATH, ODTTypes.END_PATH);

    private ODTTokenSets() {
    }
}
