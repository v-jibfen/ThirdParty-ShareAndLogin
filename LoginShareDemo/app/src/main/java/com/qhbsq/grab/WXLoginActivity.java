package com.qhbsq.grab;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qhbsq.grab.share.ShareRequest;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.qhbsq.grab.Config.BUNDLE_SHARE_REQUEST;
import static com.qhbsq.grab.Config.BUNDLE_WX_SCENE;
import static com.qhbsq.grab.Config.BUNDLE_WX_TYPE;

/**
 * Created by MSI05 on 2017/9/1.
 */

public class WXLoginActivity extends BaseActivity{

    private IWXAPI api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
        api = WXAPIFactory.createWXAPI(this, Config.WEICHAT_APP_ID);
        api.registerApp(Config.WEICHAT_APP_ID);

        login();
    }

    private void login() {
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "none";
        api.sendReq(req);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handlerLoginResult(String status) {

        switch (status) {
            case "0":
                setResult(Config.ACTIVITY_RESULT_LOGIN_WX_SUCCESS);
                break;
            case "1":
                setResult(Config.ACTIVITY_RESULT_LOGIN_WX_CANCEL);
                break;
            case "2":
                setResult(Config.ACTIVITY_RESULT_LOGIN_WX_FAIL);
                break;
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
