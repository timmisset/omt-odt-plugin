package com.misset.opp.util;

import com.intellij.openapi.util.IconLoader;
import com.misset.opp.omt.OMTFileType;

import javax.swing.*;

public class Icons {

    private Icons() {
        // empty constructor
    }

    public static final Icon TTLFile = IconLoader.findIcon("/icons/foaflogo-svg-16.png", Icons.class.getClassLoader());

    public static final Icon PLUGIN_ICON = IconLoader.findIcon("/icons/cartman-16.png", OMTFileType.class.getClassLoader());
}
