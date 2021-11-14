package com.misset.opp.odt.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.search.SearchScope;
import com.misset.opp.omt.psi.OMTFile;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLPsiElement;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

public interface ODTFile extends PsiFile {

    /**
     * Returns the Yaml Host element where this ODT file is injected into
     */
    YAMLPsiElement getHost();

    /**
     * Returns the containing OMT File that contains the Yaml Host element where this ODT file is injected into
     */
    OMTFile getHostFile();

    /**
     * Returns the exporting member use-scope for this ODT file
     * This is limited to the files that import the OMT host container of this file
     */
    SearchScope getExportingMemberUseScope();

    /**
     * Based on the position of this ODTFile as injected language in the OMT structure, the content of this ODT file
     * can be exportable, meaning, other OMT files can import it.
     * Basically any model item or root queries and commands are exportable
     */
    boolean isExportable();

    /**
     * Returns the first entry of:
     *
     * @see ODTFile#getProviders(java.lang.Class)
     */
    <T> YAMLMapping getClosestProvider(Class<T> metaTypeOrInterface);

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
    <T> LinkedHashMap<YAMLMapping, T> getProviders(Class<T> metaTypeOrInterface);

    /**
     * Resolve the reference present in the ODT language to an OMT element
     * For example, a Call to a ModelItem (YamlMapping)
     */
    <T> Optional<ResolveResult[]> resolveInOMT(Class<T> providerClass,
                                               String key,
                                               BiFunction<T, YAMLMapping, HashMap<String, List<PsiElement>>> mapFunction);

    /**
     * Returns the prefix::namespaces that are available at the point of this Injected ODT fragment.
     * It does not provide the prefix declarations that are declared in the ODT file itself since those depend on the position
     * of the element that wants to use it
     */
    Map<String, String> getAvailableNamespaces();
}
