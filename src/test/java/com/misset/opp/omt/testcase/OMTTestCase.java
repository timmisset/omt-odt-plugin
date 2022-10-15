package com.misset.opp.omt.testcase;

import com.intellij.application.options.CodeStyle;
import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNamedElement;
import com.misset.opp.indexing.PrefixIndex;
import com.misset.opp.odt.psi.ODTCallName;
import com.misset.opp.odt.psi.impl.ODTFileImpl;
import com.misset.opp.odt.psi.resolvable.call.ODTCall;
import com.misset.opp.omt.OMTFileType;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegateFactory;
import com.misset.opp.omt.startup.IndexOMTPrefixes;
import com.misset.opp.testcase.BasicTestCase;
import com.misset.opp.testcase.CompletionUtil;
import com.misset.opp.testcase.InspectionUtil;
import org.jetbrains.yaml.formatter.YAMLCodeStyleSettings;
import org.jetbrains.yaml.psi.YAMLPsiElement;
import org.junit.jupiter.api.BeforeEach;

import java.util.stream.Collectors;

public abstract class OMTTestCase extends BasicTestCase<OMTFile> {

    protected InspectionUtil inspection;
    protected CompletionUtil completion;

    @BeforeEach
    public void setUp() {
        super.setUp();
        PrefixIndex.clear();
        setOntologyModel();
        inspection = new InspectionUtil(myFixture);
        completion = new CompletionUtil(myFixture);
    }

    public OMTTestCase() {
        super(OMTFileType.INSTANCE);
    }

    /**
     * Provides prefixes ont, rdf and rdfs in a root prefixes block
     */
    protected String withPrefixes(String content) {
        String prefixes = testPrefixes.entrySet().stream()
                .map(entry -> String.format("    %s: <%s>\n", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining());
        return String.format("prefixes:\n%s\n%s", prefixes, content);
    }

    protected String insideProcedureRunWithPrefixes(String onRunContent) {
        onRunContent = onRunContent.replace("\n", "\n            "); // alignment
        return withPrefixes(String.format("model:\n" +
                        "    Procedure: !Procedure\n" +
                        "        onRun: |\n" +
                        "            %s",
                onRunContent));
    }

    protected String insideActivityWithPrefixes(String activityContent) {
        return insideActivityWithPrefixes(activityContent, "Activiteit");
    }

    protected String insideActivityWithPrefixes(String activityContent,
                                                String activityName) {
        activityContent = activityContent.replace("\n", "\n        "); // alignment
        return withPrefixes(String.format("model:\n" + "    %s: !Activity\n" + "        %s",
                activityName,
                activityContent));
    }

    protected String insideComponentWithPrefixes(String componentContent) {
        componentContent = componentContent.replace("\n", "\n        "); // alignment
        return withPrefixes(
                String.format("model:\n" +
                        "    Component: !Component\n" +
                        "        %s", componentContent));
    }

    protected String insideStandaloneQueryWithPrefixes(String componentContent) {
        componentContent = componentContent.replace("\n", "\n        "); // alignment
        return withPrefixes(
                String.format("model:\n" +
                        "    StandaloneQuery: !StandaloneQuery\n" +
                        "        %s\n", componentContent));
    }

    /**
     * Places the input statement into a template with common prefixes and inside a queries block
     *
     * @param queryStatement only the queryStatement without the define and semicolon
     */
    protected String insideQueryWithPrefixes(String queryStatement, String... params) {
        return insideQueryWithPrefixesNoSemicolonEnding(queryStatement) + ";";

    }

    protected String insideQueryWithPrefixesNoSemicolonEnding(String queryStatement, String... params) {
        return withPrefixes(String.format("queries: |\n" +
                "   DEFINE QUERY query(%s) => %s", String.join(", ", params), queryStatement));
    }

    protected ODTCall getCallByName(String callName) {
        final ODTCallName elementByText = myFixture.findElementByText(callName, ODTCallName.class);
        if (elementByText != null) {
            return (ODTCall) elementByText.getParent();
        }
        fail("Could not find call by name: " + callName + ", make sure the caret is located within an ODT script and includes " +
                "the @ if it's a Command callname");
        return null;
    }

    @Override
    protected OMTFile castToFile(PsiFile file) {
        if (file instanceof ODTFileImpl) {
            // the fixture returns the nested ODT file instead of the containing OMT file
            return getContainingOMTFile((ODTFileImpl) file);
        }
        return (OMTFile) file;
    }

    @Override
    protected void buildIndexes(OMTFile file) {
        ReadAction.run(() -> {
            IndexOMTPrefixes.analyse(file);
            PrefixIndex.orderIndexByFrequency();
        });
    }

    protected PsiNamedElement getDelegateAtCaret() {
        return ReadAction.compute(() -> {
            PsiElement elementAtCaret = myFixture.getElementAtCaret();
            if (elementAtCaret instanceof YAMLPsiElement) {
                return OMTYamlDelegateFactory.createDelegate((YAMLPsiElement) elementAtCaret);
            }
            return null;
        });
    }

    protected void setYamlIndentation(OMTFile file) {
        YAMLCodeStyleSettings yamlCodeStyleSettings = CodeStyle.getCustomSettings(file, YAMLCodeStyleSettings.class);
        yamlCodeStyleSettings.INDENT_SEQUENCE_VALUE = false;
    }
}
