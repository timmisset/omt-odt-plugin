package com.misset.opp.omt.documentation;

import java.util.Collections;
import java.util.List;

/**
 * Interface to indicate that the implementing class provides a String based documentation for the
 * DocumentationProvider. This string can contain HTML.
 * <p>
 * For MetaTypes, the OMTMetaTypeProvider should be used to determine the element at the caret.
 * For mappings, implement the OMTDocumented on the MetaMapType extension (for example OMTRulesMetaType)
 * For arrays, implement the OMTDocumented on the MetaType itself (for example OMTVariableMetaType)
 * <p>
 * When a meta-type can be used both in a map or array (for example, Actions), implement the documentation
 * on both the MetaMapType and the MetaType itself.
 */
public interface OMTDocumented {
    /**
     * Returns a list with additional headers that are present on the description level of the current element
     */
    default List<String> getAdditionalDescriptionHeaders() {
        return Collections.emptyList();
    }

    /**
     * Returns a list with additional headers that are present on the same level of the current element
     */
    default List<String> getAdditionalHeaders() {
        return Collections.emptyList();
    }

    default String getLevel1Header() {
        return "Classes";
    }

    String getDocumentationClass();
}
