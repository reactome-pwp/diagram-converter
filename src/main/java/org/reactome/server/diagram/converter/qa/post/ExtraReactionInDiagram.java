package org.reactome.server.diagram.converter.qa.post;

import org.reactome.server.diagram.converter.graph.output.Graph;
import org.reactome.server.diagram.converter.layout.output.Diagram;
import org.reactome.server.diagram.converter.layout.output.Edge;
import org.reactome.server.diagram.converter.qa.common.PostTest;
import org.reactome.server.diagram.converter.utils.TestReportsHelper;
import org.reactome.server.graph.service.AdvancedDatabaseObjectService;
import org.reactome.server.graph.utils.ReactomeGraphCore;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("unused")
@PostTest
public class ExtraReactionInDiagram implements PostQA {

    private static AdvancedDatabaseObjectService gdb = ReactomeGraphCore.getService(AdvancedDatabaseObjectService.class);

    private static final List<String> lines = new ArrayList<>();

    @Override
    public String getName() {
        return "ExtraReactionInDiagram";
    }

    @Override
    public String getDescription() {
        return "Reactions seen in a diagram that shouldn't be there. Mainly the reason is because the reaction belongs to a subpathway that also has its own diagram";
    }

    @Override
    public List<String> getReport() {
        if (!lines.isEmpty()) lines.add(0, "Diagram,DiagramName,Reaction,ReactionName,Created,Modified");
        return lines;
    }

    @Override
    public void run(Diagram diagram, Graph graph) {
        if (diagram.isDisease()) return;
        if (diagram.getEdges() != null) {
            for (Edge edge : diagram.getEdges()) {
                if (!graph.containsEdge(edge.reactomeId)) {
                    lines.add(String.format("%s,\"%s\",%s,\"%s\",%s",
                            diagram.getStableId(),
                            diagram.getDisplayName(),
                            edge.reactomeId,
                            edge.displayName,
                            TestReportsHelper.getCreatedModified(edge.reactomeId)
                    ));
                }
            }
        }
    }
}
