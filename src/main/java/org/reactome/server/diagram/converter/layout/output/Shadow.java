package org.reactome.server.diagram.converter.layout.output;


import org.reactome.server.diagram.converter.graph.output.SubpathwayNode;
import org.reactome.server.diagram.converter.layout.shadows.ShadowColours;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Kostas Sidiropoulos (ksidiro@ebi.ac.uk)
 */
public class Shadow extends DiagramObject {

    private static final int MARGIN = 10;

    public NodeProperties prop = new NodeProperties();
    public List<Coordinate> points = new ArrayList<>();
    public String colour;

    public Shadow(Long id, SubpathwayNode subpathway, List<DiagramObject> participants, Integer colorId) {
        super(subpathway);  //Please note the super constructor won't do anything since SubpathwayNode doesn't have the expected methods

        this.id = id;
        this.colour = ShadowColours.getShadow(colorId);
        this.reactomeId = subpathway.dbId;
        this.displayName = subpathway.displayName;
        this.schemaClass = "Pathway";
        this.renderableClass = "Shadow";

        setPoints(participants);
        setBoundaries();
        setPosition();
        setProp(); //By default the node properties are set to show text centered in the box
    }

    @Override
    public void translate(Coordinate panning){
        super.translate(panning);
        if(prop!=null) prop.translate(panning);
        if(points!=null) points.forEach(p->p.translate(panning));
        setBoundaries();
    }

    private void setPoints(List<DiagramObject> participants) {
        List<Integer> xx = new ArrayList<>();
        List<Integer> yy = new ArrayList<>();
        for (DiagramObject participant : participants) {
            if(participant instanceof NodeCommon){
                NodeCommon node = (NodeCommon) participant;
                xx.add(node.prop.x);
                xx.add(node.prop.x + node.prop.width);
                yy.add(node.prop.y);
                yy.add(node.prop.y + node.prop.height);

            }else {
                xx.add(participant.minX);
                xx.add(participant.maxX);
                yy.add(participant.minY);
                yy.add(participant.maxY);
            }
        }

        try {
            Integer minX = Collections.min(xx) - MARGIN;
            Integer maxX = Collections.max(xx) + MARGIN;
            Integer minY = Collections.min(yy) - MARGIN;
            Integer maxY = Collections.max(yy) + MARGIN;

            this.points.add(new Coordinate(minX, minY));
            this.points.add(new Coordinate(maxX, minY));
            this.points.add(new Coordinate(maxX, maxY));
            this.points.add(new Coordinate(minX, maxY));
        }catch (NoSuchElementException e){
            e.printStackTrace();
        }
    }

    private void setPosition() {
        position = new Coordinate((minX + maxX) / 2, (minY + maxY) / 2);
    }

    private void setBoundaries() {
        List<Integer> xx = new ArrayList<>();
        List<Integer> yy = new ArrayList<>();

        for (Coordinate point : points) {
            xx.add(point.x);
            yy.add(point.y);
        }

        this.minX = Collections.min(xx);
        this.maxX = Collections.max(xx);
        this.minY = Collections.min(yy);
        this.maxY = Collections.max(yy);
    }

    private void setProp(){
        this.prop.x = minX;
        this.prop.y = minY;
        this.prop.width = maxX - minX;
        this.prop.height = maxY - minY;
    }
}
