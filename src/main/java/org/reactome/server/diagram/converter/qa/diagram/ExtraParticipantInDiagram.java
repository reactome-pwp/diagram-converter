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
public class ExtraParticipantInDiagram implements DiagramQA {

    private static final List<String> lines = new ArrayList<>();

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public String getDescription() {
        return "Participants seen in a diagram that shouldn't be there. Mainly the reason is because the reaction where they participate belongs to a subpathway that also has its own diagram.";
    }

    @Override
    public List<String> getReport() {
        if (!lines.isEmpty()) lines.add(0, "Diagram,DiagramName,Entity,EntityName,Created,Modified");
        return lines;
    }

    public static void add(String diagramStId, String diagramName, Long entityId, String entityName) {
        lines.add(String.format("%s,\"%s\",%d,\"%s\",%s",
                diagramStId,
                diagramName,
                entityId,
                entityName,
                TestReportsHelper.getCreatedModified(entityId)
                ));
    }
}
