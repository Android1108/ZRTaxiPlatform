package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.ImageZoomActivity;
import org.wzeiri.zr.zrtaxiplatform.util.GlideUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author k-lm on 2017/12/19.
 */

public class ImagePagerAdapter extends PagerAdapter {
    private List<String> mUrlList;

    public ImagePagerAdapter(List<String> list) {
        mUrlList = list;
        if (mUrlList == null) {
            mUrlList = new ArrayList<>();
        }
    }

    @Override
    public int getCount() {
        return mUrlList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final String path = mUrlList.get(position);
        ImageView imageView = (ImageView) LayoutInflater.from(container.getContext())
                .inflate(R.layout.item_image_view, container, false);
        GlideUtil.loadPath(container.getContext(), imageView, path);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageZoomActivity.startPath(v.getContext(), path);
            }
        });

        return imageView;
    }
}
