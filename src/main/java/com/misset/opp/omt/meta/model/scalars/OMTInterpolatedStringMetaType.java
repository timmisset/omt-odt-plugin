package com.misset.opp.omt.meta.model.scalars;

import com.intellij.openapi.util.TextRange;
import com.misset.opp.omt.meta.ODTInjectable;
import org.jetbrains.yaml.meta.model.YamlStringType;
import org.jetbrains.yaml.psi.impl.YAMLScalarImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OMTInterpolatedStringMetaType extends YamlStringType implements ODTInjectable {
    private final Pattern INTERPOLATION = Pattern.compile("\\$\\{([^}]+)}");
    private final Pattern TITLE_VARIABLE = Pattern.compile("^(\\$[-A-z]+)$");

    @Override
    public List<TextRange> getTextRanges(YAMLScalarImpl host) {
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
