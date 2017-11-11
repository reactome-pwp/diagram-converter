package org.reactome.server.diagram.converter.layout.shadows.partitioner;

public class XYNodeSearchTree {
    private XYTNode root = null;
    private XYNodeList polygon;

    public XYNodeSearchTree(XYNodeList p) {
        this.polygon = p;
    }

    public boolean isEmpty() {
        return this.root == null;
    }

    public void insert(int x, int y) {
        XYTNode n = new XYTNode(x, y, this.polygon.getNode(x, y));
        if(this.root == null) {
            this.root = n;
        } else {
            XYTNode t = this.root;

//            while(t != null && (x != t.getX() || y != t.getY())) {
            while(x != t.getX() || y != t.getY()) {
                if(y > t.getY() || y == t.getY() && x < t.getX()) {
                    if(t.getRight() == null) {
                        t.setRight(n);
                        break;
                    }

                    t = t.getRight();
                } else {
                    if(t.getLeft() == null) {
                        t.setLeft(n);
                        break;
                    }

                    t = t.getLeft();
                }
            }

        }
    }

    public XYTNode pop() {
        XYTNode t = this.root;

        XYTNode t_parent;
        for(t_parent = null; t != null && t.getRight() != null; t = t.getRight()) {
            t_parent = t;
        }

        if(t == this.root) {
            this.root = this.root.getLeft();
        } else {
            t_parent.setRight(t.getLeft());
        }

        return t;
    }
}
