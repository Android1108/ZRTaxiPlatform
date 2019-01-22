package org.wzeiri.zr.zrtaxiplatform.bean.base;

import java.util.List;

/**
 * @author k-lm on 2017/12/12.
 */

public class BaseListBean<T> {

    /**
     * totalCount : 0
     */
    List<T> items;


    private int totalCount;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public int size() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }
}
