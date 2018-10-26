package org.reactome.server.diagram.converter.tasks.reaction;

import org.reactome.server.diagram.converter.tasks.common.ConverterTask;
import org.reactome.server.diagram.converter.tasks.common.annotation.InitialTask;
import org.reactome.server.graph.domain.model.ReactionLikeEvent;
import org.reactome.server.graph.exception.CustomQueryException;
import org.reactome.server.graph.service.AdvancedDatabaseObjectService;
import org.reactome.server.graph.utils.ReactomeGraphCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Removes the category field from all {@link ReactionLikeEvent} instances in the graph database. This field will be
 * populated again during the conversion process and by the {@link RemainingReactionCategories} task.
 *
 * @author Antonio Fabregat (fabregat@ebi.ac.uk)
 */
@SuppressWarnings("unused")
@InitialTask
public class CleaningReactionCategories implements ConverterTask {

    private static final Logger logger = LoggerFactory.getLogger("converter");

    private static final AdvancedDatabaseObjectService ads = ReactomeGraphCore.getService(AdvancedDatabaseObjectService.class);

    private String report = "Not executed";

    @Override
    public String getName() {
        return "Reactions category cleanup";
    }

    @Override
    public String getReport() {
        return report;
    }

    @Override
    public void run() {
        String query = "" +
                "MATCH (rle:ReactionLikeEvent) " +
                "REMOVE rle.category " +
                "RETURN COUNT(DISTINCT rle) AS rles";
        try {
            Integer c = ads.getCustomQueryResult(Integer.class, query);
            report = String.format("Reaction 'category' has been cleaned up for all reactions (%,d)", c);
        } catch (CustomQueryException e) {
            String msg = String.format("Error while executing '%s': '%s'", this.getClass().getSimpleName(), e.getMessage());
            logger.error(msg, e);
            report = msg + ". Please see logs for more details.";
        }
    }

}
