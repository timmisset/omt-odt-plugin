package com.misset.opp.omt.meta;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.omt.meta.model.ODTSimpleInjectable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MetaMap that provides an entry/item for any name that is entered as key
 * it has no opinion about the validity of keys
 */
@ODTSimpleInjectable
public abstract class OMTMetaShorthandType extends OMTMetaType {

    protected OMTMetaShorthandType(@NotNull String name) {
        super(name);
    }

    protected abstract Pattern getShorthandPattern();
    protected abstract String getShorthandSyntaxError(YAMLValue value);

    @Override
    public void validateValue(@NotNull YAMLValue value,
                              @NotNull ProblemsHolder problemsHolder) {

        // do not validate the destructed notation
        if(value instanceof YAMLMapping) { return; }

        // When the parameter is used as short-hand notation, the value itself is tested here
        // make sure it conforms to the RegEx used to determine the name and type:
        final Matcher matcher = getShorthandPattern().matcher(value.getText());
        if(!matcher.find()) {
            problemsHolder.registerProblem(value, getShorthandSyntaxError(value));
        }
    }
}
