package org.reactome.server.diagram.converter.layout.shadows.partitioner;

import java.util.LinkedList;
import java.util.List;

/**
 * Generic Box with a list of Points
 * which will be returned in the Partition
 *
 * @author Guilherme S Viteri
 */
public class Box {

    private List<XYPoint> xyPoints;

    public void addPoint(XYPoint xyPoint){
        if(this.xyPoints == null){
            xyPoints = new LinkedList<>();
        }

        xyPoints.add(xyPoint);
    }

    public List<XYPoint> getXYPoints() {
        return xyPoints;
    }
}
