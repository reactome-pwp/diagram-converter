package org.reactome.server.diagram.converter.qa.common.data;

/**
 * @author Antonio Fabregat (fabregat@ebi.ac.uk)
 */
public class ReactionShape {

    //The following two fields are automatically populated
    private Long dbId;
    private String shape;

    public Long getDbId() {
        return dbId;
    }

    public ShapeType getShape() {
        return ShapeType.getShapeType(shape);
    }
}
