package com.misset.opp.callable.global;

import com.misset.opp.callable.Variable;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;

import java.util.HashMap;
import java.util.Set;

public abstract class GlobalVariable implements Variable {

    private static final HashMap<String, GlobalVariable> globalVariableHashMap = new HashMap<>();

    private static final GlobalVariable MEDEWERKER_GRAPH = new GlobalVariable() {
        @Override
        public String getName() {
            return "$medewerkerGraph";
        }

        @Override
        public String getDescription() {
            return "Graph of the currently logged in user";
        }

        @Override
        public Set<OntResource> getType() {
            return Set.of(OppModel.INSTANCE.MEDEWERKER_GRAPH);
        }
    };
    private static final GlobalVariable USERNAME = new GlobalVariable() {
        @Override
        public String getName() {
            return "$username";
        }

        @Override
        public String getDescription() {
            return "Name of current user";
        }

        @Override
        public Set<OntResource> getType() {
            return Set.of(OppModel.INSTANCE.XSD_STRING_INSTANCE);
        }
    };
    private static final GlobalVariable OFFLINE = new GlobalVariable() {
        @Override
        public String getName() {
            return "$offline";
        }

        @Override
        public String getDescription() {
            return "Checks if the user is currently working in offline mode";
        }

        @Override
        public Set<OntResource> getType() {
            return Set.of(OppModel.INSTANCE.XSD_BOOLEAN_INSTANCE);
        }
    };

    static {
        globalVariableHashMap.put(MEDEWERKER_GRAPH.getName(), MEDEWERKER_GRAPH);
        globalVariableHashMap.put(USERNAME.getName(), USERNAME);
        globalVariableHashMap.put(OFFLINE.getName(), OFFLINE);
    }

    public static GlobalVariable getVariable(String name) {
        return globalVariableHashMap.get(name);
    }

}
