package com.misset.opp.odt.psi;

import com.intellij.psi.PsiFile;
import com.intellij.psi.search.SearchScope;
import com.misset.opp.omt.psi.OMTFile;
import org.jetbrains.yaml.psi.YAMLPsiElement;

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
}
