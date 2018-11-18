package org.reactome.server.diagram.converter.utils.reports;

/**
 * @author Antonio Fabregat (fabregat@ebi.ac.uk)
 */
@SuppressWarnings("WeakerAccess")
public class Participant {
    Long dbId;
    String schemaClass;
    String renderableClass;

    public Long getDbId() {
        return dbId;
    }

    public String getSchemaClass() {
        return schemaClass;
    }

    public String getRenderableClass() {
        return renderableClass;
    }
}
