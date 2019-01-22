package org.wzeiri.zr.zrtaxiplatform.network.api;

import org.wzeiri.zr.zrtaxiplatform.bean.DriverInteractionPostBean;
import org.wzeiri.zr.zrtaxiplatform.bean.ForumSectionBean;
import org.wzeiri.zr.zrtaxiplatform.bean.PostDetailBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseListBean;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author k-lm on 2017/12/12.
 */

public interface IPost {
    /**
     * GET /Api/App/Post/GetForumSections
     * 获取论坛板块
     *
     * @return
     */
    @GET("/Api/App/Post/GetForumSections")
    Call<BaseBean<List<ForumSectionBean>>> getForumSections();

    /**
     * GET /Api/App/Post/GetPosts
     * 获取全部司机互动列表
     *
     * @param sectionId 板块Id
     * @param pageSize
     * @param page
     * @return
     */
    @GET("/Api/App/Post/GetPosts")
    Call<BaseBean<BaseListBean<DriverInteractionPostBean>>> getPosts(@Query("SectionId") int sectionId,
                                                                     @Query("PageSize") int pageSize,
                                                                     @Query("Page") int page);

    /**
     * GET /Api/App/Post/GetMyPosts
     * 获取我的司机互动
     *
     * @param pageSize
     * @param page
     * @return
     */
    @GET("/Api/App/Post/GetMyPosts")
    Call<BaseBean<BaseListBean<DriverInteractionPostBean>>> getMyPosts(@Query("PageSize") int pageSize,
                                                                       @Query("Page") int page);

    /**
     * POST /Api/App/Post/CreatePost
     * 提交司机互动
     *
     * @param body sectionId (integer, optional): 所属板块Id ,
     *             title (string, optional): 标题 ,
     *             content (string, optional): 帖子内容 ,
     *             pictures (Array[string], optional): 上传的图片
     * @return
     */
    @POST("/Api/App/Post/CreatePost")
    Call<BaseBean<String>> createPost(@Body RequestBody body);

    /**
     * GET /Api/App/Post/GetPostDetail
     * 获取司机互动详情
     *
     * @param id
     * @return
     */
    @GET("/Api/App/Post/GetPostDetail")
    Call<BaseBean<PostDetailBean>> getPostDetail(@Query("Id") long id);

    /**
     * POST /Api/App/Post/PostGreate
     * 帖子点赞
     *
     * @param body id (integer, optional)
     * @return
     */
    @POST("/Api/App/Post/PostGreate")
    Call<BaseBean<String>> postGreate(@Body RequestBody body);

    /**
     * POST /Api/App/Post/CreatePostComment
     * 帖子评论
     *
     * @param body postId (integer, optional): 帖子Id ,
     *             content (string, optional): 评论内容
     * @return
     */
    @POST("/Api/App/Post/CreatePostComment")
    Call<BaseBean<String>> createPostComment(@Body RequestBody body);

    /**
     * POST /Api/App/Post/DeletePost
     * 删除自己发布的某个帖子
     *
     * @param body id (integer, optional)
     * @return
     */
    @POST("/Api/App/Post/DeletePost")
    Call<BaseBean<String>> deletePost(@Body RequestBody body);

    /**
     * POST /Api/App/Post/UpdatePost
     * 编辑司机互动
     *
     * @param body title (string, optional): 标题 ,
     *             sectionId (integer, optional): 所属板块 ,
     *             content (string, optional): 内容 ,
     *             pictures (Array[string], optional): 新增的帖子图片 ,
     *             deletePictures (Array[integer], optional): 被删除的帖子的图片Id集合 ,
     *             id (integer, optional)
     * @return
     */
    @POST("/Api/App/Post/UpdatePost")
    Call<BaseBean<String>> updatePost(@Body RequestBody body);
}
