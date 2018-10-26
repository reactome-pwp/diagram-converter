package org.reactome.server.diagram.converter.layout.output;

import org.reactome.server.diagram.converter.layout.input.model.OrgGkRenderRenderableFeature;
import org.reactome.server.diagram.converter.layout.util.ShapeBuilder;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Kostas Sidiropoulos (ksidiro@ebi.ac.uk)
 * @author Antonio Fabregat (fabregat@ebi.ac.uk)
 */
@XmlRootElement
public class NodeAttachment {

    public Long reactomeId;
    public String label;
    public String description;
    public Shape shape;

    public transient Float relativeX;
    public transient Float relativeY;

    public NodeAttachment(OrgGkRenderRenderableFeature obj, Node node) {
        this.reactomeId = obj.getReactomeId() != null ? obj.getReactomeId().longValue() : null;
        this.label = obj.getLabel();
        this.description = obj.getDescription();

        relativeX = obj.getRelativeX().floatValue();
        relativeY = obj.getRelativeY().floatValue();
        Coordinate boxCentre = new Coordinate(
                Math.round(node.prop.x + node.prop.width * relativeX),
                Math.round(node.prop.y + node.prop.height * relativeY)
        );
        this.shape = ShapeBuilder.createNodeAttachmentBox(boxCentre, label);
    }

    public void translate(Coordinate panning) {
        if (shape != null) shape.translate(panning);
    }
}
