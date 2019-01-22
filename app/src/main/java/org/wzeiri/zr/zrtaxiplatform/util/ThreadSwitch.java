package org.wzeiri.zr.zrtaxiplatform.util;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Queue;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * /**
 * <p>线程切换工具类.</p>
 * <p>
 * <p>Example:</p>
 * <pre>
 *     ThreadSwitch.createTask(new ThreadSwitch.OnCreateListener<String>() {
 *          &#064; @Override
 *           public String onCreate(ThreadSwitch threadSwitch) throws InterruptedException {
 *               return "Hello ";
 *               }
 *           })
 *                   .addTaskListener(new ThreadSwitch.OnTaskListener<String, String>() {
 *                     &#064;  @Override
 *                           public String onNext(ThreadSwitch threadSwitch, String value) {
 *                                 return value + "Word";
 *                           }
 *           })
 *                   .switchLooper(ThreadSwitch.MAIN_THREAD)
 *                   .submit(new ThreadSwitch.OnSubmitListener<String>() {
 *                     &#064;  @Override
 *                       public void onSubmit(ThreadSwitch threadSwitch, String value) {
 *                                 Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
 *                           }
 *           });
 *
 *
 * </pre>
 *
 * @author klm on 2017/8/5.
 */

public class ThreadSwitch {
    @IntDef({MAIN_THREAD, IO_THREAD})
    @Retention(RetentionPolicy.SOURCE)
    private @interface ThreadLooper {
    }

    /**
     * 线程类型 主线程
     */
    public static final int MAIN_THREAD = 1;
    /**
     * 线程类型 子线程
     */
    public static final int IO_THREAD = 2;
    /**
     * 执行子线程任务线程池
     */
    private static ExecutorService mExecutorService;
    /**
     * 根据任务id保存任务
     */
    private static SparseArray<ThreadSwitch> mThreadQueueArray;
    /**
     * 任务id ，每次每次创建完后都会加1
     */
    private static int ThreadSwitchId = 1;

    /**
     * 超时时间
     */
    private final static long OVERTIME = 30_000;
    /**
     * 任务队列
     */
    private Queue<BaseTask> mQueueTask;

    /**
     * 当前线程任务id
     */
    private Integer mThreadId = null;
    /**
     * 当前线程
     */
    private int mLooper = MAIN_THREAD;
    /**
     * 是否提交
     */
    private boolean isSubmit = false;
    /**
     * 创建时间
     */
    private long createTime;

    private static OvertimeRunnable mOvertimeRunnable;

    static {
        mThreadQueueArray = new SparseArray<>();
        mExecutorService = Executors.newCachedThreadPool();
    }


    private ThreadSwitch(Integer id, int looper) {
        mThreadId = id;
        createTime = System.currentTimeMillis();
        mQueueTask = new LinkedBlockingQueue<>();
        mLooper = looper;
    }

    /**
     * 创建队列,线程默认为子线程
     *
     * @param listener
     * @return ThreadSwitch
     */
    synchronized public static <R> ThreadSwitch createTask(OnCreateListener<R> listener) {
        return createTask(IO_THREAD, listener);
    }

    /**
     * 创建队列
     *
     * @param looper   线程编号
     * @param listener
     * @return ThreadSwitch
     */
    synchronized public static <R> ThreadSwitch createTask(@ThreadLooper int looper, OnCreateListener<R> listener) {
        if (looper != MAIN_THREAD && looper != IO_THREAD) {
            throw new RuntimeException("任务执行的线程错误");
        }


        ThreadSwitch threadSwitch = new ThreadSwitch(ThreadSwitchId, looper);
        threadSwitch.create(listener);
        mThreadQueueArray.put(ThreadSwitchId, threadSwitch);
        ThreadSwitchId++;
        if (mOvertimeRunnable == null) {
            mOvertimeRunnable = new OvertimeRunnable();
            mExecutorService.execute(mOvertimeRunnable);
        }

        return threadSwitch;
    }

