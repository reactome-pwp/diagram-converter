package org.reactome.server.diagram.converter.layout.shadows.partitioner;

import java.util.HashMap;

public class Horizontal {
    HashMap<String, XRegionList> ht = new HashMap<>();

    public Horizontal() {
    }

    public void insert(int y, int startX, int endX) {
        XRegionList rList = this.ht.get("" + y);
        if(rList == null) {
            rList = new XRegionList();
            rList.insert(startX, endX);
            this.ht.put("" + y, rList);
        } else if(!rList.isExist(startX, endX)) {
            rList.insert(startX, endX);
            rList.generateDisjointSets();
            this.ht.remove("" + y);
            this.ht.put("" + y, rList);
        }

    }

    public boolean isConnected(int x1, int y1, int x2, int y2) {
        if(y1 != y2) {
            return false;
        } else {
            XRegionList list = this.ht.get("" + y1);
            return list !=null && list.isInRegion(x1, x2);
        }
    }
}
