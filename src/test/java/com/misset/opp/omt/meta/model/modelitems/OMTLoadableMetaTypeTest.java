package com.misset.opp.omt.meta.model.modelitems;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.openapi.application.ReadAction;
import com.misset.opp.omt.inspection.structure.OMTMissingKeysInspection;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegateFactory;
import com.misset.opp.omt.psi.impl.delegate.keyvalue.OMTYamlModelItemDelegate;
import com.misset.opp.omt.psi.impl.yaml.YAMLOMTKeyValueImpl;
import com.misset.opp.testCase.OMTInspectionTestCase;
import com.misset.opp.ttl.OppModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

class OMTLoadableMetaTypeTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(OMTMissingKeysInspection.class);
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
        Assertions.assertEquals(Set.of(OppModel.INSTANCE.JSON_OBJECT), new OMTLoadableMetaType().resolve(null, null));
    }

    @Test
    void testIsVoidIsFalse() {
        Assertions.assertFalse(new OMTLoadableMetaType().isVoid(null));
    }

    @Test
    void testCanBeAppliedTo() {
        Assertions.assertFalse(new OMTLoadableMetaType().canBeAppliedTo(null, null));
    }

    @Test
    void testSecondArgument() {
        Assertions.assertEquals(Collections.emptySet(), new OMTLoadableMetaType().getSecondReturnArgument());
    }

    @Test
    void testGetType() {
        Assertions.assertEquals("Loadable", new OMTLoadableMetaType().getType());
    }

    private OMTYamlModelItemDelegate getDelegate() {
        String content = "model:\n" +
                "   <caret>MyLoadable: !Loadable\n" +
                "       id: test\n" +
                "       schema: test.json\n";
        configureByText(content);
        return ReadAction.compute(() -> {
            YAMLOMTKeyValueImpl keyValue = (YAMLOMTKeyValueImpl) myFixture.getElementAtCaret();
            OMTYamlModelItemDelegate delegate = (OMTYamlModelItemDelegate) OMTYamlDelegateFactory.createDelegate(keyValue);
            return delegate;
        });
    }
}
