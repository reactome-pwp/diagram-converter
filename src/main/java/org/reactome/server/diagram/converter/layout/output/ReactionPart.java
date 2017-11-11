package org.reactome.server.diagram.converter.layout.output;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Kostas Sidiropoulos (ksidiro@ebi.ac.uk)
 */
public class ReactionPart {

    public Long id;
    public List<Coordinate> points;
    public Integer stoichiometry;

    public ReactionPart(Object obj) {
        for (Method method : obj.getClass().getMethods()) {
            switch (method.getName()) {
                case "getId":
                    this.id = DiagramObject.getLong(method, obj);
                    break;
                case "getPoints":
                    String points = DiagramObject.getString(method, obj);
                    this.points = DiagramObject.extractIntegerPairsListFromString(points, ",", " ");
                    break;
                case "getStoichiometry":
                    this.stoichiometry = DiagramObject.getInteger(method, obj);
                    break;
            }
        }
    }
}
