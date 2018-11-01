package org.reactome.server.diagram.converter.tasks.common;

/**
 * @author Antonio Fabregat (fabregat@ebi.ac.uk)
 */
public interface ConverterTask {

    String getName();

    String getReportSummary();

    void run(Object target);

}
