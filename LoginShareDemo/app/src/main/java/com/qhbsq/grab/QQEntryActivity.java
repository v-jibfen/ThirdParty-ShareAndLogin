package com.qhbsq.grab;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.qhbsq.grab.share.ShareRequest;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.open.utils.ThreadManager;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.qhbsq.grab.Config.ACTIVITY_RESULT_LOGIN_QQ_CANCEL;
import static com.qhbsq.grab.Config.ACTIVITY_RESULT_LOGIN_QQ_FAIL;
import static com.qhbsq.grab.Config.ACTIVITY_RESULT_LOGIN_QQ_SUCCESS;
import static com.qhbsq.grab.Config.ACTIVITY_RESULT_SHARE_QQ_CANCEL;
import static com.qhbsq.grab.Config.ACTIVITY_RESULT_SHARE_QQ_FAIL;
import static com.qhbsq.grab.Config.ACTIVITY_RESULT_SHARE_QQ_SUCCESS;
import static com.qhbsq.grab.Config.BUNDLE_QQ_ACTION;
import static com.qhbsq.grab.Config.BUNDLE_QQ_SCENE;
import static com.qhbsq.grab.Config.BUNDLE_SHARE_REQUEST;

/**
 * Created by MSI05 on 2017/9/1.
 */

public class QQEntryActivity extends BaseActivity{

    private int shareScene;
    private ShareRequest shareRequest;
    private int wbAction;

    private Tencent mTencent;
    private IUiListener loginListener; //授权登录监听器
    private IUiListener userInfoListener; //获取用户信息监听器
    private IUiListener shareListener; //分享监听
    private String scope;
    private UserInfo userInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTencent = Tencent.createInstance(Config.QQ_APP_ID, this);
        scope = "all";

        initQQListener();

        Bundle bundle = getIntent().getExtras();
        wbAction = bundle.getInt(BUNDLE_QQ_ACTION);
        shareScene = bundle.getInt(BUNDLE_QQ_SCENE);
        shareRequest = (ShareRequest) bundle.getSerializable(BUNDLE_SHARE_REQUEST);

