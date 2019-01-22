package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.BaseListAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author k-lm on 2017/12/18.
 */

public class SelectListAdapter extends BaseListAdapter<String> {

    private int mSelectPosition = -1;

    public SelectListAdapter(List<String> list) {
        super(list);

    }

    @Override
    public void getItemView(int position, View convertView, ViewGroup parent, int viewType) {
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolder(convertView);
        }

        String content = getData(position);
        viewHolder.mContent.setText(content);

        if (mSelectPosition == position) {

        }
    }

    /**
     * 设置当前选择的位置
     *
     * @param position
     */
    public void setSelectPosition(int position) {
        mSelectPosition = position;
    }

    public int getSelectPositon() {
        return mSelectPosition;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_text_view;
    }

    static class ViewHolder {
        @BindView(R.id.item_text_view_content)
        TextView mContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }
    }
}
