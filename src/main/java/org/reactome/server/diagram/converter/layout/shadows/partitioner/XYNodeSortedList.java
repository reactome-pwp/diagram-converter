package org.reactome.server.diagram.converter.layout.shadows.partitioner;

import java.util.HashMap;

public class XYNodeSortedList {
    private XYNode head = null;
    //private XYNode rear = null;
    private HashMap<String, XYNode> ht = new HashMap<>();

    public XYNodeSortedList() {
    }

    public void insert(int x, int y) {
        XYNode n = new XYNode(x, y);
        this.ht.put(x + "^" + y, n);
        if(this.head == null) {
            this.head = n;
            //this.rear = n;
        } else {
            XYNode t = this.head;

            XYNode t_prev;
            for(t_prev = null; t != null; t = t.getNext()) {
                if(x <= t.getX()) {
                    if(x >= t.getX()) {
                        return;
                    }
                    break;
                }

                t_prev = t;
            }

            if(t_prev == null) {
                n.setNext(this.head);
                this.head.setPrev(n);
                this.head = n;
            } else {
                t_prev.setNext(n);
                n.setNext(t);
                n.setPrev(t_prev);
                if(t != null) {
                    t.setPrev(n);
                }
            }

        }
    }

    public XYNode getLeftNeighbor(XYNodeList polygon, int x, int y) {
        XYNode t = this.ht.get(x + "^" + y);
        if(t == null) {
            return null;
        } else {
            t = t.getPrev();
            if(t == null) {
                return null;
            } else {
                for(XYNode t2 = t; t2 != null; t2 = t2.getPrev()) {
                    XYNode node = polygon.getNode(t2.getX(), t2.getY());
                    if(node.getNext().getY() > node.getY() && node.getNext().getY() >= y) {
                        return node;
                    }
                }

                return null;
            }
        }
    }

    public XYNode getRightNeighbor(XYNodeList polygon, int x, int y) {
        XYNode t = this.ht.get(x + "^" + y);
        if(t == null) {
            return null;
        } else if(t.getNext() != null) {
            t = t.getNext();
            return polygon.getNode(t.getX(), t.getY());
        } else {
            return null;
        }
    }

    public void remove(int x, int y) {
        XYNode t_prev = null;
        if(this.head != null) {
            XYNode t = this.ht.get(x + "^" + y);
            if(t != null) {
                t_prev = t.getPrev();
            }

            if(t_prev == null && t != null) {
                this.head = this.head.getNext();
                if(this.head != null) {
                    this.head.setPrev(null);
                }

                this.ht.remove(x + "^" + y);
            } else if(t != null) {
                if(t.getNext() != null) {
                    t.getNext().setPrev(t_prev);
                }

                t_prev.setNext(t.getNext());
                this.ht.remove(x + "^" + y);
            }
        }
    }
}
