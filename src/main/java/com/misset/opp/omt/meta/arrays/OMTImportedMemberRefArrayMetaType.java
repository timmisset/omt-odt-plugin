package com.misset.opp.omt.meta.arrays;

import com.misset.opp.omt.meta.OMTImportedMemberRefMetaType;

public class OMTImportedMemberRefArrayMetaType extends OMTSortedArrayMetaType {
    private static final OMTImportedMemberRefArrayMetaType INSTANCE = new OMTImportedMemberRefArrayMetaType();

    private OMTImportedMemberRefArrayMetaType() {
        super(OMTImportedMemberRefMetaType.getInstance());
    }

    public static OMTImportedMemberRefArrayMetaType getInstance() {
        return INSTANCE;
    }
}
