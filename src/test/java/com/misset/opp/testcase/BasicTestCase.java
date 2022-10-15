package com.misset.opp.testcase;

import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import com.misset.opp.model.OntologyModel;
import com.misset.opp.model.OntologyModelLoader;
import com.misset.opp.odt.psi.impl.ODTFileImpl;
import com.misset.opp.omt.psi.OMTFile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class BasicTestCase<T extends PsiFile> extends LightJavaCodeInsightFixtureTestCase {

    protected OntologyModel ontologyModel;

    /**
     * Set with prefix/namespace declarations used during tests
     * Key: Prefix
     * Value: Namespace, not! wrapped with <>
     */
    protected final static Map<String, String> testPrefixes = new HashMap<>();

    static {
        testPrefixes.put("ont", "http://ontology#");
        testPrefixes.put("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
        testPrefixes.put("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
        testPrefixes.put("owl", "http://www.w3.org/2002/07/owl#");
        testPrefixes.put("xsd", "http://www.w3.org/2001/XMLSchema#");
        testPrefixes.put("unique", "http://unique#");
        testPrefixes.put("sh", "http://www.w3.org/ns/shacl#");
    }

    @BeforeEach
    protected void setUp() {
        try {
            super.setUp();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    protected void tearDown() {
        try{
            super.tearDown();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final FileType fileType;
    public BasicTestCase(FileType fileType) {
        this.fileType = fileType;
    }

    private static int files = 0;

    protected String getFileName() {
        return getFileName(fileType.getDefaultExtension());
    }

    protected String getFileName(String extension) {
        return String.format("test-%s.%s", files++, extension);
    }

    protected T configureByText(String content) {
        return configureByText(getFileName(), content, false);
    }

    protected T configureByText(String fileName,
                                String content) {
        return configureByText(fileName, content, false);
    }

    protected T configureByText(String content,
                                boolean acceptErrorElements) {
        return configureByText(getFileName(), content, acceptErrorElements);
    }

    protected T addFileToProject(String relativePath, String content) {
        PsiFile psiFile = myFixture.addFileToProject(relativePath, content);
        T typedFile = castToFile(psiFile);
        buildIndexes(typedFile);
        return typedFile;
    }

    protected abstract T castToFile(PsiFile file);

    protected void buildIndexes(T file) {
    }

    protected OMTFile getContainingOMTFile(ODTFileImpl file) {
        final InjectedLanguageManager instance = InjectedLanguageManager.getInstance(file.getProject());
        return (OMTFile) ReadAction.compute(() -> instance.getInjectionHost(file).getContainingFile());
    }

    protected T configureByText(String fileName,
                                String content,
                                boolean acceptErrorElements) {
        if (myFixture == null) {
            fail("Fixture is not defined, call super.setUp()");
        }
        final PsiFile psiFile = myFixture.configureByText(fileName, content);
        if (!acceptErrorElements && ReadAction.compute(() -> PsiTreeUtil.hasErrorElements(psiFile))) {
            String errorMessage = ReadAction.compute(() -> PsiTreeUtil.findChildrenOfType(psiFile,
                            PsiErrorElement.class)
                    .stream()
                    .map(psiErrorElement -> String.format("description: %s, error element: %s",
                            psiErrorElement.getErrorDescription(),
                            psiErrorElement.getText()))
                    .collect(Collectors.joining("\n")));
            fail(String.format("Configured PsiFile has an error element: %n%s%n%n%s", errorMessage, ReadAction.compute(
                    psiFile::getText)));
        }
        final T file = castToFile(psiFile);
        buildIndexes(file);
        return file;
    }

    protected void underProgress(Runnable runnable) {
        ProgressManager.getInstance().runProcessWithProgressSynchronously(
                runnable, "Test", false, getProject()
        );
    }

    /**
     * When the ontology is required in a test that is not inheriting from OMTOntologyTestCase
     * this method can be used to load the ontology and return the OntologyModel
     */
    public static OntologyModel initOntologyModel(Project project) {
        return OntologyModelLoader.getInstance(project).read(getRootPath());
    }

    private static File getRootPath() {
        return getRootPath("model", "root.ttl");
    }

    private static File getRootPath(String folder, String file) {
        Path resourceDirectory = Paths.get("src", "test", "resources", folder, file);
        return resourceDirectory.toFile();
    }

    protected void setOntologyModel() {
        ontologyModel = initOntologyModel(getProject());
    }

    protected String createOntologyUri(String localName) {
        return "http://ontology#" + localName;
    }

}
