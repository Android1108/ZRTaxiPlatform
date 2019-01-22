package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.service.BaiduLocationService;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.util.LogUtil;
import org.wzeiri.zr.zrtaxiplatform.util.PermissionsUtil;
import org.wzeiri.zr.zrtaxiplatform.widget.dialog.SoundRecordingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 便捷服务搜索
 *
 * @author k-lm on 2017/12/4.
 */

public class ConvenientServiceSearchActivity extends ActionbarActivity {
    @BindView(R.id.aty_convenient_service_map_search_edit_search)
    EditText mSearchView;

    @BindView(R.id.aty_convenient_service_map_search_list)
    ListView mListView;
    /**
     * 搜索
     */

    private PoiSearch mPoiSearch;
    private PoiCitySearchOption mCitySearchOption;

    private List<PoiInfo> mList = new ArrayList<>();

    private SimpleAdapter mAdapter;

    private List<Map<String, String>> mData = new ArrayList<>();


    private final String NAME = "name";

    private final String ADDRESS = "address";

    private SoundRecordingDialog mSoundRecordingDialog;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_convenient_service_search;
    }


    @Override
    protected void init() {
        super.init();
        setCenterTitle("便捷服务");
        setContentBackgroundColor(Color.TRANSPARENT);
        initPoiSearch();
        initList();

        mSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    mList.clear();
                    mData.clear();
                    mAdapter.notifyDataSetChanged();
                    return;
                }
                // 搜索内容

                mCitySearchOption.keyword(s.toString());
                mPoiSearch.searchInCity(mCitySearchOption);


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        Intent intent = getIntent();
        if (intent != null) {
            boolean isStartVoice = intent.getBooleanExtra("isStartVoice", false);
            if (isStartVoice) {
                initVoicePermission();
            }
        }


    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }

    /**
     * 初始化poi搜索
     */
    private void initPoiSearch() {
        mPoiSearch = PoiSearch.newInstance();
        mCitySearchOption = new PoiCitySearchOption();
        mCitySearchOption.isReturnAddr(true);

        mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult result) {
                mData.clear();
                mList.clear();
                // 搜索内容为空
                if (TextUtils.isEmpty(mSearchView.getText().toString())) {
                    mAdapter.notifyDataSetChanged();
                    return;
                }
                //详情检索失败
                if (result.error != SearchResult.ERRORNO.NO_ERROR) {
                    mAdapter.notifyDataSetChanged();
                    return;
                }


                List<PoiInfo> list = result.getAllPoi();
                if (list == null || list.size() == 0) {
                    return;
                }

                mList.addAll(list);

                for (PoiInfo info : list) {
                    if (info.location == null || TextUtils.isEmpty(info.name)) {

                        continue;
                    }
                    mList.add(info);
                    Map<String, String> map = new HashMap<>();
                    map.put(NAME, info.name);
                    map.put(ADDRESS, info.address);
                    mData.add(map);
                }
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult result) {
            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult result) {
            }
        });


        if (!PermissionsUtil.isLocationPermissions(this) ||
                (BaiduLocationService.getLongitude() == 0 &&
                        BaiduLocationService.getLatitude() == 0)
                || TextUtils.isEmpty(BaiduLocationService.getCity())
                ) {
            mCitySearchOption.city("温州市");
        } else {
            mCitySearchOption.city(BaiduLocationService.getCity());
        }

    }

    /**
     * 开启语音
     */
    @OnClick(R.id.aty_convenient_service_map_search_image_voice)
    void onOpenVoice() {
        initVoicePermission();
    }


    /**
     * 初始化语音权限
     */
    private void initVoicePermission() {
        boolean isPermission = PermissionsUtil.lacksPermissions(getThis(),
                Manifest.permission.RECORD_AUDIO);

        if (!isPermission) {
            showVoiceDialog();
            return;
        }

        PermissionsUtil.startActivityForResult(getThis(), 1, Manifest.permission.RECORD_AUDIO);
    }


    /**
     * 显示语音对话框
     */
    private void showVoiceDialog() {
        if (mSoundRecordingDialog == null) {
            mSoundRecordingDialog = new SoundRecordingDialog(getThis(), R.style.NoTitleDialog);
            mSoundRecordingDialog.setOnVoiceListener(new SoundRecordingDialog.OnVoiceListener() {
                @Override
                public void onStartSpeech() {

                }

                @Override
                public void onEndSpeech(String content) {
                    if (TextUtils.isEmpty(content)) {
                        showToast("没有检测到声音");
                        return;
                    }
                    mSearchView.setText(content);
                    mSearchView.setSelection(content.length());
                    mSoundRecordingDialog.dismiss();
                }

                @Override
                public void onError(int errorCode, String errorMessage) {
//                    showToast(errorCode + ": " + errorMessage);
                    if (errorCode == 20006) {
                        showToast("无法获取声音，请检查是否开启录音权限");
                    }
                    LogUtil.d("onError: " + errorCode + ": " + errorMessage);
                    mSoundRecordingDialog.dismiss();
                }
            });
            mSoundRecordingDialog.setNegativeButton_(null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }

        mSoundRecordingDialog.show();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }


    private void initList() {
        mAdapter = new SimpleAdapter(this, mData, R.layout.item_convenient_service_search,
                new String[]{NAME, ADDRESS},
                new int[]{R.id.item_convenient_service_search_text_name, R.id.item_convenient_service_search_text_address});
        mListView.setAdapter(mAdapter);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PoiInfo poiInfo = mList.get(position);
                ConvenientServiceActivity.start(getThis(), poiInfo);
                mSearchView.setText(poiInfo.name);

            }
        });

    }


    @Override
    protected void onDestroy() {
        mPoiSearch.destroy();
        super.onDestroy();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (!PermissionsUtil.lacksPermissions(getThis(), permissions)) {
                showVoiceDialog();
            } else {
                showToast("请开启录音权限");
            }
        }
    }

    /**
     * 打开语音
     */
    public static void startVoice(Context context) {
        Intent starter = new Intent(context, ConvenientServiceSearchActivity.class);
        starter.putExtra("isStartVoice", true);
        context.startActivity(starter);
    }
}
