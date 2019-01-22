package org.wzeiri.zr.zrtaxiplatform.bean;

import java.util.Date;
import java.util.List;

/**
 * @author k-lm on 2018/1/2.
 */

public class NotificationBean {


    /**
     * unreadCount : 0
     * totalCount : 0
     * items : [{"readStatus":0,"notificationName":"string","title":"string","message":"string","creationTime":"2018-01-02T05:34:25.880Z","id":"string"}]
     */

    private int unreadCount;
    private int totalCount;
    private List<ItemsBean> items;

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean {
        /**
         * readStatus : 0
         * notificationName : string
         * title : string
         * message : string
         * creationTime : 2018-01-02T05:34:25.880Z
         * id : string
         */

        private int readStatus = -1;
        private String notificationName;
        private String title;
        private String message;
        private Date creationTime;
        private String id;
        private String creationTimeStr;

        public int getReadStatus() {
            return readStatus;
        }

        public void setReadStatus(int readStatus) {
            this.readStatus = readStatus;
        }

        public String getNotificationName() {
            return notificationName;
        }

        public void setNotificationName(String notificationName) {
            this.notificationName = notificationName;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Date getCreationTime() {
            return creationTime;
        }

        public void setCreationTime(Date creationTime) {
            this.creationTime = creationTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreationTimeStr() {
            return creationTimeStr;
        }

        public void setCreationTimeStr(String creationTimeStr) {
            this.creationTimeStr = creationTimeStr;
        }
    }
}
