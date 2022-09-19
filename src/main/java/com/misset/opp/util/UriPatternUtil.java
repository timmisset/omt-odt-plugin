package com.misset.opp.util;

import com.intellij.openapi.util.TextRange;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UriPatternUtil {

    /**
     * <a href="https://www.rfc-editor.org/rfc/rfc3986#section-3">See official RFC3986 document</a>
     */
    private static final Pattern RFC_3986 = Pattern.compile("^(([^:\\/?#]+):)?(\\/\\/([^\\/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?");
    private static final Pattern CURIE = Pattern.compile("(([^:]*):)([^)]*)");

    private UriPatternUtil() {
    }

    public static boolean isUri(String text) {
        if (text == null || text.length() == 0) {
            return false;
        }
        return isUri(RFC_3986.matcher(unwrap(text)));
    }

    private static boolean isUri(Matcher matcher) {
        boolean b = matcher.find();
        // the matcher should at least find a scheme (2), authority (4) and a path (5)
        return b && matcher.group(2) != null && matcher.group(4) != null && matcher.group(5) != null;
    }

    public static boolean isCurie(String text) {
        if (text == null || text.length() == 0 || isUri(text)) {
            return false;
        }
        return isCurie(CURIE.matcher(unwrap(text)));
    }

    private static boolean isCurie(Matcher matcher) {
        boolean b = matcher.find();
        // the matcher should at least find an (unnamed) prefix with colon (1) and (5)
        return b && matcher.group(1) != null && matcher.group(3) != null;
    }

    /**
     * Returns the localname from the provided resource, can be an uri or curie.
     * <p>
     * It returns the last step of the path as localname when there is no fragment
     * https://some/path/to/an/element => element (using last path step)
     * https://some/path/to/an#element => element (using fragment)
     */
    public static String getLocalname(String resource) {
        String unwrapped = unwrap(resource);
        if (isUri(unwrapped)) {
            return getUriLocalname(unwrapped);
        } else if (isCurie(resource)) {
            return getCurieLocalname(unwrapped);
        }
        return null;
    }

    private static String getUriLocalname(String uri) {
        Matcher matcher = RFC_3986.matcher(uri);
        boolean b = matcher.find();
        if (b && matcher.group(9) != null) {
            // group 9 is present when the fragment is separated from the path by a pound # token
            return matcher.group(9);
        } else if (b) {
            // extract it from the path, split the path and return the last path member as localname
            String group = matcher.group(5);
            String[] split = group.split("/");
            return split[split.length - 1];
        }
        return null;
    }

    private static String getCurieLocalname(String uri) {
        Matcher matcher = CURIE.matcher(uri);
        if (matcher.find()) {
            return matcher.group(3);
        }
        return null;
    }

    public static TextRange getLocalnameRange(String resource) {
        int offset = getWrapOffset(resource);
        resource = unwrap(resource);
        String localname = getLocalname(resource);
        if (localname == null) {
            return TextRange.EMPTY_RANGE;
        }

        if (isUri(resource)) {
            String namespace = getNamespace(resource);
            if (namespace == null) {
                return TextRange.EMPTY_RANGE;
            }
            return TextRange.create(offset + namespace.length(), offset + namespace.length() + localname.length());
        } else if (isCurie(resource)) {
            offset += 1;
            String prefix = getPrefix(resource);
            if (prefix == null) {
                return TextRange.EMPTY_RANGE;
            }
            return TextRange.create(offset + prefix.length(), offset + prefix.length() + localname.length());
        }
        return TextRange.EMPTY_RANGE;
    }

    /**
     * Returns the namespace part of the URI by remove the localname from the unwrapped uri
     * https://some/path/to/an/element => https://some/path/to/an/
     * https://some/path/to/an#element => https://some/path/to/an#
     */
    public static String getNamespace(String uri) {
        uri = unwrap(uri);
        String localname = getLocalname(uri);
        if (uri != null && localname != null) {
            return uri.substring(0, uri.length() - localname.length());
        }
        return null;
    }

    public static TextRange getNamespaceRange(String uri) {
        String namespace = getNamespace(uri);
        if (namespace == null) {
            return TextRange.EMPTY_RANGE;
        }
        int offset = getWrapOffset(uri);
        return TextRange.create(offset, namespace.length() + offset);
    }

    /**
     * Returns the prefix part of a curie
     * For curies without prefix it returns an empty string
     */
    public static String getPrefix(String curie) {
        if (curie == null) {
            return null;
        }
        curie = unwrap(curie);
        Matcher matcher = CURIE.matcher(curie);
        if (isCurie(matcher)) {
            return matcher.group(2) == null ? "" : matcher.group(2);
        }
        return null;
    }

    /**
     * Returns the prefix part of the curie as TextRange
     */
    public static TextRange getPrefixRange(String curie) {
        if (curie == null) {
            return null;
        }
        Matcher matcher = CURIE.matcher(unwrap(curie));
        if (isCurie(matcher)) {
            return getRangeFromMatchingGroup(matcher, 2, getWrapOffset(curie));
        }
        return null;
    }

    private static TextRange getRangeFromMatchingGroup(Matcher matcher, int group, int offset) {
        if (matcher.group(group) == null) {
            return TextRange.EMPTY_RANGE;
        }
        int start = matcher.start(group) + offset;
        int end = matcher.end(group) + offset;
        return TextRange.create(start, end);
    }

    /**
     * Remove diamond and/or parenthesis wrapping from the uri
     */
    public static String unwrap(String text) {
        if (text == null) {
            return null;
        }
        while (isWrapped(text, new String[]{"<>", "()"})) {
            text = text.substring(1, text.length() - 1);
        }
        return text;
    }

    private static int getWrapOffset(String text) {
        String unwrap = unwrap(text);
        if (unwrap == null) {
            return 0;
        }
        return text.indexOf(unwrap);
    }

    private static boolean isWrapped(String text, String[] wrappers) {
        return Arrays.stream(wrappers).anyMatch(wrapper -> isWrapped(text, wrapper.substring(0, 1), wrapper.substring(1)));
    }

    private static boolean isWrapped(String text, String prefix, String suffix) {
        return text != null && text.length() > 2 &&
                text.startsWith(prefix) && text.endsWith(suffix);
    }
}
