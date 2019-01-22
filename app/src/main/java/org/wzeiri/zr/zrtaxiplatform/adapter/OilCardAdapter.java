package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.view.View;
import android.view.ViewGroup;

import org.wzeiri.zr.zrtaxiplatform.adapter.base.BaseListAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.OilCardBean;

import java.util.List;

/**
 * @author k-lm on 2017/12/14.
 */

public class OilCardAdapter extends BaseListAdapter<OilCardBean> {
    public OilCardAdapter(List<OilCardBean> list) {
        super(list);
    }

    @Override
    public void getItemView(int position, View convertView, ViewGroup parent, int viewType) {

    }

    @Override
    public int getItemLayoutId(int viewType) {
        return 0;
    }
}
