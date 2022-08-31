package com.misset.opp.odt.builtin.commands;

import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;

import java.util.Collections;
import java.util.List;

public class TimeStampCommand extends AbstractBuiltInCommand {
    private TimeStampCommand() {
    }

    public static final TimeStampCommand INSTANCE = new TimeStampCommand();

    @Override
    public String getName() {
        return "TIMESTAMP";
    }

    @Override
    public boolean isVoid() {
        return false;
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    public OntResource resolveSingle() {
        return OppModelConstants.getXsdIntegerInstance();
    }

    @Override
    protected List<String> getParameters() {
        return Collections.emptyList();
    }
}
