package org.wzeiri.zr.zrtaxiplatform.util;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.provider.LiveFolders.DESCRIPTION;

/**
 * @author k-lm on 2017/12/7.
 */

public class DriverInteractionUtil {

    public static CharSequence getTopTitle(Context context, CharSequence title) {
        SpannableStringBuilder builder = new SpannableStringBuilder("置顶");
        if (title != null) {
            builder.append("  ");
        }

        builder.setSpan(new AbsoluteSizeSpan(DensityUtil.dip2px(context, 15), false),
                0,
                builder.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        builder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.orange1)),
                0,
                builder.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        builder.append(title);
        return builder;
    }


    /**
     * 设置热门标题
     *
     * @param title 原标题
     */
    public static void setHostTitle(Context context, final TextView textView, final CharSequence title) {
        SpannableStringBuilder builder = new SpannableStringBuilder(title);
        if (title != null) {
            builder.append("  ");
        } else {
            return;
        }
        final SpannableString host = new SpannableString("热门");
        host.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.white)),
                0,
                host.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        host.setSpan(new AbsoluteSizeSpan(DensityUtil.dip2px(context, 15), false),
                0,
                host.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        host.setSpan(new BackgroundColorSpan(ContextCompat.getColor(context, R.color.orange1)),
                0,
                host.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        builder.append(host);
        textView.setText(builder);
        textView.setTag(builder);


        int viewWidth = textView.getMeasuredWidth();
        // 如果能获取到宽度，则不需要设置view的绘制回调，直接设置text
        if (viewWidth > 0) {
            setHostTitle(textView, builder, host);
            return;
        }

        textView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                SpannableStringBuilder builder = (SpannableStringBuilder) textView.getTag();
                setHostTitle(textView, builder, host);
                // 移除view的布局回调
                textView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    /**
     * 根据最大行数设置热门信息
     *
     * @param textView
     * @param builder
     * @param host
     */
    private static void setHostTitle(final TextView textView, SpannableStringBuilder builder, SpannableString host) {
        TextPaint paint = textView.getPaint();
        // 获取最大行数
        int maxLines = textView.getMaxLines();
        // 最大行数为Integer.MAX_VALUE 表示没有设置最大行数
        if (maxLines == Integer.MAX_VALUE) {
            textView.setText(builder);
            return;
        }

        if (maxLines == 0) {
            maxLines = 1;
        }
        int textWidth = StringUtil.getTextWidth(paint, builder);
        int viewWidth = textView.getMeasuredWidth();
        if (textWidth <= viewWidth || textWidth / viewWidth < 2) {
            return;
        }

        int hostWidth = StringUtil.getTextWidth(paint, host.toString());
        int intervalWidth = StringUtil.getTextWidth(paint, "  ");

        CharSequence ellipsizeStr = TextUtils.ellipsize(builder, paint, viewWidth * maxLines - hostWidth - intervalWidth, TextUtils.TruncateAt.END);
        builder = new SpannableStringBuilder(ellipsizeStr);
        builder.append("  ");
        builder.append(host);
        textView.setText(builder);
    }


    /**
     * 返回更新时间
     * 1分钟内 返回刚刚</p>
     * 1小时内 返回 mm分钟前</p>
     * 1天内 返回 昨天 hh:mm分钟前</p>
     * 1年内 返回 昨天 MM-DD hh:mm</p>
     * 超出1年 返回 yyyy-MM-DD hh:mm
     *
     * @param date 日期
     * @return 时间
     */
    public static String getUpdateString(Date date) {
        if (date == null) {
            return "刚刚更新";
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        long updateTime = (new Date().getTime() - date.getTime()) / 1000;

        long year = updateTime / (24 * 60 * 60 * 365);
        if (year > 0) {
            return TimeUtil.getServerDateAndTime(date) + "更新";
        }

        long mouth = (updateTime / (24 * 60 * 60 * 30));
        if (mouth > 0) {
            SimpleDateFormat format = new SimpleDateFormat("MM-DD hh:mm", Locale.getDefault());
            return format.format(date) + "更新";
        }

        long day = (updateTime / (24 * 60 * 60));


        if (day > 0) {
            SimpleDateFormat format = new SimpleDateFormat("MM-DD hh:mm", Locale.getDefault());
            return format.format(date) + "更新";
        }
        long hour = updateTime / (60 * 60);

        if (hour > 0) {
            SimpleDateFormat format = new SimpleDateFormat(" hh:mm", Locale.getDefault());
            return format.format(date) + "更新";
        }

        long min = hour / 60;

        if (min > 0) {
            return min + "分钟前更新";
        }

        return "刚刚更新";

    }


}
