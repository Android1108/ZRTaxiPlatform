package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.FragmentFunctionMenuAdapter;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.BaseRecyclerAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.FunctionMenuBean;
import org.wzeiri.zr.zrtaxiplatform.recycler.decoration.DividerGridItemDecoration;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 司机服务
 *
 * @author k-lm on 2018/1/26.
 */

public class DriverServiceActivity extends ActionbarActivity {
    @BindView(R.id.layout_recycler_view)
    RecyclerView mRecyclerView;

    public static final int REQUEST_CODE = 30001;

    private FragmentFunctionMenuAdapter mAdapter;

    private List<FunctionMenuBean> mMenuBeanList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.layout_recycler_view;
    }


    @Override
    protected void init() {
        super.init();
        setCenterTitle("司机服务");
        setContentBackgroundColor(Color.WHITE);
        int padding = getResources().getDimensionPixelOffset(R.dimen.layout_margin);
        mRecyclerView.setPadding(0, padding, 0, 0);
        initMenu();
    }

    /**
     * 初始化功能菜单
     */
    private void initMenu() {
        int count = 3;
        GridLayoutManager manager = new GridLayoutManager(getThis(),
                count,
                GridLayoutManager.VERTICAL,
                false);


        mRecyclerView.setLayoutManager(manager);

        mAdapter = new FragmentFunctionMenuAdapter(mMenuBeanList);
        mRecyclerView.setAdapter(mAdapter);

        DividerGridItemDecoration decoration = new DividerGridItemDecoration(DividerGridItemDecoration.HORIZONTAL);
        decoration.setHorizontalmDividerHeight(getResources().getDimensionPixelOffset(R.dimen.layout_margin_tiny));

        mRecyclerView.addItemDecoration(decoration);


        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case 0:
                        //求职
                        startActivityForResult(JobHuntingActivity.class, ReleaseCandidateInfoActivity.REQUEST_CODE);
                        break;
                    case 1:
                        //车辆交易
                        startActivity(VehicleTransactionActivity.class);
                        break;
                    case 2:
                        //司机互动
                        startActivityForResult(DriverInteractionActivity.class,
                                ReleaseDriverInteractionActivity.REQUEST_CODE);
                        break;
                    case 3:
                        //法律咨询
                        startActivityForResult(LegalAdviceActivity.class, LegalAdviceActivity.REQUEST_CODE);
                        break;
                }
            }
        });

        String[] titles = new String[]{"招聘信息", "车辆交易", "司机之家", "法律咨询"};
        int[] imageIds = new int[]{
                R.drawable.ic_recruitment_information, R.drawable.ic_vehicle_resale, R.drawable.ic_driver_interaction,
                R.drawable.ic_legal_advice};

        for (int i = 0; i < titles.length; i++) {
            FunctionMenuBean bean = new FunctionMenuBean();
            bean.setImageId(imageIds[i]);
            bean.setTitle(titles[i]);
            mMenuBeanList.add(bean);
        }
        mAdapter.notifyDataSetChanged();

    }

    /**
     * 返回上一个页面返回的RequestCode
     *
     * @return 返回上一个页面返回的RequestCode
     */
    public static int getRequestCode(Intent data) {
        if (data == null) {
            return -1;
        }
        return data.getIntExtra("requestCode", -1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (data == null) {
            data = new Intent();
        }
        data.putExtra("requestCode", requestCode);
        setResult(RESULT_OK, data);
        finish();
    }

}
