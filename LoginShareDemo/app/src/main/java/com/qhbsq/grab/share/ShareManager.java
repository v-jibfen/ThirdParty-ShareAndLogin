package com.qhbsq.grab.share;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.qhbsq.grab.QQEntryActivity;
import com.qhbsq.grab.WXShareActivity;
import com.qhbsq.grab.WeiboEntryActivity;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;

import static com.qhbsq.grab.Config.ACTIVITY_REQUEST_LOGIN_WB;
import static com.qhbsq.grab.Config.ACTIVITY_REQUEST_SHARE_QQ;
import static com.qhbsq.grab.Config.ACTIVITY_REQUEST_SHARE_WB;
import static com.qhbsq.grab.Config.ACTIVITY_REQUEST_SHARE_WX;
import static com.qhbsq.grab.Config.BUNDLE_QQ_ACTION;
import static com.qhbsq.grab.Config.BUNDLE_QQ_SCENE;
import static com.qhbsq.grab.Config.BUNDLE_SHARE_REQUEST;
import static com.qhbsq.grab.Config.BUNDLE_WB_ACTION;
import static com.qhbsq.grab.Config.BUNDLE_WX_SCENE;
import static com.qhbsq.grab.Config.BUNDLE_WX_TYPE;

/**
 * Created by MSI05 on 2017/8/31.
 */

public class ShareManager {

    public static final int SHARE_TYPE_TEXT = 1;
    public static final int SHARE_TYPE_IMAGE = 2;
    public static final int SHARE_TYPE_LINK = 3;

    private static ShareManager _instance;

    private ShareManager() {

    }

    public static ShareManager getInstance() {
        if (_instance == null) {
            _instance = new ShareManager();
        }

        return _instance;
    }

    public static void share(Activity activity, ShareRequest shareRequest, SHARE_SCENE shareScene, int shareType) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();

        bundle.putSerializable(BUNDLE_SHARE_REQUEST, shareRequest);

        switch (shareScene) {
            case WX_SESSION:
                bundle.putInt(BUNDLE_WX_TYPE, shareType);
                bundle.putInt(BUNDLE_WX_SCENE, SendMessageToWX.Req.WXSceneSession);
                intent.putExtras(bundle);
                intent.setClass(activity, WXShareActivity.class);
                activity.startActivityForResult(intent, ACTIVITY_REQUEST_SHARE_WX);
                break;
            case WX_MOMENT:
                bundle.putInt(BUNDLE_WX_TYPE, shareType);
                bundle.putInt(BUNDLE_WX_SCENE, SendMessageToWX.Req.WXSceneTimeline);
                intent.setClass(activity, WXShareActivity.class);
                intent.putExtras(bundle);
                activity.startActivityForResult(intent, ACTIVITY_REQUEST_SHARE_WX);
                break;
            case WX_FAVORITE:
                bundle.putInt(BUNDLE_WX_TYPE, shareType);
                bundle.putInt(BUNDLE_WX_SCENE, SendMessageToWX.Req.WXSceneFavorite);
                intent.putExtras(bundle);
                intent.setClass(activity, WXShareActivity.class);
                activity.startActivityForResult(intent, ACTIVITY_REQUEST_SHARE_WX);
                break;

            case WB_MOMENT:
                bundle.putInt(BUNDLE_WB_ACTION, 2);
                intent.putExtras(bundle);
                intent.setClass(activity, WeiboEntryActivity.class);
                activity.startActivityForResult(intent, ACTIVITY_REQUEST_SHARE_WB);
                break;
            case QQ_SESSION:
                bundle.putInt(BUNDLE_QQ_ACTION, 2);
                bundle.putInt(BUNDLE_QQ_SCENE, 1);
                intent.setClass(activity, QQEntryActivity.class);
                intent.putExtras(bundle);
                activity.startActivityForResult(intent, ACTIVITY_REQUEST_SHARE_QQ);
                break;

            case QQ_MOMENT:
                bundle.putInt(BUNDLE_QQ_ACTION, 2);
                bundle.putInt(BUNDLE_QQ_SCENE, 2);
                intent.setClass(activity, QQEntryActivity.class);
                intent.putExtras(bundle);
                activity.startActivityForResult(intent, ACTIVITY_REQUEST_SHARE_QQ);
                break;
        }
    }

    public enum SHARE_SCENE {
        WX_SESSION, WX_MOMENT, WX_FAVORITE, WB_MOMENT, QQ_SESSION, QQ_MOMENT
    }
}
