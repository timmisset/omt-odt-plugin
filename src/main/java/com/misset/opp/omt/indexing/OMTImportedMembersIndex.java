package com.misset.opp.omt.indexing;

import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiSearchHelper;
import com.misset.opp.omt.OMTFileType;
import com.misset.opp.omt.psi.OMTFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to retrieve files that might contain an import for a PsiCallable
 * The Index uses the platform WordsIndex (via PsiSearchHelper) to allocate all files with callable name as literal.
 */
public class OMTImportedMembersIndex {
    public static List<OMTFile> getImportingFiles(Project project, String callableName) {
        GlobalSearchScope searchScope =
                GlobalSearchScope.getScopeRestrictedByFileTypes(GlobalSearchScope.allScope(project), OMTFileType.INSTANCE);
        List<OMTFile> files = new ArrayList<>();
        PsiSearchHelper.getInstance(project)
                .processAllFilesWithWordInLiterals(callableName, searchScope, file -> files.add((OMTFile) file));
        return files;
    }
}
