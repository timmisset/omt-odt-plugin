package com.misset.opp.odt.formatter;

import com.intellij.psi.tree.TokenSet;
import com.misset.opp.odt.psi.ODTTypes;

public interface ODTTokenSets {

    TokenSet QUERY_TYPES = TokenSet.create(
            ODTTypes.QUERY_PATH, ODTTypes.QUERY_ARRAY, ODTTypes.BOOLEAN_STATEMENT, ODTTypes.EQUATION_STATEMENT
    );
}
