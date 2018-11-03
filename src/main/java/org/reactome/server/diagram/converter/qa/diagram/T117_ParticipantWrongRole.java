package org.reactome.server.diagram.converter.qa.diagram;

import org.reactome.server.diagram.converter.layout.output.Connector;
import org.reactome.server.diagram.converter.layout.output.Diagram;
import org.reactome.server.diagram.converter.layout.output.Edge;
import org.reactome.server.diagram.converter.layout.output.Node;
import org.reactome.server.diagram.converter.qa.common.AbstractConverterQA;
import org.reactome.server.diagram.converter.qa.common.QAPriority;
import org.reactome.server.diagram.converter.qa.common.annotation.ConverterReport;
import org.reactome.server.diagram.converter.utils.TestReportsHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * The actual test is performed in T116_StoichiometryMismatch (as collateral effect of the target in that test) but this
 * class is used as a means to report the findings separately.
 *
 * @author Antonio Fabregat (fabregat@ebi.ac.uk)
 */
@SuppressWarnings("unused")
@ConverterReport
public class T117_ParticipantWrongRole extends AbstractConverterQA {

    private static final List<String> lines = new ArrayList<>();

    @Override
    protected String getHeader() {
        return "Diagram,DiagramName,Reaction,ReactionName,EntityId,EntityName,WrongRole,Created,Modified";
    }

    @Override
    public String getDescription() {
        return "Participants whose role is wrongly displayed in the diagram.";
    }

    @Override
    public QAPriority getPriority() {
        return QAPriority.HIGH;
    }

    @Override
    public List<String> getReport() {
        return getReport(lines);
    }

    public static void add(Diagram diagram, Edge edge, Node node, Connector.Type role){
        lines.add(String.format("%s,\"%s\",%d,\"%s\",%d,\"%s\",%s,%s",
                diagram.getStableId(),
                diagram.getDisplayName(),
                edge.reactomeId,
                edge.displayName,
                node.reactomeId,
                node.displayName,
                role,
                TestReportsHelper.getCreatedModified(edge.reactomeId)));
    }

}
