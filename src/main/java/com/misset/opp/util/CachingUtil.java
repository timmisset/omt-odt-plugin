package com.misset.opp.util;

import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;

import java.util.function.Supplier;

public class CachingUtil {


    public static  <T> T getCachedOrCalcute(PsiElement context, Key<CachedValue<T>> key, Supplier<T> calculate) {
        return CachedValuesManager.getCachedValue(context, key, () ->
                new CachedValueProvider.Result<>(calculate.get(), context, context.getContainingFile())
        );
    }

}
