package org.reactome.server.diagram.converter.sbgn;

import com.google.common.collect.ImmutableList;
import org.reactome.server.diagram.converter.layout.output.Connector;
import org.reactome.server.diagram.converter.layout.output.Edge;
import org.reactome.server.diagram.converter.layout.output.Node;
import org.reactome.server.diagram.converter.layout.output.Segment;
import org.sbgn.bindings.Arc;
import org.sbgn.bindings.Bbox;
import org.sbgn.bindings.Glyph;
import org.sbgn.bindings.Label;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Antonio Fabregat (fabregat@ebi.ac.uk)
 */
class Participant {

    final Node node;
    final Edge edge;
    private Connector connector;

    Participant(Node node, Edge edge, Connector connector) {
        this.node = node;
        this.edge = edge;
        this.connector = connector;
    }

    public Connector.Type getType() {
        return connector.type;
    }

    List<Segment> getSegments() {
        if (connector.segments != null && !connector.segments.isEmpty()) return connector.segments;
        switch (connector.type) {
            case INPUT:
                return edge.segments.subList(0, 1);
            case OUTPUT:
                if(edge.segments.size() == 1) return edge.segments;
                return edge.segments.subList(1, 2);
            default:
                return edge.segments;
        }
    }

    Glyph getStoichiometry(){
        if(connector.stoichiometry.shape == null) return null;

        Glyph glyph = new Glyph();
        glyph.setClazz("stoichiometry");    // "cardinality"
        glyph.setId("Stoichiometry_" + node.reactomeId + "_" + connector.type + "_" + edge.reactomeId );

        Label label = new Label();
        label.setText(connector.stoichiometry.value + "");
        glyph.setLabel(label);

        Bbox bBox = new Bbox();
        bBox.setX(connector.stoichiometry.shape.a.x);
        bBox.setY(connector.stoichiometry.shape.a.y);
        bBox.setW(connector.stoichiometry.shape.b.x - connector.stoichiometry.shape.a.x);
        bBox.setH(connector.stoichiometry.shape.b.y - connector.stoichiometry.shape.a.y);
        glyph.setBbox(bBox);

        return glyph;
    }

    List<Arc.Next> getArcNextList(){
        List<Arc.Next> nextList = new ArrayList<>();
        if (connector.segments != null) {
            for (Segment segment : connector.segments) {
                if (!edge.reactionShape.touches(segment)) {
                    Arc.Next next = new Arc.Next();
                    next.setX(segment.to.x);
                    next.setY(segment.to.y);
                    nextList.add(next);
                }
            }
        }

        if (edge.segments != null) {
            switch (connector.type) {
                case INPUT:
                    for (Segment segment : edge.segments) {
                        if (!edge.reactionShape.touches(segment)) {
                            Arc.Next next = new Arc.Next();
                            next.setX(segment.to.x);
                            next.setY(segment.to.y);
                            nextList.add(next);
                        } else {
                            break;
                        }
                    }
                    break;
                case OUTPUT:
                    for (Segment segment : ImmutableList.copyOf(edge.segments).reverse()) {
                        if (!edge.reactionShape.touches(segment)) {
                            Arc.Next next = new Arc.Next();
                            next.setX(segment.from.x);
                            next.setY(segment.from.y);
                            nextList.add(next);
                        } else {
                            break;
                        }
                    }
                    break;
            }
        }

        if(connector.type.equals(Connector.Type.OUTPUT)) Collections.reverse(nextList);
        return nextList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return Objects.equals(node, that.node) &&
                Objects.equals(edge, that.edge) &&
                Objects.equals(connector, that.connector);
    }

    @Override
    public int hashCode() {
        return Objects.hash(node, edge, connector);
    }

    @Override
    public String toString() {
        return "Participant{" +
                "node=" + node.reactomeId +
                ", edge=" + edge.reactomeId +
                '}';
    }
}
