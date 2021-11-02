package com.misset.opp.odt.syntax;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import com.intellij.openapi.util.NlsContexts;
import com.misset.opp.omt.OMTFileType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class ODTColorSettingsPage implements ColorSettingsPage {
    private static final HashMap<String, TextAttributesKey> enforcedAttributes = new HashMap<>();
    static {
        enforcedAttributes.put("call", ODTSyntaxHighlighter.BaseCallAttributesKey);
        enforcedAttributes.put("defineName", ODTSyntaxHighlighter.DefineAttributesKey);
    }
    @Override
    public @Nullable Icon getIcon() {
        return OMTFileType.ICON;
    }

    @Override
    public @NotNull SyntaxHighlighter getHighlighter() {
        return new ODTSyntaxHighlighter();
    }

    @Override
    public @NonNls @NotNull String getDemoText() {
        return "DEFINE QUERY <defineName>query</defineName>($param) => $param / ont:property / <call>call</call>;\n";
    }

    @Override
    public @Nullable Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return enforcedAttributes;
    }

    @Override
    public AttributesDescriptor @NotNull [] getAttributeDescriptors() {
        return ODTSyntaxHighlighter.getAttributes()
                .stream()
                .map(textAttributesKey -> new AttributesDescriptor(textAttributesKey.getExternalName(), textAttributesKey))
                .toArray(AttributesDescriptor[]::new);
    }

    @Override
    public ColorDescriptor @NotNull [] getColorDescriptors() {
        return new ColorDescriptor[0];
    }

    @Override
    public @NotNull @NlsContexts.ConfigurableName String getDisplayName() {
        return "ODT";
    }
}
