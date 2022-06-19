package com.misset.opp.omt.meta.arrays;

import com.misset.opp.omt.meta.OMTExportMemberMetaType;

public class OMTExportArrayMetaType extends OMTSortedArrayMetaType {

    private static final OMTExportArrayMetaType INSTANCE = new OMTExportArrayMetaType();

    public static OMTExportArrayMetaType getInstance() {
        return INSTANCE;
    }

    private OMTExportArrayMetaType() {
        super(OMTExportMemberMetaType.getInstance());
    }
}
