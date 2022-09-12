package com.misset.opp.omt.psi.impl.delegate.keyvalue;

import com.intellij.psi.PsiElement;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.omt.meta.model.modelitems.OMTModelItemMetaType;
import com.misset.opp.omt.util.OMTRefactoringUtil;
import com.misset.opp.refactoring.SupportsSafeDelete;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLValue;

public class OMTYamlModelItemDelegate extends OMTYamlKeyValueDelegateAbstract implements SupportsSafeDelete {

    private final transient YAMLKeyValue keyValue;
    private final transient OMTModelItemMetaType metaType = OMTModelItemMetaType.getInstance();
    private transient YAMLMapping mapping = null;

    public OMTYamlModelItemDelegate(YAMLKeyValue keyValue) {
        super(keyValue.getNode());
        this.keyValue = keyValue;
        YAMLValue value = keyValue.getValue();
        if (value instanceof YAMLMapping) {
            this.mapping = (YAMLMapping) value;
        }
    }

    public boolean isCallable() {
        if (mapping == null) {
            return false;
        }
        return metaType.isCallable(mapping);
    }

    @Override
    public PsiElement getKey() {
        return keyValue.getKey();
    }

    @Override
    public PsiElement getOriginalElement() {
        return keyValue;
    }

    @Override
    public @NotNull SearchScope getUseScope() {
        return keyValue.getContainingFile().getUseScope();
    }

    @Override
    public boolean isUnused() {
        return !isCallable() || ReferencesSearch.search(keyValue, keyValue.getUseScope()).findFirst() == null;
    }

    @Override
    public void delete() throws IncorrectOperationException {
        OMTRefactoringUtil.removeEOLToken(keyValue);
        super.delete();
    }
}
