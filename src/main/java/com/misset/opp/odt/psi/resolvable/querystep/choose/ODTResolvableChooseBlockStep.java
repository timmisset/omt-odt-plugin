package com.misset.opp.odt.psi.resolvable.querystep.choose;

import com.misset.opp.odt.psi.ODTOtherwisePath;
import com.misset.opp.odt.psi.ODTWhenPath;
import com.misset.opp.odt.psi.resolvable.querystep.ODTResolvableQueryStep;

import java.util.List;

public interface ODTResolvableChooseBlockStep extends ODTResolvableQueryStep {

    List<ODTWhenPath> getWhenPathList();

    ODTOtherwisePath getOtherwisePath();

}
