package com.misset.opp.util.resources;

import com.google.common.io.CharStreams;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.openapi.extensions.PluginId;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class Helper {

    protected static ArrayList<String> getResources(List<String> names, String folder) {
        ClassLoader classLoader = getClassLoader();

        return names.stream()
                .map(name -> {
                    String format = String.format("%s/%s", folder, name);
                    InputStream inputStream = classLoader.getResourceAsStream(format);
                    try (Reader reader = new InputStreamReader(Objects.requireNonNull(inputStream))) {
                        return CharStreams.toString(reader);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return "";
                    }
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static ClassLoader getClassLoader() {
        return Objects.requireNonNull(PluginManagerCore.getPlugin(PluginId.getId("com.misset.OMT"))).getPluginClassLoader();
    }


}
