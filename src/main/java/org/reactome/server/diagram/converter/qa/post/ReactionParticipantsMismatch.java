package org.reactome.server.diagram.converter.qa.post;

import org.reactome.server.diagram.converter.graph.output.EntityNode;
import org.reactome.server.diagram.converter.graph.output.EventNode;
import org.reactome.server.diagram.converter.graph.output.Graph;
import org.reactome.server.diagram.converter.layout.output.Diagram;
import org.reactome.server.diagram.converter.layout.output.DiagramObject;
import org.reactome.server.diagram.converter.layout.output.Edge;
import org.reactome.server.diagram.converter.layout.output.ReactionPart;
import org.reactome.server.diagram.converter.qa.common.PostTest;
import org.reactome.server.diagram.converter.utils.TestReportsHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Antonio Fabregat (fabregat@ebi.ac.uk)
 * @author Konstantinos Sidiropoulos (ksidiro@ebi.ac.uk)
 */
@SuppressWarnings("unused")
@PostTest
public class ReactionParticipantsMismatch implements PostQA {

    private static final List<String> lines = new ArrayList<>();

    @Override
    public String getName() {
        return "ReactionParticipantsMismatch";
    }

    @Override
    public String getDescription() {
        return "Detects mismatches between the participants of a reaction in a diagram and the database";
    }

    @Override
    public List<String> getReport() {
        if (!lines.isEmpty()) lines.add(0, "Pathway,PathwayName,Reaction,ReactionName,Participant,ParticipantName,Role,Created,Modified");
        return lines;
    }

    @Override
    public void run(Diagram diagram, Graph graph) {
        if (graph.getEdges() == null) return;
        for (EventNode eventNode : graph.getEdges()) {
            for (Edge edge : diagram.getEdges(eventNode.dbId)) {
                check(diagram, graph, eventNode, eventNode.inputs, edge.inputs, "input");
                check(diagram, graph, eventNode, eventNode.outputs, edge.outputs, "output");
                check(diagram, graph, eventNode, eventNode.catalysts, edge.catalysts, "catalyst");
                check(diagram, graph, eventNode, eventNode.inhibitors, edge.inhibitors, "inhibitor");
                //activators and requirements are kept separately in the eventNode
                List<Long> aux = new ArrayList<>();
                if (eventNode.activators != null) aux.addAll(eventNode.activators);
                if (eventNode.requirements != null) aux.addAll(eventNode.requirements);
                check(diagram, graph, eventNode, aux, edge.activators, "activator");
            }
        }
    }

    private void check(Diagram diagram, Graph graph, EventNode reaction, List<Long> graphParticipants, List<ReactionPart> diagramParticipants, String role) {
        if (diagramParticipants != null && graphParticipants != null) { // The "else" is reported in another test!
            for (Long graphParticipant : graphParticipants) {
                boolean found = false;
                for (ReactionPart part : diagramParticipants) {
                    DiagramObject diagramObject = diagram.getDiagramObjectByDiagramId(part.id);
                    found = Objects.equals(diagramObject.reactomeId, graphParticipant);
                    if (found) break;
                }
                if (!found) addReport(diagram, reaction, graph.getNode(graphParticipant), role);
            }
        }
    }

    private void addReport(Diagram diagram, EventNode reaction, EntityNode participant, String role) {
        lines.add(String.format("%s,\"%s\",%s,\"%s\",%s,\"%s\",%s,%s",
                diagram.getStableId(),
                diagram.getDisplayName(),
                reaction.getStId(),
                reaction.getDisplayName(),
                participant.stId,
                participant.displayName,
                role,
                TestReportsHelper.getCreatedModified(reaction.dbId)
        ));
    }
}
