package org.wzeiri.zr.zrtaxiplatform.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by adamzfc on 1/4/17.
 * sharedprefences util
 */

public class SharedPreferencesUtil {

    /**
     * file name
     */
    public static final String FILE_NAME = "mf_sp";

    /**
     * 保存数据
     *
     * @param context 上下文
     * @param key     key
     * @param obj     value
     */
    public static void put(Context context, String key, Object obj) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (obj instanceof Boolean) {
            editor.putBoolean(key, (Boolean) obj);
        } else if (obj instanceof Float) {
            editor.putFloat(key, (Float) obj);
        } else if (obj instanceof Integer) {
            editor.putInt(key, (Integer) obj);
        } else if (obj instanceof Long) {
            editor.putLong(key, (Long) obj);
        } else {
            editor.putString(key, (String) obj);
        }
        editor.apply();
    }

    /**
     * 保存数据
     *
     * @param context 上下文
     * @param hashMap 内容集合
     */
    public static void put(Context context, HashMap<String, Object> hashMap) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        for (String itemKey :
                hashMap.keySet()) {
            Object itemObject = hashMap.get(itemKey);

            if (itemObject instanceof Boolean) {
                editor.putBoolean(itemKey, (Boolean) itemObject);
            } else if (itemObject instanceof Float) {
                editor.putFloat(itemKey, (Float) itemObject);
            } else if (itemObject instanceof Integer) {
                editor.putInt(itemKey, (Integer) itemObject);
            } else if (itemObject instanceof Long) {
                editor.putLong(itemKey, (Long) itemObject);
            } else {
                editor.putString(itemKey, (String) itemObject);
            }
        }
        editor.apply();
    }

    public static void putString(Context context, String key, String str) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, str);
        editor.commit();
    }

    public static void putInt(Context context, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void putLong(Context context, String key, long value) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static String getString(Context context, String key, String defaultStr) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        return sp.getString(key, defaultStr);
    }


    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }


    public static boolean getBoolean(Context context, String key, boolean defaultStr) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultStr);
    }

    public static int getInt(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        return sp.getInt(key, 0);
    }

    public static int getInt(Context context, String key, int defaultStr) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        return sp.getInt(key, defaultStr);
    }

    public static long getLong(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        return sp.getLong(key, 0);
    }

    public static long getLong(Context context, String key, int defaultStr) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        return sp.getLong(key, defaultStr);
    }



    /**
     * 获取指定的数据
     *
     * @param context    context
     * @param key        key
     * @param defaultObj defaultObj
     * @return
     */
    public static Object get(Context context, String key, Object defaultObj) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        if (defaultObj == null) {
            return sp.getString(key, null);
        }
        if (defaultObj instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObj);
        } else if (defaultObj instanceof Float) {
            return sp.getFloat(key, (Float) defaultObj);
        } else if (defaultObj instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObj);
        } else if (defaultObj instanceof Long) {
            return sp.getLong(key, (Long) defaultObj);
        } else if (defaultObj instanceof String) {
            return sp.getString(key, (String) defaultObj);
        }
        return null;
    }

    /**
     * 删除指定的数据
     *
     * @param key key
     */
    public static void remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.apply();
    }

    /**
     * 删除指定的数据
     *
     * @param key key
     */
    public static void remove(Context context, String... key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        for (String item :
                key) {
            editor.remove(item);
        }
        editor.apply();
    }

    /**
     * 返回所有的键值对
     *
     * @return
     */
    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        Map<String, ?> map = sp.getAll();
        return map;
    }

    /**
     * 清除所有数据
     *
     * @param context context
     */
    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

    /**
     * 检查是否存在此key对应的数据
     *
     * @param context context
     * @param key     key
     * @return
     */
    public static boolean contains(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        return sp.contains(key);
    }
}
