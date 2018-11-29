package org.reactome.server.diagram.converter.qa.common.data;

/**
 * @author Antonio Fabregat (fabregat@ebi.ac.uk)
 */
public class ReactionCategory {

    //The following two fields are automatically populated
    private Long dbId;
    private String category;

    public Long getDbId() {
        return dbId;
    }

    public Category getCategory() {
        return Category.getShapeType(category);
    }
}
