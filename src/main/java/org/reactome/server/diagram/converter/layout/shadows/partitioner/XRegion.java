package org.reactome.server.diagram.converter.layout.shadows.partitioner;

public class XRegion {
    private int xStart;
    private int xEnd;
    private XRegion next;

    public XRegion(int s, int e) {
        this.xStart = s;
        this.xEnd = e;
        this.next = null;
    }

    public int getStartX() {
        return this.xStart;
    }

    public int getEndX() {
        return this.xEnd;
    }

    public XRegion getNext() {
        return this.next;
    }

    public void setNext(XRegion n) {
        this.next = n;
    }
}
