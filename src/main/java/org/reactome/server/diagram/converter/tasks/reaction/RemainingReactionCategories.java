package org.reactome.server.diagram.converter.tasks.reaction;

import org.reactome.server.diagram.converter.qa.common.data.ShapeType;
import org.reactome.server.diagram.converter.tasks.common.ConverterTask;
import org.reactome.server.diagram.converter.tasks.common.annotation.FinalTask;
import org.reactome.server.graph.domain.model.Pathway;
import org.reactome.server.graph.domain.model.ReactionLikeEvent;
import org.reactome.server.graph.exception.CustomQueryException;
import org.reactome.server.graph.service.AdvancedDatabaseObjectService;
import org.reactome.server.graph.utils.ReactomeGraphCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Finds all {@link ReactionLikeEvent} instances where the category field does not exist and infers which category
 * should be assign in each case.
 *
 * The reason why these instances do not have a category at this point is because they have not been found in any
 * diagram, meaning that these are orphan reactions (not present in the 'hasEvent' list of any {@link Pathway}.
 *
 * Note: An orphan reaction instance is normally used to manually infer another reaction that will actually be
 * present in a diagram (and therefore will be found in the 'hasEvent' list of a {@link Pathway} instance).
 *
 * @author Antonio Fabregat (fabregat@ebi.ac.uk)
 */
@SuppressWarnings("unused")
@FinalTask
public class RemainingReactionCategories implements ConverterTask {

    private static final Logger logger = LoggerFactory.getLogger("converter");

    private static final AdvancedDatabaseObjectService ads = ReactomeGraphCore.getService(AdvancedDatabaseObjectService.class);

    private String report = "Not executed";

    @Override
    public String getName() {
        return "Orphan reactions categorisation";
    }

    @Override
    public String getReport() {
        return report;
    }

    @Override
    public void run() {
        String query = "" +
                "MATCH (rle:ReactionLikeEvent) " +
                "WHERE rle.category IS NULL " +
                "OPTIONAL MATCH (rle)-[i:input]->(:PhysicalEntity) " +
                "WITH DISTINCT rle, COLLECT(DISTINCT i) AS ii " +
                "OPTIONAL MATCH (rle)-[o:output]->(:PhysicalEntity) " +
                "WITH DISTINCT rle, REDUCE(n=0, i IN ii | n + i.stoichiometry) AS ni, COLLECT(DISTINCT o) AS oo " +
                "WITH DISTINCT rle, ni, REDUCE(n=0, o IN oo | n + o.stoichiometry) AS no " +
                "WITH rle, ni-no AS d " +
                "SET rle.category = " +
                "       CASE " +
                "         WHEN (rle:BlackBoxEvent) THEN {omitted} " +
                "         WHEN (rle:Polymerisation) OR (rle:Depolymerisation) THEN {transition} " +
                "         WHEN (rle)-[:catalystActivity]->() THEN {transition} " +
                "         WHEN d > 0 THEN CASE " +
                "                           WHEN (rle)-[:output]->(:Complex) THEN {binding} " +
                "                           ELSE {transition} " +
                "                         END " +
                "         WHEN d < 0 THEN CASE " +
                "                           WHEN (rle)-[:input]->(:Complex) THEN {dissociation} " +
                "                           ELSE {transition} " +
                "                         END " +
                "         ELSE {transition} " +
                "       END " +
                "RETURN COUNT(DISTINCT rle) AS updated";
        final Map<String, Object> params = new HashMap<>();
        params.put("transition", ShapeType.TRANSITION.getName());
        params.put("binding", ShapeType.BINDING.getName());
        params.put("dissociation", ShapeType.DISSOCIATION.getName());
        params.put("omitted", ShapeType.OMITTED.getName());
        try {
            Integer c = ads.getCustomQueryResult(Integer.class, query, params);
            report = String.format("Reaction 'category' field has been filled up for %,d orphan reactions", c);
        } catch (CustomQueryException e) {
            String msg = String.format("Error while executing '%s': '%s'", this.getClass().getSimpleName(), e.getMessage());
            logger.error(msg, e);
            report = msg + ". Please see logs for more details.";
        }
    }

}
