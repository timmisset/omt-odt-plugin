
package com.misset.opp.odt.builtin.commands;

import com.intellij.openapi.project.Project;

import java.util.List;

public class LoadOntologyCommand extends AbstractBuiltInCommand {

    protected static final String DESCRIPTION = "Unlike normal ontologies, which are loaded with the OMT module, a locally defined ontology has to be explicitly </br>" +
            "loaded. A special ODT command `@LOAD_ONTOLOGY` is used to load an ontology in the current OMT activity, procedure, </br>" +
            "command, or other OMT object. This means that if two activities share data through a locally loaded ontology, </br>" +
            "they would both have to load the same ontology.";
    private static final List<String> PARAMETER_NAMES = List.of("ontology");

    private LoadOntologyCommand() {
    }

    public static final LoadOntologyCommand INSTANCE = new LoadOntologyCommand();

    @Override
    public String getDescription(Project project) {
        return DESCRIPTION;
    }

    @Override
    public String getName() {
        return "LOAD_ONTOLOGY";
    }

    @Override
    public int minNumberOfArguments() {
        return 1;
    }

    @Override
    public int maxNumberOfArguments() {
        return 1;
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
