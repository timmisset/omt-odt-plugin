package com.misset.opp.ttl.testcase;

import com.intellij.psi.PsiFile;
import com.misset.opp.testcase.BasicTestCase;
import com.misset.opp.ttl.TTLFileType;
import com.misset.opp.ttl.psi.TTLFile;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Property;

public class TTLTestCase extends BasicTestCase<TTLFile> {
    public TTLTestCase() {
        super(TTLFileType.INSTANCE);
    }

    @Override
    protected TTLFile castToFile(PsiFile file) {
        return (TTLFile) file;
    }

    protected OntResource createResource(String localName) {
        return createResource("http://ontology#", localName);
    }

    protected Property createProperty(String localName) {
        return oppModel.getProperty(createResource("http://ontology#", localName));
    }

    protected OntClass createClass(String name) {
        return oppModel.getClass(createResource(name));
    }

    protected OntResource createXsdResource(String localName) {
        return createResource("http://www.w3.org/2001/XMLSchema#", localName);
    }

    protected OntResource createResource(String namespace, String localName) {
        return oppModel.getModel().createOntResource(namespace + localName);
    }
}
