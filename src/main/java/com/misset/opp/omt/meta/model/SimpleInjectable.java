package com.misset.opp.omt.meta.model;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Identify the OMTInjectable as being a simple Injectable which should not have semicolon ending and multiple lines
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface SimpleInjectable {
}
