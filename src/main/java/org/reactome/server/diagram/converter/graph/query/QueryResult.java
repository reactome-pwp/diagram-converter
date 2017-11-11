package org.reactome.server.diagram.converter.graph.query;

public class QueryResult {

    private Long dbId;
    private String stId;
    private String displayName;
    private String schemaClass;
    private Long speciesID;

    public Long getDbId() {
        return dbId;
    }

    public String getStId() {
        return stId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getSchemaClass() {
        return schemaClass;
    }

    public Long getSpeciesID() {
        return speciesID;
    }
}
