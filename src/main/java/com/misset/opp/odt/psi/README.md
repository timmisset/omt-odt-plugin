###Complex class structures
The ASTWrapperPsiElements are a combination of the auto-generated class files based on the ODT.bnf grammar and the
and can be enriched by creating abstract classes to be extended by the auto-generated ones.

The way to do this is to provide an extension in the bnf giving the abstract class.  
If the abstract class needs to access generated info, simple have it implement the corresponding auto-generated interface

###References
Referencing ODT element from other files requires the useScope of those ODT PsiElements
to be overwritten.

This is because the ODT elements are injected and considered a VirtualFileWindow
making their default scope the file of the host element (i.e. the OMT file).
