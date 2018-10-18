package org.reactome.server.diagram.converter.sbgn;

import org.reactome.server.diagram.converter.layout.output.*;
import org.sbgn.bindings.*;
import org.sbgn.bindings.Label;
import org.sbgn.bindings.Map;
import sun.font.FontDesignMetrics;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Generates the SBGN of a given Diagram
 *
 * @author Antonio Fabregat (fabregat@ebi.ac.uk)
 */
public class SbgnConverter {

    private static final String ENTITY_PREFIX = "entityVertex_";
    private static final String REACTION_PREFIX = "reactionVertex_";
    private static final String COMPARTMENT_PREFIX = "compartmentVertex_";

    private static final FontMetrics FONT_DESIGN_METRICS = FontDesignMetrics.getMetrics(new Font("Arial", Font.PLAIN, 16));

    private Diagram diagram;
    private Set<Participant> participants = new HashSet<>();
    private java.util.Map<DiagramObject, Glyph> glyphMap = new HashMap<>();
    private java.util.Map<Edge, List<Port>> portMap = new HashMap<>();

    public SbgnConverter(Diagram diagram) {
        this.diagram = diagram;
    }

    public Sbgn getSbgn() {
        Map map = new Map();
        map.setLanguage("process description");


        for (Compartment compartment : diagram.getCompartments()) {
            map.getGlyph().add(getNodeCompartmentGlyph(compartment));
        }

        for (Node node : diagram.getNodes()) {
            if (node.isFadeOut == null && node.isCrossed == null){
                glyphMap.put(node, getNodeGlyph(node));
            }
        }

        for (Edge edge : diagram.getEdges()) {
            if (edge.isFadeOut == null) glyphMap.put(edge, getEdgeGlyph(edge));
        }

        glyphMap.values().forEach(g -> map.getGlyph().add(g));

        for (Participant participant : participants) {
            map.getArc().add(getArc(participant));
        }

        Sbgn sbgn = new Sbgn();
        sbgn.setMap(map);
        return sbgn;
    }

    private Glyph getNodeGlyph(Node node) {
        for (Connector connector : node.connectors) {
            Edge edge = (Edge) diagram.getDiagramObjectByDiagramId(connector.edgeId);
            if (edge.isFadeOut == null) participants.add(new Participant(node, edge, connector));
        }

        Glyph glyph = new Glyph();
        glyph.setId(ENTITY_PREFIX + node.reactomeId + "_" + node.id);
        glyph.setClazz("unspecified entity");

        //The starts with is to automatically handle Drug renderable classes
        if (node.renderableClass.startsWith("Complex")) glyph.setClazz("complex");
        else if (node.renderableClass.startsWith("Protein")) glyph.setClazz("macromolecule");
        else if (node.renderableClass.startsWith("Chemical")) glyph.setClazz("simple chemical");
        //Green boxes to "process"
        else if (node.renderableClass.startsWith("Process")) glyph.setClazz("submap");
        else if (node.renderableClass.startsWith("EncapsulatedNode")) glyph.setClazz("submap");

        if (node.nodeAttachments != null) {
            for (NodeAttachment nodeAttachment : node.nodeAttachments) {
                glyph.getGlyph().add(getNodeAttachmentGlyph(nodeAttachment));
            }
        }

        Bbox cBox = new Bbox();
        cBox.setX(node.prop.x);
        cBox.setY(node.prop.y);
        cBox.setW(node.prop.width);
        cBox.setH(node.prop.height);
        glyph.setBbox(cBox);

        Label label = new Label();
        label.setText(node.displayName);

        glyph.setLabel(label);
        return glyph;
    }

    private int attachmentCounter = 1;

    private Glyph getNodeAttachmentGlyph(NodeAttachment attachment) {
        Glyph glyph = new Glyph();
        glyph.setClazz("unit of information");
        glyph.setId(ENTITY_PREFIX + attachment.reactomeId + "_" + attachmentCounter++ + "_mt");

        String lbl = attachment.label == null ? "mt:prot" : attachment.label;
//        String lbl = "mt:prot";

        Label label = new Label();
        label.setText(lbl);
        glyph.setLabel(label);

        Bbox lBox = new Bbox();
        lBox.setX(attachment.shape.a.x);
        lBox.setY(attachment.shape.a.y);
        lBox.setW(FONT_DESIGN_METRICS.stringWidth(lbl));
        lBox.setH(16f);
        glyph.setBbox(lBox);

        return glyph;
    }

    private Glyph getNodeCompartmentGlyph(Compartment compartment) {
        Glyph glyph = new Glyph();

        glyph.setId(COMPARTMENT_PREFIX + compartment.reactomeId + "_" + compartment.id);
        glyph.setClazz("compartment");
        glyph.setCompartmentOrder(10f);

        Bbox cBox = new Bbox();
        cBox.setX(compartment.prop.x);
        cBox.setY(compartment.prop.y);
        cBox.setW(compartment.prop.width);
        cBox.setH(compartment.prop.height);
        glyph.setBbox(cBox);

        Label label = new Label();
        label.setText(compartment.displayName);
        Bbox lBox = new Bbox();
        lBox.setX(compartment.textPosition.x);
        lBox.setY(compartment.textPosition.y);
        lBox.setW(FONT_DESIGN_METRICS.stringWidth(compartment.displayName));
        lBox.setH(24f);
        label.setBbox(lBox);

        glyph.setLabel(label);
        return glyph;
    }

