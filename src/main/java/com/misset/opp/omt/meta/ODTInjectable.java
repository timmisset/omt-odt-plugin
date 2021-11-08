package com.misset.opp.omt.meta;

import com.intellij.openapi.util.TextRange;
import org.jetbrains.yaml.psi.impl.YAMLScalarImpl;

import java.util.List;

public interface ODTInjectable {

    default List<TextRange> getTextRanges(YAMLScalarImpl host) {
        int startOffset = host.getContentRanges()
                .stream()
                .map(TextRange::getStartOffset)
                .sorted()
                .findFirst()
                .orElse(0);
        int endOffset = host.getTextLength();
        return List.of(TextRange.create(startOffset, endOffset));
    }

}
