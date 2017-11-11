package org.reactome.server.diagram.converter.layout.shadows.partitioner;

import java.util.HashMap;

public class XYNodeList {
    private XYNode head = null;
    private XYNode rear = null;
    private HashMap<String, XYNode> ht = new HashMap<>();
    private XYNode iterator;

    public XYNodeList() {
    }

    public void insert(int x, int y) {
        XYNode node = new XYNode(x, y);
        if(this.head == null) {
            this.head = node;
            this.rear = node;
        } else {
            this.rear.setNext(node);
            node.setPrev(this.rear);
            this.rear = node;
        }

        String key = x + "^" + y;
        this.ht.put(key, node);
    }

    public XYNode getNode(int x, int y) {
        return this.ht.get(x + "^" + y);
    }

    public void setIterator() {
        this.iterator = this.head;
    }

    public boolean isEmptyIterator() {
        return this.iterator == this.rear;
    }

    public void nextIterator() {
        this.iterator = this.iterator.getNext();
    }

    public XYNode getIterator() {
        return this.iterator;
    }

    public void connectHeadAndRear() {
        this.rear.setNext(this.head);
        this.head.setPrev(this.rear);
    }

    public void modify(int x1, int y1, int x2, int y2) {
        if(y1 == y2) {
            XYNode node1 = this.getNode(x1, y1);
            XYNode node2 = this.getNode(x2, y2);
            XYNode node1_prev = node1.getPrev();
            XYNode node2_next = node2.getNext();
            if(node1 != node2_next || node2 != node1_prev) {
                if(node1.getY() == node1_prev.getY()) {
                    node1 = node1_prev;
                }

                if(node2.getY() == node2_next.getY()) {
                    node2 = node2_next;
                }

                node1.setNext(node2);
                node2.setPrev(node1);
                this.head = node1;
                this.rear = node1.getPrev();
            }
        }
    }

    public void modify(int x1, int y1, int x2, int y2, int x3, int y3) {
        //XYNode t = this.head;
        XYNode node1 = this.getNode(x1, y1);
        XYNode node3 = this.getNode(x3, y3);
        XYNode node3_next;
        if(y1 > y3) {
            node3_next = node1.getPrev();
            if(node1.getY() == node3_next.getY()) {
                node1 = node3_next;
            }
        } else if(y1 < y3) {
            node3_next = node3.getNext();
            if(node3.getY() == node3_next.getY()) {
                node3 = node3_next;
            }
        }

        XYNode node2 = new XYNode(x2, y2);
        node1.setNext(node2);
        node2.setNext(node3);
        node2.setPrev(node1);
        node3.setPrev(node2);
        this.ht.put(x2 + "^" + y2, node2);
        this.head = node1;
        this.rear = this.head.getPrev();
    }

    public void modify(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
        XYNode node1 = this.getNode(x1, y1);
        XYNode node4 = this.getNode(x4, y4);
        XYNode node2 = new XYNode(x2, y2);
        XYNode node3 = new XYNode(x3, y3);
        this.ht.put(x2 + "^" + y2, node2);
        this.ht.put(x3 + "^" + y3, node3);
        node1.setNext(node2);
        node2.setNext(node3);
        node3.setNext(node4);
        node4.setPrev(node3);
        node3.setPrev(node2);
        node2.setPrev(node1);
        this.head = node1;
        this.rear = this.head.getPrev();
    }
}
