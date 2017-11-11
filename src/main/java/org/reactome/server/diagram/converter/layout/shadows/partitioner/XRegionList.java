package org.reactome.server.diagram.converter.layout.shadows.partitioner;

import java.util.HashMap;

public class XRegionList {
    private XRegion head = null;
    private XRegion rear = null;
    private HashMap<String, XRegion> ht = new HashMap<>();

    public XRegionList() {
    }

    public boolean isExist(int s, int e) {
        return this.ht.containsKey(s + "^" + e);
    }

    public void insert(int s, int e) {
        XRegion t = new XRegion(s, e);
        if(this.head == null) {
            this.head = t;
            this.rear = t;
        } else {
            this.rear.setNext(t);
            this.rear = t;
        }

        this.ht.put(s + "^" + e, t);
    }

    public void insertFront(int s, int e) {
        if(!this.isExist(s, e)) {
            XRegion t = new XRegion(s, e);
            t.setNext(this.head);
            this.head = t;
            this.ht.put(s + "^" + e, t);
        }
    }

    public boolean isInRegion(int x1, int x2) {
        if(this.ht.containsKey(x1 + "^" + x2)) {
            return true;
        } else {
            for(XRegion t = this.head; t != null; t = t.getNext()) {
                if(t.getStartX() <= x1 && x2 <= t.getEndX()) {
                    return true;
                }
            }

            return false;
        }
    }

    public void generateDisjointSets() {
        XRegion sortedList = this.sort();
        int sX = sortedList.getStartX();
        int eX = sortedList.getEndX();
        boolean newDisjointedSet = false;

        for(XRegion t = sortedList; t != null; t = t.getNext()) {
            if(t.getStartX() <= eX) {
                if(eX < t.getEndX()) {
                    eX = t.getEndX();
                    newDisjointedSet = true;
                }
            } else {
                if(newDisjointedSet) {
                    this.insertFront(sX, eX);
                }

                sX = t.getStartX();
                eX = t.getEndX();
                newDisjointedSet = false;
            }
        }

        if(newDisjointedSet) {
            this.insertFront(sX, eX);
        }

    }

    public XRegion sort() {
        XRegion res = null;

        for(XRegion t = this.head; t != null; t = t.getNext()) {
            XRegion n = new XRegion(t.getStartX(), t.getEndX());
            if(res == null) {
                res = n;
            } else {
                XRegion r_prev = null;

                XRegion r;
                for(r = res; r != null && t.getStartX() > r.getStartX(); r = r.getNext()) {
                    r_prev = r;
                }

                if(r_prev == null) {
                    n.setNext(res);
                    res = n;
                } else {
                    r_prev.setNext(n);
                    n.setNext(r);
                }
            }
        }

        return res;
    }
}
