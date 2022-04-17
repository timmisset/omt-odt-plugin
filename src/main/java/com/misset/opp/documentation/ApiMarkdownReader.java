package com.misset.opp.documentation;

import com.intellij.openapi.util.text.Strings;
import org.commonmark.html.HtmlRenderer;
import org.commonmark.node.Document;
import org.commonmark.node.Header;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.parser.Parser;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Helper class to read the API documentation
 */
public class ApiMarkdownReader {

    private final Parser parser = Parser.builder().build();
    private final HtmlRenderer renderer = HtmlRenderer.builder().build();

    private final @NotNull String content;

    public ApiMarkdownReader(Path path) throws IOException {
        content = readFile(path);
    }

    private String readFile(Path path) throws IOException {
        List<String> strings = Files.readAllLines(path, StandardCharsets.UTF_8);
        return Strings.join(strings, "\n");
    }

    /**
     * Returns the text/data at the specified path, where the path contains
     * markdown headers. For example: /Operators/[OPERATOR_NAME]/Examples
     */
    public String getDescription(String path) {
        if (content.isBlank()) {
            return null;
        }
        String[] steps = path.split("/");

        Node node = parser.parse(content).getFirstChild();

        for (int level = 1; level <= steps.length; level++) {
            if (node != null) {
                node = getChapterNode(node.getNext(), level, steps[level - 1]);
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
