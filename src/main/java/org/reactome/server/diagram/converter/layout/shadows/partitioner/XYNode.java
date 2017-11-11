package org.reactome.server.diagram.converter.layout.shadows.partitioner;

public class XYNode {
    private int x;
    private int y;
    private XYNode prev;
    private XYNode next;

    public XYNode(int x_value, int y_value) {
        this.x = x_value;
        this.y = y_value;
        this.prev = null;
        this.next = null;
    }

    public void setPrev(XYNode p) {
        this.prev = p;
    }

    public void setNext(XYNode n) {
        this.next = n;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public XYNode getPrev() {
        return this.prev;
    }

    public XYNode getNext() {
        return this.next;
    }
}
