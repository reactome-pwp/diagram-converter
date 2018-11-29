package org.reactome.server.diagram.converter.qa.common.data;

/**
 * @author Antonio Fabregat (fabregat@ebi.ac.uk)
 */
public enum Category {

    TRANSITION("transition"),
    BINDING("binding"),
    DISSOCIATION("dissociation"),
    OMITTED("omitted"),
    UNCERTAIN("uncertain");

    private String name;

    Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Category getShapeType(String name) {
        for (Category value : values()) {
            if (value.name.equals(name)) return value;
        }
        throw new IllegalArgumentException("'" + name + "' does not correspond to any Category");
    }

    public boolean equals(Category that) {
        if (this == that) return true;
        //noinspection RedundantIfStatement
        if ((this == UNCERTAIN && that == OMITTED) || (this == OMITTED && that == UNCERTAIN)) return true;
        return false;
    }
}
