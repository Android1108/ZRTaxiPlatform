package org.wzeiri.zr.zrtaxiplatform.widget.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.SelectListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 底部弹出dialog
 *
 * @author k-lm on 2017/12/9.
 */

public class BottomDialog extends BottomSheetDialog {

    private BaseAdapter mAdapter;
    private ListView mListView;
    private List<String> mDataList = new ArrayList<>();
    private boolean isUserSimpleAdapter = true;

    private OnItemLongClickListener mItemLongClickListener;
    private OnItemClickListener mItemClickListener;

    public BottomDialog(@NonNull Context context) {
        super(context);
        init();
    }

    public BottomDialog(@NonNull Context context, int theme) {
        super(context, theme);
        init();
    }

    protected BottomDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }


    private void init() {
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_bottom, null);
        mListView = view.findViewById(R.id.dialog_bottom_select_list);

        mAdapter = getAdapter();
        mListView.setAdapter(mAdapter);

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (mItemLongClickListener == null) {
                    return false;
                }
                return mItemLongClickListener.onItemLongClick(BottomDialog.this, position);
            }
        });


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(BottomDialog.this, position);
                }
                dismiss();
            }
        });

        View clear = view.findViewById(R.id.dialog_bottom_text_clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        setContentView(view);
    }

    /**
     * 返回adapter
     *
     * @return
     */
    private SelectListAdapter getAdapter() {
        isUserSimpleAdapter = true;
        mDataList.clear();
        SelectListAdapter adapter = new SelectListAdapter(mDataList);
        return adapter;
    }

    /**
     * 添加数据
     * <p>
     * 在调用{@link #setAdapter(BaseAdapter)}后 {@link #addData(String)} 不再执行
     * </p>
     *
     * @param data
     */
    public void addData(String data) {
        // 设置其他适配器后不再添加数据
        if (!isUserSimpleAdapter) {
            return;
        }

        mDataList.add(data);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 添加数据
     * <p>
     * 在调用{@link #setAdapter(BaseAdapter)}后 {@link #addDataArray(String[])} 不再执行
     * </p>
     *
     * @param datas
     */
    public void addDataArray(String... datas) {
        // 设置其他适配器后不再添加数据
        if (!isUserSimpleAdapter || datas == null || datas.length == 0) {
            return;
        }
        mDataList.addAll(Arrays.asList(datas));
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 添加数据
     * <p>
     * 在调用{@link #setAdapter(BaseAdapter)}后 {@link #addDataList(List)} 不再执行
     * </p>
     *
     * @param datas
     */
    public void addDataList(List<String> datas) {
        // 设置其他适配器后不再添加数据
        if (!isUserSimpleAdapter || datas == null || datas.size() == 0) {
            return;
        }
        mDataList.addAll(datas);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 替换适配器
     * <p>
     * 替换adapter后，数据需要添加 {@link #addData(String)} , {@link #addDataArray(String[])} ,
     * {@link #addDataList(List)} 方法将不再生效
     * </p>
     * <p>
     * 设置为null后adapter将变为默认adapter
     * </p>
     *
     * @param adapter adapter
     */
    public void setAdapter(BaseAdapter adapter) {
        // 使用其他的适配器后设置为null 则显示默认的适配器
        if (adapter == null && !isUserSimpleAdapter) {
            mAdapter = getAdapter();
            mListView.setAdapter(mAdapter);
            return;
        }

        // 设置适配器
        if (adapter != null) {
            mAdapter = adapter;
            isUserSimpleAdapter = false;
            mDataList.clear();
            mListView.setAdapter(mAdapter);
        }
    }

    /**
     * 返回数据
     *
     * @param position 位置
     * @return data
     */
    public String getData(int position) {
        if (!isUserSimpleAdapter) {
            return null;
        }

        return mDataList.get(position);
    }

    /**
     * 设置当前选择的位置
     *
     * @param position
     */
    public void setSelectPosition(int position) {
        if (mAdapter instanceof SelectListAdapter) {
            ((SelectListAdapter) mAdapter).setSelectPosition(position);
        }
    }

    /**
     * 设置当前选择的位置
     *
     * @param date
     */
    public void setSelectDate(String date) {
        if (mAdapter instanceof SelectListAdapter) {
            int count = mDataList.size();
            for (int i = 0; i < count; i++) {
                String str = mDataList.get(i);

                if (TextUtils.equals(date, str)) {
                    ((SelectListAdapter) mAdapter).setSelectPosition(i);
                    return;
                }
            }
        }
    }


    /**
     * 设置item点击事件
     *
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    /**
     * 设置item长按事件
     *
     * @param listener
     */
    public void setOnLongClickListener(OnItemLongClickListener listener) {
        mItemLongClickListener = listener;
    }


    public interface OnItemClickListener {
        void onItemClick(BottomDialog dialog, int position);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(BottomDialog dialog, int position);
    }


}
