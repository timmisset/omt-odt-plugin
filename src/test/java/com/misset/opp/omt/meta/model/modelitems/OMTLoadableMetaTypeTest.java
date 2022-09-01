package com.misset.opp.omt.meta.model.modelitems;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.openapi.application.ReadAction;
import com.misset.opp.odt.inspection.calls.ODTCallInspection;
import com.misset.opp.omt.inspection.structure.OMTMissingKeysInspection;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegateFactory;
import com.misset.opp.omt.psi.impl.delegate.keyvalue.OMTYamlModelItemDelegate;
import com.misset.opp.omt.psi.impl.yaml.YAMLOMTKeyValueImpl;
import com.misset.opp.testCase.OMTInspectionTestCase;
import com.misset.opp.ttl.model.OppModelConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

class OMTLoadableMetaTypeTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return List.of(OMTMissingKeysInspection.class,
                ODTCallInspection.class);
    }

    @Test
    void testPathAndSchemaRequired() {
        String content = "model:\n" +
                "   MyLoadable: !Loadable\n" +
                "       id: test\n";
        configureByText(content);
        assertHasError("Missing required key(s): 'path, schema'");
    }

    @Test
    void testPathRequired() {
        String content = "model:\n" +
                "   MyLoadable: !Loadable\n" +
                "       id: test\n" +
                "       path: test.json\n";
        configureByText(content);
        assertHasError("Missing required key(s): 'schema'");
    }

    @Test
    void testSchemaRequired() {
        String content = "model:\n" +
                "   MyLoadable: !Loadable\n" +
                "       id: test\n" +
                "       schema: test.json\n";
        configureByText(content);
        assertHasError("Missing required key(s): 'path'");
    }

    @Test
    void testIsCallable() {
        Assertions.assertTrue(getDelegate().isCallable());
    }

    @Test
    void testResolvesToJson() {
        Assertions.assertEquals(Set.of(OppModelConstants.getJsonObject()), OMTLoadableMetaType.getInstance().resolve(null, null));
    }

    @Test
    void testIsVoidIsFalse() {
        Assertions.assertFalse(OMTLoadableMetaType.getInstance().isVoid(null));
    }

    @Test
    void testCanBeAppliedTo() {
        Assertions.assertFalse(OMTLoadableMetaType.getInstance().canBeAppliedTo(null, null));
    }

    @Test
    void testSecondArgument() {
        Assertions.assertEquals(Collections.emptySet(), OMTLoadableMetaType.getInstance().getSecondReturnArgument());
    }

    @ParameterizedTest
    @ValueSource(strings = {"!silent", "!load", "!release", "!retain", "!load!retain", "!retain!load"})
    void testAcceptsFlag(String flag) {
        String content = String.format("model:\n" +
                "   MyLoadable: !Loadable\n" +
                "       id: test\n" +
                "       path: test.json\n" +
                "       schema: test.json\n" +
                "   Procedure: !Procedure\n" +
                "       onRun:\n" +
                "           VAR $data = MyLoadable%s;", flag);
        configureByText(content);
        assertNoErrors();
    }

    @Test
    void testIllegalFlagsHasError() {
        String content = "model:\n" +
                "   MyLoadable: !Loadable\n" +
                "       id: test\n" +
                "       path: test.json\n" +
                "       schema: test.json\n" +
                "   Procedure: !Procedure\n" +
                "       onRun:\n" +
                "           VAR $data = MyLoadable!illegalFlag;";
        configureByText(content);
        assertHasError("Illegal flag, options are: ");
    }

    @ParameterizedTest
    @ValueSource(strings = {"local", "parent", "omt", "session"})
    void testAcceptsValue(String value) {
        String content = String.format("model:\n" +
                "   MyLoadable: !Loadable\n" +
                "       id: test\n" +
                "       path: test.json\n" +
                "       schema: test.json\n" +
                "   Procedure: !Procedure\n" +
                "       onRun:\n" +
                "           VAR $data = MyLoadable('%s');", value);
        configureByText(content);
        assertNoErrors();
    }

    @Test
    void testDoesntAcceptIllegalValue() {
        String content = "model:\n" +
                "   MyLoadable: !Loadable\n" +
                "       id: test\n" +
                "       path: test.json\n" +
                "       schema: test.json\n" +
                "   Procedure: !Procedure\n" +
                "       onRun:\n" +
                "           VAR $data = MyLoadable!retain('oops');";
        configureByText(content);
        assertHasError("Incompatible value, expected: ");
    }

    @Test
    void testAcceptVariable() {
        String content = "model:\n" +
                "   MyLoadable: !Loadable\n" +
                "       id: test\n" +
                "       path: test.json\n" +
                "       schema: test.json\n" +
                "   Procedure: !Procedure\n" +
                "       onRun:\n" +
                "           VAR $value = 'omt';\n" +
                "           VAR $data = MyLoadable!retain($value);";
        configureByText(content);
        assertNoError("Incompatible value, expected: ");
    }

    @Test
    void testAcceptQuery() {
        String content = "model:\n" +
                "   MyLoadable: !Loadable\n" +
                "       id: test\n" +
                "       path: test.json\n" +
                "       schema: test.json\n" +
                "   Procedure: !Procedure\n" +
                "       onRun:\n" +
                "           VAR $value = 'omt';\n" +
                "           VAR $data = MyLoadable!retain('oops' / CAST(xsd:string));";
        configureByText(content);
        assertNoError("Incompatible value, expected: ");
    }

    @Test
    void testWarningWhenContextWithoutReleaseOrRetain() {
        String content = "model:\n" +
                "   MyLoadable: !Loadable\n" +
                "       id: test\n" +
                "       path: test.json\n" +
                "       schema: test.json\n" +
                "   Procedure: !Procedure\n" +
                "       onRun:\n" +
                "           VAR $data = MyLoadable!load('omt');";
        configureByText(content);
        assertHasWarning(OMTLoadableMetaType.CALLING_WITH_RETAIN_OR_RELEASE_FLAG);
    }

    @Test
    void testWarningWhenCombiningReleaseFlag() {
        String content = "model:\n" +
                "   MyLoadable: !Loadable\n" +
                "       id: test\n" +
                "       path: test.json\n" +
                "       schema: test.json\n" +
                "   Procedure: !Procedure\n" +
                "       onRun:\n" +
                "           VAR $data = MyLoadable!release!load;";
        configureByText(content);
        assertHasWarning(OMTLoadableMetaType.RELEASE_FLAG_SHOULD_NOT_BE_COMBINED_WITH_OTHER_FLAGS);
    }

    private OMTYamlModelItemDelegate getDelegate() {
        String content = "model:\n" +
                "   <caret>MyLoadable: !Loadable\n" +
                "       id: test\n" +
                "       schema: test.json\n";
        configureByText(content);
        return ReadAction.compute(() -> {
            YAMLOMTKeyValueImpl keyValue = (YAMLOMTKeyValueImpl) myFixture.getElementAtCaret();
            return (OMTYamlModelItemDelegate) OMTYamlDelegateFactory.createDelegate(keyValue);
        });
    }
}
