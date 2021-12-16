package com.misset.opp.ttl.style;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import com.misset.opp.util.Icons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class TTLColorSettingsPage implements ColorSettingsPage {
    private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
            new AttributesDescriptor("String", TTLSyntaxHighlighter.STRING),
            new AttributesDescriptor("Comment", TTLSyntaxHighlighter.COMMENT),
            new AttributesDescriptor("Subjects", TTLSyntaxHighlighter.SUBJECTS),
            new AttributesDescriptor("Predicates", TTLSyntaxHighlighter.PREDICATES),
            new AttributesDescriptor("Objects", TTLSyntaxHighlighter.OBJECTS),
    };

    @Nullable
    @Override
    public Icon getIcon() {
        return Icons.TTLFile;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return new TTLSyntaxHighlighter();
    }

    @NotNull
    @Override
    public String getDemoText() {
        return "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" +
                "@prefix dc: <http://purl.org/dc/elements/1.1/> .\n" +
                "@prefix ex: <http://example.org/stuff/1.0/> .\n" +
                "@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" +
                "\n" +
                "<SUBJECT><http://www.w3.org/TR/rdf-syntax-grammar></SUBJECT>\n" +
                "  <PREDICATE>dc:title</PREDICATE> \"RDF/XML Syntax Specification (Revised)\" ;\n" +
                "  <PREDICATE>ex:editor</PREDICATE> [\n" +
                "    <PREDICATE>ex:fullname</PREDICATE> \"Dave Beckett\";\n" +
                "    <PREDICATE>ex:homePage</PREDICATE> <OBJECT><http://purl.org/net/dajobe/></OBJECT>\n" +
                "  ] .\n" +
                "\n" +
                "# Comments\n" +
                "<SUBJECT>ex:Class</SUBJECT>\n" +
                "   <PREDICATE>a</PREDICATE> <OBJECT>owl:Class</OBJECT> ;\n" +
                "   <PREDICATE>owl:versionInfo</PREDICATE> \"Created manually\"; \n" +
                "  .\n";
    }

    @Nullable
    @Override
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        HashMap<String, TextAttributesKey> mapping = new HashMap<>();
        mapping.put("SUBJECT", TTLSyntaxHighlighter.SUBJECTS);
        mapping.put("PREDICATE", TTLSyntaxHighlighter.PREDICATES);
        mapping.put("OBJECT", TTLSyntaxHighlighter.OBJECTS);
        return mapping;
    }

    @NotNull
    @Override
    public AttributesDescriptor @NotNull [] getAttributeDescriptors() {
        return DESCRIPTORS;
    }

    @NotNull
    @Override
    public ColorDescriptor @NotNull [] getColorDescriptors() {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "Turtle";
    }
}
