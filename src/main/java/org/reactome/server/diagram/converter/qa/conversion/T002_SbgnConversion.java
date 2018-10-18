package org.reactome.server.diagram.converter.qa.conversion;

import org.reactome.server.diagram.converter.qa.common.AbstractConverterQA;
import org.reactome.server.diagram.converter.qa.common.QAPriority;
import org.reactome.server.diagram.converter.qa.common.annotation.ConverterReport;
import org.reactome.server.graph.domain.model.Pathway;

import java.util.ArrayList;
import java.util.List;

/**
 *  @author Antonio Fabregat (fabregat@ebi.ac.uk)
 */
@ConverterReport
public class T002_SbgnConversion extends AbstractConverterQA {

    private static final List<String> lines = new ArrayList<>();

    @Override
    protected String getHeader() {
        return "Identifier,Name,Reason";
    }

    @Override
    public String getDescription() {
        return "SBGN converter failed to generate the reported pathways (report to the developers list)";
    }

    @Override
    public QAPriority getPriority() {
        return QAPriority.ALARM;
    }

    @Override
    public List<String> getReport() {
        return getReport(lines);
    }

    public static void add(Pathway p){
        add(p, "Unknown reason");
    }
    public static void add(Pathway p, String msg){
        lines.add(String.format("%s,\"%s\",\"%s\"", p.getStId(), p.getDisplayName(), msg));
    }

    public static int size(){
        return lines.size();
    }
}
