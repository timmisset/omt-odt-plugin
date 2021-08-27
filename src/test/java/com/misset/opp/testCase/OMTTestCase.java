package com.misset.opp.testCase;

import com.misset.opp.omt.OMTFileType;
import com.misset.opp.omt.psi.OMTFile;

public class OMTTestCase extends BasicTestCase {
    public OMTTestCase() {
        super(OMTFileType.INSTANCE);
    }

    public OMTFile getOMTFile() {
        return (OMTFile) getFile();
    }

    /**
     * Provides prefixes ont, rdf and rdfs in a root prefixes block
     *
     * @param content
     * @return
     */
    protected String withPrefixes(String content) {
        return String.format("prefixes:\n" +
                "    ont:       <http://ontologie#>\n" +
                "    rdf:       <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "    rdfs:      <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "    owl:       <http://www.w3.org/2002/07/owl#>\n" +
                "    unique:    <http://unique#>\n" +
                "\n" +
                "%s", content);
    }

    protected String insideProcedureRunWithPrefixes(String onRunContent) {
        onRunContent = onRunContent.replace("\n", "\n            "); // alignment
        return withPrefixes(String.format("model:\n" +
                        "    Procedure: !Procedure\n" +
                        "        onRun: |\n" +
                        "            %s",
                onRunContent));
    }

    protected String insideActivityWithPrefixes(String activityContent) {
        return insideActivityWithPrefixes(activityContent, "Activiteit");
    }

    protected String insideActivityWithPrefixes(String activityContent,
                                                String activityName) {
        activityContent = activityContent.replace("\n", "\n        "); // alignment
        return withPrefixes(String.format("model:\n" + "    %s: !Activity\n" + "        %s",
                activityName,
                activityContent));
    }

    protected String insideComponentWithPrefixes(String componentContent) {
        componentContent = componentContent.replace("\n", "\n        "); // alignment
        return withPrefixes(
                String.format("model:\n" +
                        "    Component: !Component\n" +
                        "        %s", componentContent));
    }

    protected String insideStandaloneQueryWithPrefixes(String componentContent) {
        componentContent = componentContent.replace("\n", "\n        "); // alignment
        return withPrefixes(
                String.format("model:\n" +
                        "    StandaloneQuery: !StandaloneQuery\n" +
                        "        %s\n", componentContent));
    }

    protected String inModel(String content) {
        return String.format("model:\n" +
                "    Activiteit: !Activity\n" +
                "        %s", content.replace("\n", "\n        "));
    }

    /**
     * Places the input statement into a template with common prefixes and inside a queries block
     *
     * @param queryStatement only the queryStatement without the define and semicolon
     * @return
     */
    protected String insideQueryWithPrefixes(String queryStatement) {
        return insideQueryWithPrefixesNoSemicolonEnding(queryStatement) + ";";

    }

    protected String insideQueryWithPrefixesNoSemicolonEnding(String queryStatement) {
        return withPrefixes(String.format("queries: |\n" +
                "   DEFINE QUERY query => %s\n" +
                "\n", queryStatement));
    }
}
