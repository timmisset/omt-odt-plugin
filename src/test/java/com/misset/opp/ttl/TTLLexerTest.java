package com.misset.opp.ttl;

import com.intellij.psi.tree.IElementType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.intellij.testFramework.UsefulTestCase.assertDoesntContain;

class TTLLexerTest {

    @Test
    void testExample1() {
        String contentToTest = "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" +
                "@prefix dc: <http://purl.org/dc/elements/1.1/> .\n" +
                "@prefix ex: <http://example.org/stuff/1.0/> .\n" +
                "\n" +
                "<http://www.w3.org/TR/rdf-syntax-grammar>\n" +
                "  dc:title \"RDF/XML Syntax Specification (Revised)\" ;\n" +
                "  ex:editor [\n" +
                "    ex:fullname \"Dave Beckett\";\n" +
                "    ex:homePage <http://purl.org/net/dajobe/>\n" +
                "  ] .";
        List<String> elements = getElements(contentToTest);
        assertDoesntContain(elements, "BAD_CHARACTER");
    }

    @Test
    void testShacl() {
        String contentToTest = "##  W3C SOFTWARE AND DOCUMENT NOTICE AND LICENSE\n" +
                "## https://www.w3.org/Consortium/Legal/2015/copyright-software-and-document\n" +
                "## --------\n" +
                "\n" +
                "# W3C Shapes Constraint Language (SHACL) Vocabulary\n" +
                "# Version from 2017-07-20\n" +
                "\n" +
                "@prefix owl:  <http://www.w3.org/2002/07/owl#> .\n" +
                "@prefix rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" +
                "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" +
                "@prefix xsd:  <http://www.w3.org/2001/XMLSchema#> .\n" +
                "\n" +
                "@prefix sh:   <http://www.w3.org/ns/shacl#> .\n" +
                "\n" +
                "sh:\n" +
                "\ta owl:Ontology ;\n" +
                "\trdfs:label \"W3C Shapes Constraint Language (SHACL) Vocabulary\"@en ;\n" +
                "\trdfs:comment \"This vocabulary defines terms used in SHACL, the W3C Shapes Constraint Language.\"@en ;\n" +
                "\tsh:declare [\n" +
                "\t\tsh:prefix \"sh\" ;\n" +
                "\t\tsh:namespace \"http://www.w3.org/ns/shacl#\" ;\n" +
                "\t] ;\n" +
                "\tsh:suggestedShapesGraph <http://www.w3.org/ns/shacl-shacl#> .\n" +
                "\n" +
                "\n" +
                "# Shapes vocabulary -----------------------------------------------------------\n" +
                "\n" +
                "sh:Shape\n" +
                "\ta rdfs:Class ;\n" +
                "\trdfs:label \"Shape\"@en ;\n" +
                "\trdfs:comment \"A shape is a collection of constraints that may be targeted for certain nodes.\"@en ;\n" +
                "\trdfs:subClassOf rdfs:Resource ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:NodeShape\n" +
                "\ta rdfs:Class ;\n" +
                "\trdfs:label \"Node shape\"@en ;\n" +
                "\trdfs:comment \"A node shape is a shape that specifies constraint that need to be met with respect to focus nodes.\"@en ;\n" +
                "\trdfs:subClassOf sh:Shape ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:PropertyShape\n" +
                "\ta rdfs:Class ;\n" +
                "\trdfs:label \"Property shape\"@en ;\n" +
                "\trdfs:comment \"A property shape is a shape that specifies constraints on the values of a focus node for a given property or path.\"@en ;\n" +
                "\trdfs:subClassOf sh:Shape ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:deactivated\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"deactivated\"@en ;\n" +
                "\trdfs:comment \"If set to true then all nodes conform to this.\"@en ;\n" +
                "\t# rdfs:domain sh:Shape or sh:SPARQLConstraint\n" +
                "\trdfs:range xsd:boolean ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:targetClass \n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"target class\"@en ;\n" +
                "\trdfs:comment \"Links a shape to a class, indicating that all instances of the class must conform to the shape.\"@en ;\n" +
                "\trdfs:domain sh:Shape ;\n" +
                "\trdfs:range rdfs:Class ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:targetNode \n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"target node\"@en ;\n" +
                "\trdfs:comment \"Links a shape to individual nodes, indicating that these nodes must conform to the shape.\"@en ;\n" +
                "\trdfs:domain sh:Shape ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:targetObjectsOf\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"target objects of\"@en ;\n" +
                "\trdfs:comment \"Links a shape to a property, indicating that all all objects of triples that have the given property as their predicate must conform to the shape.\"@en ;\n" +
                "\trdfs:domain sh:Shape ;\n" +
                "\trdfs:range rdf:Property ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:targetSubjectsOf\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"target subjects of\"@en ;\n" +
                "\trdfs:comment \"Links a shape to a property, indicating that all subjects of triples that have the given property as their predicate must conform to the shape.\"@en ;\n" +
                "\trdfs:domain sh:Shape ;\n" +
                "\trdfs:range rdf:Property ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:message\n" +
                "\ta rdf:Property ;\n" +
                "\t# domain: sh:Shape or sh:SPARQLConstraint or sh:SPARQLSelectValidator or sh:SPARQLAskValidator\n" +
                "\t# range: xsd:string or rdf:langString\n" +
                "\trdfs:label \"message\"@en ;\n" +
                "\trdfs:comment \"A human-readable message (possibly with placeholders for variables) explaining the cause of the result.\"@en ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:severity\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"severity\"@en ;\n" +
                "\trdfs:comment \"Defines the severity that validation results produced by a shape must have. Defaults to sh:Violation.\"@en ;\n" +
                "\trdfs:domain sh:Shape ;\n" +
                "\trdfs:range sh:Severity ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\n" +
                "# Node kind vocabulary --------------------------------------------------------\n" +
                "\n" +
                "sh:NodeKind\n" +
                "\ta rdfs:Class ;\n" +
                "\trdfs:label \"Node kind\"@en ;\n" +
                "\trdfs:comment \"The class of all node kinds, including sh:BlankNode, sh:IRI, sh:Literal or the combinations of these: sh:BlankNodeOrIRI, sh:BlankNodeOrLiteral, sh:IRIOrLiteral.\"@en ;\n" +
                "\trdfs:subClassOf rdfs:Resource ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:BlankNode\n" +
                "\ta sh:NodeKind ;\n" +
                "\trdfs:label \"Blank node\"@en ;\n" +
                "\trdfs:comment \"The node kind of all blank nodes.\"@en ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:BlankNodeOrIRI\n" +
                "\ta sh:NodeKind ;\n" +
                "\trdfs:label \"Blank node or IRI\"@en ;\n" +
                "\trdfs:comment \"The node kind of all blank nodes or IRIs.\"@en ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:BlankNodeOrLiteral\n" +
                "\ta sh:NodeKind ;\n" +
                "\trdfs:label \"Blank node or literal\"@en ;\n" +
                "\trdfs:comment \"The node kind of all blank nodes or literals.\"@en ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:IRI\n" +
                "\ta sh:NodeKind ;\n" +
                "\trdfs:label \"IRI\"@en ;\n" +
                "\trdfs:comment \"The node kind of all IRIs.\"@en ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:IRIOrLiteral\n" +
                "\ta sh:NodeKind ;\n" +
                "\trdfs:label \"IRI or literal\"@en ;\n" +
                "\trdfs:comment \"The node kind of all IRIs or literals.\"@en ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:Literal\n" +
                "\ta sh:NodeKind ;\n" +
                "\trdfs:label \"Literal\"@en ;\n" +
                "\trdfs:comment \"The node kind of all literals.\"@en ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\n" +
                "# Results vocabulary ----------------------------------------------------------\n" +
                "\n" +
                "sh:ValidationReport\n" +
                "\ta rdfs:Class ;\n" +
                "\trdfs:label \"Validation report\"@en ;\n" +
                "\trdfs:comment \"The class of SHACL validation reports.\"@en ;\n" +
                "\trdfs:subClassOf rdfs:Resource ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:conforms\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"conforms\"@en ;\n" +
                "\trdfs:comment \"True if the validation did not produce any validation results, and false otherwise.\"@en ;\n" +
                "\trdfs:domain sh:ValidationReport ;\n" +
                "\trdfs:range xsd:boolean ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:result\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"result\"@en ;\n" +
                "\trdfs:comment \"The validation results contained in a validation report.\"@en ;\n" +
                "\trdfs:domain sh:ValidationReport ;\n" +
                "\trdfs:range sh:ValidationResult ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:shapesGraphWellFormed\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"shapes graph well-formed\"@en ;\n" +
                "\trdfs:comment \"If true then the validation engine was certain that the shapes graph has passed all SHACL syntax requirements during the validation process.\"@en ;\n" +
                "\trdfs:domain sh:ValidationReport ;\n" +
                "\trdfs:range xsd:boolean ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:AbstractResult\n" +
                "\ta rdfs:Class ;\n" +
                "\trdfs:label \"Abstract result\"@en ;\n" +
                "\trdfs:comment \"The base class of validation results, typically not instantiated directly.\"@en ;\n" +
                "\trdfs:subClassOf rdfs:Resource ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:ValidationResult\n" +
                "\ta rdfs:Class ;\n" +
                "\trdfs:label \"Validation result\"@en ;\n" +
                "\trdfs:comment \"The class of validation results.\"@en ;\n" +
                "\trdfs:subClassOf sh:AbstractResult ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:Severity\n" +
                "\ta rdfs:Class ;\n" +
                "\trdfs:label \"Severity\"@en ;\n" +
                "\trdfs:comment \"The class of validation result severity levels, including violation and warning levels.\"@en ;\n" +
                "\trdfs:subClassOf rdfs:Resource ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:Info\n" +
                "\ta sh:Severity ;\n" +
                "\trdfs:label \"Info\"@en ;\n" +
                "\trdfs:comment \"The severity for an informational validation result.\"@en ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:Violation\n" +
                "\ta sh:Severity ;\n" +
                "\trdfs:label \"Violation\"@en ;\n" +
                "\trdfs:comment \"The severity for a violation validation result.\"@en ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:Warning\n" +
                "\ta sh:Severity ;\n" +
                "\trdfs:label \"Warning\"@en ;\n" +
                "\trdfs:comment \"The severity for a warning validation result.\"@en ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:detail\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"detail\"@en ;\n" +
                "\trdfs:comment \"Links a result with other results that provide more details, for example to describe violations against nested shapes.\"@en ;\n" +
                "\trdfs:domain sh:AbstractResult ;\n" +
                "\trdfs:range sh:AbstractResult ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:focusNode\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"focus node\"@en ;\n" +
                "\trdfs:comment \"The focus node that was validated when the result was produced.\"@en ;\n" +
                "\trdfs:domain sh:AbstractResult ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:resultMessage\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"result message\"@en ;\n" +
                "\trdfs:comment \"Human-readable messages explaining the cause of the result.\"@en ;\n" +
                "\trdfs:domain sh:AbstractResult ;\n" +
                "\t# range: xsd:string or rdf:langString\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:resultPath\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"result path\"@en ;\n" +
                "\trdfs:comment \"The path of a validation result, based on the path of the validated property shape.\"@en ;\n" +
                "\trdfs:domain sh:AbstractResult ;\n" +
                "\trdfs:range rdfs:Resource ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:resultSeverity\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"result severity\"@en ;\n" +
                "\trdfs:comment \"The severity of the result, e.g. warning.\"@en ;\n" +
                "\trdfs:domain sh:AbstractResult ;\n" +
                "\trdfs:range sh:Severity ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:sourceConstraint\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"source constraint\"@en ;\n" +
                "\trdfs:comment \"The constraint that was validated when the result was produced.\"@en ;\n" +
                "\trdfs:domain sh:AbstractResult ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:sourceShape\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"source shape\"@en ;\n" +
                "\trdfs:comment \"The shape that is was validated when the result was produced.\"@en ;\n" +
                "\trdfs:domain sh:AbstractResult ;\n" +
                "\trdfs:range sh:Shape ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:sourceConstraintComponent\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"source constraint component\"@en ;\n" +
                "\trdfs:comment \"The constraint component that is the source of the result.\"@en ;\n" +
                "\trdfs:domain sh:AbstractResult ;\n" +
                "\trdfs:range sh:ConstraintComponent ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:value\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"value\"@en ;\n" +
                "\trdfs:comment \"An RDF node that has caused the result.\"@en ;\n" +
                "\trdfs:domain sh:AbstractResult ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\t\n" +
                "# Graph properties ------------------------------------------------------------\n" +
                "\n" +
                "sh:shapesGraph\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"shapes graph\"@en ;\n" +
                "\trdfs:comment \"Shapes graphs that should be used when validating this data graph.\"@en ;\n" +
                "\trdfs:domain owl:Ontology ;\n" +
                "\trdfs:range owl:Ontology ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:suggestedShapesGraph\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"suggested shapes graph\"@en ;\n" +
                "\trdfs:comment \"Suggested shapes graphs for this ontology. The values of this property may be used in the absence of specific sh:shapesGraph statements.\"@en ;\n" +
                "\trdfs:domain owl:Ontology ;\n" +
                "\trdfs:range owl:Ontology ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:entailment\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"entailment\"@en ;\n" +
                "\trdfs:comment \"An entailment regime that indicates what kind of inferencing is required by a shapes graph.\"@en ;\n" +
                "\trdfs:domain owl:Ontology ;\n" +
                "\trdfs:range rdfs:Resource ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\n" +
                "# Path vocabulary -------------------------------------------------------------\n" +
                "\n" +
                "sh:path\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"path\"@en ;\n" +
                "\trdfs:comment \"Specifies the property path of a property shape.\"@en ;\n" +
                "\trdfs:domain sh:PropertyShape ;\n" +
                "\trdfs:range rdfs:Resource ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:inversePath\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"inverse path\"@en ;\n" +
                "\trdfs:comment \"The (single) value of this property represents an inverse path (object to subject).\"@en ;\n" +
                "\trdfs:range rdfs:Resource ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:alternativePath\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"alternative path\"@en ;\n" +
                "\trdfs:comment \"The (single) value of this property must be a list of path elements, representing the elements of alternative paths.\"@en ;\n" +
                "\trdfs:range rdf:List ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:zeroOrMorePath\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"zero or more path\"@en ;\n" +
                "\trdfs:comment \"The (single) value of this property represents a path that is matched zero or more times.\"@en ;\n" +
                "\trdfs:range rdfs:Resource ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:oneOrMorePath\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"one or more path\"@en ;\n" +
                "\trdfs:comment \"The (single) value of this property represents a path that is matched one or more times.\"@en ;\n" +
                "\trdfs:range rdfs:Resource ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:zeroOrOnePath\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"zero or one path\"@en ;\n" +
                "\trdfs:comment \"The (single) value of this property represents a path that is matched zero or one times.\"@en ;\n" +
                "\trdfs:range rdfs:Resource ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\n" +
                "# Parameters metamodel --------------------------------------------------------\n" +
                "\n" +
                "sh:Parameterizable\n" +
                "\ta rdfs:Class ;\n" +
                "\trdfs:label \"Parameterizable\"@en ;\n" +
                "\trdfs:comment \"Superclass of components that can take parameters, especially functions and constraint components.\"@en ;\n" +
                "\trdfs:subClassOf rdfs:Resource ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:parameter\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"parameter\"@en ;\n" +
                "\trdfs:comment \"The parameters of a function or constraint component.\"@en ;\n" +
                "\trdfs:domain sh:Parameterizable ;\n" +
                "\trdfs:range sh:Parameter ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:labelTemplate\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"label template\"@en ;\n" +
                "\trdfs:comment \"Outlines how human-readable labels of instances of the associated Parameterizable shall be produced. The values can contain {?paramName} as placeholders for the actual values of the given parameter.\"@en ;\n" +
                "\trdfs:domain sh:Parameterizable ;\n" +
                "\t# range: xsd:string or rdf:langString\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:Parameter\n" +
                "\ta rdfs:Class ;\n" +
                "\trdfs:label \"Parameter\"@en ;\n" +
                "\trdfs:comment \"The class of parameter declarations, consisting of a path predicate and (possibly) information about allowed value type, cardinality and other characteristics.\"@en ;\n" +
                "\trdfs:subClassOf sh:PropertyShape ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:optional\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"optional\"@en ;\n" +
                "\trdfs:comment \"Indicates whether a parameter is optional.\"@en ;\n" +
                "\trdfs:domain sh:Parameter ;\n" +
                "\trdfs:range xsd:boolean ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\n" +
                "# Constraint components metamodel ---------------------------------------------\n" +
                "\n" +
                "sh:ConstraintComponent\n" +
                "\ta rdfs:Class ;\n" +
                "\trdfs:label \"Constraint component\"@en ;\n" +
                "\trdfs:comment \"The class of constraint components.\"@en ;\n" +
                "\trdfs:subClassOf sh:Parameterizable ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:validator\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"validator\"@en ;\n" +
                "\trdfs:comment \"The validator(s) used to evaluate constraints of either node or property shapes.\"@en ;\n" +
                "\trdfs:domain sh:ConstraintComponent ;\n" +
                "\trdfs:range sh:Validator ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:nodeValidator\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"shape validator\"@en ;\n" +
                "\trdfs:comment \"The validator(s) used to evaluate a constraint in the context of a node shape.\"@en ;\n" +
                "\trdfs:domain sh:ConstraintComponent ;\n" +
                "\trdfs:range sh:Validator ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:propertyValidator\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"property validator\"@en ;\n" +
                "\trdfs:comment \"The validator(s) used to evaluate a constraint in the context of a property shape.\"@en ;\n" +
                "\trdfs:domain sh:ConstraintComponent ;\n" +
                "\trdfs:range sh:Validator ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:Validator\n" +
                "\ta rdfs:Class ;\n" +
                "\trdfs:label \"Validator\"@en ;\n" +
                "\trdfs:comment \"The class of validators, which provide instructions on how to process a constraint definition. This class serves as base class for the SPARQL-based validators and other possible implementations.\"@en ;\n" +
                "\trdfs:subClassOf rdfs:Resource ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:SPARQLAskValidator\n" +
                "\ta rdfs:Class ;\n" +
                "\trdfs:label \"SPARQL ASK validator\"@en ;\n" +
                "\trdfs:comment \"The class of validators based on SPARQL ASK queries. The queries are evaluated for each value node and are supposed to return true if the given node conforms.\"@en ;\n" +
                "\trdfs:subClassOf sh:Validator ;\n" +
                "\trdfs:subClassOf sh:SPARQLAskExecutable ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:SPARQLSelectValidator\n" +
                "\ta rdfs:Class ;\n" +
                "\trdfs:label \"SPARQL SELECT validator\"@en ;\n" +
                "\trdfs:comment \"The class of validators based on SPARQL SELECT queries. The queries are evaluated for each focus node and are supposed to produce bindings for all focus nodes that do not conform.\"@en ;\n" +
                "\trdfs:subClassOf sh:Validator ;\n" +
                "\trdfs:subClassOf sh:SPARQLSelectExecutable ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\n" +
                "# Library of Core Constraint Components and their properties ------------------\n" +
                "\n" +
                "sh:AndConstraintComponent\n" +
                "\ta sh:ConstraintComponent ;\n" +
                "\trdfs:label \"And constraint component\"@en ;\n" +
                "\trdfs:comment \"A constraint component that can be used to test whether a value node conforms to all members of a provided list of shapes.\"@en ;\n" +
                "\tsh:parameter sh:AndConstraintComponent-and ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:AndConstraintComponent-and\n" +
                "\ta sh:Parameter ;\n" +
                "\tsh:path sh:and ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:and\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"and\"@en ;\n" +
                "\trdfs:comment \"RDF list of shapes to validate the value nodes against.\"@en ;\n" +
                "\trdfs:range rdf:List ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\n" +
                "sh:ClassConstraintComponent \n" +
                "\ta sh:ConstraintComponent ;\n" +
                "\trdfs:label \"Class constraint component\"@en ;\n" +
                "\trdfs:comment \"A constraint component that can be used to verify that each value node is an instance of a given type.\"@en ;\n" +
                "\tsh:parameter sh:ClassConstraintComponent-class ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:ClassConstraintComponent-class\n" +
                "\ta sh:Parameter ;\n" +
                "\tsh:path sh:class ;\n" +
                "\tsh:nodeKind sh:IRI ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:class\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"class\"@en ;\n" +
                "\trdfs:comment \"The type that all value nodes must have.\"@en ;\n" +
                "\trdfs:range rdfs:Class ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\n" +
                "sh:ClosedConstraintComponent\n" +
                "\ta sh:ConstraintComponent ;\n" +
                "\trdfs:label \"Closed constraint component\"@en ;\n" +
                "\trdfs:comment \"A constraint component that can be used to indicate that focus nodes must only have values for those properties that have been explicitly enumerated via sh:property/sh:path.\"@en ;\n" +
                "\tsh:parameter sh:ClosedConstraintComponent-closed ;\n" +
                "\tsh:parameter sh:ClosedConstraintComponent-ignoredProperties ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:ClosedConstraintComponent-closed\n" +
                "\ta sh:Parameter ; \n" +
                "\tsh:path sh:closed ;\n" +
                "\tsh:datatype xsd:boolean ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:ClosedConstraintComponent-ignoredProperties\n" +
                "\ta sh:Parameter ;\n" +
                "\tsh:path sh:ignoredProperties ;\n" +
                "\tsh:optional true ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:closed\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"closed\"@en ;\n" +
                "\trdfs:comment \"If set to true then the shape is closed.\"@en ;\n" +
                "\trdfs:range xsd:boolean ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:ignoredProperties\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"ignored properties\"@en ;\n" +
                "\trdfs:comment \"An optional RDF list of properties that are also permitted in addition to those explicitly enumerated via sh:property/sh:path.\"@en ;\n" +
                "\trdfs:range rdf:List ;    # members: rdf:Property\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\n" +
                "sh:DatatypeConstraintComponent\n" +
                "\ta sh:ConstraintComponent ;\n" +
                "\trdfs:label \"Datatype constraint component\"@en ;\n" +
                "\trdfs:comment \"A constraint component that can be used to restrict the datatype of all value nodes.\"@en ;\n" +
                "\tsh:parameter sh:DatatypeConstraintComponent-datatype ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:DatatypeConstraintComponent-datatype\n" +
                "\ta sh:Parameter ;\n" +
                "\tsh:path sh:datatype ;\n" +
                "\tsh:nodeKind sh:IRI ;\n" +
                "\tsh:maxCount 1 ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:datatype\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"datatype\"@en ;\n" +
                "\trdfs:comment \"Specifies an RDF datatype that all value nodes must have.\"@en ;\n" +
                "\trdfs:range rdfs:Datatype ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\n" +
                "sh:DisjointConstraintComponent\n" +
                "\ta sh:ConstraintComponent ;\n" +
                "\trdfs:label \"Disjoint constraint component\"@en ;\n" +
                "\trdfs:comment \"A constraint component that can be used to verify that the set of value nodes is disjoint with the the set of nodes that have the focus node as subject and the value of a given property as predicate.\"@en ;\n" +
                "\tsh:parameter sh:DisjointConstraintComponent-disjoint ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:DisjointConstraintComponent-disjoint\n" +
                "\ta sh:Parameter ;\n" +
                "\tsh:path sh:disjoint ;\n" +
                "\tsh:nodeKind sh:IRI ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:disjoint\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"disjoint\"@en ;\n" +
                "\trdfs:comment \"Specifies a property where the set of values must be disjoint with the value nodes.\"@en ;\n" +
                "\trdfs:range rdf:Property ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\n" +
                "sh:EqualsConstraintComponent\n" +
                "\ta sh:ConstraintComponent ;\n" +
                "\trdfs:label \"Equals constraint component\"@en ;\n" +
                "\trdfs:comment \"A constraint component that can be used to verify that the set of value nodes is equal to the set of nodes that have the focus node as subject and the value of a given property as predicate.\"@en ;\n" +
                "\tsh:parameter sh:EqualsConstraintComponent-equals ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:EqualsConstraintComponent-equals\n" +
                "\ta sh:Parameter ;\n" +
                "\tsh:path sh:equals ;\n" +
                "\tsh:nodeKind sh:IRI ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:equals\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"equals\"@en ;\n" +
                "\trdfs:comment \"Specifies a property that must have the same values as the value nodes.\"@en ;\n" +
                "\trdfs:range rdf:Property ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\n" +
                "sh:HasValueConstraintComponent\n" +
                "\ta sh:ConstraintComponent ;\n" +
                "\trdfs:label \"Has-value constraint component\"@en ;\n" +
                "\trdfs:comment \"A constraint component that can be used to verify that one of the value nodes is a given RDF node.\"@en ;\n" +
                "\tsh:parameter sh:HasValueConstraintComponent-hasValue ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:HasValueConstraintComponent-hasValue\n" +
                "\ta sh:Parameter ;\n" +
                "\tsh:path sh:hasValue ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:hasValue\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"has value\"@en ;\n" +
                "\trdfs:comment \"Specifies a value that must be among the value nodes.\"@en ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\n" +
                "sh:InConstraintComponent\n" +
                "\ta sh:ConstraintComponent ;\n" +
                "\trdfs:label \"In constraint component\"@en ;\n" +
                "\trdfs:comment \"A constraint component that can be used to exclusively enumerate the permitted value nodes.\"@en ;\n" +
                "\tsh:parameter sh:InConstraintComponent-in ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:InConstraintComponent-in\n" +
                "\ta sh:Parameter ;\n" +
                "\tsh:path sh:in ;\n" +
                "\tsh:maxCount 1 ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:in\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"in\"@en ;\n" +
                "\trdfs:comment \"Specifies a list of allowed values so that each value node must be among the members of the given list.\"@en ;\n" +
                "\trdfs:range rdf:List ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\n" +
                "sh:LanguageInConstraintComponent\n" +
                "\ta sh:ConstraintComponent ;\n" +
                "\trdfs:label \"Language-in constraint component\"@en ;\n" +
                "\trdfs:comment \"A constraint component that can be used to enumerate language tags that all value nodes must have.\"@en ;\n" +
                "\tsh:parameter sh:LanguageInConstraintComponent-languageIn ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:LanguageInConstraintComponent-languageIn\n" +
                "\ta sh:Parameter ;\n" +
                "\tsh:path sh:languageIn ;\n" +
                "\tsh:maxCount 1 ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:languageIn\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"language in\"@en ;\n" +
                "\trdfs:comment \"Specifies a list of language tags that all value nodes must have.\"@en ;\n" +
                "\trdfs:range rdf:List ;   # members: xsd:string\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\n" +
                "sh:LessThanConstraintComponent\n" +
                "\ta sh:ConstraintComponent ;\n" +
                "\trdfs:label \"Less-than constraint component\"@en ;\n" +
                "\trdfs:comment \"A constraint component that can be used to verify that each value node is smaller than all the nodes that have the focus node as subject and the value of a given property as predicate.\"@en ;\n" +
                "\tsh:parameter sh:LessThanConstraintComponent-lessThan ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:LessThanConstraintComponent-lessThan\n" +
                "\ta sh:Parameter ;\n" +
                "\tsh:path sh:lessThan ;\n" +
                "\tsh:nodeKind sh:IRI ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:lessThan\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"less than\"@en ;\n" +
                "\trdfs:comment \"Specifies a property that must have smaller values than the value nodes.\"@en ;\n" +
                "\trdfs:range rdf:Property ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\n" +
                "sh:LessThanOrEqualsConstraintComponent\n" +
                "\ta sh:ConstraintComponent ;\n" +
                "\trdfs:label \"less-than-or-equals constraint component\"@en ;\n" +
                "\trdfs:comment \"A constraint component that can be used to verify that every value node is smaller than all the nodes that have the focus node as subject and the value of a given property as predicate.\"@en ;\n" +
                "\tsh:parameter sh:LessThanOrEqualsConstraintComponent-lessThanOrEquals ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:LessThanOrEqualsConstraintComponent-lessThanOrEquals\n" +
                "\ta sh:Parameter ;\n" +
                "\tsh:path sh:lessThanOrEquals ;\n" +
                "\tsh:nodeKind sh:IRI ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:lessThanOrEquals\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"less than or equals\"@en ;\n" +
                "\trdfs:comment \"Specifies a property that must have smaller or equal values than the value nodes.\"@en ;\n" +
                "\trdfs:range rdf:Property ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\n" +
                "sh:MaxCountConstraintComponent\n" +
                "\ta sh:ConstraintComponent ;\n" +
                "\trdfs:label \"Max-count constraint component\"@en ;\n" +
                "\trdfs:comment \"A constraint component that can be used to restrict the maximum number of value nodes.\"@en ;\n" +
                "\tsh:parameter sh:MaxCountConstraintComponent-maxCount ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:MaxCountConstraintComponent-maxCount\n" +
                "\ta sh:Parameter ;\n" +
                "\tsh:path sh:maxCount ;\n" +
                "\tsh:datatype xsd:integer ;\n" +
                "\tsh:maxCount 1 ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:maxCount\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"max count\"@en ;\n" +
                "\trdfs:comment \"Specifies the maximum number of values in the set of value nodes.\"@en ;\n" +
                "\trdfs:range xsd:integer ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\n" +
                "sh:MaxExclusiveConstraintComponent\n" +
                "\ta sh:ConstraintComponent ;\n" +
                "\trdfs:label \"Max-exclusive constraint component\"@en ;\n" +
                "\trdfs:comment \"A constraint component that can be used to restrict the range of value nodes with a maximum exclusive value.\"@en ;\n" +
                "\tsh:parameter sh:MaxExclusiveConstraintComponent-maxExclusive ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:MaxExclusiveConstraintComponent-maxExclusive\n" +
                "\ta sh:Parameter ;\n" +
                "\tsh:path sh:maxExclusive ;\n" +
                "\tsh:maxCount 1 ;\n" +
                "\tsh:nodeKind sh:Literal ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:maxExclusive\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"max exclusive\"@en ;\n" +
                "\trdfs:comment \"Specifies the maximum exclusive value of each value node.\"@en ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\n" +
                "sh:MaxInclusiveConstraintComponent\n" +
                "\ta sh:ConstraintComponent ;\n" +
                "\trdfs:label \"Max-inclusive constraint component\"@en ;\n" +
                "\trdfs:comment \"A constraint component that can be used to restrict the range of value nodes with a maximum inclusive value.\"@en ;\n" +
                "\tsh:parameter sh:MaxInclusiveConstraintComponent-maxInclusive ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:MaxInclusiveConstraintComponent-maxInclusive\n" +
                "\ta sh:Parameter ;\n" +
                "\tsh:path sh:maxInclusive ;\n" +
                "\tsh:maxCount 1 ;\n" +
                "\tsh:nodeKind sh:Literal ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:maxInclusive\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"max inclusive\"@en ;\n" +
                "\trdfs:comment \"Specifies the maximum inclusive value of each value node.\"@en ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\n" +
                "sh:MaxLengthConstraintComponent\n" +
                "\ta sh:ConstraintComponent ;\n" +
                "\trdfs:label \"Max-length constraint component\"@en ;\n" +
                "\trdfs:comment \"A constraint component that can be used to restrict the maximum string length of value nodes.\"@en ;\n" +
                "\tsh:parameter sh:MaxLengthConstraintComponent-maxLength ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:MaxLengthConstraintComponent-maxLength\n" +
                "\ta sh:Parameter ;\n" +
                "\tsh:path sh:maxLength ;\n" +
                "\tsh:datatype xsd:integer ;\n" +
                "\tsh:maxCount 1 ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:maxLength\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"max length\"@en ;\n" +
                "\trdfs:comment \"Specifies the maximum string length of each value node.\"@en ;\n" +
                "\trdfs:range xsd:integer ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\n" +
                "sh:MinCountConstraintComponent\n" +
                "\ta sh:ConstraintComponent ;\n" +
                "\trdfs:label \"Min-count constraint component\"@en ;\n" +
                "\trdfs:comment \"A constraint component that can be used to restrict the minimum number of value nodes.\"@en ;\n" +
                "\tsh:parameter sh:MinCountConstraintComponent-minCount ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:MinCountConstraintComponent-minCount\n" +
                "\ta sh:Parameter ;\n" +
                "\tsh:path sh:minCount ;\n" +
                "\tsh:datatype xsd:integer ;\n" +
                "\tsh:maxCount 1 ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:minCount\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"min count\"@en ;\n" +
                "\trdfs:comment \"Specifies the minimum number of values in the set of value nodes.\"@en ;\n" +
                "\trdfs:range xsd:integer ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\n" +
                "sh:MinExclusiveConstraintComponent\n" +
                "\ta sh:ConstraintComponent ;\n" +
                "\trdfs:label \"Min-exclusive constraint component\"@en ;\n" +
                "\trdfs:comment \"A constraint component that can be used to restrict the range of value nodes with a minimum exclusive value.\"@en ;\n" +
                "\tsh:parameter sh:MinExclusiveConstraintComponent-minExclusive ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:MinExclusiveConstraintComponent-minExclusive\n" +
                "\ta sh:Parameter ;\n" +
                "\tsh:path sh:minExclusive ;\n" +
                "\tsh:maxCount 1 ;\n" +
                "\tsh:nodeKind sh:Literal ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:minExclusive\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"min exclusive\"@en ;\n" +
                "\trdfs:comment \"Specifies the minimum exclusive value of each value node.\"@en ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\n" +
                "sh:MinInclusiveConstraintComponent\n" +
                "\ta sh:ConstraintComponent ;\n" +
                "\trdfs:label \"Min-inclusive constraint component\"@en ;\n" +
                "\trdfs:comment \"A constraint component that can be used to restrict the range of value nodes with a minimum inclusive value.\"@en ;\n" +
                "\tsh:parameter sh:MinInclusiveConstraintComponent-minInclusive ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:MinInclusiveConstraintComponent-minInclusive\n" +
                "\ta sh:Parameter ;\n" +
                "\tsh:path sh:minInclusive ;\n" +
                "\tsh:maxCount 1 ;\n" +
                "\tsh:nodeKind sh:Literal ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:minInclusive\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"min inclusive\"@en ;\n" +
                "\trdfs:comment \"Specifies the minimum inclusive value of each value node.\"@en ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\n" +
                "sh:MinLengthConstraintComponent\n" +
                "\ta sh:ConstraintComponent ;\n" +
                "\trdfs:label \"Min-length constraint component\"@en ;\n" +
                "\trdfs:comment \"A constraint component that can be used to restrict the minimum string length of value nodes.\"@en ;\n" +
                "\tsh:parameter sh:MinLengthConstraintComponent-minLength ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:MinLengthConstraintComponent-minLength\n" +
                "\ta sh:Parameter ;\n" +
                "\tsh:path sh:minLength ;\n" +
                "\tsh:datatype xsd:integer ;\n" +
                "\tsh:maxCount 1 ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:minLength\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"min length\"@en ;\n" +
                "\trdfs:comment \"Specifies the minimum string length of each value node.\"@en ;\n" +
                "\trdfs:range xsd:integer ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\n" +
                "sh:NodeConstraintComponent\n" +
                "\ta sh:ConstraintComponent ;\n" +
                "\trdfs:label \"Node constraint component\"@en ;\n" +
                "\trdfs:comment \"A constraint component that can be used to verify that all value nodes conform to the given node shape.\"@en ;\n" +
                "\tsh:parameter sh:NodeConstraintComponent-node ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:NodeConstraintComponent-node\n" +
                "\ta sh:Parameter ;\n" +
                "\tsh:path sh:node ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:node\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"node\"@en ;\n" +
                "\trdfs:comment \"Specifies the node shape that all value nodes must conform to.\"@en ;\n" +
                "\trdfs:range sh:NodeShape ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\n" +
                "sh:NodeKindConstraintComponent\n" +
                "\ta sh:ConstraintComponent ;\n" +
                "\trdfs:label \"Node-kind constraint component\"@en ;\n" +
                "\trdfs:comment \"A constraint component that can be used to restrict the RDF node kind of each value node.\"@en ;\n" +
                "\tsh:parameter sh:NodeKindConstraintComponent-nodeKind ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:NodeKindConstraintComponent-nodeKind\n" +
                "\ta sh:Parameter ;\n" +
                "\tsh:path sh:nodeKind ;\n" +
                "\tsh:in ( sh:BlankNode sh:IRI sh:Literal sh:BlankNodeOrIRI sh:BlankNodeOrLiteral sh:IRIOrLiteral ) ;\n" +
                "\tsh:maxCount 1 ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:nodeKind\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"node kind\"@en ;\n" +
                "\trdfs:comment \"Specifies the node kind (e.g. IRI or literal) each value node.\"@en ;\n" +
                "\trdfs:range sh:NodeKind ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\n" +
                "sh:NotConstraintComponent\n" +
                "\ta sh:ConstraintComponent ;\n" +
                "\trdfs:label \"Not constraint component\"@en ;\n" +
                "\trdfs:comment \"A constraint component that can be used to verify that value nodes do not conform to a given shape.\"@en ;\n" +
                "\tsh:parameter sh:NotConstraintComponent-not ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:NotConstraintComponent-not\n" +
                "\ta sh:Parameter ;\n" +
                "\tsh:path sh:not ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:not\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"not\"@en ;\n" +
                "\trdfs:comment \"Specifies a shape that the value nodes must not conform to.\"@en ;\n" +
                "\trdfs:range sh:Shape ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\n" +
                "sh:OrConstraintComponent\n" +
                "\ta sh:ConstraintComponent ;\n" +
                "\trdfs:label \"Or constraint component\"@en ;\n" +
                "\trdfs:comment \"A constraint component that can be used to restrict the value nodes so that they conform to at least one out of several provided shapes.\"@en ;\n" +
                "\tsh:parameter sh:OrConstraintComponent-or ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:OrConstraintComponent-or\n" +
                "\ta sh:Parameter ;\n" +
                "\tsh:path sh:or ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:or\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"or\"@en ;\n" +
                "\trdfs:comment \"Specifies a list of shapes so that the value nodes must conform to at least one of the shapes.\"@en ;\n" +
                "\trdfs:range rdf:List ;    # members: sh:Shape ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\n" +
                "sh:PatternConstraintComponent\n" +
                "\ta sh:ConstraintComponent ;\n" +
                "\trdfs:label \"Pattern constraint component\"@en ;\n" +
                "\trdfs:comment \"A constraint component that can be used to verify that every value node matches a given regular expression.\"@en ;\n" +
                "\tsh:parameter sh:PatternConstraintComponent-pattern ;\n" +
                "\tsh:parameter sh:PatternConstraintComponent-flags ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:PatternConstraintComponent-pattern\n" +
                "\ta sh:Parameter ;\n" +
                "\tsh:path sh:pattern ;\n" +
                "\tsh:datatype xsd:string ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:PatternConstraintComponent-flags\n" +
                "\ta sh:Parameter ;\n" +
                "\tsh:path sh:flags ;\n" +
                "\tsh:datatype xsd:string ;\n" +
                "\tsh:optional true ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:flags\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"flags\"@en ;\n" +
                "\trdfs:comment \"An optional flag to be used with regular expression pattern matching.\"@en ;\n" +
                "\trdfs:range xsd:string ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:pattern\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"pattern\"@en ;\n" +
                "\trdfs:comment \"Specifies a regular expression pattern that the string representations of the value nodes must match.\"@en ;\n" +
                "\trdfs:range xsd:string ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\n" +
                "sh:PropertyConstraintComponent\n" +
                "\ta sh:ConstraintComponent ;\n" +
                "\trdfs:label \"Property constraint component\"@en ;\n" +
                "\trdfs:comment \"A constraint component that can be used to verify that all value nodes conform to the given property shape.\"@en ;\n" +
                "\tsh:parameter sh:PropertyConstraintComponent-property ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:PropertyConstraintComponent-property\n" +
                "\ta sh:Parameter ;\n" +
                "\tsh:path sh:property ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:property\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"property\"@en ;\n" +
                "\trdfs:comment \"Links a shape to its property shapes.\"@en ;\n" +
                "\trdfs:domain sh:Shape ;\n" +
                "\trdfs:range sh:PropertyShape ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\n" +
                "sh:QualifiedMaxCountConstraintComponent\n" +
                "\ta sh:ConstraintComponent ;\n" +
                "\trdfs:label \"Qualified-max-count constraint component\"@en ;\n" +
                "\trdfs:comment \"A constraint component that can be used to verify that a specified maximum number of value nodes conforms to a given shape.\"@en ;\n" +
                "\tsh:parameter sh:QualifiedMaxCountConstraintComponent-qualifiedMaxCount ;\n" +
                "\tsh:parameter sh:QualifiedMaxCountConstraintComponent-qualifiedValueShape ;\n" +
                "\tsh:parameter sh:QualifiedMaxCountConstraintComponent-qualifiedValueShapesDisjoint ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:QualifiedMaxCountConstraintComponent-qualifiedMaxCount\n" +
                "\ta sh:Parameter ;\n" +
                "\tsh:path sh:qualifiedMaxCount ;\n" +
                "\tsh:datatype xsd:integer ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:QualifiedMaxCountConstraintComponent-qualifiedValueShape\n" +
                "\ta sh:Parameter ;\n" +
                "\tsh:path sh:qualifiedValueShape ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:QualifiedMaxCountConstraintComponent-qualifiedValueShapesDisjoint\n" +
                "\ta sh:Parameter ;\n" +
                "\tsh:path sh:qualifiedValueShapesDisjoint ;\n" +
                "\tsh:datatype xsd:boolean ;\n" +
                "\tsh:optional true ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\n" +
                "sh:QualifiedMinCountConstraintComponent\n" +
                "\ta sh:ConstraintComponent ;\n" +
                "\trdfs:label \"Qualified-min-count constraint component\"@en ;\n" +
                "\trdfs:comment \"A constraint component that can be used to verify that a specified minimum number of value nodes conforms to a given shape.\"@en ;\n" +
                "\tsh:parameter sh:QualifiedMinCountConstraintComponent-qualifiedMinCount ;\n" +
                "\tsh:parameter sh:QualifiedMinCountConstraintComponent-qualifiedValueShape ;\n" +
                "\tsh:parameter sh:QualifiedMinCountConstraintComponent-qualifiedValueShapesDisjoint ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:QualifiedMinCountConstraintComponent-qualifiedMinCount\n" +
                "\ta sh:Parameter ;\n" +
                "\tsh:path sh:qualifiedMinCount ;\n" +
                "\tsh:datatype xsd:integer ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:QualifiedMinCountConstraintComponent-qualifiedValueShape\n" +
                "\ta sh:Parameter ;\n" +
                "\tsh:path sh:qualifiedValueShape ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:QualifiedMinCountConstraintComponent-qualifiedValueShapesDisjoint\n" +
                "\ta sh:Parameter ;\n" +
                "\tsh:path sh:qualifiedValueShapesDisjoint ;\n" +
                "\tsh:datatype xsd:boolean ;\n" +
                "\tsh:optional true ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:qualifiedMaxCount\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"qualified max count\"@en ;\n" +
                "\trdfs:comment \"The maximum number of value nodes that can conform to the shape.\"@en ;\n" +
                "\trdfs:range xsd:integer ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:qualifiedMinCount\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"qualified min count\"@en ;\n" +
                "\trdfs:comment \"The minimum number of value nodes that must conform to the shape.\"@en ;\n" +
                "\trdfs:range xsd:integer ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:qualifiedValueShape\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"qualified value shape\"@en ;\n" +
                "\trdfs:comment \"The shape that a specified number of values must conform to.\"@en ;\n" +
                "\trdfs:range sh:Shape ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\t\n" +
                "sh:qualifiedValueShapesDisjoint\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"qualified value shapes disjoint\"@en ;\n" +
                "\trdfs:comment \"Can be used to mark the qualified value shape to be disjoint with its sibling shapes.\"@en ;\n" +
                "\trdfs:range xsd:boolean ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\n" +
                "sh:UniqueLangConstraintComponent\n" +
                "\ta sh:ConstraintComponent ;\n" +
                "\trdfs:label \"Unique-languages constraint component\"@en ;\n" +
                "\trdfs:comment \"A constraint component that can be used to specify that no pair of value nodes may use the same language tag.\"@en ;\n" +
                "\tsh:parameter sh:UniqueLangConstraintComponent-uniqueLang ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:UniqueLangConstraintComponent-uniqueLang\n" +
                "\ta sh:Parameter ;\n" +
                "\tsh:path sh:uniqueLang ;\n" +
                "\tsh:datatype xsd:boolean ;\n" +
                "\tsh:maxCount 1 ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:uniqueLang\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"unique languages\"@en ;\n" +
                "\trdfs:comment \"Specifies whether all node values must have a unique (or no) language tag.\"@en ;\n" +
                "\trdfs:range xsd:boolean ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\n" +
                "sh:XoneConstraintComponent\n" +
                "\ta sh:ConstraintComponent ;\n" +
                "\trdfs:label \"Exactly one constraint component\"@en ;\n" +
                "\trdfs:comment \"A constraint component that can be used to restrict the value nodes so that they conform to exactly one out of several provided shapes.\"@en ;\n" +
                "\tsh:parameter sh:XoneConstraintComponent-xone ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:XoneConstraintComponent-xone\n" +
                "\ta sh:Parameter ;\n" +
                "\tsh:path sh:xone ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:xone\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"exactly one\"@en ;\n" +
                "\trdfs:comment \"Specifies a list of shapes so that the value nodes must conform to exactly one of the shapes.\"@en ;\n" +
                "\trdfs:range rdf:List ;    # members: sh:Shape ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\n" +
                "# General SPARQL execution support --------------------------------------------\n" +
                "\n" +
                "sh:SPARQLExecutable\n" +
                "\ta rdfs:Class ;\n" +
                "\trdfs:label \"SPARQL executable\"@en ;\n" +
                "\trdfs:comment \"The class of resources that encapsulate a SPARQL query.\"@en ;\n" +
                "\trdfs:subClassOf rdfs:Resource ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:SPARQLAskExecutable\n" +
                "\ta rdfs:Class ;\n" +
                "\trdfs:label \"SPARQL ASK executable\"@en ;\n" +
                "\trdfs:comment \"The class of SPARQL executables that are based on an ASK query.\"@en ;\n" +
                "\trdfs:subClassOf sh:SPARQLExecutable ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:ask\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"ask\"@en ;\n" +
                "\trdfs:comment \"The SPARQL ASK query to execute.\"@en ;\n" +
                "\trdfs:domain sh:SPARQLAskExecutable ;\n" +
                "\trdfs:range xsd:string ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:SPARQLConstructExecutable\n" +
                "\ta rdfs:Class ;\n" +
                "\trdfs:label \"SPARQL CONSTRUCT executable\"@en ;\n" +
                "\trdfs:comment \"The class of SPARQL executables that are based on a CONSTRUCT query.\"@en ;\n" +
                "\trdfs:subClassOf sh:SPARQLExecutable ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:construct\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"construct\"@en ;\n" +
                "\trdfs:comment \"The SPARQL CONSTRUCT query to execute.\"@en ;\n" +
                "\trdfs:domain sh:SPARQLConstructExecutable ;\n" +
                "\trdfs:range xsd:string ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:SPARQLSelectExecutable\n" +
                "\ta rdfs:Class ;\n" +
                "\trdfs:label \"SPARQL SELECT executable\"@en ;\n" +
                "\trdfs:comment \"The class of SPARQL executables based on a SELECT query.\"@en ;\n" +
                "\trdfs:subClassOf sh:SPARQLExecutable ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:select\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"select\"@en ;\n" +
                "\trdfs:comment \"The SPARQL SELECT query to execute.\"@en ;\n" +
                "\trdfs:range xsd:string ;\n" +
                "\trdfs:domain sh:SPARQLSelectExecutable ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:SPARQLUpdateExecutable\n" +
                "\ta rdfs:Class ;\n" +
                "\trdfs:label \"SPARQL UPDATE executable\"@en ;\n" +
                "\trdfs:comment \"The class of SPARQL executables based on a SPARQL UPDATE.\"@en ;\n" +
                "\trdfs:subClassOf sh:SPARQLExecutable ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:update\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"update\"@en ;\n" +
                "\trdfs:comment \"The SPARQL UPDATE to execute.\"@en ;\n" +
                "\trdfs:domain sh:SPARQLUpdateExecutable ;\n" +
                "\trdfs:range xsd:string ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:prefixes\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"prefixes\"@en ;\n" +
                "\trdfs:comment \"The prefixes that shall be applied before parsing the associated SPARQL query.\"@en ;\n" +
                "\trdfs:domain sh:SPARQLExecutable ;\n" +
                "\trdfs:range owl:Ontology ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:PrefixDeclaration\n" +
                "\ta rdfs:Class ;\n" +
                "\trdfs:label \"Prefix declaration\"@en ;\n" +
                "\trdfs:comment \"The class of prefix declarations, consisting of pairs of a prefix with a namespace.\"@en ;\n" +
                "\trdfs:subClassOf rdfs:Resource ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:declare\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"declare\"@en ;\n" +
                "\trdfs:comment \"Links a resource with its namespace prefix declarations.\"@en ;\n" +
                "\trdfs:domain owl:Ontology ;\n" +
                "\trdfs:range sh:PrefixDeclaration ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:prefix\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"prefix\"@en ;\n" +
                "\trdfs:comment \"The prefix of a prefix declaration.\"@en ;\n" +
                "\trdfs:domain sh:PrefixDeclaration ;\n" +
                "\trdfs:range xsd:string ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:namespace\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"namespace\"@en ;\n" +
                "\trdfs:comment \"The namespace associated with a prefix in a prefix declaration.\"@en ;\n" +
                "\trdfs:domain sh:PrefixDeclaration ;\n" +
                "\trdfs:range xsd:anyURI ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\t\n" +
                "\n" +
                "# SPARQL-based Constraints support --------------------------------------------\n" +
                "\n" +
                "sh:SPARQLConstraintComponent\n" +
                "\ta sh:ConstraintComponent ;\n" +
                "\trdfs:label \"SPARQL constraint component\"@en ;\n" +
                "\trdfs:comment \"A constraint component that can be used to define constraints based on SPARQL queries.\"@en ;\n" +
                "\tsh:parameter sh:SPARQLConstraintComponent-sparql ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:SPARQLConstraintComponent-sparql\n" +
                "\ta sh:Parameter ;\n" +
                "\tsh:path sh:sparql ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:sparql\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"constraint (in SPARQL)\"@en ;\n" +
                "\trdfs:comment \"Links a shape with SPARQL constraints.\"@en ;\n" +
                "\trdfs:domain sh:Shape ;\n" +
                "\trdfs:range sh:SPARQLConstraint ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:SPARQLConstraint\n" +
                "\ta rdfs:Class ;\n" +
                "\trdfs:label \"SPARQL constraint\"@en ;\n" +
                "\trdfs:comment \"The class of constraints based on SPARQL SELECT queries.\"@en ;\n" +
                "\trdfs:subClassOf sh:SPARQLSelectExecutable ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\n" +
                "# Non-validating constraint properties ----------------------------------------\n" +
                "\n" +
                "sh:defaultValue\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"default value\"@en ;\n" +
                "\trdfs:comment \"A default value for a property, for example for user interface tools to pre-populate input fields.\"@en ;\n" +
                "\trdfs:domain sh:PropertyShape ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:description\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"description\"@en ;\n" +
                "\trdfs:comment \"Human-readable descriptions for the property in the context of the surrounding shape.\"@en ;\n" +
                "\trdfs:domain sh:PropertyShape ;\n" +
                "\t# range: xsd:string or rdf:langString\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:group\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"group\"@en ;\n" +
                "\trdfs:comment \"Can be used to link to a property group to indicate that a property shape belongs to a group of related property shapes.\"@en ;\n" +
                "\trdfs:domain sh:PropertyShape ;\n" +
                "\trdfs:range sh:PropertyGroup ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:name\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"name\"@en ;\n" +
                "\trdfs:comment \"Human-readable labels for the property in the context of the surrounding shape.\"@en ;\n" +
                "\trdfs:domain sh:PropertyShape ;\n" +
                "\t# range: xsd:string or rdf:langString\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:order\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"order\"@en ;\n" +
                "\trdfs:comment \"Specifies the relative order of this compared to its siblings. For example use 0 for the first, 1 for the second.\"@en ;\n" +
                "\t# range: xsd:decimal or xsd:integer ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:PropertyGroup\n" +
                "\ta rdfs:Class ;\n" +
                "\trdfs:label \"Property group\"@en ;\n" +
                "\trdfs:comment \"Instances of this class represent groups of property shapes that belong together.\"@en ;\n" +
                "\trdfs:subClassOf rdfs:Resource ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\n" +
                "# -----------------------------------------------------------------------------\n" +
                "# SHACL ADVANCED FEATURES -----------------------------------------------------\n" +
                "# -----------------------------------------------------------------------------\n" +
                "\t\n" +
                "\n" +
                "# Advanced Target vocabulary --------------------------------------------------\n" +
                "\n" +
                "sh:target\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"target\"@en ;\n" +
                "\trdfs:comment \"Links a shape to a target specified by an extension language, for example instances of sh:SPARQLTarget.\"@en ;\n" +
                "\trdfs:domain sh:Shape ;\n" +
                "\trdfs:range sh:Target ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:Target\n" +
                "\ta rdfs:Class ;\n" +
                "\trdfs:label \"Target\"@en ;\n" +
                "\trdfs:comment \"The base class of targets such as those based on SPARQL queries.\"@en ;\n" +
                "\trdfs:subClassOf rdfs:Resource ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:TargetType\n" +
                "\ta rdfs:Class ;\n" +
                "\trdfs:label \"Target type\"@en ;\n" +
                "\trdfs:comment \"The (meta) class for parameterizable targets.\tInstances of this are instantiated as values of the sh:target property.\"@en ;\n" +
                "\trdfs:subClassOf rdfs:Class ;\n" +
                "\trdfs:subClassOf sh:Parameterizable ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:SPARQLTarget\n" +
                "\ta rdfs:Class ;\n" +
                "\trdfs:label \"SPARQL target\"@en ;\n" +
                "\trdfs:comment \"The class of targets that are based on SPARQL queries.\"@en ;\n" +
                "\trdfs:subClassOf sh:Target ;\n" +
                "\trdfs:subClassOf sh:SPARQLAskExecutable ;\n" +
                "\trdfs:subClassOf sh:SPARQLSelectExecutable ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:SPARQLTargetType\n" +
                "\ta rdfs:Class ;\n" +
                "\trdfs:label \"SPARQL target type\"@en ;\n" +
                "\trdfs:comment \"The (meta) class for parameterizable targets that are based on SPARQL queries.\"@en ;\n" +
                "\trdfs:subClassOf sh:TargetType ;\n" +
                "\trdfs:subClassOf sh:SPARQLAskExecutable ;\n" +
                "\trdfs:subClassOf sh:SPARQLSelectExecutable ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\n" +
                "# Functions Vocabulary --------------------------------------------------------\n" +
                "\n" +
                "sh:Function\n" +
                "\ta rdfs:Class ;\n" +
                "\trdfs:label \"Function\"@en ;\n" +
                "\trdfs:comment \"The class of SHACL functions.\"@en ;\n" +
                "\trdfs:subClassOf sh:Parameterizable ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:returnType\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"return type\"@en ;\n" +
                "\trdfs:comment \"The expected type of values returned by the associated function.\"@en ;\n" +
                "\trdfs:domain sh:Function ;\n" +
                "\trdfs:range rdfs:Class ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:SPARQLFunction\n" +
                "\ta rdfs:Class ;\n" +
                "\trdfs:label \"SPARQL function\"@en ;\n" +
                "\trdfs:comment \"A function backed by a SPARQL query - either ASK or SELECT.\"@en ;\n" +
                "\trdfs:subClassOf sh:Function ;\n" +
                "\trdfs:subClassOf sh:SPARQLAskExecutable ;\n" +
                "\trdfs:subClassOf sh:SPARQLSelectExecutable ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\n" +
                "# Result Annotations ----------------------------------------------------------\n" +
                "\n" +
                "sh:resultAnnotation\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"result annotation\"@en ;\n" +
                "\trdfs:comment \"Links a SPARQL validator with zero or more sh:ResultAnnotation instances, defining how to derive additional result properties based on the variables of the SELECT query.\"@en ;\n" +
                "\trdfs:domain sh:SPARQLSelectValidator ;\n" +
                "\trdfs:range sh:ResultAnnotation ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:ResultAnnotation\n" +
                "\ta rdfs:Class ;\n" +
                "\trdfs:label \"Result annotation\"@en ;\n" +
                "\trdfs:comment \"A class of result annotations, which define the rules to derive the values of a given annotation property as extra values for a validation result.\"@en ;\n" +
                "\trdfs:subClassOf rdfs:Resource ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:annotationProperty\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"annotation property\"@en ;\n" +
                "\trdfs:comment \"The annotation property that shall be set.\"@en ;\n" +
                "\trdfs:domain sh:ResultAnnotation ;\n" +
                "\trdfs:range rdf:Property ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:annotationValue\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"annotation value\"@en ;\n" +
                "\trdfs:comment \"The (default) values of the annotation property.\"@en ;\n" +
                "\trdfs:domain sh:ResultAnnotation ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:annotationVarName\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"annotation variable name\"@en ;\n" +
                "\trdfs:comment \"The name of the SPARQL variable from the SELECT clause that shall be used for the values.\"@en ;\n" +
                "\trdfs:domain sh:ResultAnnotation ;\n" +
                "\trdfs:range xsd:string ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\t\n" +
                "# Node Expressions ------------------------------------------------------------\n" +
                "\n" +
                "sh:this\n" +
                "\ta rdfs:Resource ;\n" +
                "\trdfs:label \"this\"@en ;\n" +
                "\trdfs:comment \"A node expression that represents the current focus node.\"@en ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:filterShape\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"filter shape\"@en ;\n" +
                "\trdfs:comment \"The shape that all input nodes of the expression need to conform to.\"@en ;\n" +
                "\trdfs:range sh:Shape ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:nodes\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"nodes\"@en ;\n" +
                "\trdfs:comment \"The node expression producing the input nodes of a filter shape expression.\"@en ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:intersection\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"intersection\"@en ;\n" +
                "\trdfs:comment \"A list of node expressions that shall be intersected.\"@en ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:union\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"union\"@en ;\n" +
                "\trdfs:comment \"A list of node expressions that shall be used together.\"@en ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\n" +
                "# Expression Constraints ------------------------------------------------------\n" +
                "\n" +
                "sh:ExpressionConstraintComponent\n" +
                "\ta sh:ConstraintComponent ;\n" +
                "\trdfs:label \"Expression constraint component\"@en ;\n" +
                "\trdfs:comment \"A constraint component that can be used to verify that a given node expression produces true for all value nodes.\"@en ;\n" +
                "\tsh:parameter sh:ExpressionConstraintComponent-expression ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:ExpressionConstraintComponent-expression\n" +
                "\ta sh:Parameter ;\n" +
                "\tsh:path sh:expression ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:expression\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"expression\"@en ;\n" +
                "\trdfs:comment \"The node expression that must return true for the value nodes.\"@en ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\n" +
                "# Rules -----------------------------------------------------------------------\n" +
                "\n" +
                "sh:Rule\n" +
                "\ta rdfs:Class ;\n" +
                "\trdfs:label \"Rule\"@en ;\n" +
                "\trdfs:comment \"The class of SHACL rules. Never instantiated directly.\"@en ;\n" +
                "\trdfs:subClassOf rdfs:Resource ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:rule\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"rule\"@en ;\n" +
                "\trdfs:comment \"The rules linked to a shape.\"@en ;\n" +
                "\trdfs:domain sh:Shape ;\n" +
                "\trdfs:range sh:Rule ;\n" +
                "\trdfs:isDefinedBy sh:  .\n" +
                "\n" +
                "sh:condition\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"condition\"@en ;\n" +
                "\trdfs:comment \"The shapes that the focus nodes need to conform to before a rule is executed on them.\"@en ;\n" +
                "\trdfs:domain sh:Rule ;\n" +
                "\trdfs:range sh:Shape ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:TripleRule\n" +
                "\ta rdfs:Class ;\n" +
                "\trdfs:label \"A rule based on triple (subject, predicate, object) pattern.\"@en ;\n" +
                "\trdfs:subClassOf sh:Rule ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:subject\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"subject\"@en ;\n" +
                "\trdfs:comment \"An expression producing the resources that shall be inferred as subjects.\"@en ;\n" +
                "\trdfs:domain sh:TripleRule ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:predicate\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"predicate\"@en ;\n" +
                "\trdfs:comment \"An expression producing the properties that shall be inferred as predicates.\"@en ;\n" +
                "\trdfs:domain sh:TripleRule ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:object\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"object\"@en ;\n" +
                "\trdfs:comment \"An expression producing the nodes that shall be inferred as objects.\"@en ;\n" +
                "\trdfs:domain sh:TripleRule ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:SPARQLRule\n" +
                "\ta rdfs:Class ;\n" +
                "\trdfs:label \"SPARQL CONSTRUCT rule\"@en ;\n" +
                "\trdfs:comment \"The class of SHACL rules based on SPARQL CONSTRUCT queries.\"@en ;\n" +
                "\trdfs:subClassOf sh:Rule ;\n" +
                "\trdfs:subClassOf sh:SPARQLConstructExecutable ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "\n" +
                "# SHACL-JS --------------------------------------------------------------------\n" +
                "\n" +
                "sh:JSExecutable\n" +
                "\ta rdfs:Class ;\n" +
                "\trdfs:label \"JavaScript executable\"@en ;\n" +
                "\trdfs:comment \"Abstract base class of resources that declare an executable JavaScript.\"@en ;\n" +
                "\trdfs:subClassOf rdfs:Resource ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:JSTarget\n" +
                "\ta rdfs:Class ;\n" +
                "\trdfs:label \"JavaScript target\"@en ;\n" +
                "\trdfs:comment \"The class of targets that are based on JavaScript functions.\"@en ;\n" +
                "\trdfs:subClassOf sh:Target ;\n" +
                "\trdfs:subClassOf sh:JSExecutable ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:JSTargetType\n" +
                "\ta rdfs:Class ;\n" +
                "\trdfs:label \"JavaScript target type\"@en ;\n" +
                "\trdfs:comment \"The (meta) class for parameterizable targets that are based on JavaScript functions.\"@en ;\n" +
                "\trdfs:subClassOf sh:TargetType ;\n" +
                "\trdfs:subClassOf sh:JSExecutable ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:JSConstraint\n" +
                "\ta rdfs:Class ;\n" +
                "\trdfs:label \"JavaScript-based constraint\"@en ;\n" +
                "\trdfs:comment \"The class of constraints backed by a JavaScript function.\"@en ;\n" +
                "\trdfs:subClassOf sh:JSExecutable ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\t\n" +
                "sh:JSConstraintComponent\n" +
                "\ta sh:ConstraintComponent ;\n" +
                "\trdfs:label \"JavaScript constraint component\"@en ;\n" +
                "\trdfs:comment \"A constraint component with the parameter sh:js linking to a sh:JSConstraint containing a sh:script.\"@en ;\n" +
                "  \tsh:parameter sh:JSConstraint-js ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                " \n" +
                "sh:JSConstraint-js\n" +
                "\ta sh:Parameter ;\n" +
                "\tsh:path sh:js ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\t\n" +
                "sh:js\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"JavaScript constraint\"@en ;\n" +
                "\trdfs:comment \"Constraints expressed in JavaScript.\" ;\n" +
                "  \trdfs:range sh:JSConstraint ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:jsFunctionName\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"JavaScript function name\"@en ;\n" +
                "\trdfs:comment \"The name of the JavaScript function to execute.\"@en ;\n" +
                "\trdfs:domain sh:JSExecutable ;\n" +
                "\trdfs:range xsd:string ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:jsLibrary\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"JavaScript library\"@en ;\n" +
                "  \trdfs:comment \"Declares which JavaScript libraries are needed to execute this.\"@en ;\n" +
                "\trdfs:range sh:JSLibrary ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:jsLibraryURL\n" +
                "\ta rdf:Property ;\n" +
                "\trdfs:label \"JavaScript library URL\"@en ;\n" +
                "\trdfs:comment \"Declares the URLs of a JavaScript library. This should be the absolute URL of a JavaScript file. Implementations may redirect those to local files.\"@en ;\n" +
                "\trdfs:domain sh:JSLibrary ;\n" +
                "\trdfs:range xsd:anyURI ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\t\n" +
                "sh:JSFunction\n" +
                "\ta rdfs:Class ;\n" +
                "  \trdfs:label \"JavaScript function\"@en ;\n" +
                "\trdfs:comment \"The class of SHACL functions that execute a JavaScript function when called.\"@en ;\n" +
                "\trdfs:subClassOf sh:Function ;\n" +
                "\trdfs:subClassOf sh:JSExecutable ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:JSLibrary\n" +
                "\ta rdfs:Class ;\n" +
                "\trdfs:label \"JavaScript library\"@en ;\n" +
                "\trdfs:comment \"Represents a JavaScript library, typically identified by one or more URLs of files to include.\"@en ;\n" +
                "\trdfs:subClassOf rdfs:Resource ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:JSRule\n" +
                "\ta rdfs:Class ;\n" +
                "\trdfs:label \"JavaScript rule\"@en ;\n" +
                "\trdfs:comment \"The class of SHACL rules expressed using JavaScript.\"@en ;\n" +
                "\trdfs:subClassOf sh:JSExecutable ;\n" +
                "\trdfs:subClassOf sh:Rule ;\n" +
                "\trdfs:isDefinedBy sh: .\n" +
                "\n" +
                "sh:JSValidator\n" +
                "\ta rdfs:Class ;\n" +
                "  \trdfs:label \"JavaScript validator\"@en ;\n" +
                "\trdfs:comment \"A SHACL validator based on JavaScript. This can be used to declare SHACL constraint components that perform JavaScript-based validation when used.\"@en ;\n" +
                "  \trdfs:subClassOf sh:JSExecutable ;\n" +
                "  \trdfs:subClassOf sh:Validator ;\n" +
                "  \trdfs:isDefinedBy sh: .\n";
        List<String> elements = getElements(contentToTest);
        assertDoesntContain(elements, "BAD_CHARACTER");
    }

    private List<String> getElements(String content, int start, int end) {
        TTLLexerAdapter lexer = new TTLLexerAdapter();
        lexer.start(content, start, end, 0);
        List<String> elements = new ArrayList<>();
        IElementType element = lexer.getTokenType();
        if (element != null) {
            elements.add(element.toString());
        }
        boolean cont = true;
        while (cont) {
            lexer.advance();
            element = lexer.getTokenType();
            if (element != null) {
                elements.add(element.toString());
            } else {
                cont = false;
            }
        }
        return elements;
    }

    private List<String> getElements(String content) {
        return getElements(content, 0, content.length());
    }

}
