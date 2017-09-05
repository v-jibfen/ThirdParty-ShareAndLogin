package com.qhbsq.grab.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.qhbsq.grab.QQEntryActivity;
import com.qhbsq.grab.WXLoginActivity;
import com.qhbsq.grab.WeiboEntryActivity;
import com.qhbsq.grab.share.ShareManager;

import static com.qhbsq.grab.Config.ACTIVITY_REQUEST_LOGIN_QQ;
import static com.qhbsq.grab.Config.ACTIVITY_REQUEST_LOGIN_WB;
import static com.qhbsq.grab.Config.ACTIVITY_REQUEST_LOGIN_WX;
import static com.qhbsq.grab.Config.BUNDLE_QQ_ACTION;
import static com.qhbsq.grab.Config.BUNDLE_WB_ACTION;

/**
 * Created by MSI05 on 2017/9/1.
 */

public class LoginManager {
    private static LoginManager _instance;

    private LoginManager() {

    }

    public static LoginManager getInstance() {
        if (_instance == null) {
            _instance = new LoginManager();
        }

        return _instance;
    }

    public static void login(Activity activity, LOGIN_TYPE login_type) {
        Intent intent = new Intent();

        Bundle bundle = new Bundle();
        switch (login_type){
            case WX_LOGIN:
                intent.setClass(activity, WXLoginActivity.class);
                activity.startActivityForResult(intent, ACTIVITY_REQUEST_LOGIN_WX);
                break;
            case WEIBO_LOGIN:

                bundle.putInt(BUNDLE_WB_ACTION, 1);
                intent.setClass(activity, WeiboEntryActivity.class);
                intent.putExtras(bundle);
                activity.startActivityForResult(intent, ACTIVITY_REQUEST_LOGIN_WB);
                break;
            case QQ_LOGIN:
                bundle.putInt(BUNDLE_QQ_ACTION, 1);
                intent.setClass(activity, QQEntryActivity.class);
                intent.putExtras(bundle);
                activity.startActivityForResult(intent, ACTIVITY_REQUEST_LOGIN_QQ);
                break;
        }
    }

    public enum LOGIN_TYPE{
        WX_LOGIN, WEIBO_LOGIN, QQ_LOGIN
    }
}