        if (wbAction == 1) {
            login();
        } else {
            share();
        }
    }

    private void login() {
        //如果session无效，就开始登录
        if (!mTencent.isSessionValid()) {
            //开始qq授权登录
            mTencent.login(QQEntryActivity.this, scope, loginListener);
        }
    }

    private void share() {
        switch (shareScene){
            case 1:
                final Bundle params1 = new Bundle();
                params1.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
                params1.putString(QQShare.SHARE_TO_QQ_TITLE, shareRequest.getTitle());// 标题
                params1.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareRequest.getDescription());// 摘要
                params1.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareRequest.getWebUrl());// 内容地址
                params1.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, shareRequest.getImageUrl());// 网络图片地址　　params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "应用名称");// 应用名称
                params1.putString(QQShare.SHARE_TO_QQ_EXT_INT, "");

                ThreadManager.getMainHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        mTencent.shareToQQ(QQEntryActivity.this, params1, shareListener);
                    }
                });
                break;

            case 2:
                final Bundle params2 = new Bundle();
                params2.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
                params2.putString(QzoneShare.SHARE_TO_QQ_TITLE, shareRequest.getTitle());// 标题
                params2.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, shareRequest.getDescription());// 摘要
                params2.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, shareRequest.getWebUrl());// 内容地址
                ArrayList<String> imgUrlList = new ArrayList<>();
                imgUrlList.add(shareRequest.getImageUrl());
                params2.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL,imgUrlList);// 图片地址

                ThreadManager.getMainHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        mTencent.shareToQzone(QQEntryActivity.this, params2, shareListener);
                    }
                });
                break;
        }

    }

    private void initQQListener() {
        loginListener = new IUiListener() {

            @Override
            public void onError(UiError uiError) {
                // TODO Auto-generated method stub
                Toast.makeText(QQEntryActivity.this, "登录失败，错误码：" + uiError.errorCode, Toast.LENGTH_LONG).show();
                setResult(ACTIVITY_RESULT_LOGIN_QQ_FAIL);
                finish();
            }

            /**
             * 返回json数据样例
             *
             * {"ret":0,"pay_token":"D3D678728DC580FBCDE15722B72E7365",
             * "pf":"desktop_m_qq-10000144-android-2002-",
             * "query_authority_cost":448,
             * "authority_cost":-136792089,
             * "openid":"015A22DED93BD15E0E6B0DDB3E59DE2D",
             * "expires_in":7776000,
             * "pfkey":"6068ea1c4a716d4141bca0ddb3df1bb9",
             * "msg":"",
             * "access_token":"A2455F491478233529D0106D2CE6EB45",
             * "login_cost":499}
             */
            @Override
            public void onComplete(Object value) {
                // TODO Auto-generated method stub

                System.out.println("有数据返回..");
                if (value == null) {
                    return;
                }

                try {
                    JSONObject jo = (JSONObject) value;

                    int ret = jo.getInt("ret");

                    System.out.println("json=" + String.valueOf(jo));

                    if (ret == 0) {
                        Toast.makeText(QQEntryActivity.this, "登录成功",
                                Toast.LENGTH_LONG).show();

                        String openID = jo.getString("openid");
                        String accessToken = jo.getString("access_token");
                        String expires = jo.getString("expires_in");
                        mTencent.setOpenId(openID);
                        mTencent.setAccessToken(accessToken, expires);
                    }

                    userInfo = new UserInfo(QQEntryActivity.this, mTencent.getQQToken());
                    userInfo.getUserInfo(userInfoListener);

                } catch (Exception e) {
                    // TODO: handle exception
                }


            }

            @Override
            public void onCancel() {
                // TODO Auto-generated method stub
                Toast.makeText(QQEntryActivity.this, "登录取消", Toast.LENGTH_LONG).show();
                setResult(ACTIVITY_RESULT_LOGIN_QQ_CANCEL);
                finish();
            }
        };

        userInfoListener = new IUiListener() {

            @Override
            public void onError(UiError uiError) {
                // TODO Auto-generated method stub
                Toast.makeText(QQEntryActivity.this, "获取用户信息失败，错误码：" + uiError.errorCode, Toast.LENGTH_LONG).show();
            }

            /**
             * 返回用户信息样例
             *
             * {"is_yellow_year_vip":"0","ret":0,
             * "figureurl_qq_1":"http:\/\/q.qlogo.cn\/qqapp\/1104732758\/015A22DED93BD15E0E6B0DDB3E59DE2D\/40",
             * "figureurl_qq_2":"http:\/\/q.qlogo.cn\/qqapp\/1104732758\/015A22DED93BD15E0E6B0DDB3E59DE2D\/100",
             * "nickname":"攀爬←蜗牛","yellow_vip_level":"0","is_lost":0,"msg":"",
             * "city":"黄冈","
             * figureurl_1":"http:\/\/qzapp.qlogo.cn\/qzapp\/1104732758\/015A22DED93BD15E0E6B0DDB3E59DE2D\/50",
             * "vip":"0","level":"0",
             * "figureurl_2":"http:\/\/qzapp.qlogo.cn\/qzapp\/1104732758\/015A22DED93BD15E0E6B0DDB3E59DE2D\/100",
             * "province":"湖北",
             * "is_yellow_vip":"0","gender":"男",
             * "figureurl":"http:\/\/qzapp.qlogo.cn\/qzapp\/1104732758\/015A22DED93BD15E0E6B0DDB3E59DE2D\/30"}
             */
            @Override
            public void onComplete(Object arg0) {
                // TODO Auto-generated method stub
                if(arg0 == null){
                    return;
                }
                try {
                    JSONObject jo = (JSONObject) arg0;
                    int ret = jo.getInt("ret");
                    System.out.println("json=" + String.valueOf(jo));
                    String nickName = jo.getString("nickname");
                    String gender = jo.getString("gender");

                    Toast.makeText(QQEntryActivity.this, "你好，" + nickName,
                            Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    // TODO: handle exception
                }


            }

            @Override
            public void onCancel() {
                // TODO Auto-generated method stub
                Toast.makeText(QQEntryActivity.this, "获取用户信息取消", Toast.LENGTH_LONG).show();
            }
        };

        shareListener = new IUiListener() {
            @Override
            public void onComplete(Object o) {
                Toast.makeText(QQEntryActivity.this, "分享成功", Toast.LENGTH_LONG).show();
                setResult(ACTIVITY_RESULT_SHARE_QQ_SUCCESS);
                finish();
            }

            @Override
            public void onError(UiError uiError) {
                Toast.makeText(QQEntryActivity.this, "分享失败，错误码：" + uiError.errorCode, Toast.LENGTH_LONG).show();
                setResult(ACTIVITY_RESULT_SHARE_QQ_FAIL);
                finish();
            }

            @Override
            public void onCancel() {
                Toast.makeText(QQEntryActivity.this, "分享取消", Toast.LENGTH_LONG).show();
                setResult(ACTIVITY_RESULT_SHARE_QQ_CANCEL);
                finish();
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == com.tencent.connect.common.Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
        }
        if (requestCode == com.tencent.connect.common.Constants.REQUEST_QQ_SHARE
                || requestCode == com.tencent.connect.common.Constants.REQUEST_QZONE_SHARE) {
            Tencent.onActivityResultData(requestCode, resultCode, data, shareListener);
        }

    }
}
