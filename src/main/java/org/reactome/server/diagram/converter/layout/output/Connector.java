package org.reactome.server.diagram.converter.layout.output;

import org.reactome.server.diagram.converter.layout.util.ShapeBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Connector belongs to Nodes, so that is the reason why edge is needed. A connector points to
 * the edge backbone (either the first, last or middle point) so we keep the edge instead
 *  1) Memory usage is lower
 *  2) If a node is not meant to be drawn (note this is different of not being in the viewport)
 *     we have a way to detect it and easily avoid drawing it
 *
 * Connector.Type is kept to distinguish whether it ends with arrow, or other kind of shapes
 *
 * Connectors are further divided into segments, so the math to detect point in segment is
 * moved one layer down. Segments start from the node to the backbone
 *
 * Connector also includes the shape (arrow, circle, etc.). The points of the shapes are
 * calculated server-side to avoid the cost of processing at the client
 *
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class Connector {

    public enum Type { INPUT, OUTPUT, CATALYST, ACTIVATOR, INHIBITOR }

    private Edge edge;
    public Long edgeId;
    public Type type;
    public Boolean isFadeOut;

    public List<Segment> segments = new ArrayList<>();
    public Stoichiometry stoichiometry = new Stoichiometry();
    public Shape endShape;
    public Boolean isDisease;

    public Connector(Edge edge, ReactionPart part, Type type) {
        this.stoichiometry.value = part.stoichiometry!=null ? part.stoichiometry : 1;
        this.edge = edge;
        this.edgeId = edge.id;
        this.type = type;
        this.isDisease = edge.isDisease;
        setSegments(part.points);
        setStoichiometryPosition();
        setPointer(segments, type);
    }

    private void setSegments(List<Coordinate> coordinates){
        if(coordinates ==null) return;
        Coordinate from = coordinates.get(0);
        Coordinate to;
        for (int i = 1; i < coordinates.size(); i++) {
            to = coordinates.get(i);
            Segment segment = new Segment(from, to);
            if(!segment.isPoint()) {
                segments.add(segment);
            }
            from = to;
        }
        if(edge.segments.size()>0) {
            switch (type) {
                case INPUT:
                    to = edge.segments.get(0).from;
                    break;
                case OUTPUT:
                    to = edge.segments.get(edge.segments.size() - 1).to;
                    break;
                default:
                    to = edge.position;
            }
        }else{
            // attach the connector to the edge position
            to = edge.position;
        }

        Segment segment = new Segment(from, to);
        if(!segment.isPoint()) {
            this.segments.add(new Segment(from, to));
        }
    }

    /**
     * Calculates the position of the stoichiometry box.
     * However, there are cases where the node is attached directly on the
     * backbone without any connector segments. In this case, we place the
     * stoichiometry box on the backbone segments of the edge.
     */
    private void setStoichiometryPosition(){
        if(stoichiometry.value>1) {
            Segment segment;
            if(segments.size()>0){
                // First try to set the Stoichiometry Position in the segments
                // Place stoichiometry box in the first segment which is closer to the node
                segment = this.segments.get(0);
            }else{
                // If no segments are present then place the stoichiometry box on the backbone
                // of the edge. The backbone is also split in segments.
                switch (type){
                    case INPUT:
                        // Use the first segment of the backbone
                        segment = edge.segments.get(0);
                        break;
                    case OUTPUT:
                        // Use the last segment of the backbone
                        segment = edge.segments.get(edge.segments.size()-1);
                        break;
                    default:
                        System.err.println(" >> Segment was not properly placed");
                        return;
                }
            }
            this.stoichiometry.shape = ShapeBuilder.createStoichiometryBox(
                    setStoichiometryPosition(segment.from, segment.to),
                    stoichiometry.value.toString()
            );
        }
    }

    /**
     * Calculate the position of Stoichiometry box on the segments.
     */
    private Coordinate setStoichiometryPosition(Coordinate from, Coordinate to){
        Double x = (from.x + to.x)/2.0d;
        Double y = (from.y + to.y)/2.0d;

        return new Coordinate(x.intValue(), y.intValue() );
    }

    private void setPointer(List<Segment> segments, Type connectorType){

        if(segments.size()>0){
            Segment segment;
            List<Coordinate> points;
            switch (connectorType){
                case INPUT:
                    return;
                case OUTPUT:
                    // Use the first segment of the Connector - closer to the node
                    // IMPORTANT!!! Segments start from the node and point to the backbone
                    segment = segments.get(0);
                    points = ShapeBuilder.createArrow(
                            segment.from.x,
                            segment.from.y,
                            segment.to.x,
                            segment.to.y);
                    // Shape is a filled arrow
                    this.endShape = new Shape(points.get(0), points.get(1), points.get(2), Boolean.FALSE, Shape.Type.ARROW);
                    break;
                case CATALYST:
                    // Use the last segment of the Connector - closer to the edge (reaction)
                    segment = segments.get(segments.size() - 1);
                    // Adjust the position of the segment to have a distance from the reaction position
                    Integer radius = Math.round((float) (ShapeBuilder.EDGE_MODULATION_WIDGET_WIDTH / 2.0d));
                    Coordinate centre =  calculateEndpoint(segment, getDistanceForEndpoint(type));
                    segment.to  =  calculateEndpoint(segment, getDistanceForEndpoint(type, radius));
                    // Shape is an empty circle
                    this.endShape = new Shape(centre, radius, Boolean.TRUE, Shape.Type.CIRCLE);
                    break;
                case INHIBITOR:
                    // Use the last segment of the Connector - closer to the edge (reaction)
                    segment = segments.get(segments.size() - 1);
                    // Adjust the position of the segment to have a distance from the reaction position
                    segment.to = calculateEndpoint(segment, getDistanceForEndpoint(type));
                    points = ShapeBuilder.createStop(
                            segment.to.x,
                            segment.to.y,
                            segment.from.x,
                            segment.from.y);
                    // Shape is a stop sign
                    this.endShape = new Shape(points.get(0), points.get(1), points.get(2), Boolean.FALSE, Shape.Type.STOP);
                    break;
                case ACTIVATOR:
                    // Use the last segment of the Connector - closer to the edge (reaction)
                    segment = segments.get(segments.size() - 1);
                    segment.to = calculateEndpoint(segment, getDistanceForEndpoint(type));
                    points = ShapeBuilder.createArrow(
                            segment.to.x,
                            segment.to.y,
                            segment.from.x,
                            segment.from.y);
                    // Shape is an empty arrow
                    this.endShape = new Shape(points.get(0), points.get(1), points.get(2), Boolean.TRUE, Shape.Type.ARROW);
                    break;
            }
        }
    }

    /**
     * Calculates the end point on the segment
     */
    public Coordinate calculateEndpoint(Segment segment, double dist){
        // Point used to calculate the angle of the segment
        double controlX = segment.from.x;
        double controlY = segment.from.y;
        // Point to replace
        double oldX = segment.to.x;
        double oldY = segment.to.y;

        // Remember: the y axis is contrary to the ordinary coordinate system
        double tan = (controlY - oldY) / (controlX - oldX);
        double theta = Math.atan(tan);
        if (controlX - oldX < 0)
            theta +=  Math.PI;
        double x = oldX + dist * Math.cos(theta);
        double y = oldY + dist * Math.sin(theta);

        return new Coordinate(Math.round((float) x), Math.round((float) y));
    }

    private double getDistanceForEndpoint(Type connectRole) {
        return getDistanceForEndpoint(connectRole, 0);
    }

    private double getDistanceForEndpoint(Type connectRole, Integer radius) {
        if (connectRole == Type.CATALYST) {
            return (ShapeBuilder.EDGE_TYPE_WIDGET_WIDTH + ShapeBuilder.EDGE_MODULATION_WIDGET_WIDTH) * 0.6 + radius;
        }else {
            return ShapeBuilder.EDGE_TYPE_WIDGET_WIDTH * 0.75;
        }
    }
}
