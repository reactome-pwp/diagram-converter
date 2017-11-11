package org.reactome.server.diagram.converter.graph.query;

import java.util.List;

public class SubpathwaysQueryResult {

    private Long dbId;
    private String stId;
    private String displayName;
    private List<Long> events;
    private Integer level;

    public Long getDbId() {
        return dbId;
    }

    public String getStId() {
        return stId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<Long> getEvents() {
        return events;
    }

    public Integer getLevel() {
        return level;
    }
}
