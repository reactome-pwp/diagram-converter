package org.reactome.server.diagram.converter.qa.diagram;

import org.reactome.server.diagram.converter.layout.output.Diagram;
import org.reactome.server.diagram.converter.qa.common.DiagramTest;
import org.reactome.server.diagram.converter.utils.TestReportsHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("unused")
@DiagramTest
public class DiagramEmpty implements DiagramQA {

    private static final List<String> lines = new ArrayList<>();

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public String getDescription() {
        return "Detects diagrams without any nodes inside them.";
    }

    @Override
    public List<String> getReport() {
        if (!lines.isEmpty()) lines.add(0, "Diagram,DiagramName,Created,Modified");
        return lines;
    }

    @Override
    public void run(Diagram diagram) {
        if(diagram.getNodes()==null || diagram.getNodes().isEmpty()){
            lines.add(String.format("%s,\"%s\",%s",
                    diagram.getStableId(),
                    diagram.getDisplayName(),
                    TestReportsHelper.getCreatedModified(diagram.getDbId())));
        }
    }
}
