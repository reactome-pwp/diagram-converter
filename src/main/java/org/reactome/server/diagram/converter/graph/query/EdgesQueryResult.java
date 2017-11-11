package org.reactome.server.diagram.converter.graph.query;

import java.util.List;

@SuppressWarnings("unused")
public class EdgesQueryResult extends QueryResult {

    private List<Long> inputs;
    private List<Long> outputs;
    private List<Long> catalysts;
    private List<RegulationQueryResult> regulation;
    private List<Long> preceding;
    private List<Long> following;

    public List<Long> getInputs() {
        return inputs.isEmpty() ? null : inputs;
    }

    public List<Long> getOutputs() {
        return outputs.isEmpty() ? null : outputs;
    }

    public List<Long> getCatalysts() {
        return catalysts.isEmpty() ? null : catalysts;
    }

    public List<RegulationQueryResult> getRegulation() {
        return regulation;
    }

    public List<Long> getPreceding() {
        return preceding.isEmpty() ? null : preceding;
    }

    public List<Long> getFollowing() {
        return following.isEmpty() ? null :following;
    }
}
