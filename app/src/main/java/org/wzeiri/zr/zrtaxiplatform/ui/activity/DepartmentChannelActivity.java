package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.FragmentFunctionMenuAdapter;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.BaseRecyclerAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.FunctionMenuBean;
import org.wzeiri.zr.zrtaxiplatform.recycler.decoration.DividerGridItemDecoration;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.util.UserInfoHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 部门专区
 * @author k-lm on 2018/1/26.
 */

public class DepartmentChannelActivity extends ActionbarActivity {
    @BindView(R.id.layout_recycler_view)
    RecyclerView mRecyclerView;

    private FragmentFunctionMenuAdapter mAdapter;

    private List<FunctionMenuBean> mMenuBeanList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.layout_recycler_view;
    }


    @Override
    protected void init() {
        super.init();
        setCenterTitle("部门专区");
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
                        //运管
                        DepartmentNoticeActivity.start(getThis(), 1);
                        break;
                    case 1:
                        //交警
                        DepartmentNoticeActivity.start(getThis(), 2);
                        break;
                    case 2:
                        // 公司
                        if (!UserInfoHelper.getInstance().isAuthentication(getThis())) {
                            return;
                        }
                        DepartmentNoticeActivity.start(getThis(), 4);
                        break;
                    case 3:
                        //文明创建
                        startActivity(CivilizedSocietyActivity.class);
                        break;
                }
            }
        });

        String[] titles = new String[]{"运管专区", "交警专区", "企业专区","文明创建"};
        int[] imageIds = new int[]{
                R.drawable.ic_transportation_management_zone, R.drawable.ic_traffic_police_zone, R.drawable.ic_company_zone,R.drawable.ic_civilized_society};

        for (int i = 0; i < titles.length; i++) {
            FunctionMenuBean bean = new FunctionMenuBean();
            bean.setImageId(imageIds[i]);
            bean.setTitle(titles[i]);
            mMenuBeanList.add(bean);
        }
        mAdapter.notifyDataSetChanged();

    }
}
