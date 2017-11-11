package org.reactome.server.diagram.converter.qa.post;

import org.reactome.server.diagram.converter.graph.output.EventNode;
import org.reactome.server.diagram.converter.graph.output.Graph;
import org.reactome.server.diagram.converter.layout.output.Diagram;
import org.reactome.server.diagram.converter.qa.common.PostTest;
import org.reactome.server.diagram.converter.utils.TestReportsHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("unused")
@PostTest
public class DiagramMissingReaction implements PostQA {


    private static final List<String> lines = new ArrayList<>();

    @Override
    public String getName() {
        return "DiagramsMissingReaction";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public List<String> getReport() {
        if (!lines.isEmpty()) lines.add(0, "Diagram,DiagramName,Reaction,ReactionName,Created,Modified");
        return lines;
    }

    @Override
    public void run(Diagram diagram, Graph graph) {
        if (graph.getEdges() != null) {
            for (EventNode eventNode : graph.getEdges()) {
                if (!diagram.containsEdge(eventNode.dbId)) {
                    lines.add(String.format("%s,\"%s\",%s,\"%s\",%s",
                            diagram.getStableId(),
                            diagram.getDisplayName(),
                            eventNode.getStId(),
                            eventNode.getDisplayName(),
                            TestReportsHelper.getCreatedModified(eventNode.getDbId())
                    ));
                }
            }
        }
    }

}
