package com.misset.opp.omt.meta.providers;

import com.misset.opp.resolvable.local.LocalCommand;

import java.util.HashMap;

public interface OMTLocalCommandProvider {
    HashMap<String, LocalCommand> getLocalCommandsMap();
}
