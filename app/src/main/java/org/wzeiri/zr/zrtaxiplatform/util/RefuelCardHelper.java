package org.wzeiri.zr.zrtaxiplatform.util;

import org.wzeiri.zr.zrtaxiplatform.bean.OilCardBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author k-lm on 2018/1/26.
 */

public class RefuelCardHelper {
    private static final RefuelCardHelper ourInstance = new RefuelCardHelper();

    /**
     * 加油卡页数
     */
    private int mPagerIndex = 1;

    private int mPagerSize = 15;

    private List<OilCardBean> mCardBeanList = new ArrayList<>();

    public static RefuelCardHelper getInstance() {
        return ourInstance;
    }

    private RefuelCardHelper() {

    }

    /**
     * 设置当前加油卡加载的页数
     *
     * @param pager 页数
     */
    public void setCurrentPagerIndex(int pager) {
        mPagerIndex = pager;
    }

    /**
     * 设置每页加载数量
     *
     * @param pagerSize
     */
    public void setPagerSize(int pagerSize) {
        mPagerSize = pagerSize;
    }

    /**
     * 添加加油卡数据
     *
     * @param oilCardBeanList 加油卡数据
     */
    public void addCardDateList(List<OilCardBean> oilCardBeanList) {
        if (oilCardBeanList == null || oilCardBeanList.size() == 0) {
            return;
        }
        mCardBeanList.addAll(oilCardBeanList);
    }

    /**
     * 添加加油卡数据
     *
     * @param oilCardBean 加油卡数据
     */
    public void addCardDate(OilCardBean oilCardBean) {
        if (oilCardBean == null) {
            return;
        }
        mCardBeanList.add(oilCardBean);
    }

    /**
     * 返回页数
     *
     * @return 返回页数
     */
    public int getPagerIndex() {
        return mPagerIndex;
    }

    /**
     * 返回每页加载大小
     *
     * @return
     */
    public int getPagerSize() {
        return mPagerSize;
    }

    /**
     * 返回加油卡数量
     *
     * @return 加油卡数量
     */
    public int getCardDateSize() {
        return mCardBeanList.size();
    }

    /**
     * 返回加油卡数据
     *
     * @return 加油卡数据
     */
    public List<OilCardBean> getCardDateList() {
        return mCardBeanList;
    }

    /**
     * 返回加油卡数据
     *
     * @param position 位置
     * @return 加油卡数据
     */
    public OilCardBean getCardDate(int position) {
        if (position < 0 || position >= mCardBeanList.size()) {
            return null;
        }
        return mCardBeanList.get(position);
    }


    /**
     * 清空加油卡数据
     */
    public void clearCardDate() {
        mPagerIndex = 1;
        mCardBeanList.clear();
    }


}
