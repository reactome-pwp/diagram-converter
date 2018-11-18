package org.reactome.server.diagram.converter.qa.diagram;

import org.reactome.server.diagram.converter.qa.common.AbstractConverterQA;
import org.reactome.server.diagram.converter.qa.common.QAPriority;
import org.reactome.server.diagram.converter.qa.common.annotation.ConverterReport;
import org.reactome.server.diagram.converter.utils.TestReportsHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kostas Sidiropoulos (ksidiro@ebi.ac.uk)
 * @author Antonio Fabregat (fabregat@ebi.ac.uk)
 */
@SuppressWarnings("unused")
@ConverterReport
public class T112_UnidentifiedCompartments extends AbstractConverterQA implements DiagramQA {

    private static final List<String> lines = new ArrayList<>();

    @Override
    public String getDescription() {
        return "A diagram contains an unidentified compartment (report to the developers list)";
    }

    @Override
    public QAPriority getPriority() {
        return QAPriority.BLOCKER;
    }

    @Override
    protected String getHeader() {
        return "Diagram,DiagramName,Compartment,Created,Modified";
    }

    @Override
    public List<String> getReport() {
        return getReport(lines);
    }

    public static void add(String diagram, String diagramName, Long compartment){
        lines.add(String.format("%s,\"%s\",%s,%s",
                diagram,
                diagramName,
                compartment,
                TestReportsHelper.getCreatedModified(compartment)));
    }
}