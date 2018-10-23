package org.reactome.server.diagram.converter.qa.diagram;

import org.reactome.server.diagram.converter.layout.output.Coordinate;
import org.reactome.server.diagram.converter.layout.output.Diagram;
import org.reactome.server.diagram.converter.layout.output.Edge;
import org.reactome.server.diagram.converter.layout.output.Shape;
import org.reactome.server.diagram.converter.layout.util.ShapeBuilder;
import org.reactome.server.diagram.converter.qa.common.AbstractConverterQA;
import org.reactome.server.diagram.converter.qa.common.QAPriority;
import org.reactome.server.diagram.converter.qa.common.annotation.DiagramTest;
import org.reactome.server.diagram.converter.qa.common.data.ReactionShape;
import org.reactome.server.diagram.converter.qa.common.data.ShapeType;
import org.reactome.server.diagram.converter.utils.TestReportsHelper;
import org.reactome.server.graph.exception.CustomQueryException;
import org.reactome.server.graph.service.AdvancedDatabaseObjectService;
import org.reactome.server.graph.utils.ReactomeGraphCore;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Antonio Fabregat (fabregat@ebi.ac.uk)
 */
@SuppressWarnings("unused")
@DiagramTest
public class T113_ReactionShapeMismatch extends AbstractConverterQA implements DiagramQA {

    private static final AdvancedDatabaseObjectService ads = ReactomeGraphCore.getService(AdvancedDatabaseObjectService.class);

    //private static final List<String> lines = new ArrayList<>();
    private static final Boolean FIX_CATEGORIES = true;
    private static final Map<String, List<String>> map = new HashMap<>();

    @Override
    public String getDescription() {
        return "Detects reaction categories that differ from the expected ones based on the annotation in the data model.";
    }

    @Override
    public QAPriority getPriority() {
        return QAPriority.MEDIUM;
    }

    @Override
    protected String getHeader() {
        return "Diagram,DiagramName,Reaction,ReactionName,ReactionSchemaClass,CurrentCategory,ExpectedCategory,Created,Modified";
    }

