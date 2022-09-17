# ODT

The ODT language is a domain specific language that uses an ontology to resolve information. It uses the OntologyModel
class
to get ontology information and uses basic subject-predicate-object inquiries to determine possible outcomes, valid
inputs etc.

## Ontology types

The OntologyModel (since version 5 of the plugin) makes a clear distinction between an ontology Class and
Instance/Individual. This makes it possible to get more specific type validation so that no classes are provided by
accident when an instance is required.

```
  /ont:Class              # resolves to the class
  /ont:Class / ^rdf:type  # resolves to the instances
```

Moreover, other platform types such as NamedGraph, GraphShape etc. are now part of the validation. This enables
validations more specific to certain parts of the OMT model or Builtin commands. For example, only a query that resolves
to a NamedGraph can be listed in the graphs: section. Another example are the graph and subject creation Builtin
commands (@NEW, @NEW_GRAPH) that have type-checks.

NamedGraphs that are part of external sources can be added to the plugin using:
File | Settings | Languages & Frameworks | OMT / ODT Settings

## Highlighting

There is some highlighting available in the ODT language. Please note that when ODT is injected in OMT, the first time
you see the document the highlighting might not be present. This is because the InjectionManager of IntelliJ runs after
the initial highlighting. This is a known issue, simply type something in the document to trigger the highlighting.

The highlighting also uses resolved types to make it possible to give a class and a class instance a different color,
underscore etc.

By default, IntelliJ will set a background highlighting for injected language fragments, you can modify / remove this
here:
File | Settings | Editor | Color Scheme | General

## Resolvable

Most of the features of the plugin for the ODT language revolve around the Resolvable interface. Anything which is a
query(step), variable, callable etc. Everything is resolvable and returns a set of Ontology Resources (Set<OntResource>)
that can be matched against the Ontology model for validity.

This allows the plugin to do type checks, code completions and much more.

## Callables

The ODT language has a number of Callables

* Builtin Operators & Commands: All Builtin members are explicitly added to the plugin. They are used for call
  validation, return types etc.
* DEFINE statements:
  * Queries: The query is resolved and the return type is extracted from resolving the statement
  * Commands: The command is only checked as callable (number of arguments, types etc.)

## Calls

All calls are evaluated in the same manner; a callable counterpart is retrieved from either a Builtin member, the OMT
language (Local commands, Activities, Procedures) or the ODT language
(DEFINE QUERY, DEFINE COMMAND). The call is then evaluated by the callable element where the call provides information
about the arguments (number, type), flag, input set etc.

## Variables

There a number of variable implementations in the ODT language:

* usage
* declared
* assigned

Since the PsiTree only knows the $variable itself, delegates are used to defer the logic based on how the variable is
contained. For example:

```
VAR $variable = 'test'; # has a declared variable delegate
$variable = 'test';     # has an assignment variable delegate
@LOG($variable);        # has a usage variable delegate
``` 

Variables are resolved via the assignments or explicit type assignments (parameters). The usage variable will inquire
all preceding assignments and thus can be a different type at different positions:

```
VAR $variable;          # has no type yet
IF true {
  $variable = 1;        # has an integer type
} ELSE {
  $variable = 'a';      # has a string type
}
@LOG($variable);        # has both integer and string types
```

### Parameter types

In ODT, there is no method to explicitly type an input parameter for a DEFINE QUERY or COMMAND. However, by adding a
DocComment, the plugin will understand the desired types and include it in the type-checks

```
  /**
   * @base (ont:ClassA)
   * @param $paramA (string)
   */
   DEFINE QUERY filter($paramA) => ont:Property == $paramA;
   DEFINE QUERY query => /ont:ClassA / ^rdf:type[filter('someValue')]
```

Add a @base annotation to specify to which types the queries will be applied.

## Queries

Queries are compiled structures that are recursively constructed by the PsiTree with a QueryStep as root element. There
are many implementations of the QueryStep but all implement Resolvable. The Query itself also implements Resolvable and
is resolved by different methods.

* QueryPath (a / step / based / query) returns the resolved value of the last query step
* QueryArray (queryA | queryB), combines the types of all Queries
* BooleanStatement (queryA AND queryB OR queryC), always returns boolean
* EquationStatement (queryA == queryB), always returns boolean The queries themselves don't do a lot of resolving work,
  that is mostly deferred to the steps

## QuerySteps

A queryStep is resolved based on its implementation, some examples:

* VariableStep resolves the contained variable
* QueryForwardStep resolves the previous step (subject) and itself (predicate) to determine possible objects.
* QueryReverseStep resolves the previous step (object) and itself (predicate) to determine possible subjects.
* ConstantValue resolves to the XSD variant of the value (xsd:string, xsd:integer etc)

### Filters

Queries can be filtered which can influence the possible types at a given position in the query:

```
  $variable[rdf:type == /ont:SomeClassInstance];
```

For simple type filters, the plugin will understand how to resolve them, when it becomes more complex it will fall back
to the type-safe owl:Thing so that no errors and warnings will be shown in the editor.

### URI steps

There a different ways of using URI steps in the ODT language; curies, fully qualified uris, schemaless etc. For the
plugin, these variations are all resolved to the fully qualified uri and then used to traverse the model.
