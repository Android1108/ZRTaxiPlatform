package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IUser;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItem;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueEditText;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 修改详细地址
 *
 * @author k-lm on 2017/12/21.
 */

public class ChangeAddressActivity extends ActionbarActivity {
    @BindView(R.id.aty_change_address_edit_address)
    EditText mVetAddress;

    public static int REQUEST_CODE = 10014;

    public static final String KEY_ADDRESS = "address";

    private IUser mIUser;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_address;
    }


    @Override
    protected void init() {
        super.init();
        setCenterTitle("个人信息");
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        String address = intent.getStringExtra(KEY_ADDRESS);
        mVetAddress.setText(address);
    }

    @OnClick(R.id.aty_change_address_text_submit)
    public void onViewClicked() {
        final String address = mVetAddress.getText().toString();
        if (TextUtils.isEmpty(address)) {
            showToast("请输入详细地址");
            return;
        }

        if (mIUser == null) {
            mIUser = RetrofitHelper.create(IUser.class);
        }

        mIUser.updateUserAddress(RetrofitHelper
                .getBody(new JsonItem("address", address)))
                .enqueue(new MsgCallBack<BaseBean<String>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                        showToast("修改成功");
                        Intent intent = new Intent();
                        intent.putExtra(KEY_ADDRESS, address);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
    }


    public static void start(Activity activity, String address) {
        Intent starter = new Intent(activity, ChangeAddressActivity.class);
        starter.putExtra(KEY_ADDRESS, address);
        activity.startActivityForResult(starter, REQUEST_CODE);
    }
}
