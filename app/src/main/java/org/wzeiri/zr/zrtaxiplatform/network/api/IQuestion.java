package org.wzeiri.zr.zrtaxiplatform.network.api;

import org.wzeiri.zr.zrtaxiplatform.bean.AnswerResultsBean;
import org.wzeiri.zr.zrtaxiplatform.bean.PracticeQuestionBean;
import org.wzeiri.zr.zrtaxiplatform.bean.PracticeQuestionListBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.LostFoundBean;
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

public interface IQuestion {
    /**
     * GET /Api/App/Question/GetPracticeQuestions
     * 获取练习的题目
     *
     * @param page     当前页数
     * @param pageSize 每页大小
     * @return
     */
    @GET("/Api/App/Question/GetPracticeQuestions")
    Call<BaseBean<BaseListBean<PracticeQuestionBean>>> getPracticeQuestions(@Query("Page") int page,
                                                                            @Query("PageSize") int pageSize);

    /**
     * GET /Api/App/Question/GetPracticeQuestions
     * 获取模拟考试的题目
     *
     * @return
     */
    @GET("/Api/App/Question/GetExamQuestions")
    Call<BaseBean<PracticeQuestionListBean>> getExamQuestions();

    /**
     * POST /Api/App/Question/CreateExamQuiz
     * 创建考试的测试结果,需要提交每个题目的选项,服务器端校验结果
     *
     * @param body
     * @return
     */
    @POST("/Api/App/Question/CreateExamQuiz")
    Call<BaseBean<AnswerResultsBean>> submitExamQuiz(@Body RequestBody body);

    /**
     * POST /Api/App/Question/CreatePracticeQuiz
     * 创建练习结果,客户端已经得到了结果,只是把数据提交给服务器端
     *
     * @param body
     * @return
     */
    @POST("/Api/App/Question/CreatePracticeQuiz")
    Call<BaseBean<String>> submitPracticeQuiz(@Body RequestBody body);


}
