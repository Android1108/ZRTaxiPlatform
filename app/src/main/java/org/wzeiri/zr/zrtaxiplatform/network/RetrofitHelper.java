package org.wzeiri.zr.zrtaxiplatform.network;


import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wzeiri.zr.zrtaxiplatform.common.Config;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItem;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItemArray;

import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author klm on 2017/8/31.
 */

public class RetrofitHelper {

    private Retrofit mRetrofit;
    protected static RetrofitHelper mHelp;


    protected RetrofitHelper() {

    }




    public static RetrofitHelper getInstance() {
        if (mHelp == null) {
            mHelp = new RetrofitHelper();
        }

        return mHelp;
    }

    /**
     * 返回retrofit 实例
     *
     * @return
     */
    public Retrofit getRetrofit() {
        if (mRetrofit == null) {
            synchronized (RetrofitHelper.class) {
                if (mRetrofit == null) {
                    OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(10, TimeUnit.SECONDS);


                    Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                            .baseUrl(Config.MOBILE_SERVER_RELEASE)
                            .addConverterFactory(
                                    GsonConverterFactory.create(
                                            new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS").create()
                                    )
                            );

                    retrofitMachining(clientBuilder, retrofitBuilder);
                    mRetrofit = retrofitBuilder.client(clientBuilder.build())
                            .build();

                }
            }
        }

        return mRetrofit;
    }

    /**
     * 用于子类加工 retrofit
     *
     * @param clientBuilder
     * @param retrofitBuilder
     */
    protected void retrofitMachining(OkHttpClient.Builder clientBuilder, Retrofit.Builder retrofitBuilder) {
        // 登录
        clientBuilder.addInterceptor(new LogInterceptor());
        clientBuilder.addInterceptor(new LoginInterceptor());

    }

    /**
     * 创建网络接口
     *
     * @param service 接口class类
     * @param <T>     接口类
     * @return 接口类实例
     */
    public static <T> T create(Class<T> service) {
        return getInstance().getRetrofit().create(service);


    }




    /**
     * 返回 RequestBody
     *
     * @param content json 字符串
     * @return RequestBody
     */
    public static RequestBody getBody(String content) {
        return RequestBody.create(MediaType.parse("application/json"), content);
    }

    public static String getString(){
        return null;
    }

    /**
     * 返回 RequestBody
     *
     * @param content
     * @return RequestBody
     */
    public static RequestBody getBody(JSONObject content) {
        return RequestBody.create(MediaType.parse("application/json"), content.toString());
    }

    /**
     * 返回 RequestBody
     *
     * @param items
     * @return RequestBody
     */
    public static RequestBody getBody(JsonItem... items) {
        JSONObject jsonObject = new JSONObject();
        if (items != null && items.length > 0) {
            try {
                for (JsonItem item : items) {
                    if (item == null) {
                        continue;
                    }
                    jsonObject.put(item.getKey(), item.getValue());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
    }

    /**
     * 返回 RequestBody
     *
     * @param itemArray
     * @return RequestBody
     */
    public static RequestBody getBody(JsonItemArray itemArray) {
        JSONObject jsonObject = new JSONObject();

        if (itemArray == null) {
            return RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        }
        String key = itemArray.getKey();
        String[] strArray = itemArray.getStrList();
        JsonItem[][] jsonItemArray = itemArray.getJsonItems();
        JSONArray jsonArray = new JSONArray();


        if (strArray != null && strArray.length > 0) {
            for (String item : strArray) {
                if (item == null) {
                    continue;
                }
                jsonArray.put(item);
            }

        } else if (jsonItemArray != null && jsonItemArray.length > 0) {
            try {
                for (JsonItem[] jsonItems : jsonItemArray) {
                    JSONObject itemObject = new JSONObject();
                    for (JsonItem item : jsonItems) {
                        if (item == null) {
                            continue;
                        }
                        itemObject.put(item.getKey(), item.getValue());
                    }
                    jsonArray.put(itemObject);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            jsonObject.put(key, jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
    }

    /**
     * 返回 RequestBody
     *
     * @param itemArray
     * @return RequestBody
     */
    public static RequestBody getBody(JsonItemArray itemArray, JsonItem... jsonItemArray) {
        JSONObject jsonObject = new JSONObject();

        if (itemArray == null) {
            return getBody(jsonItemArray);
        }
        String key = itemArray.getKey();
        String[] strArray = itemArray.getStrList();
        JsonItem[][] jsonItemArrays = itemArray.getJsonItems();
        JSONArray jsonArray = new JSONArray();


        if (strArray != null && strArray.length > 0) {
            for (String item : strArray) {
                if (item == null) {
                    continue;
                }
                jsonArray.put(item);
            }
            try {
                jsonObject.put(key, jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (jsonItemArrays != null && jsonItemArrays.length > 0) {
            try {
                for (JsonItem[] jsonItems : jsonItemArrays) {
                    JSONObject itemObject = new JSONObject();
                    for (JsonItem item : jsonItems) {
                        if (item == null) {
                            continue;
                        }
                        itemObject.put(item.getKey(), item.getValue());
                    }
                    jsonArray.put(itemObject);

                }
                jsonObject.put(key, jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        if (jsonItemArray != null && jsonItemArray.length > 0) {
            try {
                for (JsonItem item : jsonItemArray) {
                    if (item == null) {
                        continue;
                    }
                    jsonObject.put(item.getKey(), item.getValue());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        return RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
    }




}
