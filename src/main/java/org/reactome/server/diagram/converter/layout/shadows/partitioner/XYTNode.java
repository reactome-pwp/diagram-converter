package org.reactome.server.diagram.converter.layout.shadows.partitioner;

public class XYTNode {
    private int x;
    private int y;
    private XYNode xynode;
    private XYTNode left;
    private XYTNode right;

    public XYTNode(int x_value, int y_value, XYNode node) {
        this.x = x_value;
        this.y = y_value;
        this.xynode = node;
        this.left = null;
        this.right = null;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public XYTNode getLeft() {
        return this.left;
    }

    public XYTNode getRight() {
        return this.right;
    }

    public void setLeft(XYTNode l) {
        this.left = l;
    }

    public void setRight(XYTNode r) {
        this.right = r;
    }

    public XYNode getXYnode() {
        return this.xynode;
    }
}