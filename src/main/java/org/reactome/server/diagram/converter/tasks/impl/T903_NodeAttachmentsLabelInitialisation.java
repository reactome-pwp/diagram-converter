package org.reactome.server.diagram.converter.tasks.impl;

import org.reactome.server.diagram.converter.tasks.common.AbstractConverterTask;
import org.reactome.server.diagram.converter.tasks.common.annotation.InitialTask;
import org.reactome.server.graph.domain.model.AbstractModifiedResidue;
import org.reactome.server.graph.exception.CustomQueryException;
import org.reactome.server.graph.service.AdvancedDatabaseObjectService;
import org.reactome.server.graph.utils.ReactomeGraphCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Sets label fields for all {@link AbstractModifiedResidue} instances based on the PSI-MOD abbreviation.
 *
 * @author Antonio Fabregat (fabregat@ebi.ac.uk)
 */
@SuppressWarnings("unused")
@InitialTask(mandatory = true)
public class T903_NodeAttachmentsLabelInitialisation extends AbstractConverterTask {

    // List of psiMod dbIds that correspond to glycans when GroupModifiedResidue instances have a modification
    // Provided by Bijay Jassal
    private static final List<Long> glycans = Arrays.asList(1467184L, 1467185L, 1467292L, 2022987L, 913651L, 913631L, 2243916L, 2022865L, 3238099L, 448181L, 5615587L, 2063975L);

    private static final Logger logger = LoggerFactory.getLogger("converter");

    private static final AdvancedDatabaseObjectService ads = ReactomeGraphCore.getService(AdvancedDatabaseObjectService.class);

    private String report = "Not executed";

    @Override
    public String getReportSummary() {
        return report;
    }

    @Override
    public void run() {
        String query = "" +
                "MATCH (tm:TranslationalModification) " +
                "OPTIONAL MATCH (tm)-[:psiMod]->(psi) " +
                "OPTIONAL MATCH (tm)-[:modification]->(m) " +
                "WITH DISTINCT tm, " +
                "     CASE WHEN (tm:GroupModifiedResidue) AND NOT m IS NULL AND psi.dbId IN {glycans} THEN 'G' " +
                "          WHEN (tm:CrosslinkedResidue) AND NOT m IS NULL THEN 'CL' " +
                "          ELSE psi.abbreviation END AS label " +
                "WHERE NOT label IS NULL " +
                "SET tm.label = label " +
                "RETURN COUNT(DISTINCT tm) AS tms";
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("glycans", glycans);
            Integer c = ads.getCustomQueryResult(Integer.class, query, params);
            report = String.format("Node attachment 'label' has been set for (%,d) TranslationalModification instances", c);
        } catch (CustomQueryException e) {
            String msg = String.format("Error while executing '%s': '%s'", this.getClass().getSimpleName(), e.getMessage());
            logger.error(msg, e);
            report = msg + ". Please see logs for more details.";
        }
    }

}
