package org.reactome.server.diagram.converter.layout.output;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class Shape {
    public enum Type { ARROW, BOX, CIRCLE, DOUBLE_CIRCLE, STOP }

    public Coordinate a;
    public Coordinate b;
    public Coordinate c;
    public Integer r;
    public Integer r1;
    public String s;        //symbol e.g. ?, \\, 0-9
    public Type type;
    public Boolean empty = null;

    public Shape(Coordinate a, Coordinate b, Boolean empty, Type type) {
        if(!type.equals(Type.BOX)){
            throw new RuntimeException("This constructor can only be used for box");
        }
        this.a = a;
        this.b = b;
        if(empty){
            this.empty = true;
        }
        this.type = type;
        setBoundaries();
    }

    public Shape(Coordinate a, Coordinate b, Coordinate c, Boolean empty, Type type) {
        if(!type.equals(Type.ARROW) && !type.equals(Type.STOP)){
            throw new RuntimeException("This constructor can only be used for arrow or stop");
        }
        this.a = a;
        this.b = b;
        this.c = c;
        if(empty){
            this.empty = true;
        }
        this.type = type;
        setBoundaries();
    }

    public Shape(Coordinate c, Integer r, Boolean empty, Type type) {
        if(!type.equals(Type.CIRCLE) && !type.equals(Type.DOUBLE_CIRCLE)){
            throw new RuntimeException("This constructor can only be used for circles");
        }
        this.c = c;
        this.r = r;
        if(empty){
            this.empty = true;
        }
        this.type = type;
        setBoundaries();
    }

    transient Integer minX; transient Integer maxX;
    transient Integer minY; transient Integer maxY;

    protected void setBoundaries(){
        List<Integer> xx =  new ArrayList<>();
        List<Integer> yy = new ArrayList<>();
        switch (type){
            case CIRCLE:
            case DOUBLE_CIRCLE:
                xx.add(c.x+r); yy.add(c.y+r);
                xx.add(c.x-r); yy.add(c.y-r);
                break;
            case ARROW:
                xx.add(c.x); yy.add(c.y);
            default:
                xx.add(a.x); yy.add(a.y);
                xx.add(b.x); yy.add(b.y);
        }

        this.minX = Collections.min(xx);
        this.maxX = Collections.max(xx);
        this.minY = Collections.min(yy);
        this.maxY = Collections.max(yy);
    }
}
