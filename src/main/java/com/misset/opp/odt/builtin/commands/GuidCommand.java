package com.misset.opp.odt.builtin.commands;

import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;

import java.util.Collections;
import java.util.List;

public class GuidCommand extends AbstractBuiltInCommand {
    private GuidCommand() {
    }

    public static final GuidCommand INSTANCE = new GuidCommand();

    @Override
    public String getName() {
        return "GUID";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    public OntResource resolveSingle() {
        return OppModelConstants.getXsdStringInstance();
    }

    @Override
    protected List<String> getParameters() {
        return Collections.emptyList();
    }
}
