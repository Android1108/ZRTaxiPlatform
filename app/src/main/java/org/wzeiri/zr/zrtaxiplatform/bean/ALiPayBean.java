package org.wzeiri.zr.zrtaxiplatform.bean;

/**
 * @author k-lm on 2018/1/18.
 */

public class ALiPayBean {


    /**
     * result : &app_id=2017120600417439&biz_content=%7B%22body%22%3A%22%E5%B0%8A%E5%AE%B9%E6%B2%B9%E5%8D%A1%E5%85%85%E5%80%BC%22%2C%22out_trade_no%22%3A%225a6031f09bd1ba06048545b8%22%2C%22product_code%22%3A%22+QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E6%B2%B9%E5%8D%A1%E5%85%85%E5%80%BC%22%2C%22total_amount%22%3A%2299.00%22%7D&charset=utf-8&format=JSON&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fadmin.daxiapp.cn%2FApi%2FApp%2FAlipay%2FNotify&sign=Hro3gLaNixlde6zIMj1YhrHlOtzcecJDa3XdknZ2%2FBER%2BH7aPJgwgnSQN%2FAUD9R8MlChlBH81hciNSuysIHZ4jkSqtxlStS7SGwSHU9Lb1ByikCG6wixSySG%2B5CIAghfHeGVwQy6U%2BPpMxUMt6u5ZDpxIM7eP5kamq4y2D9yuqd8RIWlHAfuPuePZFe60YQpLaksOu7F4bitclikDwci6EIhZKSijAKM3hfGUIwXyQpwfeSBpxMNR6xwcod4WveqikTw2j7UceRKv8Gy6xQ3rpaj6S2fb1%2F3X6fwTa6Cd%2Bx%2FDmy%2BtxuhydmK095QPACbkZrLadJhZmWTHK5R%2BaS2Qw%3D%3D&sign_type=RSA2&timestamp=2018-01-18+13%3A34%3A40&version=1.0
     * id : 1
     * exception : null
     * status : 5
     * isCanceled : false
     * isCompleted : true
     * isCompletedSuccessfully : true
     * creationOptions : 0
     * asyncState : null
     * isFaulted : false
     */

    private String result;
    private int id;
    private int status;
    private boolean isCanceled;
    private boolean isCompleted;
    private boolean isCompletedSuccessfully;
    private int creationOptions;
    private boolean isFaulted;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isIsCanceled() {
        return isCanceled;
    }

    public void setIsCanceled(boolean isCanceled) {
        this.isCanceled = isCanceled;
    }

    public boolean isIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public boolean isIsCompletedSuccessfully() {
        return isCompletedSuccessfully;
    }

    public void setIsCompletedSuccessfully(boolean isCompletedSuccessfully) {
        this.isCompletedSuccessfully = isCompletedSuccessfully;
    }

    public int getCreationOptions() {
        return creationOptions;
    }

    public void setCreationOptions(int creationOptions) {
        this.creationOptions = creationOptions;
    }


    public boolean isIsFaulted() {
        return isFaulted;
    }

    public void setIsFaulted(boolean isFaulted) {
        this.isFaulted = isFaulted;
    }
}
