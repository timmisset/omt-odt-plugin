IntelliJ Plugin for OMT and ODT languages

## Background

Version 5+ is a complete make-over of the plugin since version 4.x. Version 4.x and earlier use a combined lexer/grammar
that parses both the OMT and ODT languages in a single run. While this is great for performance, it eventually turned
out to be too unstable to continue, this was mainly caused by some overlapping language features such as key:value pairs
in YAML/OMT and prefix:localName pairs in ODT. Another reason is the added functionalities in IntelliJ for extending
YAML/OMT with a structure reasoner (MetaTypeProvider)
which provides a very stable way to implement the rules and restrictions of OMT on the YAML base.

The ODT fragments are now all injected into dedicated parts of the YAML/OMT structure, which does have its effect on the
performance, which is slightly less, but the stability has greatly improved.

The plugin provides support for 3 languages:

## OMT

OMT is a YAML language extension that provides context-based structure using a so called MetaTypeProvider. This makes it
possible to supply a model with (coded) rules and restrictions, expected values and much more. It is by far the biggest
difference with version 4.x since the YAML lexer is used as-is and only a limited adaption of the Parser was required to
intercept PsiElement generation for specific items.

For more info, read the README at
the [omt package](https://github.com/timmisset/omt-odt-plugin/blob/master/src/main/java/com/misset/opp/omt/README.md).

## ODT

ODT is a Domain Specific Language with a Builtin toolbox of Operators and Commands that provide out-of-the-box
functionality for the language. Using DEFINE Commands & Queries, more functionalities are created. ODT can be injected
at dedicated sections in the OMT structure, for which the MultiHostInjector is used. Moreover, when using a .odt
extension the ODT language can exist stand-alone.

For more info, read the README at
the [odt package](https://github.com/timmisset/omt-odt-plugin/blob/master/src/main/java/com/misset/opp/odt/README.md).

## TTL

TTL (Turtle) is a community standard for working with Linked Data. The implementation in this plugin expects a SHACL
based model that is then translated into a simple Subject-Predicate-Object statement model. This simplified model is
used to resolve the elements in the OMT and ODT languages. This allows the plugin to provide (ontology) type-checking,
code completion suggestions and much more.

## Language interoperability

TTL is a standalone language and is unaware of the ODT and OMT languages. ODT uses the TTL language and OMT language (
when injected). It is aware of both languages and has direct dependencies on both languages to be operational. OMT is
the host language but is not directly aware of the hosted language. Meaning, it uses the interfaces of resolvable and
shared to provide/inject context to the hosted language but has no direct relationship. This would make it possible to
completely swap out ODT for another injected language, as long as it implements the interfaces correctly.

### Call & Callable

In practice, only the ODT language makes calls. Every Call requires a Callable counterpart. If not, an error
highlighting is shown declaring the call to be undeclared. A Callable can be a Builtin member, a DEFINE QUERY/COMMAND or
OMT ModelItem. Anything Callable must implement Resolvable and should implement a validation method to ensure that the
Call that is made is properly setup. The majority of this is available in the generic interfaces in the resolvable
package, however, more specific validations that require much more context than only the Call itself should be created
in dedicated LocalInspection implementations. The ODT language has a set of these available for advanced Builtin
commands and operators.

Finally, there are LocalCommands such as @COMMIT, @CANCEL which also implement Callable and as such are inspected for
valid used by a call. This results in calling @COMMIT() with arguments would cause an error highlighting without
specifically implementing an inspection for these commands.

### Variables

Variables can be declared both by the OMT language in specific parts of the structure, or in the ODT Language using the
VAR $variable declaration mechanism. Moreover, certain OMT entries like onChange provide local variables. This is also
true for specific ODT Builtin Commands ForEach and Map. Finally, there are global variables which are available
project-wide and available in both the OMT & ODT language. In all cases, the resulting variable and their usage all
implement the Variable interface. The provides described above are used to determine which variables are available at a
given position.

### JDK Compatability

Make sure to target JDK 11 when running the tests
