package org.reactome.server.diagram.converter.layout.output;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("WeakerAccess")
public class Coordinate {

    public Integer x;
    public Integer y;

    public Coordinate(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate(List<Integer> points) {
        this.x = points.get(0);
        this.y = points.get(1);
    }

    public Coordinate add(Coordinate value){
        return new Coordinate(x+value.x, y+value.y);
    }

    public Coordinate minus(Coordinate value){
        return new Coordinate(x-value.x, y-value.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinate coordinate = (Coordinate) o;

        //noinspection SimplifiableIfStatement
        if (x != null ? !x.equals(coordinate.x) : coordinate.x != null) return false;
        return !(y != null ? !y.equals(coordinate.y) : coordinate.y != null);

    }

    @Override
    public int hashCode() {
        int result = x != null ? x.hashCode() : 0;
        result = 31 * result + (y != null ? y.hashCode() : 0);
        return result;
    }
}
