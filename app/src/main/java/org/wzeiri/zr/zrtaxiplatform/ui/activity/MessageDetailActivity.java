package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 详细详情
 *
 * @author k-lm on 2018/1/23.
 */

public class MessageDetailActivity extends ActionbarActivity {
    @BindView(R.id.aty_message_detail_text_title)
    TextView mTextTitle;
    @BindView(R.id.aty_message_detail_text_content)
    TextView mTextContent;

    private String mTitle;
    private String mContent;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_message_detail;
    }

    @Override
    protected void create() {
        super.create();

        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        mTitle = intent.getStringExtra("title");
        mContent = intent.getStringExtra("content");

    }

    @Override
    protected void init() {
        super.init();
        setContentBackgroundColor(ContextCompat.getColor(getThis(), R.color.white));
        setCenterTitle("详细详情");

        mTextContent.setText(mContent);
        mTextTitle.setText(mTitle);
    }


    public static void start(Context context, String title, String content) {
        Intent starter = new Intent(context, MessageDetailActivity.class);
        starter.putExtra("title", title);
        starter.putExtra("content", content);
        context.startActivity(starter);
    }

}
