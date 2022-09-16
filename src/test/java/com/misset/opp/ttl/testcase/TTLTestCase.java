package com.misset.opp.ttl.testcase;

import com.intellij.psi.PsiFile;
import com.misset.opp.testcase.BasicTestCase;
import com.misset.opp.ttl.TTLFileType;
import com.misset.opp.ttl.psi.TTLFile;

import java.util.stream.Collectors;

public abstract class TTLTestCase extends BasicTestCase<TTLFile> {
    public TTLTestCase() {
        super(TTLFileType.INSTANCE);
    }

    @Override
    protected TTLFile castToFile(PsiFile file) {
        return (TTLFile) file;
    }

    protected String withPrefixes(String content) {
        String prefixes = testPrefixes.entrySet().stream()
                .map(entry -> String.format("@prefix %s: <%s> .\n", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining());
        return prefixes + content;
    }

}
