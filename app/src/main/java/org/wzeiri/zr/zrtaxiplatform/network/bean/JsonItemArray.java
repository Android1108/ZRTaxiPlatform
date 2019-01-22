package org.wzeiri.zr.zrtaxiplatform.network.bean;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.wzeiri.zr.zrtaxiplatform.bean.LoginBean;

import java.util.List;

/**
 * @author k-lm on 2017/12/12.
 */

public class JsonItemArray {
    private String key;
    private String[] strList;
    private JsonItem[][] jsonItems;


    public JsonItemArray(String key, String... strs) {
        strList = strs;
        this.key = key;
    }

    public JsonItemArray(String key, List<String> strList) {
        this.key = key;

        if (strList == null || strList.size() == 0) {
            this.strList = new String[0];
            return;
        }
        this.strList = new String[strList.size()];

        for (int i = 0; i < this.strList.length; i++) {
            this.strList[i] = strList.get(i);
        }

    }

    public JsonItemArray(String key, JsonItem[][] jsonItems) {
        this.jsonItems = jsonItems;
        this.key = key;
    }

    public String[] getStrList() {
        return strList;
    }

    public void setStrList(String[] strList) {
        this.strList = strList;
    }

    public JsonItem[][] getJsonItems() {
        return jsonItems;
    }

    public void setJsonItems(JsonItem[][] jsonItems) {
        this.jsonItems = jsonItems;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
