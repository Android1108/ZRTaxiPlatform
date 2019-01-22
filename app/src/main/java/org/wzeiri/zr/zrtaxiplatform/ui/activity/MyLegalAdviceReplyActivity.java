package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.Context;
import android.content.Intent;

import org.wzeiri.zr.zrtaxiplatform.adapter.LegalAdviceReplyPagerAdapter;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.BaseTabLayoutActivity;

/**
 * 查看我的法律咨询
 *
 * @author k-lm on 2017/12/6.
 */

public class MyLegalAdviceReplyActivity extends BaseTabLayoutActivity<LegalAdviceReplyPagerAdapter> {
    @Override
    protected LegalAdviceReplyPagerAdapter getPagerAdapter() {
        return new LegalAdviceReplyPagerAdapter(getSupportFragmentManager());
    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("法律咨询");
        changeTab();
    }

    /**
     * 切换tab页
     */
    private void changeTab() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        boolean isReply = intent.getBooleanExtra("isReply", true);
        if (isReply) {
            return;
        }
        mLayoutPager.setCurrentItem(1);

    }


    /**
     * @param isReply 是否回复
     */
    public static void start(Context context, boolean isReply) {
        Intent starter = new Intent(context, MyLegalAdviceReplyActivity.class);
        starter.putExtra("isReply", isReply);
        context.startActivity(starter);
    }
}
