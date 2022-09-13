package com.misset.opp.odt;

import com.misset.opp.resolvable.Variable;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ODTGlobalVariable implements Variable {
    private static final HashMap<String, Variable> globalVariableHashMap = new HashMap<>();

    static {
        List<ODTGlobalVariable> globalVariables = List.of(
                new ODTGlobalVariable("$medewerkerGraph", "Graph of the currently logged in user", OppModelConstants.getMedewerkerGraph()),
                new ODTGlobalVariable("$username", "Name of current user", OppModelConstants.getXsdStringInstance()),
                new ODTGlobalVariable("$user", "Iri of current user", OppModelConstants.getOwlThingInstance()),
                new ODTGlobalVariable("$rollen", "The current roles of the user", OppModelConstants.getJsonObject()),
                new ODTGlobalVariable("$offline", "Checks if the user is currently working in offline mode", OppModelConstants.getXsdBooleanInstance()),
                new ODTGlobalVariable("$heeftPreviewRol", "Checks if the user can preview functionality not ready for production", OppModelConstants.getXsdBooleanInstance()),
                new ODTGlobalVariable("$activityConfig", "Returns specific activity configuration based on the context", OppModelConstants.getJsonObject()),
                new ODTGlobalVariable("$geoApiKey", "The API key used for the GeoViewer plugin", OppModelConstants.getXsdStringInstance())
        );
        globalVariableHashMap.putAll(globalVariables.stream().collect(Collectors.toMap(ODTGlobalVariable::getName, gv -> gv)));
    }

    private final String name;
    private final String description;
    private final OntResource type;

    private ODTGlobalVariable(String name, String description, OntResource type) {

        this.name = name;
        this.description = description;
        this.type = type;
    }

    public static Variable getVariable(String name) {
        return globalVariableHashMap.get(name);
    }

    public static Collection<Variable> getVariables() {
        return globalVariableHashMap.values();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Set<OntResource> resolve() {
        return Set.of(type);
    }

    @Override
    public boolean isReadonly() {
        return true;
    }

    @Override
    public boolean isGlobal() {
        return true;
    }

    @Override
    public String getSource() {
        return "Global";
    }

    @Override
    public Scope getScope() {
        return Scope.GLOBAL;
    }

}