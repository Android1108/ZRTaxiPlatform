package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.WidgetActivity;
import org.wzeiri.zr.zrtaxiplatform.util.GlideUtil;

import butterknife.BindView;

/**
 * 查看大图
 *
 * @author k-lm on 2017/12/9.
 */

public class ImageZoomActivity extends ActionbarActivity {
    @BindView(R.id.aty_image_zoom_pv_photo)
    PhotoView mPhotoView;

    public static final String TYPE_URL = "url";
    public static final String TYPE_PATH = "path";
    public static final String TYPE_URI = "uri";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_image_zoom;
    }


    @Override
    protected void init() {
        super.init();

        Intent intent = getIntent();

        if (intent == null) {
            finish();
            return;
        }

        String type = intent.getStringExtra("type");
        Object object = null;
        if (TextUtils.equals(TYPE_URL, type)) {
            String url = intent.getStringExtra("date");
            object = url;


        } else if (TextUtils.equals(TYPE_URI, type)) {
            Uri uri = intent.getParcelableExtra("date");
            object = uri;


        } else if (TextUtils.equals(TYPE_PATH, type)) {
            String path = intent.getStringExtra("date");
            object = path;
        }

        if (object == null) {
            finish();
            return;
        }
        Glide.with(getThis())
                .load(object)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(mPhotoView);


    }


    public static void startUrl(Activity activity, View view, String url) {
        Intent starter = new Intent(activity, ImageZoomActivity.class);
        starter.putExtra("date", url);
        starter.putExtra("type", TYPE_URL);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.startActivity(starter, ActivityOptions.makeSceneTransitionAnimation(activity, view, "zoomImage").toBundle());
        } else {
            activity.startActivity(starter);
        }
    }


    public static void startPath(Activity activity, View view, String path) {
        Intent starter = new Intent(activity, ImageZoomActivity.class);
        starter.putExtra("date", path);
        starter.putExtra("type", TYPE_PATH);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.startActivity(starter, ActivityOptions.makeSceneTransitionAnimation(activity, view, "zoomImage").toBundle());
        } else {
            activity.startActivity(starter);
        }
    }

    public static void startUri(Activity activity, View view, Uri uri) {
        Intent starter = new Intent(activity, ImageZoomActivity.class);
        starter.putExtra("date", uri);
        starter.putExtra("type", TYPE_URI);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.startActivity(starter, ActivityOptions.makeSceneTransitionAnimation(activity, view, "zoomImage").toBundle());
        } else {
            activity.startActivity(starter);
        }
    }


    public static void startUrl(Context context, String url) {
        Intent starter = new Intent(context, ImageZoomActivity.class);
        starter.putExtra("date", url);
        starter.putExtra("type", TYPE_URL);

        context.startActivity(starter);
    }


    public static void startPath(Context context, String path) {
        Intent starter = new Intent(context, ImageZoomActivity.class);
        starter.putExtra("date", path);
        starter.putExtra("type", TYPE_PATH);
        context.startActivity(starter);
    }

    public static void startUri(Context context, Uri uri) {
        Intent starter = new Intent(context, ImageZoomActivity.class);
        starter.putExtra("date", uri);
        starter.putExtra("type", TYPE_URI);

        context.startActivity(starter);
    }


}
