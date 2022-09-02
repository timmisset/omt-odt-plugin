# OMT

The OMT language is an extension of YAML.

## Structure

The OMT structure determines the markup and context of the OMT language. For this the (experimental) MetaTypeProvider is
used which is developed and maintained by IntelliJ/Jetbrains. The MetaTypeProvider was extended a bit for OMT to provide
support for !tag identified elements, this allows for specific structures to be provided for things like !Activity. All
modelItems reside in the key:value pair model:value, there was no support to narrow the meta type based on the tag. See:
com.misset.opp.omt.meta.OMTMetaTypeProvider

### Injection

OMT MetaType classes that implement the OMTMetaInjectable interface provide text-ranges which part of their content is
available to be re-parsed as the injected language. The responsibility of re-parsing and anything after that is part of
the injected language itself.

### Context providers

Some MetaType classes provide additional context to a part of the OMT file. This is primarily used to provide context to
the injected language fragments. The injected language should be able to deal with these providers and their provided
elements/context. Examples are:

* Activity provides PsiVariables, PsiCallables, Prefixes, LocalCommands
    * PsiVariables: Available variables that are declared by the OMT language and are available to injected fragments
      that are part of the Activity
    * PsiCallables: Available commands and queries that were declared in an (injected) part of the Activity and are
      available everywhere else in that Activity
    * Prefixes: Available prefixes that are declared in the variable
    * LocalCommands: @COMMIT, @CANCEL etc

The OMT structure allows for multiple layers of such providers to exist at a given point. This means that PsiCallables
are a combination of Callable elements of the Activity and the OMT root.

### Validation

The OMT MetaType classes provide overrides to inspect specific values, missing keys etc. In general only, non-injected
language parts are validated. Examples are:

* A given set of acceptable values
* Syntax for shorthand notations (parameters, variables)
* Type validation for known boolean-based resolvable values.

Since the OMT language no longer contains a lexer and grammar parser of itself, things like a variable declaration is
resolved using good old regular expressions.

## Module files vs regular files

All OMT files that use filename.module.omt are considered OMT modules and therefor have a different MetaType structure
available. For certain features, the regular files should be able to resolve to their module file. Please note that this
is actually a bit a cheat, since an omt file is not necessarily restricted to a single module file. Any module can
import any omt file and make it part of their module. In practice, the module.omt file will be part of the root of the
module folder and all omt files that are part of this folder (recursively) are considered to be part of that module
file. This helps to resolve declared module imports (module:MyModule) in omt files.

## YAML Element interception

Although most of the YAML language could be used as-is, there were some issues when working with declarations that were
declared inside YAMLPlainText. Since declared elements should implement PsiNamedElement, and YAMLPlainText is not such
an implementation its creation process is intercepted by the YAMLOMTParserDefinition. This is also done for the
YAMLKeyValue for convenience, it turned out it's much easier to provide references directly from the generated
PsiElement than using the ReferenceProvider structure. It also gives a bit more control of the element.

### Delegates

There are a number of delegates used to facilitate the mechanism described above. These are located at psi/impl/delegate
and
are hooked to the original YamlPsiElement used the UserDataHolder extension available in all PsiElements. This makes
sure the delegate is set only once and reusable afterwards. The delegate can resolve certain methods directly and defers
more complex requests to MetaType instances that reflect the delegate.

### YAMLPlainText

The YAMLPlainText is the only valid element type that can act as an injected language host. It also serves as a
shorthand for variable and parameter declarations:
variables:

- name: $test value: true Can be shortened to - $test = true

### YAMLKeyValue

Certain parts of the OMT structure, such as model items, are considered to be PsiCallable. However, they can also be a
prefix declaration or an import declaration to another file.

## References

The references created in the OMTLanguage are all owned by either YAMLPlainText or YAMLKeyValue elements. In the case of
YAMLPlainText, the references are confined to a narrow part of the text. For example, a parameter declaration:
$param (ont:SomeClass). In this case, there are multiple references, one to the prefix (ont) and another to the
Ontology (Class). The entire YAMLPlainText field is considered a PsiNamedElement and can be used to FindUsage of the
$param in the scope of the file.

## Indexing

Since the OMT language depends heavily on the injected language fragments to determine what Callable elements are
available, it is not possible to use the IntelliJ Indexing mechanism. This is because the StubIndex doesn't run on
injected fragments. Actually, language injection is performed after indexing on the file is already done, so it's even
not possible to provide this ourselves. Instead, using the IndexOMTStartupActivity, all OMT files are indexed after the
initial indexing and language injection. This results in a bit of a slow start of IntelliJ (10sec) but it greatly
improves performance afterwards. 
