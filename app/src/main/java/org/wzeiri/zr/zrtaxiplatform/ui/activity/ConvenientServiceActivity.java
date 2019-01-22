package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.district.DistrictResult;
import com.baidu.mapapi.search.district.DistrictSearch;
import com.baidu.mapapi.search.district.DistrictSearchOption;
import com.baidu.mapapi.search.district.OnGetDistricSearchResultListener;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.DistanceUtil;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.ToiletsBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IToilet;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.service.BaiduLocationService;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.util.PermissionsUtil;
import org.wzeiri.zr.zrtaxiplatform.util.ThirdPartyMapUtil;
import org.wzeiri.zr.zrtaxiplatform.widget.dialog.SoundRecordingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 便捷服务 地图
 *
 * @author k-lm on 2017/11/30.
 */

public class ConvenientServiceActivity extends ActionbarActivity {
    @BindView(R.id.aty_convenient_service_map)
    TextureMapView mMap;
    @BindView(R.id.aty_convenient_service_map_text_name)
    TextView mTextName;
    @BindView(R.id.aty_convenient_service_map_text_address)
    TextView mTextAddress;
    @BindView(R.id.aty_convenient_service_map_text_distance)
    TextView mTextDistance;
    @BindView(R.id.aty_convenient_service_map_layout_navigation)
    LinearLayout mLayoutNavigation;
    @BindView(R.id.aty_convenient_service_map_text_search)
    TextView mTextSearch;

    @BindView(R.id.aty_convenient_service_map_image_location)
    ImageView mImageLocation;

    private BaiduMap mBaiduMap;
    /**
     * poi检索
     */
    private PoiSearch mPoiSearch;

    /**
     * 当前选择的覆盖物
     */
    private Marker mCurrentMarker = null;

    private MapPoi mCurrentMapPoi = null;

    /**
     * 搜索结果key
     */
    private static final String KEY_POI_INFO = "poiInfo";
    /**
     * 是否显示搜索信息
     */
    private static final String KEY_SHOW_SEARCH_INFO = "showSearchInfo";

    /**
     * 当前是否显示搜索结果
     */
    private boolean mIsShowSearchInfo = false;
    /**
     * 搜索结果页面
     */
    private PoiInfo mPoiInfo;

    private GeoCoder mGeoCoder;

    private ReverseGeoCodeOption mGeoCodeOption;

    BitmapDescriptor mDescriptor;

