package com.misset.opp.util.resources;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Arrays;
import java.util.List;

/**
 * The JsonModelLoader will add all json files describing the OMT model
 * The
 */
public class JsonModelLoader extends Helper {

    private static final JsonObject parsedModel = new JsonObject();

    private static void addToJson(JsonElement jsonElement) {
        JsonObject asObject = (JsonObject) jsonElement;
        if (asObject.has("name")) {
            parsedModel.add(asObject.get("name").getAsString(), asObject);
        } else if (asObject.has("key")) {
            parsedModel.add(asObject.get("key").getAsString(), asObject);
        }
    }

    public static JsonObject getModel() {
        if (parsedModel.size() == 0) {
            loadModelAttributes();
        }
        return parsedModel;
    }

    /**
     * Load the model (attributes) from the json files
     */
    private static void loadModelAttributes() {
        List<String> rootItems = Arrays.asList(
                "action.json", "binding.json", "declare.json", "graphSelection.json",
                "module.json", "onChange.json", "param.json", "payload.json",
                "root.json", "rules.json", "queryWatcher.json", "service.json", "variable.json"
        );
        List<String> modelItems = Arrays.asList("activity.json", "component.json", "modelItem.json", "ontology.json", "procedure.json", "standaloneQuery.json");
        List<String> handlers = Arrays.asList("handler.json", "mergePredicates.json", "mergeValidation.json", "mergeLists.json");

        List<String> files = getResources(rootItems, "model");
        files.addAll(getResources(modelItems, "model/modelItems"));
        files.addAll(getResources(handlers, "model/handlers"));

        for (String content : files) {
            JsonElement jsonElement = JsonParser.parseString(content);
            if (jsonElement.isJsonArray()) {
                ((JsonArray) jsonElement).forEach(JsonModelLoader::addToJson);
            } else {
                addToJson(jsonElement);
            }
        }
    }

    public static boolean hasValue(JsonObject jsonObject, String key, Object value) {
        return jsonObject.has(key) && jsonObject.get(key).equals(value);
    }
    public static <T extends JsonElement> JsonElement getOrDefault(JsonObject jsonObject, String key, T defaultValue) {
        return getOrDefault(jsonObject, key, defaultValue, JsonElement.class);
    }
    public static <T extends JsonElement> T getOrDefault(JsonObject jsonObject, String key, T defaultValue, Class<T> clazz) {
        if(jsonObject.has(key)) { return clazz.cast(jsonObject.get(key)); }
        return defaultValue;
    }

}
