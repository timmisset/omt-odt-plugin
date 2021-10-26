package com.misset.opp.omt.meta.providers;

import com.misset.opp.callable.Callable;

import java.util.HashMap;

public interface OMTLocalCommandProvider {
    HashMap<String, Callable> getLocalCommandsMap();
}