    /**
     * 执行主线程的任务的handler
     */
    public static Handler mThreadHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int threadId = msg.arg1;
            Object value = msg.obj;
            executeTask(threadId, value);
        }

    };

    /**
     * 切换线程
     *
     * @param looper 线程
     * @return 返回自身
     */
    public ThreadSwitch switchLooper(@ThreadLooper int looper) {
        if (looper != MAIN_THREAD && looper != IO_THREAD) {
            throw new RuntimeException("任务执行的线程错误");
        }
        mLooper = looper;
        return this;
    }


    /**
     * 创建任务队列
     *
     * @param listener
     * @return 返回自身
     */
    private <R> ThreadSwitch create(OnCreateListener<R> listener) {
        BaseTask baseTask = getTask();
        baseTask.create(listener);
        mQueueTask.offer(baseTask);
        return this;
    }

    /**
     * 添加任务
     *
     * @param taskListener
     * @param <V>          上一个任务返回的类型
     * @param <R>          需要返回的类型
     * @return 返回自身
     */
    public <V, R> ThreadSwitch addTaskListener(@Nullable OnTaskListener<V, R> taskListener) {
        BaseTask baseTask = getTask();
        baseTask.addTaskListener(taskListener);
        mQueueTask.offer(baseTask);
        return this;
    }

    /**
     * 提交任务
     *
     * @param onSubmit
     * @param <V>      上一个任务返回的类型
     */
    public <V> void submit(@Nullable OnSubmitListener<V> onSubmit) {
        if (mQueueTask == null) {
            throw new RuntimeException("当前任务已关闭无法提交任务");
        }

        if (isSubmit) {
            throw new RuntimeException("已提交当前任务,无法再次提交");
        }
        //onSubmit 不为null则创建新的任务
        BaseTask baseTask;
        if (onSubmit != null) {
            baseTask = getTask();
            baseTask.submit(onSubmit);
            mQueueTask.offer(baseTask);
        } else {
            // onSubmit 为null 则执行最后一个任务
            baseTask = mQueueTask.peek();
        }

        if (baseTask == null) {
            return;
        }

        isSubmit = true;
        // 执行OnCreateListener接口
        baseTask = mQueueTask.peek();
        baseTask.execute(null);
    }

    /**
     * 提交线程
     */
    public void submit() {
        submit(null);
    }


    /**
     * 根据当前线程状态返回 执行任务类型
     *
     * @return 返回任务实例
     */
    private BaseTask getTask() {
        BaseTask baseTask;

        if (mLooper == MAIN_THREAD) {
            baseTask = new MainLoopTask();
        } else {
            baseTask = new ThreadTask();
        }
        return baseTask;
    }


    /**
     * 关闭已创建的队列
     */
    public void close() {
        // 已被关闭
        if (mQueueTask == null) {
            return;
        }
        mQueueTask.clear();
        mThreadQueueArray.remove(mThreadId);
        mQueueTask = null;
        mThreadId = null;
    }

    /**
     * 关闭指定队列
     *
     * @param threadSwitch
     */
    public static void close(ThreadSwitch threadSwitch) {
        if (threadSwitch == null) {
            return;
        }
        threadSwitch.close();
    }


    /**
     * 关闭所有队列
     */
    public static void closeAll() {
        for (int i = 0; i < mThreadQueueArray.size(); i++) {
            ThreadSwitch threadSwitch = mThreadQueueArray.valueAt(i);
            if (threadSwitch == null) {
                continue;
            }

            threadSwitch.close();
            i--;

        }

    }

    /**
     * 执行当前任务
     *
     * @param id    任务id
     * @param value 当前任务需要的参数
     */
    private static void executeTask(int id, Object value) {
        ThreadSwitch threadSwitch = mThreadQueueArray.get(id);

        if (threadSwitch == null ||
                threadSwitch.mQueueTask == null ||
                threadSwitch.mQueueTask.size() == 0) {
            mThreadQueueArray.remove(id);
            return;
        }

        Queue<BaseTask> queue = threadSwitch.mQueueTask;
        BaseTask task = queue.poll();
        if (task == null) {
            mThreadQueueArray.remove(id);
            return;
        }
        Object object = task.submit(value);
        task = queue.peek();

        if (task == null) {
            mThreadQueueArray.remove(id);
            return;
        }

        task.execute(object);
    }

    /**
     * 执行主线程的任务
     */
    private class MainLoopTask extends BaseTask {


        @Override
        protected <V> void execute(V value) {
            Message message = new Message();
            message.arg1 = mThreadId;
            message.obj = value;
            mThreadHandler.sendMessage(message);
        }
    }

    /**
     * 执行子线程的任务
     */
    private class ThreadTask extends BaseTask {

        @Override
        protected <V> void execute(final V value) {
            Runnable runnable = new TimerTask() {
                @Override
                public void run() {
                    executeTask(mThreadId, value);
                }
            };

            mExecutorService.execute(runnable);

        }
    }


    private abstract class BaseTask {

        OnCreateListener mCreateListener;

        OnTaskListener mTaskListener;

        OnSubmitListener mSubmitListener;


        public <R> ThreadSwitch create(OnCreateListener<R> listener) {
            mCreateListener = listener;
            mTaskListener = null;
            mSubmitListener = null;
            return getThis();
        }

        public <V, R> ThreadSwitch addTaskListener(OnTaskListener<V, R> listener) {
            mTaskListener = listener;

            mCreateListener = null;
            mSubmitListener = null;
            return getThis();
        }

        public <V> void submit(OnSubmitListener<V> listener) {
            mSubmitListener = listener;

            mTaskListener = null;
            mCreateListener = null;


        }

        private Object submit(Object value) {
            try {
                if (mCreateListener != null) {
                    return mCreateListener.onCreate(getThis());
                } else if (mTaskListener != null) {
                    return mTaskListener.onNext(getThis(), value);
                } else if (mSubmitListener != null) {
                    mSubmitListener.onSubmit(getThis(), value);
                    return null;
                }
            } catch (Exception e) {
                e.getMessage();
            }

            return null;
        }

        /**
         * 执行任务的方法，需要子类去实现
         *
         * @param value
         * @param <V>
         */
        protected abstract <V> void execute(V value);

    }

    /**
     * 创建一个任务
     *
     * @param <R> 当前任务需要返回的类型
     */
    public interface OnCreateListener<R> {
        R onCreate(ThreadSwitch threadSwitch);
    }

    /**
     * 添加任务接口
     *
     * @param <V> 上一个任务返回的类型
     * @param <R> 当前任务需要返回的类型
     */
    public interface OnTaskListener<V, R> {
        R onNext(ThreadSwitch threadSwitch, V value);

    }


    /**
     * 最终任务接口
     *
     * @param <V> 上一个任务返回的类型
     */

    public interface OnSubmitListener<V> {
        void onSubmit(ThreadSwitch threadSwitch, V value);
    }

    /**
     * 返回自身
     *
     * @return
     */
    private ThreadSwitch getThis() {
        return this;
    }


    @Override
    public int hashCode() {
        return mThreadId;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ThreadSwitch)) {
            return false;
        }
        ThreadSwitch threadSwitch = (ThreadSwitch) obj;
        return !(mThreadId == null || threadSwitch.mThreadId == null) &&
                mThreadId.equals(threadSwitch.mThreadId);

    }

    /**
     * 遍历已创建的任务，任务创建后，在规定时间内没有执行，则删除该任务
     */
    private static class OvertimeRunnable implements Runnable {

        @Override
        public void run() {

            while (true) {
                for (int i = 0; i < mThreadQueueArray.size(); i++) {
                    ThreadSwitch threadSwitch;
                    try {
                        threadSwitch = mThreadQueueArray.valueAt(i);
                    } catch (Exception e) {
                        break;
                    }


                    if (threadSwitch == null) {
                        continue;
                    }
                    // 判断是否超时且没有执行submit()方法
                    if (!threadSwitch.isSubmit && threadSwitch.createTime + OVERTIME < System.currentTimeMillis()) {
                        Log.d("OvertimeRunnable", "已关闭一个未执行的任务");
                        threadSwitch.close();
                        break;
                    }
                }

                try {
                    Thread.sleep(OVERTIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }
    }

}
