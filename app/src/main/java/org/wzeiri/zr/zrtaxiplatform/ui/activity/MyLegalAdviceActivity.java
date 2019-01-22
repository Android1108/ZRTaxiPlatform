package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;

import butterknife.BindView;

/**
 * 法律咨询 - 咨询
 *
 * @author k-lm on 2017/12/6.
 */

public class MyLegalAdviceActivity extends ActionbarActivity {
    @BindView(R.id.aty_legal_advice_edit_content)
    EditText mEditContent;
    @BindView(R.id.aty_legal_advice_vtv_type)
    ValueTextView mVtvType;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_legal_advice;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        TextView textView = new TextView(getThis());
        getMenuInflater().inflate(R.menu.menu_blank, menu);
        menu.findItem(R.id.menu_blank).setActionView(textView);
        textView.setText("咨询回复");
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_small));
        textView.setTextColor(ContextCompat.getColor(this, R.color.black70));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MyLegalAdviceReplyActivity.class);
            }
        });


        return true;
    }


    @Override
    protected void init() {
        super.init();
        setCenterTitle("法律咨询");
    }
}
