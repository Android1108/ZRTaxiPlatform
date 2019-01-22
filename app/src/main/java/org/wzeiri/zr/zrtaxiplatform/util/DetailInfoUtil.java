package org.wzeiri.zr.zrtaxiplatform.util;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.ImagePagerAdapter;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.ImageZoomActivity;

import java.util.List;

/**
 * @author k-lm on 2017/12/19.
 */

public class DetailInfoUtil {
    /**
     * 加载详情页的图片
     *
     * @param layout    布局
     * @param imageUrls 图片链接
     */
    public static void loadImages(final LinearLayout layout, List<String> imageUrls) {
        if (layout == null) {
            return;
        }
        if (imageUrls == null || imageUrls.size() == 0) {
            layout.setVisibility(View.GONE);
            return;
        }
        layout.setVisibility(View.VISIBLE);


        for (final String path : imageUrls) {
            ImageView imageView = new ImageView(layout.getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layout.addView(imageView, params);
            params.topMargin = layout.getResources().getDimensionPixelOffset(R.dimen.layout_margin);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageZoomActivity.startPath(layout.getContext(), path);
                }
            });
            GlideUtil.loadPath(layout.getContext(), imageView, path);

        }
    }


    /**
     * 加载详情页的图片
     *
     * @param viewPager
     * @param imageUrls 图片链接
     * @param textView
     */
    public static void loadImages(final List<String> imageUrls, final ViewPager viewPager, final TextView textView) {
        if (viewPager == null || textView == null) {
            return;
        }
        if (imageUrls == null || imageUrls.size() == 0) {
            viewPager.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
            return;
        }
        textView.setVisibility(View.VISIBLE);
        ImagePagerAdapter adapter = new ImagePagerAdapter(imageUrls);
        viewPager.setAdapter(adapter);
        String count = 1 + "/" + imageUrls.size();
        textView.setText(count);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String count = (position + 1) + "/" + imageUrls.size();
                textView.setText(count);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

}
