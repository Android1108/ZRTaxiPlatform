package org.wzeiri.zr.zrtaxiplatform.network.api;

import org.wzeiri.zr.zrtaxiplatform.bean.AnnouncementDetailBean;
import org.wzeiri.zr.zrtaxiplatform.bean.ArticleDetailBean;
import org.wzeiri.zr.zrtaxiplatform.bean.ArticleTypeBean;
import org.wzeiri.zr.zrtaxiplatform.bean.CivilizationArticleBean;
import org.wzeiri.zr.zrtaxiplatform.bean.TrainingBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseListBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author k-lm on 2017/12/28.
 */

public interface IArticle {
    /**
     * GET /Api/App/Article/GetArticleCategories
     * 获取运管专区,交警专区,公司专区中的分类
     *
     * @param type 类型,1-运管,2-交警,4-公司
     * @return
     */
    @GET("/Api/App/Article/GetArticleCategories")
    Call<BaseBean<List<ArticleTypeBean>>> getArticleCategories(@Query("ArticleSourceType") int type);

    /**
     * GET /Api/App/Article/GetTrainings
     * 获取运管专区,交警专区,公司专区中的培训公告分页数据
     *
     * @param type     类型,1-运管,2-交警,4-公司
     * @param pageSize
     * @param page
     * @return
     */
    @GET("/Api/App/Article/GetTrainings")
    Call<BaseBean<BaseListBean<TrainingBean>>> getTrainings(@Query("AnnouncementType") int type,
                                                            @Query("PageSize") int pageSize,
                                                            @Query("Page") int page);

    /**
     * GET /Api/App/Article/GetDiscoveryAnnouncements
     * 获取发现中全部的司机培训
     *
     * @param pageSize
     * @param page
     * @return
     */
    @GET("/Api/App/Article/GetDiscoveryAnnouncements")
    Call<BaseBean<BaseListBean<TrainingBean>>> getDiscoveryAnnouncements(
            @Query("PageSize") int pageSize,
            @Query("Page") int page);

    /**
     * /Api/App/Article/GetArticles
     * 获取运管专区,交警专区,公司专区中的其他文章类的数据
     *
     * @param id
     * @param pageSize
     * @param page
     * @return
     */
    @GET("/Api/App/Article/GetArticles")
    Call<BaseBean<BaseListBean<CivilizationArticleBean>>> getArticles(@Query("ArticleCategoryId") int id,
                                                                      @Query("PageSize") int pageSize,
                                                                      @Query("Page") int page);

    /**
     * /Api/App/Article/GetCivilizationArticles
     * 获取文明创建的文章
     *
     * @param pageSize
     * @param page
     * @return
     */
    @GET("/Api/App/Article/GetCivilizationArticles")
    Call<BaseBean<BaseListBean<CivilizationArticleBean>>> getCivilizationArticles(@Query("PageSize") int pageSize,
                                                                                  @Query("Page") int page);

    /**
     * GET /Api/App/Article/GetAnnouncementDetail
     * 培训公告,通知公告的详情
     *
     * @return
     */
    @GET("/Api/App/Article/GetAnnouncementDetail")
    Call<BaseBean<AnnouncementDetailBean>> getAnnouncementDetail(@Query("Id") int id);

    /**
     * GET /Api/App/Article/GetArticleDetail
     * 获取文章详情,运管,交警,公司专区中非培训公告的,其他分类的文章的详情, 文明创建中文章的详情
     *
     * @param id
     * @return
     */
    @GET("/Api/App/Article/GetArticleDetail")
    Call<BaseBean<ArticleDetailBean>> getArticleDetail(@Query("Id") int id);

    /**
     * GET /Api/App/Article/GetDiscoveryArticles
     * 获取发现当中,前三个分类的文章
     *
     * @param type     发现类型,1-运管动态,2-公司动态,3-美丽温州
     * @param pageSize
     * @param page
     * @return
     */
    @GET("/Api/App/Article/GetDiscoveryArticles")
    Call<BaseBean<BaseListBean<CivilizationArticleBean>>> getDiscoveryArticles(@Query("DiscoveryType") int type,
                                                                               @Query("PageSize") int pageSize,
                                                                               @Query("Page") int page);


}
