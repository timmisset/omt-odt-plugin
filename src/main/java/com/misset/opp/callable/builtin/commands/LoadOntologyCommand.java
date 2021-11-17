
package com.misset.opp.callable.builtin.commands;

public class LoadOntologyCommand extends BuiltInCommand {
    private LoadOntologyCommand() {
    }

    public static final LoadOntologyCommand INSTANCE = new LoadOntologyCommand();

    @Override
    public String getDescription(String context) {
        return " Unlike normal ontologies, which are loaded with the OMT module, a locally defined ontology has to be explicitly </br>" +
                "loaded. A special ODT command `@LOAD_ONTOLOGY` is used to load an ontology in the current OMT activity, procedure, </br>" +
                "command, or other OMT object. This means that if two activities share data through a locally loaded ontology, </br>" +
                "they would both have to load the same ontology.";
    }

    @Override
    public String getName() {
        return "LOAD_ONTOLOGY";
    }

    @Override
    public boolean isVoid() {
        return true;
    }

    @Override
    public int minNumberOfArguments() {
        return 1;
    }

    @Override
    public int maxNumberOfArguments() {
        return 1;
    }
}
