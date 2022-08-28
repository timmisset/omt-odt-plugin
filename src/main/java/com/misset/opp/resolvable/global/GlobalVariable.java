package com.misset.opp.resolvable.global;

import com.misset.opp.resolvable.Variable;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public abstract class GlobalVariable implements Variable {

    private static final HashMap<String, Variable> globalVariableHashMap = new HashMap<>();

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
        public @NotNull Set<OntResource> resolve() {
            return Set.of(OppModelConstants.MEDEWERKER_GRAPH);
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
        public @NotNull Set<OntResource> resolve() {
            return Set.of(OppModelConstants.XSD_STRING_INSTANCE);
        }
    };

    private static final GlobalVariable USER = new GlobalVariable() {
        @Override
        public String getName() {
            return "$user";
        }

        @Override
        public String getDescription() {
            return "Iri of current user";
        }

        @Override
        public @NotNull Set<OntResource> resolve() {
            return Set.of(OppModelConstants.OWL_THING_INSTANCE);
        }
    };

    private static final GlobalVariable ROLES = new GlobalVariable() {
        @Override
        public String getName() {
            return "$rollen";
        }

        @Override
        public String getDescription() {
            return "The current roles of the user";
        }

        @Override
        public @NotNull Set<OntResource> resolve() {
            return Set.of(OppModelConstants.JSON_OBJECT);
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
        public @NotNull Set<OntResource> resolve() {
            return Set.of(OppModelConstants.XSD_BOOLEAN_INSTANCE);
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
        public @NotNull Set<OntResource> resolve() {
            return Set.of(OppModelConstants.XSD_BOOLEAN_INSTANCE);
        }
    };

    private static final GlobalVariable ACTIVITY_CONFIG = new GlobalVariable() {
        @Override
        public String getName() {
            return "$activityConfig";
        }

        @Override
        public String getDescription() {
            return "Returns specific activity configuration based on the context";
        }

        @Override
        public @NotNull Set<OntResource> resolve() {
            return Set.of(OppModelConstants.JSON_OBJECT);
        }
    };

    private static final GlobalVariable GEO_API_KEY = new GlobalVariable() {
        @Override
        public String getName() {
            return "$geoApiKey";
        }

        @Override
        public String getDescription() {
            return "The API key used for the GeoViewer plugin";
        }

        @Override
        public @NotNull Set<OntResource> resolve() {
            return Set.of(OppModelConstants.XSD_STRING_INSTANCE);
        }
    };

    static {
        globalVariableHashMap.put(MEDEWERKER_GRAPH.getName(), MEDEWERKER_GRAPH);
        globalVariableHashMap.put(USERNAME.getName(), USERNAME);
        globalVariableHashMap.put(USER.getName(), USER);
        globalVariableHashMap.put(ROLES.getName(), ROLES);
        globalVariableHashMap.put(OFFLINE.getName(), OFFLINE);
        globalVariableHashMap.put(HEEFT_PREVIEW_ROL.getName(), HEEFT_PREVIEW_ROL);
        globalVariableHashMap.put(ACTIVITY_CONFIG.getName(), ACTIVITY_CONFIG);
        globalVariableHashMap.put(GEO_API_KEY.getName(), GEO_API_KEY);
    }

    public static Variable getVariable(String name) {
        return globalVariableHashMap.get(name);
    }

    public static Collection<Variable> getVariables() {
        return globalVariableHashMap.values();
    }

}
