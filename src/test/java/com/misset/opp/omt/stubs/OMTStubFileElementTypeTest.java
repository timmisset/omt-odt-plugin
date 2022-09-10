package com.misset.opp.omt.stubs;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBuilder;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IStubFileElementType;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.omt.testcase.OMTTestCase;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class OMTStubFileElementTypeTest extends OMTTestCase {


    private void hasStubOfClass(String content,
                                Class<? extends StubElement<? extends PsiElement>> stubClass) {
        final OMTFile omtFile = configureByText(content);
        ASTNode node = omtFile.getNode();
        Assertions.assertNotNull(node);
        IElementType type = node.getElementType();
        Assertions.assertTrue(type instanceof IStubFileElementType);

        IStubFileElementType stubFileType = (IStubFileElementType) type;
        StubBuilder builder = stubFileType.getBuilder();
        ReadAction.run(() -> {
            StubElement<? extends PsiElement> element = builder.buildStubTree(omtFile);
            final List<StubElement<? extends PsiElement>> childrenStubs = getAllStubs(element);
            Assertions.assertTrue(childrenStubs.stream()
                    .anyMatch(stub -> stubClass.isAssignableFrom(stub.getClass())));
        });
    }

    private List<StubElement<? extends PsiElement>> getAllStubs(StubElement<? extends PsiElement> element) {
        final ArrayList<StubElement<? extends PsiElement>> stubs = new ArrayList<>();
        stubs.add(element);
        element.getChildrenStubs().stream().map(stub -> getAllStubs(stub))
                .flatMap(Collection::stream)
                .forEach(o -> stubs.add((StubElement<? extends PsiElement>) o));
        return stubs;
    }

}
