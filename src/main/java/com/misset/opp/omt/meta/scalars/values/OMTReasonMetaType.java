package com.misset.opp.omt.meta.scalars.values;

import com.misset.opp.omt.startup.LoadReasonsStartupActivity;

import java.util.HashMap;
import java.util.Set;

public class OMTReasonMetaType extends OMTFixedValueScalarMetaType {
    public OMTReasonMetaType() {
        super("Reason");
    }

    @Override
    Set<String> getAcceptableValues() {
        return LoadReasonsStartupActivity.getReasons().keySet();
    }

    @Override
    protected HashMap<String, String> getDescribedValues() {
        return LoadReasonsStartupActivity.getReasons();
    }
}
