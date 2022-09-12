package com.misset.opp.odt.testcase;

import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.misset.opp.odt.ODTFileType;
import com.misset.opp.odt.ODTParserDefinition;
import com.misset.opp.odt.psi.resolvable.callable.ODTDefineStatement;
import com.misset.opp.testcase.BasicTestCase;
import com.misset.opp.testcase.CompletionUtil;
import com.misset.opp.testcase.InspectionUtil;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class ODTTestCase extends BasicTestCase<ODTFileTestImpl> {

    static {
        ODTParserDefinition.setFileViewProvider2ODTFile(ODTFileTestImpl::new);
    }

    protected InspectionUtil inspection;
    protected CompletionUtil completion;

    public ODTTestCase() {
        super(ODTFileType.INSTANCE);
    }

    @BeforeEach
    public void setUp() {
        super.setUp();
        setOntologyModel();
        inspection = new InspectionUtil(myFixture);
        completion = new CompletionUtil(myFixture);
    }

    @Override
    protected ODTFileTestImpl castToFile(PsiFile file) {
        return (ODTFileTestImpl) file;
    }

    protected String withPrefixes(String content) {
        String prefixes = testPrefixes.entrySet().stream()
                .map(entry -> String.format("PREFIX %s: <%s>;", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("\n"));
        return prefixes + "\n" + content;
    }

    protected OntResource resolveQueryStatementToSingleResult(String query) {
        return resolveQueryStatement(query).stream().findFirst().orElse(null);
    }

    protected Set<OntResource> resolveQueryStatement(String query) {
        // adding <caret> is required to make sure the fixture focus is on the injected ODT fragment
        // otherwise the findElementByText will return null
        String content = withPrefixes("DEFINE QUERY query => <caret>" + query);
        configureByText(content);
        return ReadAction.compute(() -> myFixture.findElementByText("query", ODTDefineStatement.class).resolve());
    }

    protected Set<OntResource> resolveQueryAtCaret(String content) {
        configureByText(content);
        return ReadAction.compute(() -> {
            final PsiElement elementAtCaret = myFixture.getElementAtCaret();
            if (elementAtCaret instanceof ODTDefineStatement) {
                return ((ODTDefineStatement) elementAtCaret).resolve();
            } else {
                Assertions.fail("Could not resolve query");
            }
            return Collections.emptySet();
        });
    }

    protected Set<String> getUris(Set<OntResource> resources) {
        return resources.stream().map(Resource::getURI).collect(Collectors.toSet());
    }

}
