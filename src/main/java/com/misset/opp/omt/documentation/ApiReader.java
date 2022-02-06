package com.misset.opp.omt.documentation;

import com.intellij.openapi.util.text.Strings;
import org.commonmark.html.HtmlRenderer;
import org.commonmark.node.Document;
import org.commonmark.node.Header;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.parser.Parser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Helper class to read the API documentation for the OMT language
 * It expects a certain format with respect to header levels:
 * <p>
 * # Classes
 * ## Class
 * ### Attributes of the class
 * Description of the attribute
 * <p>
 * #### Example
 * Example code for using the attribute
 */
public class ApiReader {

    private final Parser parser = Parser.builder().build();
    private final HtmlRenderer renderer = HtmlRenderer.builder().build();

    private final String content;

    public ApiReader(Path path) throws IOException {
        content = readFile(path);
    }

    private String readFile(Path path) throws IOException {
        List<String> strings = Files.readAllLines(path, StandardCharsets.UTF_8);
        return Strings.join(strings, "\n");
    }

    /**
     * Return the paragraph contents of a specific path, returns null if not available.
     * Expects every step in the path to be a heading level increment
     * Example:
     * Classes/Activity/Description
     * will look for:
     * # Classes
     * ## Activity
     * ### Description
     * <p>
     * Classes/Activity/graphs/Example
     * # Classes
     * ## Activity
     * ### graphs
     * #### Description
     * <p>
     * The heading is looked for with a startsWith since headings can contain
     * required/optional flags and return types
     */
    public String getDescription(String path) {
        if (content == null) {
            return null;
        }
        String[] steps = path.split("/");

        Node node = parser.parse(content).getFirstChild();

        for (int level = 1; level <= steps.length; level++) {
            if (node != null) {
                node = getChapterNode(node.getNext(), level, steps[level - 1]);
            } else {
                return null;
            }
        }
        return getContentAsHtml(node);
    }

    private String getContentAsHtml(Node node) {
        if (node == null) {
            return null;
        }
        node = node.getNext();
        Document document = new Document();
        while (node != null && !(node instanceof Header)) {
            Node nextNode = node.getNext();
            document.appendChild(node);
            node = nextNode;
        }
        return renderer.render(document);
    }

    private Node getChapterNode(Node node, int level, String heading) {
        while (node != null && !(node instanceof Header)) {
            node = node.getNext();
        }
        if (node == null) {
            return null;
        }
        Header header = (Header) node;
        int headerLevel = header.getLevel();
        if (headerLevel != level) {
            if (headerLevel == level - 1) {
                // we've reached the next section without finding the heading at the appropriate level
                return null;
            }
            return getChapterNode(header.getNext(), level, heading);
        }
        if (header.getFirstChild() instanceof Text) {
            Text headerText = (Text) header.getFirstChild();
            if (headerText.getLiteral().startsWith(heading)) {
                return header;
            }
        }
        return getChapterNode(header.getNext(), level, heading);
    }

}
