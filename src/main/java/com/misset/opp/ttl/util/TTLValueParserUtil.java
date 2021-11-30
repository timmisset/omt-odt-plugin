package com.misset.opp.ttl.util;

import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Literal;

public class TTLValueParserUtil {

    public static OntResource parseValue(String value) {
        final Literal ttl = OppModel.INSTANCE.getModel().createLiteral(value);
        return OppModel.INSTANCE.getResource(ttl.getDatatypeURI());
    }

}
