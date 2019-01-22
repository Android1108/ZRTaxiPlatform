package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Menu;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.AnswerCheckErrorPagerAdapter;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.util.AnswerHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 查看错题集
 *
 * @author k-lm on 2017/12/8.
 */

public class AnswerCheckErrorActivity extends ActionbarActivity {
    @BindView(R.id.aty_answer_check_error_pager)
    ViewPager mViewPager;

    @BindView(R.id.aty_answer_check_error_text_last_subject)
    TextView mLastSubject;
    @BindView(R.id.aty_answer_check_error_text_next_subject)
    TextView mNextSubject;
    private AnswerCheckErrorPagerAdapter mAdapter;
    private List<Integer> mErrorSubjectNoList;

    private TextView mCurrentSubjectNum;

    private int mCurrentNum = 0;
    /**
     * 错误数
     */
    private int mSubjectNumber = 0;

    @OnClick(R.id.aty_answer_check_error_text_last_subject)
    void onLastSubject() {
        mViewPager.setCurrentItem(mCurrentNum - 1);
    }

    @OnClick(R.id.aty_answer_check_error_text_next_subject)
    void onNextSubject() {
        mViewPager.setCurrentItem(mCurrentNum + 1);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_answer_check_error;
    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("错题集");
        AnswerHelper.SubjectInfoBean bean = AnswerHelper.getInstance().getSubjectInfo();
        mErrorSubjectNoList = bean.getErrorSubjectNo();
        mSubjectNumber = bean.getSubjectNumber();
        mAdapter = new AnswerCheckErrorPagerAdapter(getSupportFragmentManager(), mErrorSubjectNoList);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentNum = position;
                String str = mErrorSubjectNoList.get(position) + "/" + mSubjectNumber;
                mCurrentSubjectNum.setText(str);

                if (position == 0) {
                    mLastSubject.setEnabled(false);
                } else {
                    mLastSubject.setEnabled(true);
                }

                if (position == mErrorSubjectNoList.size()) {
                    mNextSubject.setEnabled(false);
                } else {
                    mNextSubject.setEnabled(true);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setAdapter(mAdapter);


        if (mSubjectNumber == 0) {
            mNextSubject.setEnabled(false);
        } else {
            mNextSubject.setEnabled(true);
        }

        //mViewPager.setCurrentItem(0);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_blank, menu);
        mCurrentSubjectNum = new TextView(this);
        mCurrentSubjectNum.setTextColor(ContextCompat.getColor(this, R.color.black70));
        mCurrentSubjectNum.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_small));
        menu.findItem(R.id.menu_blank).setActionView(mCurrentSubjectNum);


        mCurrentNum = (mErrorSubjectNoList.get(0));
        String str = mCurrentNum + "/" + AnswerHelper.getInstance().getSubjectSize();
        mCurrentNum--;
        mCurrentSubjectNum.setText(str);

        return true;

    }

}
