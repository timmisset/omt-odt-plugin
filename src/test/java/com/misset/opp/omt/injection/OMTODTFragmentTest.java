package com.misset.opp.omt.injection;

import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.omt.meta.model.modelitems.OMTActivityMetaType;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.omt.psi.impl.yaml.YAMLOMTScalarList;
import com.misset.opp.resolvable.local.LocalCommand;
import com.misset.opp.resolvable.psi.PsiCallable;
import com.misset.opp.testCase.OMTTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class OMTODTFragmentTest extends OMTTestCase {
    @Test
    void testAddsCallablesFromContext() {
        OMTFile omtFile = configureByText("import:\n" +
                "   myFile.omt:\n" +
                "   - myCallable\n" +
                "\n" +
                "commands: |\n" +
                "   <caret>DEFINE COMMAND command => { myCallable(); }\n");
        ReadAction.run(() -> {
            OMTODTFragment injectedFragment = getInjectedFragment(omtFile);
            Collection<PsiCallable> psiCallables = injectedFragment.listPsiCallables();

            assertEquals(2, psiCallables.size());
            assertTrue(psiCallables.stream().anyMatch(callable -> callable.getCallId().equals("myCallable")));
        });
    }

    @Test
    void testAddsLocalCommands() {
        String content = insideActivityWithPrefixes("onStart: |\n" +
                "   @LOG('test');");
        OMTFile omtFile = configureByText(content);
        ReadAction.run(() -> {
            OMTODTFragment injectedFragment = getInjectedFragment(omtFile);
            HashMap<String, LocalCommand> localCommandsMap = OMTActivityMetaType.getInstance().getLocalCommandsMap();

            localCommandsMap.values().forEach(
                    localCommand -> assertFalse(injectedFragment.getCallables(localCommand.getCallId()).isEmpty())
            );
        });
    }

    @Test
    void testAddsCallables() {
        String content = insideActivityWithPrefixes("onStart: |\n" +
                "   @LOG('test');", "MyActivity");
        OMTFile omtFile = configureByText(content);
        ReadAction.run(() -> {
            OMTODTFragment injectedFragment = getInjectedFragment(omtFile);
            assertFalse(injectedFragment.getCallables("@MyActivity").isEmpty());
        });
    }

    @Test
    void testAddsPrefixes() {
        String content = insideActivityWithPrefixes("onStart: |\n" +
                "   @LOG('test');", "MyActivity");
        OMTFile omtFile = configureByText(content);
        ReadAction.run(() -> {
            OMTODTFragment injectedFragment = getInjectedFragment(omtFile);
            Map<String, String> availableNamespaces = injectedFragment.getAvailableNamespaces();
            testPrefixes.forEach((key, value) -> assertEquals(key, availableNamespaces.get(value)));
        });
    }

    private OMTODTFragment getInjectedFragment(OMTFile omtFile) {
        PsiLanguageInjectionHost childOfType = PsiTreeUtil.findChildOfType(omtFile, YAMLOMTScalarList.class);
        List<Pair<PsiElement, TextRange>> injectedPsiFiles = InjectedLanguageManager.getInstance(getProject()).getInjectedPsiFiles(childOfType);
        PsiElement injectedFragment = injectedPsiFiles.iterator().next().getFirst();
        assertTrue(injectedFragment instanceof OMTODTFragment);
        return (OMTODTFragment) injectedFragment;
    }
}
