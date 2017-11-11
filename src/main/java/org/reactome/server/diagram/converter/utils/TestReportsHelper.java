package org.reactome.server.diagram.converter.utils;

import org.reactome.server.graph.domain.result.SimpleDatabaseObject;
import org.reactome.server.graph.exception.CustomQueryException;
import org.reactome.server.graph.service.AdvancedDatabaseObjectService;
import org.reactome.server.graph.utils.ReactomeGraphCore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public abstract class TestReportsHelper {

    private static AdvancedDatabaseObjectService ads = ReactomeGraphCore.getService(AdvancedDatabaseObjectService.class);

    public static String getCreatedModified(Long dbId) {
        Map<String, Object> parametersMap = new HashMap<>();
        parametersMap.put("dbId", dbId);
        try {
            CurationDetails rtn = ads.customQueryForObject(CurationDetails.class, "" +
                            "MATCH (d:DatabaseObject{dbId:{dbId}}) " +
                            "OPTIONAL MATCH (a)-[:created]->(d) " +
                            "OPTIONAL MATCH (m)-[:modified]->(d) " +
                            "RETURN a.displayName As created, m.displayName AS modified",
                    parametersMap);
            return rtn.toString();
        } catch (NullPointerException e) {
            return "N/A, N/A";  //dbId is not in the database
        } catch (CustomQueryException e) {
            return "null,null";
        }
    }

    //An easy way for a single cache
    private static Long diagramId;
    private static Map<Long, String> cache = new HashMap<>();

    public static Map<Long, String> getParticipantsSchemaClass(Long diagramDbId) {
        if (Objects.equals(TestReportsHelper.diagramId, diagramDbId)) return cache;

        TestReportsHelper.diagramId = diagramDbId;
        cache = new HashMap<>();

        String query = "" +
                "MATCH path=(p:Pathway{hasDiagram:True, dbId:{dbId}})-[:hasEvent*]->(rle:ReactionLikeEvent) " +
                "WHERE SINGLE(x IN NODES(path) WHERE (x:Pathway) AND x.hasDiagram) " +
                "WITH DISTINCT rle " +
                "MATCH (rle)-[:input|output|catalystActivity|entityFunctionalStatus|physicalEntity|regulatedBy|regulator*]->(pe:PhysicalEntity) " +
                "RETURN DISTINCT pe.dbId AS dbId, pe.schemaClass AS schemaClass " +
                "UNION " +
                "MATCH (p:Pathway{hasDiagram:True, dbId:{dbId}})-[:hasEvent*]->(s:Pathway{hasDiagram:True}) " +
                "RETURN DISTINCT s.dbId AS dbId, s.schemaClass AS schemaClass " +
                "UNION " +
                "MATCH (:Pathway{hasDiagram:True, dbId:{dbId}})-[:normalPathway]->(p:Pathway), " +
                "      path=(p)-[:hasEvent*]->(rle:ReactionLikeEvent) " +
                "WHERE SINGLE(x IN NODES(path) WHERE (x:Pathway) AND x.hasDiagram) " +
                "WITH DISTINCT rle " +
                "MATCH (rle)-[:input|output|catalystActivity|entityFunctionalStatus|physicalEntity|regulatedBy|regulator*]->(pe:PhysicalEntity) " +
                "RETURN DISTINCT pe.dbId AS dbId, pe.schemaClass AS schemaClass ";
        Map<String, Object> params = new HashMap<>();
        params.put("dbId", diagramDbId);
        try {
            for (SimpleDatabaseObject simpleDatabaseObject : ads.customQueryForObjects(SimpleDatabaseObject.class, query, params)) {
                cache.put(simpleDatabaseObject.getDbId(), simpleDatabaseObject.getSchemaClass());
            }

        } catch (CustomQueryException e) {
            e.printStackTrace();
        }
        return cache;
    }
}
