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
import java.util.Map;

public class TTLColorSettingsPage implements ColorSettingsPage {
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
        return "# baseURI: http://ontologyBase#\n" +
                "# imports: http://ontology/anotherOntology\n" +
                "\n" +
                "@base <http://ontologyBase#> . # a regular line comment\n" +
                "@prefix ont: <http://ontology#> .\n" +
                "@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" +
                "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" +
                "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" +
                "@prefix sh: <http://www.w3.org/ns/shacl#> .\n" +
                "@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
                "\n" +
                "<http://http://ontology/myOntology>\n" +
                "  a owl:Ontology ;\n" +
                "  owl:versionInfo \"Created manually\" ;\n" +
                ".\n" +
                "<#Class>\n" +
                "  a owl:Class ;\n" +
                "  rdf:type sh:NodeShape ;\n" +
                "  rdfs:subClassOf ont:Superclass, ont:AnotherSuperclass ;\n" +
                "  ont:booleanValue true;\n" +
                "  rdfs:label \"Taal\" ;\n" +
                "  rdfs:label \"Label 1\"^^xsd:string ;\n" +
                "  rdfs:label \"Label 2\"^^<http://www.w3.org/2001/XMLSchema#string> ;\n" +
                "  ont:number 1 ;\n" +
                "  ont:multiline: \"\"\" The first line\n" +
                "                     The second line\n" +
                "                     more \"\"\"\n" +
                ".\n";
    }

    @Override
    public @Nullable Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return null;
    }

    @NotNull
    @Override
    public AttributesDescriptor @NotNull [] getAttributeDescriptors() {
        return TTLSyntaxHighlighter.getAttributes()
                .stream()
                .map(textAttributesKey -> new AttributesDescriptor(getPresentableName(textAttributesKey.getExternalName()), textAttributesKey))
                .toArray(AttributesDescriptor[]::new);
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

    private String getPresentableName(String externalName) {
        if (externalName.startsWith("TTL_")) {
            return externalName.substring(4);
        }
        return externalName;
    }
}
