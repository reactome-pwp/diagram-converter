package org.reactome.server.diagram.converter.graph.query;

import java.util.List;

@SuppressWarnings("unused")
public class NodesQueryResult extends QueryResult {

    private List<Long> children;
    private List<Long> parents;
    private String identifier;
    private List<String> geneNames;

    public List<Long> getChildren() {
        return children.isEmpty() ? null : children;
    }

    public List<Long> getParents() {
        return parents.isEmpty() ? null : parents;
    }

    public String getIdentifier() {
        return identifier;
    }

    public List<String> getGeneNames() {
        return geneNames;
    }
}
