package com.misset.opp.ttl;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ResourceUtil {

    private static final Model model = ModelFactory.createDefaultModel();

    /**
     * Namespace XSD is used to define primitives
     * Use fromXsd(string) => http://www.w3.org/2001/XMLSchema#string;
     */
    public static final String XSD = "http://www.w3.org/2001/XMLSchema#";

    /**
     * Returns a list with fully qualified resource named from an array of local names
     */
    public static List<Resource> fromXsd(String ... localNames) {
        return Arrays.stream(localNames).map(
                s -> model.createResource(XSD + s)
        ).collect(Collectors.toList());
    }



}
