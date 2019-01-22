package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.BaseRecyclerAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.FunctionMenuBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * @author k-lm on 2017/11/14.
 */

public class FragmentFunctionMenuAdapter extends BaseRecyclerAdapter<FunctionMenuBean, FragmentFunctionMenuAdapter.ViewHolder> {


    public FragmentFunctionMenuAdapter(List<FunctionMenuBean> list) {
        super(list);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, FunctionMenuBean data, int position) {
        holder.mText.setCompoundDrawablesWithIntrinsicBounds(0, data.getImageId(), 0, 0);
        holder.mText.setText(data.getTitle());

    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_fragment_function_menu;
    }

    @Override
    public ViewHolder getViewHolder(View view, int viewType) {
        return new ViewHolder(view);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_fragment_function_menu_text)
        TextView mText;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
