package com.misset.opp.omt.meta.arrays;

import com.misset.opp.omt.meta.OMTImportedMemberRefMetaType;

public class OMTImportedMemberRefArrayMetaType extends OMTSortedArrayMetaType {
    public OMTImportedMemberRefArrayMetaType() {
        super(new OMTImportedMemberRefMetaType());
    }
}
