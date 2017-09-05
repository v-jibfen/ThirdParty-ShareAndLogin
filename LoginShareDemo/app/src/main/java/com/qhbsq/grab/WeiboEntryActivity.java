package com.qhbsq.grab;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.qhbsq.grab.share.ShareRequest;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

import static com.qhbsq.grab.Config.ACTIVITY_RESULT_LOGIN_WB_CANCEL;
import static com.qhbsq.grab.Config.ACTIVITY_RESULT_LOGIN_WB_FAIL;
import static com.qhbsq.grab.Config.ACTIVITY_RESULT_LOGIN_WB_SUCCESS;
import static com.qhbsq.grab.Config.ACTIVITY_RESULT_SHARE_WB_CANCEL;
import static com.qhbsq.grab.Config.ACTIVITY_RESULT_SHARE_WB_FAIL;
import static com.qhbsq.grab.Config.ACTIVITY_RESULT_SHARE_WB_SUCCESS;
import static com.qhbsq.grab.Config.BUNDLE_SHARE_REQUEST;
import static com.qhbsq.grab.Config.BUNDLE_WB_ACTION;
import static com.qhbsq.grab.Config.BUNDLE_WX_SCENE;
import static com.qhbsq.grab.Config.BUNDLE_WX_TYPE;

/**
 * Created by MSI05 on 2017/9/1.
 */

public class WeiboEntryActivity extends BaseActivity implements WbShareCallback {

    private SsoHandler mSsoHandler;
    private WbShareHandler mShareHandler;
    private Oauth2AccessToken mAccessToken;

    private ShareRequest shareRequest;
    private int wbAction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WbSdk.install(this,new AuthInfo(this, Config.WEIBO_APP_KEY, Config.WEIBO_REDIRECT_URL, Config.WEIBO_SCOPE));

        mSsoHandler = new SsoHandler(WeiboEntryActivity.this);
        mShareHandler = new WbShareHandler(this);
        mShareHandler.registerApp();
        mShareHandler.setProgressColor(0xff33b5e5);
        Bundle bundle = getIntent().getExtras();
        wbAction = bundle.getInt(BUNDLE_WB_ACTION);
        shareRequest = (ShareRequest) bundle.getSerializable(BUNDLE_SHARE_REQUEST);

        if (wbAction == 1) {
            login();

        } else {
            share();
        }
    }

    private void login() {
        mSsoHandler.authorizeClientSso(new SelfWbAuthListener());
    }

    private void share() {
        sendMessage(true, true);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mShareHandler.doResultIntent(intent,this);
    }

    @Override
    public void onWbShareSuccess() {
        Toast.makeText(this, R.string.weibosdk_demo_toast_share_success, Toast.LENGTH_LONG).show();
        setResult(ACTIVITY_RESULT_SHARE_WB_SUCCESS);
        finish();
    }

    @Override
    public void onWbShareFail() {
        Toast.makeText(this,
                getString(R.string.weibosdk_demo_toast_share_failed) + "Error Message: ",
                Toast.LENGTH_LONG).show();

        setResult(ACTIVITY_RESULT_SHARE_WB_FAIL);
        finish();
    }

    @Override
    public void onWbShareCancel() {
        Toast.makeText(this, R.string.weibosdk_demo_toast_share_canceled, Toast.LENGTH_LONG).show();
        setResult(ACTIVITY_RESULT_SHARE_WB_CANCEL);
        finish();
    }

    private class SelfWbAuthListener implements com.sina.weibo.sdk.auth.WbAuthListener{
        @Override
        public void onSuccess(final Oauth2AccessToken token) {
            WeiboEntryActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAccessToken = token;
                    if (mAccessToken.isSessionValid()) {
                        // 显示 Token
                        //updateTokenView(false);
                        // 保存 Token 到 SharedPreferences
                        AccessTokenKeeper.writeAccessToken(WeiboEntryActivity.this, mAccessToken);
                        Toast.makeText(WeiboEntryActivity.this,
                                R.string.weibosdk_demo_toast_auth_success, Toast.LENGTH_SHORT).show();
                        setResult(ACTIVITY_RESULT_LOGIN_WB_SUCCESS);
                        finish();
                    }
                }
            });
        }

        @Override
        public void cancel() {
            Toast.makeText(WeiboEntryActivity.this,
                    R.string.weibosdk_demo_toast_auth_canceled, Toast.LENGTH_LONG).show();
            setResult(ACTIVITY_RESULT_LOGIN_WB_CANCEL);
            finish();
        }

        @Override
        public void onFailure(WbConnectErrorMessage errorMessage) {
            Toast.makeText(WeiboEntryActivity.this, errorMessage.getErrorMessage(), Toast.LENGTH_LONG).show();
            setResult(ACTIVITY_RESULT_LOGIN_WB_FAIL);
            finish();
        }
    }

    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     */
    private void sendMessage(boolean hasText, boolean hasImage) {
        sendMultiMessage(hasText, hasImage);
    }

    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     */
    private void sendMultiMessage(boolean hasText, boolean hasImage) {
        final WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        if (hasText) {
            weiboMessage.textObject = getTextObj();
        }
        if (hasImage) {
            final Consumer<Bitmap> consumer1 = new Consumer<Bitmap>() {
                @Override
                public void accept(Bitmap bitmap) throws Exception {
                    ImageObject imageObject = new ImageObject();
                    imageObject.setImageObject(bitmap);
                    weiboMessage.imageObject = imageObject;
                    mShareHandler.shareMessage(weiboMessage, false);
                }
            };

            Glide.with(WeiboEntryActivity.this)
                    .load(shareRequest.getImageUrl())
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>(100, 100) {
                        @Override
                        public void onResourceReady(Bitmap data, GlideAnimation anim) {
                            Flowable.just(data).subscribe(consumer1);
                        }
                    });

        }
//        if(multiImageCheckbox.isChecked()){
//            weiboMessage.multiImageObject = getMultiImageObject();
//        }
//        if(videoCheckbox.isChecked()){
//            weiboMessage.videoSourceObject = getVideoObject();
//        }


    }

    /**
     * 获取分享的文本模板。
     */
    private String getSharedText() {
        int formatId = R.string.weibosdk_demo_share_text_template;
        String format = getString(formatId);
        String text = format;

        //text = "@大屁老师，这是一个很漂亮的小狗，朕甚是喜欢-_-!! #大屁老师#http://weibo.com/p/1005052052202067/home?from=page_100505&mod=TAB&is_all=1#place";
        text = shareRequest.getDescription();
        return text;
    }

    /**
     * 创建文本消息对象。
     * @return 文本消息对象。
     */
    private TextObject getTextObj() {
        TextObject textObject = new TextObject();
        textObject.text = getSharedText();
        textObject.title = shareRequest.getTitle();
        textObject.actionUrl = shareRequest.getWebUrl();
        return textObject;
    }

    /**
     * 创建图片消息对象。
     * @return 图片消息对象。
     */
    private ImageObject getImageObj() {

        return null;
    }
}
