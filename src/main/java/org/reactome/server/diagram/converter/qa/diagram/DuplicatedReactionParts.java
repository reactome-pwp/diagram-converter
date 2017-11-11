package org.reactome.server.diagram.converter.qa.diagram;

import org.reactome.server.diagram.converter.qa.common.ConverterReport;
import org.reactome.server.diagram.converter.utils.TestReportsHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("unused")
@ConverterReport
public class DuplicatedReactionParts implements DiagramQA {

    private static final List<String> lines = new ArrayList<>();

    @Override
    public String getName() {
        return "DuplicatedReactionParts";
    }

    @Override
    public String getDescription() {
        return "Detects diagrams without any nodes inside them.";
    }

    @Override
    public List<String> getReport() {
        if (!lines.isEmpty()) lines.add(0, "Diagram,DiagramName,Reaction,Participant,Created,Modified");
        return lines;
    }

    public static void add(String diagramStId, String diagramName, Long reaction, Long participant) {
        lines.add(String.format("%s,\"%s\",%d,%d,%s",
                diagramStId,
                diagramName,
                reaction,
                participant,
                TestReportsHelper.getCreatedModified(reaction)
        ));
    }
}
