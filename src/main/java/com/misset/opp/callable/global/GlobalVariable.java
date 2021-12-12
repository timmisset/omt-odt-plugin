package com.misset.opp.callable.global;

import com.misset.opp.callable.Variable;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public abstract class GlobalVariable implements Variable {

    private static final HashMap<String, GlobalVariable> globalVariableHashMap = new HashMap<>();

    @Override
    public boolean isReadonly() {
        return true;
    }

    @Override
    public boolean isGlobal() {
        return true;
    }

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

    private static final GlobalVariable HEEFT_PREVIEW_ROL = new GlobalVariable() {
        @Override
        public String getName() {
            return "$heeftPreviewRol";
        }

        @Override
        public String getDescription() {
            return "Checks if the user can preview functionality not ready for production";
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
        globalVariableHashMap.put(HEEFT_PREVIEW_ROL.getName(), HEEFT_PREVIEW_ROL);
    }

    public static GlobalVariable getVariable(String name) {
        return globalVariableHashMap.get(name);
    }

    public static Collection<GlobalVariable> getVariables() {
        return globalVariableHashMap.values();
    }

}
