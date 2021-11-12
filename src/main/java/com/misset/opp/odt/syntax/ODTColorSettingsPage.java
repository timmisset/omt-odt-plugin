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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.misset.opp.odt.syntax.ODTSyntaxHighlighter.BaseCallAttributesKey;
import static com.misset.opp.odt.syntax.ODTSyntaxHighlighter.DefineAttributesKey;
import static com.misset.opp.odt.syntax.ODTSyntaxHighlighter.OntologyClassAttributesKey;
import static com.misset.opp.odt.syntax.ODTSyntaxHighlighter.OntologyInstanceAttributesKey;
import static com.misset.opp.odt.syntax.ODTSyntaxHighlighter.OntologyTypeAttributesKey;
import static com.misset.opp.odt.syntax.ODTSyntaxHighlighter.OntologyValueAttributesKey;

public class ODTColorSettingsPage implements ColorSettingsPage {
    private static final List<TextAttributesKey> enforcedAttributes = new ArrayList<>();

    static {
        enforcedAttributes.add(BaseCallAttributesKey);
        enforcedAttributes.add(DefineAttributesKey);
        enforcedAttributes.add(OntologyClassAttributesKey);
        enforcedAttributes.add(OntologyTypeAttributesKey);
        enforcedAttributes.add(OntologyInstanceAttributesKey);
        enforcedAttributes.add(OntologyValueAttributesKey);
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
        return
                "DEFINE QUERY <" + DefineAttributesKey.getExternalName() + ">query</\" + DefineAttributesKey.getExternalName() + \">" +
                        "($param) => $param / ont:property / <" + BaseCallAttributesKey.getExternalName() + ">call</" + BaseCallAttributesKey.getExternalName() + ">;\n" +
                        "/ont:<" + OntologyClassAttributesKey.getExternalName() + ">Class</" + OntologyClassAttributesKey.getExternalName() + ">" +
                        " / ^rdf:<" + OntologyInstanceAttributesKey.getExternalName() + ">type</" + OntologyInstanceAttributesKey.getExternalName() + ">" +
                        " / ont:<" + OntologyValueAttributesKey.getExternalName() + ">stringPredicate</" + OntologyValueAttributesKey.getExternalName() + ">\n";
    }

    @Override
    public @Nullable Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return enforcedAttributes.stream()
                .collect(Collectors.toMap(TextAttributesKey::getExternalName, textAttributesKey -> textAttributesKey));
    }

    @Override
    public AttributesDescriptor @NotNull [] getAttributeDescriptors() {
        return ODTSyntaxHighlighter.getAttributes()
                .stream()
                .map(textAttributesKey -> new AttributesDescriptor(textAttributesKey.getExternalName(),
                        textAttributesKey))
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