    @Override
    public List<String> getReport() {
        //return getReport(lines);
        return getReport(map.keySet().stream()
                .sorted(Comparator.comparingInt(o -> map.get(o).size()).reversed())
                .map(map::get)
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));
    }

    @Override
    public void run(Diagram diagram) {
        final Map<Long, ShapeType> reactionShape = getReactionsType(diagram.getStableId());
        for (Edge edge : diagram.getEdges()) {
            if(edge.isFadeOut != null && edge.isFadeOut) continue;
            ShapeType rxnDiagramShape = getShapeType(edge);
            ShapeType rxnGraphDBShape = reactionShape.get(edge.reactomeId);
            if (rxnGraphDBShape == null) continue; //Already checked in {@link T703_ExtraReactionInDiagram})
            if (!rxnDiagramShape.equals(rxnGraphDBShape)) {
                List<String> lines = map.computeIfAbsent(diagram.getStableId(), k -> new ArrayList<>());
                lines.add(String.format("%s,\"%s\",%s,\"%s\",%s,%s,%s,%s",
                        diagram.getStableId(),
                        diagram.getDisplayName(),
                        edge.reactomeId,
                        edge.displayName,
                        edge.schemaClass,
                        rxnDiagramShape.getName(),
                        rxnGraphDBShape.getName(),
                        TestReportsHelper.getCreatedModified(edge.reactomeId)));

                if (FIX_CATEGORIES) {
                    edge.reactionShape = getCorrectShape(edge.reactionShape, rxnDiagramShape, rxnGraphDBShape);
                }
            }

            //Independently of whether the shape had to be corrected or not, we now want to store the category
            //in the graph database. To do so, we distinguish two scenarios where (1) we take into account the
            //inferred categories or (2) we simply use what is received in the original diagram XML.
            if (FIX_CATEGORIES) {
                //Case 1: We want to store the inferred categories but keeping those that are UNCERTAIN from the XML
                if (rxnGraphDBShape.equals(ShapeType.OMITTED) && rxnDiagramShape.equals(ShapeType.UNCERTAIN)) {
                    setCategory(edge.reactomeId, rxnDiagramShape);
                } else {
                    setCategory(edge.reactomeId, rxnGraphDBShape);
                }
            } else {
                //Case 2: We do not fix the categories, so we store what was found in the original diagram XML
                setCategory(edge.reactomeId, rxnDiagramShape);
            }

        }
    }

    private ShapeType getShapeType(Edge edge){
        switch (edge.reactionShape.type){
            case CIRCLE: return ShapeType.BINDING;
            case DOUBLE_CIRCLE: return ShapeType.DISSOCIATION;
            case BOX:
                if(edge.reactionShape.s == null || edge.reactionShape.s.trim().isEmpty()) return ShapeType.TRANSITION;
                if("?".equals(edge.reactionShape.s)) return ShapeType.UNCERTAIN;
        }
        return ShapeType.OMITTED;
    }

    private Map<Long, ShapeType> getReactionsType(String stId) {
        String query = "" +
                "MATCH path=(p:Pathway{stId:{stId}})-[:hasEvent*]->(rle:ReactionLikeEvent) " +
                "WHERE SINGLE(x IN NODES(path) WHERE (x:Pathway) AND x.hasDiagram) " +
                "WITH DISTINCT rle " +
                "OPTIONAL MATCH (rle)-[i:input]->(:PhysicalEntity) " +
                "WITH DISTINCT rle, COLLECT(DISTINCT i) AS ii " +
                "OPTIONAL MATCH (rle)-[o:output]->(:PhysicalEntity) " +
                "WITH DISTINCT rle, REDUCE(n=0, i IN ii | n + i.stoichiometry) AS ni, COLLECT(DISTINCT o) AS oo " +
                "WITH DISTINCT rle, ni, REDUCE(n=0, o IN oo | n + o.stoichiometry) AS no " +
                "WITH rle, ni-no AS d " +
                "RETURN rle.dbId AS dbId, " +
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
                "       END AS shape";
        final Map<String, Object> params = new HashMap<>();
        params.put("stId", stId);
        params.put("transition", ShapeType.TRANSITION.getName());
        params.put("binding", ShapeType.BINDING.getName());
        params.put("dissociation", ShapeType.DISSOCIATION.getName());
        params.put("omitted", ShapeType.OMITTED.getName());
        Map<Long, ShapeType> map = new HashMap<>();
        try {
            Collection<ReactionShape> shapes = ads.getCustomQueryResults(ReactionShape.class, query, params);
            for (ReactionShape reactionShape : shapes) map.put(reactionShape.getDbId(), reactionShape.getShape());
        } catch (CustomQueryException e) { /*Nothing here*/ }
        return map;
    }

    private Shape getCorrectShape(Shape currentShape, ShapeType currentType, ShapeType newType) {
        Shape newShape = currentShape;
        Coordinate c = currentShape.getCentre();
        switch (currentType) {
            case TRANSITION:
                switch (newType) {
                    case BINDING:
                        newShape = ShapeBuilder.createReactionCircle(c);
                        break;
                    case DISSOCIATION:
                        newShape = ShapeBuilder.createReactionDoubleCircle(c);
                        break;
                }
                break;

            case BINDING:
                switch (newType) {
                    case TRANSITION:
                        newShape = ShapeBuilder.createReactionBox(c, "");
                        break;
                    case DISSOCIATION:
                        newShape = ShapeBuilder.createReactionDoubleCircle(c);
                        break;
                }
                break;

            case DISSOCIATION:
                switch (newType){
                    case TRANSITION:
                        newShape = ShapeBuilder.createReactionBox(c, "");
                        break;
                    case BINDING:
                        newShape = ShapeBuilder.createReactionDoubleCircle(c);
                        break;
                }
                break;

            case UNCERTAIN:
            case OMITTED:
                switch (newType){
                    case TRANSITION:
                        newShape = ShapeBuilder.createReactionBox(c, "");
                        break;
                    case BINDING:
                        newShape = ShapeBuilder.createReactionDoubleCircle(c);
                        break;
                    case DISSOCIATION:
                        newShape = ShapeBuilder.createReactionDoubleCircle(c);
                        break;
                }
        }
        return newShape;
    }

    private void setCategory(Long dbId, ShapeType shapeType){
        String query = "" +
                "MATCH (rle:ReactionLikeEvent{dbId:{dbId}}) " +
                "SET rle.category={category} ";
        Map<String, Object> params = new HashMap<>();
        params.put("dbId", dbId);
        params.put("category", shapeType.getName());
        ads.customQuery(query, params);
    }
}
