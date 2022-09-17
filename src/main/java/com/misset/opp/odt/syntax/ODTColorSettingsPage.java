package com.misset.opp.odt.syntax;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import com.intellij.openapi.util.NlsContexts;
import com.misset.opp.odt.ODTSyntaxHighlighter;
import com.misset.opp.util.Icons;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.misset.opp.odt.ODTSyntaxHighlighter.*;

public class ODTColorSettingsPage implements ColorSettingsPage {

    private static final HashMap<String, TextAttributesKey> tags = new HashMap<>();
    static {
        tags.put("global", GlobalVariable);
        tags.put("readonly", ODTSyntaxHighlighter.ReadonlyVariable);
        tags.put("param", ODTSyntaxHighlighter.Parameter);
    }

    @Override
    public @Nullable Icon getIcon() {
        return Icons.PLUGIN_ICON;
    }

    @Override
    public @NotNull SyntaxHighlighter getHighlighter() {
        return new ODTSyntaxHighlighter();
    }

    @Override
    public @NonNls @NotNull String getDemoText() {
        return
                "/**\n" +
                        " * @param $param (ont:Class)\n" +
                        " */\n" +
                        "DEFINE QUERY query(<param>$param</param>) => <param>$param</param> / ont:property / call;\n" +
                        "\n" +
                        "/*\n" +
                        " * A multiline comment\n" +
                        " */\n" +
                        "VAR $boolean = true; // line comment\n" +
                        "VAR $integer = 1;\n" +
                        "VAR $decimal = 1.1;\n" +
                        "@LOG(<global><readonly>$username</readonly></global>);\n" +
                        "@LOG(<readonly>$declaredReadonlyInOMT</readonly>);\n" +
                        "\n" +
                        "IF true {\n" +
                        "   RETURN myQuery[0, 2] / operatorCall!flag('foo') / . / ^ont:property;\n" +
                        "} ELSE {\n" +
                        "   $subject / ont:property = null;\n" +
                        "   RETURN @commandCall();\n" +
                        "}";
    }

    @Override
    public @Nullable Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return tags;
    }

    @Override
    public AttributesDescriptor @NotNull [] getAttributeDescriptors() {
        ArrayList<AttributesDescriptor> descriptors = ODTSyntaxHighlighter.getAttributes()
                .stream()
                .map(textAttributesKey -> new AttributesDescriptor(getPresentableName(textAttributesKey.getExternalName()),
                        textAttributesKey))
                .collect(Collectors.toCollection(ArrayList::new));
        descriptors.add(new AttributesDescriptor(getPresentableName(GlobalVariable.getExternalName()), GlobalVariable));
        descriptors.add(new AttributesDescriptor(getPresentableName(ReadonlyVariable.getExternalName()), ReadonlyVariable));
        descriptors.add(new AttributesDescriptor(getPresentableName(Parameter.getExternalName()), Parameter));
        descriptors.add(new AttributesDescriptor(getPresentableName(Braces.getExternalName()), Braces));
        descriptors.add(new AttributesDescriptor(getPresentableName(Brackets.getExternalName()), Brackets));
        descriptors.add(new AttributesDescriptor(getPresentableName(Parentheses.getExternalName()), Parentheses));
        return descriptors.toArray(AttributesDescriptor[]::new);
    }

    private String getPresentableName(String externalName) {
        if (externalName.startsWith("ODT_")) {
            return externalName.substring(4);
        }
        return externalName;
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
