package com.misset.opp.omt.meta.arrays;

import com.misset.opp.omt.meta.OMTExportMemberMetaType;

public class OMTExportArrayMetaType extends OMTSortedArrayMetaType {
    public OMTExportArrayMetaType() {
        super(new OMTExportMemberMetaType());
    }
}
