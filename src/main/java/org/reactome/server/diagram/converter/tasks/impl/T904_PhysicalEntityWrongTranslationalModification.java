package org.reactome.server.diagram.converter.tasks.impl;

import org.reactome.server.diagram.converter.qa.common.AbstractConverterQA;
import org.reactome.server.diagram.converter.qa.common.QAPriority;
import org.reactome.server.diagram.converter.qa.common.annotation.ConverterReport;
import org.reactome.server.diagram.converter.tasks.common.ConverterTask;
import org.reactome.server.diagram.converter.tasks.common.annotation.FinalTask;
import org.reactome.server.graph.exception.CustomQueryException;
import org.reactome.server.graph.service.AdvancedDatabaseObjectService;
import org.reactome.server.graph.utils.ReactomeGraphCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * This task is executed once at the end of the conversion {@link FinalTask} and the results are included in the main
 * reports {@link ConverterReport}
 *
 * @author Antonio Fabregat (fabregat@ebi.ac.uk)
 */
@SuppressWarnings("unused")
@ConverterReport
@FinalTask(mandatory = true)
public class T904_PhysicalEntityWrongTranslationalModification extends AbstractConverterQA implements ConverterTask {

    private static final Logger logger = LoggerFactory.getLogger("converter");

    private static final AdvancedDatabaseObjectService ads = ReactomeGraphCore.getService(AdvancedDatabaseObjectService.class);

    private String report = "Not executed";

    //Needs to be static based on the behaviour of ConverterReport classes
    private static Collection<String> lines = new ArrayList<>();

    @Override
    public String getDescription() {
        return "PhysicalEntity instances with a potentially wrongly annotated translational modification";
    }

    @Override
    public QAPriority getPriority() {
        return QAPriority.MEDIUM;
    }

    @Override
    protected String getHeader() {
        return "EWAS_Id,EWAS_Name,Modification,ModificationType,PSIMod,Created,Modified";
    }

    @Override
    public List<String> getReport() {
        return getReport(lines);
    }

    @Override
    public String getReportSummary() {
        return report;
    }

    @Override
    public void run() {
        String query = "" +
                "MATCH (psi:PsiMod)<-[:psiMod]-(tm:TranslationalModification)<-[:hasModifiedResidue]-(pe:PhysicalEntity{speciesName:{speciesName}}) " +
                "WHERE psi.abbreviation IS NULL " +
                "OPTIONAL MATCH (a)-[:created]->(pe) " +
                "OPTIONAL MATCH (m)-[:modified]->(pe) " +
                "WITH DISTINCT pe, tm, psi, a, m " +
                "ORDER BY m.displayName, a.displayName " +
                "RETURN DISTINCT pe.stId + ',\"' + pe.displayName + '\",\"' + tm.displayName + '\",' + tm.schemaClass + ',\"' + psi.displayName + '\",\"' + a.displayName + '\",\"' + m.displayName + '\"' AS line";
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("speciesName", "Homo sapiens");
            Collection<String> res = ads.getCustomQueryResults(String.class, query, params);
            if (res.size() > 0) {
                lines = new ArrayList<>(res);
                report = String.format("%,d physical entities found with a wrong AbstractModification annotated", res.size());
            } else {
                report = "No physical entities found with wrong AbstractModification annotated";
            }

        } catch (CustomQueryException e) {
            String msg = String.format("Error while executing '%s': '%s'", this.getClass().getSimpleName(), e.getMessage());
            logger.error(msg, e);
            report = msg + ". Please see logs for more details.";
        }
    }

}
