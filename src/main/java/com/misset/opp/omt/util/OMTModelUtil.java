package com.misset.opp.omt.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.Pair;
import com.misset.opp.util.resources.JsonModelLoader;
import org.jetbrains.yaml.YAMLUtil;
import org.jetbrains.yaml.psi.YAMLFile;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLPsiElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class OMTModelUtil {

    private static final JsonObject EMPTY_JSON = new JsonObject();
    private static final Key<JsonObject> JSON_KEY = Key.create("JSON");

    public static final String ATTRIBUTES = "attributes";
    public static final String NODE = "node";
    public static final String NAME = "name";
    public static final String MAP = "map";
    public static final String TYPE = "type";
    public static final String ROOT = "root";
    public static final String DEF = "Def";
    public static final String REQUIRES_TAG_IDENTIFIER = "requiresTagIdentifier";
    public static final String SCRIPT = "script";

    /**
     * Returns the OMT model information about the current element
     * it will use the org.jetbrains.yaml.YAMLUtil#getConfigFullName(org.jetbrains.yaml.psi.YAMLPsiElement) to obtain a
     * dot seperated full path of the current node and map that against the known OMT model. If unknown, an empty JSON
     * will be returned
     */
    public static JsonObject getJson(YAMLPsiElement element) {
        final ASTNode node = element.getNode();
        if(node.getUserData(JSON_KEY) != null) {
            return node.getUserData(JSON_KEY);
        }

        final String configFullName = YAMLUtil.getConfigFullName(element);
        JsonObject jsonObject = JsonModelLoader.getModel()
                .getAsJsonObject(ROOT);

        List<String> keys = new ArrayList<>();
        for (String key : configFullName.split("\\.")) {
            keys.add(key);
            jsonObject = getChildJson(jsonObject, key);

            if(jsonObject.has(REQUIRES_TAG_IDENTIFIER)) {
                // the element is typed, must be retrieved as type from the model
                jsonObject = getByTag((YAMLFile) element.getContainingFile(), keys);
            }
        }

        node.putUserData(JSON_KEY, jsonObject);
        return jsonObject;
    }

    private static JsonObject getChildJson(JsonObject jsonObject,
                                    String key) {
        final Pair<String, Boolean> keyIsSequence = parseKeySequenceItem(key);
        key = keyIsSequence.getFirst();
        if (jsonObject.has(ATTRIBUTES)) {
            JsonObject childJson = jsonObject.getAsJsonObject(ATTRIBUTES)
                    .getAsJsonObject(key);
            if(childJson == null) { childJson = EMPTY_JSON; }
            if(keyIsSequence.getSecond() && childJson.has(TYPE)) {
                // for sequenceItems --> sequence[index], the item is part of the traversed path
                // the sequence itself not. The above getAsJsonObject will return the sequence information
                // from the model info, then continue to obtain an instance of that sequence as Json info for
                // sequence item itself.
                childJson = getDef(childJson.get(TYPE)
                        .getAsString());
            }
            return childJson;
        } else {
            final JsonElement node = jsonObject.get(NODE);
            if (node == null) {
                return EMPTY_JSON;
            }
            if (MAP.equals(node.getAsString()) && jsonObject.has(TYPE)) {
                return getDef(jsonObject.get(TYPE)
                        .getAsString());
            }
        }
        return EMPTY_JSON;
    }
    private static Pair<String, Boolean> parseKeySequenceItem(String key) {
        final String[] split = key.split("\\[");
        return Pair.create(split[0], split.length > 1);
    }

    private static JsonObject getDef(String key) {
        if (key.endsWith(DEF)) {
            key = key.substring(0, key.length() - DEF.length());
        }
        final JsonObject model = JsonModelLoader.getModel();
        return model.has(key) ? model.getAsJsonObject(key) : EMPTY_JSON;
    }

    private static JsonObject getByTag(YAMLFile file, List<String> keys) {
        return Optional.ofNullable(YAMLUtil.getQualifiedKeyInFile(file, keys))
                .map(OMTYamlUtil::getTagIdentifier)
                .map(s -> {
                    final JsonObject model = JsonModelLoader.getModel();
                    return model.has(s) ? model.getAsJsonObject(s) : EMPTY_JSON;
                })
                .orElse(EMPTY_JSON);
    }



}
