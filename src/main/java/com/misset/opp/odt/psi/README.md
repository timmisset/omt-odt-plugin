The ASTWrapperPsiElements are a combination of the auto-generated class files based on the ODT.bnf grammar and the
wrapper or more specifically typed versions here.

In order to use abstract base classes for specifically typed classes, extend the auto-generated
version from the base class and the extend the specifically typed from the auto-generated version. This way
there is not need to duplicate/maintain the code provided by the auto-generated version while still
allowing for abstraction of these classes.
