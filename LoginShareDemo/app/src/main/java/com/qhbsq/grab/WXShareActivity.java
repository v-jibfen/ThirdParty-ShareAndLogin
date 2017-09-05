package com.qhbsq.grab;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.qhbsq.grab.share.ShareRequest;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

import static com.qhbsq.grab.Config.BUNDLE_SHARE_REQUEST;
import static com.qhbsq.grab.Config.BUNDLE_WX_SCENE;
import static com.qhbsq.grab.Config.BUNDLE_WX_TYPE;
import static com.qhbsq.grab.share.ShareManager.SHARE_TYPE_IMAGE;
import static com.qhbsq.grab.share.ShareManager.SHARE_TYPE_LINK;
import static com.qhbsq.grab.share.ShareManager.SHARE_TYPE_TEXT;

/**
 * Created by MSI05 on 2017/9/1.
 */

public class WXShareActivity extends BaseActivity{
    private IWXAPI api;
    private int shareScene;
    private int shareType;
    private ShareRequest shareRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        api = WXAPIFactory.createWXAPI(this, Config.WEICHAT_APP_ID);
        api.registerApp(Config.WEICHAT_APP_ID);
        Bundle bundle = getIntent().getExtras();
        shareScene = bundle.getInt(BUNDLE_WX_SCENE);
        shareType = bundle.getInt(BUNDLE_WX_TYPE);
        shareRequest = (ShareRequest) bundle.getSerializable(BUNDLE_SHARE_REQUEST);

        share();
    }

    private void share() {
        switch (shareType) {
            case SHARE_TYPE_TEXT:
                WXTextObject textObj = new WXTextObject();
                textObj.text = shareRequest.getDescription();

                WXMediaMessage msg = new WXMediaMessage();
                msg.mediaObject = textObj;
                msg.description = shareRequest.getDescription();

                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("text");
                req.message = msg;
                req.scene = shareScene;

                api.sendReq(req);
                //finish();
                break;

            case SHARE_TYPE_IMAGE:
                final Consumer<Bitmap> consumer = new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap b) throws Exception {
                        WXImageObject imgObj = new WXImageObject(b);

                        WXMediaMessage msg = new WXMediaMessage();
                        msg.mediaObject = imgObj;

                        msg.thumbData = Util.bmpToByteArray(b, false);;
                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                        req.transaction = buildTransaction("img");
                        req.message = msg;
                        req.scene = shareScene;
                        api.sendReq(req);
                        //finish();
                    }
                };

                Glide.with(WXShareActivity.this)
                        .load(shareRequest.getImageUrl())
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>(100, 100) {
                            @Override
                            public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                                Flowable.just(bitmap).subscribe(consumer);
                            }
                        });

                break;

            case SHARE_TYPE_LINK:
                final Consumer<byte[]> consumer1 = new Consumer<byte[]>() {
                    @Override
                    public void accept(byte[] b) throws Exception {
                        WXWebpageObject webpage = new WXWebpageObject();
                        webpage.webpageUrl = shareRequest.getWebUrl();
                        WXMediaMessage mediaMessage = new WXMediaMessage(webpage);
                        mediaMessage.title = shareRequest.getTitle();
                        mediaMessage.description = shareRequest.getDescription();

                        mediaMessage.thumbData = b;

                        SendMessageToWX.Req sendMessage = new SendMessageToWX.Req();
                        sendMessage.transaction = buildTransaction("webpage");
                        sendMessage.message = mediaMessage;
                        sendMessage.scene = shareScene;
                        api.sendReq(sendMessage);
                        //finish();
                    }
                };


                Glide.with(WXShareActivity.this)
                        .load(shareRequest.getImageUrl())
                        .asBitmap()
                        .toBytes()
                        .into(new SimpleTarget<byte[]>(100, 100) {
                            @Override
                            public void onResourceReady(byte[] data, GlideAnimation anim) {
                                Flowable.just(data).subscribe(consumer1);
                            }
                        });
                break;
        }
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handlerShareResult(String status) {
        switch (status) {
            case "0":
                setResult(Config.ACTIVITY_RESULT_SHARE_WX_SUCCESS);
                break;
            case "1":
                setResult(Config.ACTIVITY_RESULT_SHARE_WX_CANCEL);
                break;
            case "2":
                setResult(Config.ACTIVITY_RESULT_SHARE_WX_FAIL);
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