    private SoundRecordingDialog mSoundRecordingDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_convenient_service;
    }


    @OnClick(R.id.aty_convenient_service_map_image_location)
    void clickLocation() {
        boolean isLocation = mImageLocation.isSelected();
        mImageLocation.setSelected(!isLocation);
        // 点击之前没有选择定位，则开始定位用户位置
        if (!isLocation) {
            double latitude = BaiduLocationService.getLatitude();
            double longitude = BaiduLocationService.getLongitude();
            MyLocationData mLocationData = new MyLocationData.Builder()
                    .direction(0)
                    .latitude(latitude)
                    .longitude(longitude)
                    .build();
            mBaiduMap.setMyLocationData(mLocationData);
        }

    }


    @OnClick(R.id.aty_convenient_service_map_text_navigation)
    void onClickNavigation() {
        if (mCurrentMarker == null && mCurrentMapPoi == null && mPoiInfo == null) {
            return;
        }

       /* if (!PermissionsUtil.isLocationPermissions(this)) {
            showToast("无法获取准确位置，请打开定位权限");
            return;
        }*/


        String title;
        LatLng destinationLL;


        if (mCurrentMarker != null) {
            title = mCurrentMarker.getTitle();
            destinationLL = mCurrentMarker.getPosition();
        } else if (mCurrentMapPoi != null) {
            title = mCurrentMapPoi.getName();
            destinationLL = mCurrentMapPoi.getPosition();
        } else {
            title = mPoiInfo.name;
            destinationLL = mPoiInfo.location;
        }


        //百度地图
        if (ThirdPartyMapUtil.isInstallBaiduMap()) {
            ThirdPartyMapUtil.startBaiduMap(this,
                    BaiduLocationService.getCity(),
                    title,
                    BaiduLocationService.getLatitude(),
                    BaiduLocationService.getLongitude(),
                    destinationLL.latitude,
                    destinationLL.longitude);
            return;
        }

        // 百度经纬度转 国测局坐标
        destinationLL = new CoordinateConverter().
                from(CoordinateConverter.CoordType.BD09LL)
                .coord(destinationLL)
                .convert();

        LatLng originLL = new CoordinateConverter().
                from(CoordinateConverter.CoordType.BD09LL)
                .coord(new LatLng(BaiduLocationService.getLatitude(), BaiduLocationService.getLongitude()))
                .convert();

        // 高德地图
        if (ThirdPartyMapUtil.isInstallMinimap()) {
            ThirdPartyMapUtil.startMinimap(this,
                    originLL.latitude,
                    originLL.longitude,
                    title,
                    destinationLL.latitude,
                    destinationLL.longitude,
                    0);
            return;
        }
        // 腾讯地图
        if (ThirdPartyMapUtil.isInstallTencentMap()) {
            ThirdPartyMapUtil.startTencentMap(this,
                    BaiduLocationService.getAddress(),
                    originLL.latitude,
                    originLL.longitude,
                    title,
                    destinationLL.latitude,
                    destinationLL.longitude);
            return;
        }

        //打开地图app
        ThirdPartyMapUtil.startMap(this,
                BaiduLocationService.getAddress(),
                originLL.latitude,
                originLL.longitude,
                title,
                destinationLL.latitude,
                destinationLL.longitude);


    }


    @OnClick(R.id.aty_convenient_service_map_layout_search)
    void onSearch() {
        startActivity(ConvenientServiceSearchActivity.class);

        if (mPoiInfo != null && mIsShowSearchInfo) {
            finish();
        }
    }

    /**
     * 开启语音
     */
    @OnClick(R.id.aty_convenient_service_map_image_voice)
    void onOpenVoice() {
        ConvenientServiceSearchActivity.startVoice(getThis());
    }




    @Override
    protected void create() {
        super.create();
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        mPoiInfo = intent.getParcelableExtra(KEY_POI_INFO);
        mIsShowSearchInfo = intent.getBooleanExtra(KEY_SHOW_SEARCH_INFO, mIsShowSearchInfo);

    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("一键导航");
        initMap();
    }

    /**
     * 初始化地图
     */
    private void initMap() {
        mBaiduMap = mMap.getMap();
        // 设置普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        // 设置缩放
        mBaiduMap.setMaxAndMinZoomLevel(18, 21);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(18));
        // 不显示缩放按钮
        mMap.showZoomControls(false);


        // 设置图标
        mBaiduMap.setMyLocationEnabled(true);
        mDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_map_arrow);
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING,
                true, mDescriptor

        ));

        mPoiSearch = PoiSearch.newInstance();

        // 如果有定位权限，则显示当前位置
        // 在某些情况下没有权限情况下会判断已获取定位权限
        if (PermissionsUtil.isLocationPermissions(this) &&
                BaiduLocationService.getLatitude() != 0 &&
                BaiduLocationService.getLongitude() != 0) {
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(new LatLng(BaiduLocationService.getLatitude(), BaiduLocationService.getLongitude()));
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

            MyLocationData mLocationData = new MyLocationData.Builder()
                    .direction(0)
                    .latitude(BaiduLocationService.getLatitude())
                    .longitude(BaiduLocationService.getLongitude())
                    .build();
            mBaiduMap.setMyLocationData(mLocationData);
        } else {
            // 根据城市获取坐标
            final DistrictSearch search = DistrictSearch.newInstance();
            search.setOnDistrictSearchListener(new OnGetDistricSearchResultListener() {
                @Override
                public void onGetDistrictResult(DistrictResult districtResult) {
                    if (districtResult.centerPt == null) {
                        return;
                    }

                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(districtResult.centerPt);
                    mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

                    MyLocationData mLocationData = new MyLocationData.Builder()
                            .direction(0)
                            .latitude(districtResult.centerPt.latitude)
                            .longitude(districtResult.centerPt.longitude)
                            .build();
                    mBaiduMap.setMyLocationData(mLocationData);
                    search.destroy();
                }
            });

            search.searchDistrict(new DistrictSearchOption().cityName("温州"));

        }


        // 启动定位服务
        BaiduLocationService.start(this, new BaiduLocationService.OnUpLocationListener() {
            @Override
            public void onReceiveLocation() {
                if (mIsShowSearchInfo || mBaiduMap == null) {
                    return;
                }
                // 实时定位司机位置
                boolean isLocation = mImageLocation.isSelected();
                double latitude = BaiduLocationService.getLatitude();
                double longitude = BaiduLocationService.getLongitude();
/*
                if (mDescriptor != null) {
                    LatLng latLng = new LatLng(latitude, longitude);

                    OverlayOptions option = new MarkerOptions()
                            .position(latLng)
                            .icon(mDescriptor);
                }*/

                // 是否选择定位司机位置
                if (!isLocation) {
                    return;
                }


                MyLocationData mLocationData = new MyLocationData.Builder()
                        .direction(0)
                        .latitude(latitude)
                        .longitude(longitude)
                        .build();
                mBaiduMap.setMyLocationData(mLocationData);

            }

            @Override
            public void onError() {

            }
        });


        // 设置点击覆盖物事件
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                mCurrentMapPoi = null;
                mPoiInfo = null;
                mCurrentMarker = marker;

                // 经纬度转化为地址 检索
                if (mGeoCodeOption == null) {
                    mGeoCodeOption = new ReverseGeoCodeOption();
                }
                mGeoCodeOption.location(marker.getPosition());
                mGeoCoder.reverseGeoCode(mGeoCodeOption);

                //显示导航布局
                showLayoutNavigation(mCurrentMarker.getTitle(), "", mCurrentMarker.getPosition());
                return true;
            }
        });


        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                mCurrentMarker = null;
                mCurrentMapPoi = mapPoi;
                mPoiInfo = null;

                // 经纬度转化为地址 检索
                if (mGeoCodeOption == null) {
                    mGeoCodeOption = new ReverseGeoCodeOption();
                }
                mGeoCodeOption.location(mapPoi.getPosition());
                mGeoCoder.reverseGeoCode(mGeoCodeOption);

                //显示导航布局
                showLayoutNavigation(mCurrentMapPoi.getName(), "", mCurrentMapPoi.getPosition());
                return true;
            }
        });


        mGeoCoder = GeoCoder.newInstance();
        mGeoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有检索到结果
                    return;
                }
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有找到检索结果
                    return;
                }

                mTextAddress.setText(result.getSematicDescription());
            }
        });

    }

    @Override
    protected void initData() {
        super.initData();

        if (mIsShowSearchInfo && mPoiInfo != null) {
            showPoiInfo();
        } else {
            initNetData();
        }

    }

    /**
     * 显示poi检索信息
     */
    private void showPoiInfo() {
        // 显示搜索结果覆盖物
        addCovers(mPoiInfo.name, mPoiInfo.location.latitude, mPoiInfo.location.longitude);
        mTextSearch.setText(mPoiInfo.name);

        // 定位到当前搜索区域
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(mPoiInfo.location);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

        showLayoutNavigation(mPoiInfo.name, mPoiInfo.address, mPoiInfo.location);

    }

    /**
     * 显示便捷服务中的公共厕所信息
     */
    private void initNetData() {

        IToilet tokenAuth = RetrofitHelper.create(IToilet.class);
        tokenAuth.getAllToilets()
                .enqueue(new MsgCallBack<BaseBean<List<ToiletsBean>>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<List<ToiletsBean>>> call, Response<BaseBean<List<ToiletsBean>>> response) {
                        List<ToiletsBean> beanList = response.body().getResult();

                        if (beanList == null || beanList.size() == 0) {
                            return;
                        }

                        List<String> names = new ArrayList<>();
                        List<Double> latitudes = new ArrayList<>();
                        List<Double> longitudes = new ArrayList<>();

                        for (ToiletsBean bean : beanList) {
                            String name = bean.getName();
                            double latitude = bean.getLatitude();
                            double longitude = bean.getLongitude();
                            names.add(name);
                            latitudes.add(latitude);
                            longitudes.add(longitude);
                        }

                        addComfortStationCovers(names, latitudes, longitudes);
                    }
                });


        //addCovers("温州市中西医结合医院", 28.004965, 120.70078);
    }

    /**
     * 添加覆盖物
     *
     * @param name      名称
     * @param latitude  纬度
     * @param longitude 经度
     */
    private void addCovers(String name, double latitude, double longitude) {

       /* TextView textView = new TextView(this);
        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.mipmap.ic_launcher_round);
        textView.setText(name);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_medium));
        textView.setTextColor(ContextCompat.getColor(this, R.color.black70));
        textView.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.layout_margin_mini));*/

        LatLng point = new LatLng(latitude, longitude);
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .title(name)
                .visible(true)
                .icon(bitmap);

        mBaiduMap.addOverlay(option);

    }


    /**
     * 添加厕所覆盖物
     *
     * @param names      名称
     * @param latitudes  纬度
     * @param longitudes 经度
     */
    private void addComfortStationCovers(List<String> names, List<Double> latitudes, List<Double> longitudes) {
        if (names == null || latitudes == null || longitudes == null ||
                names.size() == 0 || latitudes.size() == 0 || longitudes.size() == 0) {
            return;
        }

        int nameCount = names.size();
        int latitudeCount = latitudes.size();
        int longitudeCount = longitudes.size();

        int count = Math.min(nameCount, Math.min(latitudeCount, longitudeCount));

        List<OverlayOptions> list = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            String name = names.get(i);
            double latitude = latitudes.get(i);
            double longitude = longitudes.get(i);
           /* TextView textView = new TextView(this);
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.mipmap.ic_launcher_round);
            textView.setText(name);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_medium));
            textView.setTextColor(ContextCompat.getColor(this, R.color.black70));
            textView.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.layout_margin_mini));*/

            LatLng point = new LatLng(latitude, longitude);
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_comfort_station);
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .title(name)
                    .visible(true)
                    .icon(bitmap);
            list.add(option);
        }


        mBaiduMap.addOverlays(list);

    }

    /**
     * 显示底部导航布局
     *
     * @param name   目标名称
     * @param latLng 目标位置
     */
    private void showLayoutNavigation(String name, String address, LatLng latLng) {

        if (!mLayoutNavigation.isShown()) {
            mLayoutNavigation.setVisibility(View.VISIBLE);
        }


        if (PermissionsUtil.isLocationPermissions(getThis()) &&
                BaiduLocationService.getLatitude() != 0 &&
                BaiduLocationService.getLongitude() != 0) {
            mTextDistance.setVisibility(View.VISIBLE);
        } else {
            // 无法获取定位信息
            mTextDistance.setVisibility(View.GONE);

        }

        // 当前位置
        LatLng currentLL = new LatLng(BaiduLocationService.getLatitude(), BaiduLocationService.getLongitude());
        // 覆盖物

        int distance = (int) DistanceUtil.getDistance(currentLL, latLng);

        mTextName.setText(name);
        mTextAddress.setText(address);
        mTextDistance.setText("距您" + distance + "米");
    }

    @Override
    protected void onResume() {
        mMap.onResume();
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        mMap.onDestroy();
        mBaiduMap = null;
        BaiduLocationService.stop(this);
        BaiduLocationService.stopLocation();
        BaiduLocationService.removeOnLocationListener(this);
        mPoiSearch.destroy();
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        mMap.onPause();
        super.onPause();

    }

    @Override
    public void finish() {
        super.finish();
        if (mIsShowSearchInfo && mPoiInfo != null) {
            overridePendingTransition(0, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BaiduLocationService.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }


    public static void start(Context context, PoiInfo poiInfo) {
        Intent starter = new Intent(context, ConvenientServiceActivity.class);
        starter.putExtra(KEY_POI_INFO, poiInfo);
        starter.putExtra(KEY_SHOW_SEARCH_INFO, true);
        context.startActivity(starter);
    }

}
