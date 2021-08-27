package com.misset.opp.omt.psi;

public interface OMTResettable {

    /**
     * Whenever the content of the psi file changes, the caches stored are reset. Only when running a single analysis / inspection
     * should the caches used by an OMTResettable be used
     */
    void reset();
}
