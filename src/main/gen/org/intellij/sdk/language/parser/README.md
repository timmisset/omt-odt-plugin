Little workaround for the JavaDoc element.

- Generate the parser and classes
- remove from ODTTypes: IElementType DOC_COMMENT = new ODTTokenType("DOC_COMMENT");
- add import to ODTParser: import static com.intellij.psi.impl.source.tree.JavaDocElementType.DOC_COMMENT;
