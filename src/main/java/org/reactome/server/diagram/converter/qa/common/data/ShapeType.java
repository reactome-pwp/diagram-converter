package org.reactome.server.diagram.converter.qa.common.data;

/**
 * @author Antonio Fabregat (fabregat@ebi.ac.uk)
 */
public enum ShapeType {

    TRANSITION("transition"),
    BINDING("binding"),
    DISSOCIATION("dissociation"),
    OMITTED("omitted"),
    UNCERTAIN("uncertain");

    private String name;

    ShapeType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ShapeType getShapeType(String name) {
        for (ShapeType value : values()) {
            if (value.name.equals(name)) return value;
        }
        throw new IllegalArgumentException("'" + name + "' does not correspond to any ShapeType");
    }

    public boolean equals(ShapeType that) {
        if (this == that) return true;
        //noinspection RedundantIfStatement
        if ((this == UNCERTAIN && that == OMITTED) || (this == OMITTED && that == UNCERTAIN)) return true;
        return false;
    }
}
