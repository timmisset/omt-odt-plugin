package com.misset.opp.callable.builtin;

import com.misset.opp.callable.Callable;

public abstract class Builtin implements Callable {

    @Override
    public String getDescription(String context) {
        // todo: load from the markdown
        return "";
    }

    @Override
    public int maxNumberOfArguments() {
        // by default, the max number of arguments equals the min
        return minNumberOfArguments();
    }

    @Override
    public int minNumberOfArguments() {
        // by default, the number of arguments is 1
        return 1;
    }
}
