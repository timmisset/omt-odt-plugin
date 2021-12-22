IntelliJ Plugin for OMT and ODT languages version 5+

A complete make-over of the plugin since version 4.x

The plugin provides support for 3 languages:

## OMT

OMT is a YAML language extension that provides context-based structure using a so called MetaTypeProvider. This makes it
possible to supply a model with (coded) rules and restrictions, expected values and much more. It is by far the biggest
difference with version 4.x since the YAML lexer is used as-is and only a limited adaption of the Parser was required to
intercept PsiElement generation for specific items. For more info, read the README at the omt package.

## ODT

ODT is a Domain Specific Language with a Builtin toolbox of Operators and Commands that provide out-of-the-box
functionality for the language. Using DEFINE Commands & Queries, more functionalities are created. ODT can be injected
at dedicated sections in the OMT structure, for which the MultiHostInjector is used. Moreover, when using a .odt
extension the ODT language can exist stand-alone. For more info, read the README at the odt package.

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

### JDK Compatability

Make sure to target JDK 11 when running the tests