    private Glyph getEdgeGlyph(Edge edge) {
        Glyph glyph = new Glyph();

        glyph.setId(REACTION_PREFIX + edge.reactomeId + "_" + edge.id);
        if (edge.reactionShape.s != null && !edge.reactionShape.s.isEmpty()) {
            if (Objects.equals(edge.reactionShape.s, "?")) glyph.setClazz("uncertain process");
            else glyph.setClazz("omitted process");
        } else {
            switch (edge.reactionShape.type){
                case CIRCLE:            glyph.setClazz("association");      break;
                case DOUBLE_CIRCLE:     glyph.setClazz("dissociation");     break;
                default:                glyph.setClazz("process");          break;
            }
        }

        Bbox cBox = new Bbox();
        if (edge.reactionShape.a != null) {
            cBox.setX(edge.reactionShape.a.x);
            cBox.setY(edge.reactionShape.a.y);
            cBox.setW(edge.reactionShape.b.x - edge.reactionShape.a.x);
            cBox.setH(edge.reactionShape.b.y - edge.reactionShape.a.y);
        } else {
            cBox.setX(edge.reactionShape.c.x - edge.reactionShape.r);
            cBox.setY(edge.reactionShape.c.y - edge.reactionShape.r);
            cBox.setW(edge.reactionShape.r * 2);
            cBox.setH(edge.reactionShape.r * 2);
        }
        glyph.setBbox(cBox);

        Label label = new Label();
        label.setText(edge.displayName);
        glyph.setLabel(label);

        // Adding ports
        List<Port> ports = new ArrayList<>();
        portMap.put(edge, ports);

        Port port1 = new Port();
        port1.setId(REACTION_PREFIX + edge.reactomeId + ".1");
        glyph.getPort().add(port1);
        ports.add(port1);

        Port port2 = new Port();
        port2.setId(REACTION_PREFIX + edge.reactomeId + ".2");
        glyph.getPort().add(port2);
        ports.add(port2);

        if (edge.segments != null && !edge.segments.isEmpty()) {
            List<Segment> portCandidates = new ArrayList<>();
            for (Segment segment : edge.segments) {
                if(edge.reactionShape.touches(segment)) portCandidates.add(segment);
            }

            Segment s1 = portCandidates.get(0);
            port1.setX(s1.from.x);
            port1.setY(s1.from.y);

            Segment s2 = portCandidates.size() == 1 ? s1 : portCandidates.get(1);
            port2.setX(s2.to.x);
            port2.setY(s2.to.y);
        } else {
            Coordinate c = edge.reactionShape.getCentre();
            port1.setX(c.x);
            port1.setY(c.y);

            port2.setX(c.x);
            port2.setY(c.y);
        }

        return glyph;
    }

    private Arc getArc(Participant p){
        Arc arc = new Arc();
        arc.setId("arc_" + p.node.id + "_" + p.getType().toString().toLowerCase() + "_" + p.edge.id);

        Glyph nodeGlyph = glyphMap.get(p.node);
        Glyph edgeGlyph = glyphMap.get(p.edge);
        List<Port> ports = portMap.get(p.edge);

        arc.setSource(nodeGlyph);
        arc.setTarget(edgeGlyph);
        switch (p.getType()){
            case INPUT:
                arc.setTarget(ports.get(0));
                arc.setClazz("consumption");
                break;
            case OUTPUT:
                arc.setSource(ports.get(1));
                arc.setTarget(nodeGlyph);
                arc.setClazz("production");
                break;
            case CATALYST:      arc.setClazz("catalysis");      break;
            case INHIBITOR:     arc.setClazz("inhibition");     break;
            case ACTIVATOR:     arc.setClazz("stimulation");    break;
        }

        List<Segment> segments = p.getSegments();
        if (segments != null && !segments.isEmpty()) {
            arc.setStart(new Arc.Start());
            Segment s = segments.get(0);
            arc.getStart().setX(s.from.x);
            arc.getStart().setY(s.from.y);

            for (Arc.Next next : p.getArcNextList()) arc.getNext().add(next);

            arc.setEnd(new Arc.End());
            s = segments.get(segments.size() - 1);
            arc.getEnd().setX(s.to.x);
            arc.getEnd().setY(s.to.y);
        } else {
            System.err.println(diagram.getStableId() + ": Failed 'arc' for " + p);
        }

        Glyph stoichiometry = p.getStoichiometry();
        if(stoichiometry!=null) arc.getGlyph().add(stoichiometry);

        return arc;
    }

}