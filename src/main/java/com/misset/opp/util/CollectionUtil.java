package com.misset.opp.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CollectionUtil {

    /**
     * Method to help build a grouped map where items are appended to a list to keep track of the number
     * of items that match a certain key. When the key is not yet present a new ArrayList will be created
     */
    public static <S, T> void addToGroupedMap(S key, T itemToAdd, HashMap<S, List<T>> map) {
        if(key == null) { return; }
        final List<T> orDefault = map.getOrDefault(key, new ArrayList<>());
        orDefault.add(itemToAdd);
        map.put(key, orDefault);
    }

}
