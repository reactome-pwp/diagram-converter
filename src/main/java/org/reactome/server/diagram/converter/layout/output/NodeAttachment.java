package org.reactome.server.diagram.converter.layout.output;

import org.reactome.server.diagram.converter.layout.input.model.OrgGkRenderRenderableFeature;
import org.reactome.server.diagram.converter.layout.util.ShapeBuilder;
import org.reactome.server.graph.domain.model.DatabaseObject;
import org.reactome.server.graph.service.DatabaseObjectService;
import org.reactome.server.graph.utils.ReactomeGraphCore;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@XmlRootElement
public class NodeAttachment {

    private static transient final DatabaseObjectService getDatabaseObject = ReactomeGraphCore.getService(DatabaseObjectService.class);

    public Long reactomeId;
    public String label;
    public String description;
    public Shape shape;

    public transient Float relativeX;
    public transient Float relativeY;

    public NodeAttachment(OrgGkRenderRenderableFeature obj, Node node) {
        this.label = obj.getLabel();
        this.description = obj.getDescription();

        if (obj.getReactomeId() != null) {
            this.reactomeId = obj.getReactomeId().longValue();
            DatabaseObject object =  getDatabaseObject.findById(reactomeId);
            if(object!=null) {
                this.description = object.getDisplayName();
            }
        }

        relativeX = obj.getRelativeX().floatValue();
        relativeY = obj.getRelativeY().floatValue();
        Coordinate boxCentre = new Coordinate(
                Math.round(node.prop.x + node.prop.width * relativeX),
                Math.round(node.prop.y + node.prop.height * relativeY)
        );
        this.shape = ShapeBuilder.createNodeAttachmentBox(boxCentre, label);
    }

    public void translate(Coordinate panning){
        if(shape!=null) shape.translate(panning);
    }
}
