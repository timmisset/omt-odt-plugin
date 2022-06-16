package com.misset.opp.odt.builtin.commands;

import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;

public class TimeStampCommand extends BuiltInCommand {
    private TimeStampCommand() { }
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
        return OppModelConstants.XSD_INTEGER_INSTANCE;
    }
}
