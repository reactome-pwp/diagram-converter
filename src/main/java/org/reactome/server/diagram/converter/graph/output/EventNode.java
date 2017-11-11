package org.reactome.server.diagram.converter.graph.output;

import org.reactome.server.diagram.converter.graph.query.EdgesQueryResult;
import org.reactome.server.diagram.converter.graph.query.RegulationQueryResult;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class EventNode extends GraphNode {

    private List<Long> preceding = null;
    private List<Long> following = null;

    public List<Long> inputs = null;
    public List<Long> outputs = null;
    public List<Long> catalysts = null;
    public List<Long> inhibitors = null;
    public List<Long> activators = null;
    public List<Long> requirements = null;

    public List<Long> diagramIds = null;


    public EventNode(EdgesQueryResult edge, List<Long> diagramIds) {
        super(edge);
        this.diagramIds = diagramIds;

        this.preceding = edge.getPreceding();
        this.following = edge.getFollowing();

        this.inputs = edge.getInputs();
        this.outputs = edge.getOutputs();
        this.catalysts = edge.getCatalysts();

        for (RegulationQueryResult reg : edge.getRegulation()) {
            switch (reg.getType()) {
                case "Requirement":
                    if (this.requirements == null) this.requirements = new ArrayList<>();
                    this.requirements.add(reg.getDbId());
                    break;
                case "PositiveRegulation":
                case "PositiveGeneExpressionRegulation":
                    if (this.activators == null) this.activators = new ArrayList<>();
                    this.activators.add(reg.getDbId());
                    break;
                case "NegativeRegulation":
                case "NegativeGeneExpressionRegulation":
                    if (this.inhibitors == null) this.inhibitors = new ArrayList<>();
                    this.inhibitors.add(reg.getDbId());
                    break;
                default:
                    System.err.println(reg.getType() + " not recognised");
            }
        }
    }

    public Long getDbId() {
        return dbId;
    }

    public String getStId() {
        return stId;
    }

    public String getDisplayName() {
        return displayName;
    }


    public List<Long> getPreceding() {
        return preceding;
    }

    public List<Long> getFollowing() {
        return following;
    }

    public List<Long> getInputs() {
        return inputs;
    }

    public List<Long> getOutputs() {
        return outputs;
    }

    public List<Long> getCatalysts() {
        return catalysts;
    }

    public List<Long> getInhibitors() {
        return inhibitors;
    }

    public List<Long> getActivators() {
        return activators;
    }

    public List<Long> getRequirements() {
        return requirements;
    }
}
