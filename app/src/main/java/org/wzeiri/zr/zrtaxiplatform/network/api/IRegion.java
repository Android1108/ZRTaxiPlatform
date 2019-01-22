package org.wzeiri.zr.zrtaxiplatform.network.api;

import org.wzeiri.zr.zrtaxiplatform.bean.LocationRegionBean;
import org.wzeiri.zr.zrtaxiplatform.bean.ProvinceBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author k-lm on 2017/12/22.
 */

public interface IRegion {
    /**
     * GET /Api/App/Region/GetLocationRegion
     * 根据定位的省市区名称获取本地数据库存储的省市区信息
     *
     * @param provinceName 省份
     * @param cityName     城市名
     * @param areaName     地区名称
     * @return
     */
    @GET("/Api/App/Region/GetLocationRegion")
    Call<BaseBean<LocationRegionBean>> getLocationRegion(@Query("ProvinceName") String provinceName,
                                                         @Query("CityName") String cityName,
                                                         @Query("AreaName") String areaName);

    /**
     * GET /Api/App/Region/GetProvinces
     获取全部省份
     *
     * @return
     */
    @GET("/Api/App/Region/GetProvinces")
    Call<BaseBean<List<ProvinceBean>>> getProvinces();

    /**
     * GET /Api/App/Region/GetCities
     * 根据省份代码获取城市
     *
     * @param provinceCode 省份代码
     * @return
     */
    @GET("/Api/App/Region/GetCities")
    Call<BaseBean<List<ProvinceBean>>> getCities(@Query("provinceCode") String provinceCode);

    /**
     * GET /Api/App/Region/GetAreas
     * 根据城市代码获取地区
     *
     * @param cityCode 城市代码
     * @return
     */
    @GET("/Api/App/Region/GetRegionAreas")
    Call<BaseBean<List<ProvinceBean>>> getRegionAreas(@Query("cityCode") String cityCode);


}
