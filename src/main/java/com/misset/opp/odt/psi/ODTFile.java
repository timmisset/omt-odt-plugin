package com.misset.opp.odt.psi;

import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.ModificationTracker;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValueProvider;
import com.misset.opp.omt.meta.providers.OMTMetaTypeStructureProvider;
import com.misset.opp.omt.psi.OMTFile;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLPsiElement;

import java.util.*;
import java.util.function.BiFunction;

public interface ODTFile extends PsiFile {

    /**
     * Returns the Yaml Host element where this ODT file is injected into
     */
    PsiLanguageInjectionHost getHost();

    /**
     * Returns the containing OMT File that contains the Yaml Host element where this ODT file is injected into
     */
    OMTFile getHostFile();

    /**
     * Returns the exporting member use-scope for this ODT file
     * This is limited to the files that import the OMT host container of this file
     */
    SearchScope getExportingMemberUseScope(String name);

    /**
     * Based on the position of this ODTFile as injected language in the OMT structure, the content of this ODT file
     * can be exportable, meaning, other OMT files can import it.
     * Basically any model item or root queries and commands are exportable
     */
    boolean isExportable();

    /**
     * Returns the first entry of:
     *
     * @see ODTFile#getProviders(java.lang.Class, com.intellij.openapi.util.Key)
     */
    <T extends OMTMetaTypeStructureProvider> YAMLMapping getClosestProvider(Class<T> metaTypeOrInterface,
                                                                            Key<CachedValue<LinkedHashMap<YAMLMapping, T>>> key);

    /**
     * Get all providers of the given MetaType, ordered by closest first
     * i.e.
     * When getProviders is used with OMTCallableProvider and the current ODT Injected file is located within
     * a modelItem. The modelItem is retured first, then the ODT file itself (which is also a callable provider)
     * <p>
     * If specific elements need provider information, for example, a usage variable must know all declared variables,
     * obtain this information via the ODTFile itself since it will share the cache with other usage variables and thus
     * will much faster retrieve provider information during analysis.
     */
    <T extends OMTMetaTypeStructureProvider> LinkedHashMap<YAMLMapping, T> getProviders(Class<T> metaTypeOrInterface,
                                                                                        Key<CachedValue<LinkedHashMap<YAMLMapping, T>>> key);

    <T extends YAMLPsiElement, U extends OMTMetaTypeStructureProvider> LinkedHashMap<T, U> getProviders(Class<T> yamlClass,
                                                                                                        Class<U> metaTypeOrInterface,
                                                                                                        Key<CachedValue<LinkedHashMap<T, U>>> key);

    /**
     * Resolve the reference present in the ODT language to an OMT element
     * For example, a Call to a ModelItem (YamlMapping)
     *
     * @return
     */
    <T extends OMTMetaTypeStructureProvider, U extends PsiElement> Optional<List<U>> resolveInOMT(Class<T> providerClass,
                                                                                                  Key<CachedValue<LinkedHashMap<YAMLMapping, T>>> metaTypeStructureKey,
                                                                                                  String key,
                                                                                                  BiFunction<T, YAMLMapping, HashMap<String, List<U>>> mapFunction);

    /**
     * Returns the prefix::namespaces that are available at the point of this Injected ODT fragment.
     * It does not provide the prefix declarations that are declared in the ODT file itself since those depend on the position
     * of the element that wants to use it
     */
    Map<String, String> getAvailableNamespaces();

    /**
     * Returns the value wrapped in a cached value with modification trackers based on this file being a file on itself or as
     * injected file in OMT
     */
    <T> CachedValueProvider.Result<T> getCachedValue(T result,
                                                     ModificationTracker... additionalTrackers);
}
