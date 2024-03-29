package com.misset.opp.omt.meta.scalars;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.misset.opp.omt.meta.OMTMetaInjectable;
import com.misset.opp.omt.meta.model.SimpleInjectable;
import org.jetbrains.yaml.meta.model.YamlStringType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SimpleInjectable
public class OMTInterpolatedStringMetaType extends YamlStringType implements OMTMetaInjectable {
    private static final Pattern INTERPOLATION = Pattern.compile("\\$\\{([^}]+)}");
    private static final Pattern TITLE_VARIABLE = Pattern.compile("^(\\$[-A-z]+)$");

    private static final OMTInterpolatedStringMetaType INSTANCE = new OMTInterpolatedStringMetaType();

    public static OMTInterpolatedStringMetaType getInstance() {
        return INSTANCE;
    }

    private OMTInterpolatedStringMetaType() {
    }

    @Override
    public List<TextRange> getTextRanges(PsiElement host) {
        /*
            The InterpolatedString can be injected by using a ${}. Unfortunately, the language also
            supports the option to add a variable directly as value for the property
         */
        final Matcher matcher;
        final Matcher matcherVariable = TITLE_VARIABLE.matcher(host.getText());
        final Matcher matcherInterpolation = INTERPOLATION.matcher(host.getText());
        boolean b;
        if (matcherVariable.find()) {
            matcher = matcherVariable;
            b = true;
        } else {
            matcher = matcherInterpolation;
            b = matcher.find();
        }
        if (!b) {
            return Collections.emptyList();
        }

        final ArrayList<TextRange> textRanges = new ArrayList<>();
        while (b) {
            textRanges.add(TextRange.create(matcher.start(1), matcher.end(1)));
            b = matcher.find();
        }
        return textRanges;
    }

}
