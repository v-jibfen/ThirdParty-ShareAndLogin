package com.qhbsq.grab;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.qhbsq.grab.login.LoginManager;
import com.qhbsq.grab.share.ShareManager;
import com.qhbsq.grab.share.ShareRequest;

import static com.qhbsq.grab.Config.ACTIVITY_REQUEST_LOGIN_WX;
import static com.qhbsq.grab.Config.ACTIVITY_RESULT_LOGIN_QQ_CANCEL;
import static com.qhbsq.grab.Config.ACTIVITY_RESULT_LOGIN_QQ_FAIL;
import static com.qhbsq.grab.Config.ACTIVITY_RESULT_LOGIN_QQ_SUCCESS;
import static com.qhbsq.grab.Config.ACTIVITY_RESULT_LOGIN_WB_CANCEL;
import static com.qhbsq.grab.Config.ACTIVITY_RESULT_LOGIN_WB_FAIL;
import static com.qhbsq.grab.Config.ACTIVITY_RESULT_LOGIN_WB_SUCCESS;
import static com.qhbsq.grab.Config.ACTIVITY_RESULT_LOGIN_WX_CANCEL;
import static com.qhbsq.grab.Config.ACTIVITY_RESULT_LOGIN_WX_FAIL;
import static com.qhbsq.grab.Config.ACTIVITY_RESULT_LOGIN_WX_SUCCESS;
import static com.qhbsq.grab.Config.ACTIVITY_RESULT_SHARE_QQ_CANCEL;
import static com.qhbsq.grab.Config.ACTIVITY_RESULT_SHARE_QQ_FAIL;
import static com.qhbsq.grab.Config.ACTIVITY_RESULT_SHARE_QQ_SUCCESS;
import static com.qhbsq.grab.Config.ACTIVITY_RESULT_SHARE_WB_CANCEL;
import static com.qhbsq.grab.Config.ACTIVITY_RESULT_SHARE_WB_FAIL;
import static com.qhbsq.grab.Config.ACTIVITY_RESULT_SHARE_WB_SUCCESS;
import static com.qhbsq.grab.Config.ACTIVITY_RESULT_SHARE_WX_CANCEL;
import static com.qhbsq.grab.Config.ACTIVITY_RESULT_SHARE_WX_FAIL;
import static com.qhbsq.grab.Config.ACTIVITY_RESULT_SHARE_WX_SUCCESS;

/**
 * Created by MSI05 on 2017/8/31.
 */

public class HomeActivity extends AppCompatActivity {

    private TextView mShareTextView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        mShareTextView = (TextView) findViewById(R.id.share_result);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }

    public void weichat_share_text(View view) {
        ShareRequest shareRequest = new ShareRequest();
        shareRequest.setDescription("来自测试demo的分享(文字)");
        ShareManager.getInstance().share(HomeActivity.this, shareRequest, ShareManager.SHARE_SCENE.WX_MOMENT, ShareManager.SHARE_TYPE_TEXT);
    }

    public void weichat_share_image(View view) {
        ShareRequest shareRequest = new ShareRequest();
        shareRequest.setImageUrl("http://img3.redocn.com/tupian/20150430/mantenghuawenmodianshiliangbeijing_3924704.jpg");
        ShareManager.getInstance().share(HomeActivity.this, shareRequest, ShareManager.SHARE_SCENE.WX_SESSION, ShareManager.SHARE_TYPE_IMAGE);
    }

    public void weichat_share_link(View view) {
        ShareRequest shareRequest = new ShareRequest();
        shareRequest.setWebUrl("http://www.qq.com");
        shareRequest.setImageUrl("http://img3.redocn.com/tupian/20150430/mantenghuawenmodianshiliangbeijing_3924704.jpg");
        shareRequest.setDescription("这个是demo分享的链接");
        shareRequest.setTitle("demo");
        ShareManager.getInstance().share(HomeActivity.this, shareRequest, ShareManager.SHARE_SCENE.WX_SESSION, ShareManager.SHARE_TYPE_LINK);
    }

    public void weichat_login(View view) {
        LoginManager.getInstance().login(HomeActivity.this, LoginManager.LOGIN_TYPE.WX_LOGIN);
    }

    public void qq_login(View view) {
        LoginManager.getInstance().login(HomeActivity.this, LoginManager.LOGIN_TYPE.QQ_LOGIN);
    }

    public void qq_share(View view) {
        ShareRequest shareRequest = new ShareRequest();
        shareRequest.setWebUrl("http://www.qq.com");
        shareRequest.setImageUrl("http://img3.redocn.com/tupian/20150430/mantenghuawenmodianshiliangbeijing_3924704.jpg");
        shareRequest.setDescription("这个是demo分享的链接");
        shareRequest.setTitle("demo");
        ShareManager.getInstance().share(HomeActivity.this, shareRequest, ShareManager.SHARE_SCENE.QQ_SESSION, ShareManager.SHARE_TYPE_LINK);
    }

    public void qq_qzone_login(View view) {
        ShareRequest shareRequest = new ShareRequest();
        shareRequest.setWebUrl("http://www.qq.com");
        shareRequest.setImageUrl("http://img3.redocn.com/tupian/20150430/mantenghuawenmodianshiliangbeijing_3924704.jpg");
        shareRequest.setDescription("这个是demo分享的链接");
        shareRequest.setTitle("demo");
        ShareManager.getInstance().share(HomeActivity.this, shareRequest, ShareManager.SHARE_SCENE.QQ_MOMENT, ShareManager.SHARE_TYPE_LINK);
    }

    public void weibo_login(View view) {
        LoginManager.login(HomeActivity.this, LoginManager.LOGIN_TYPE.WEIBO_LOGIN);
    }

    public void weibo_share(View view) {
        ShareRequest shareRequest = new ShareRequest();
        shareRequest.setWebUrl("http://www.qq.com");
        shareRequest.setImageUrl("http://img3.redocn.com/tupian/20150430/mantenghuawenmodianshiliangbeijing_3924704.jpg");
        shareRequest.setDescription("这个是demo分享的链接");
        shareRequest.setTitle("demo");
        ShareManager.share(HomeActivity.this, shareRequest, ShareManager.SHARE_SCENE.WB_MOMENT, ShareManager.SHARE_TYPE_LINK);
    }

    private void updateTextView(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mShareTextView.setText(text);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Config.ACTIVITY_REQUEST_SHARE_WX) {
            switch (resultCode) {
                case ACTIVITY_RESULT_SHARE_WX_SUCCESS:
                    updateTextView("WX分享成功");
                    break;
                case ACTIVITY_RESULT_SHARE_WX_CANCEL:
                    updateTextView("WX分享取消");
                    break;
                case ACTIVITY_RESULT_SHARE_WX_FAIL:
                    updateTextView("WX分享失败");
                    break;
            }
        } else if (requestCode == Config.ACTIVITY_REQUEST_LOGIN_WX) {
            switch (resultCode) {
                case ACTIVITY_RESULT_LOGIN_WX_SUCCESS:
                    updateTextView("WX登录成功");
                    break;
                case ACTIVITY_RESULT_LOGIN_WX_CANCEL:
                    updateTextView("WX登录取消");
                    break;
                case ACTIVITY_RESULT_LOGIN_WX_FAIL:
                    updateTextView("WX登录失败");
                    break;
            }
        } else if (requestCode == Config.ACTIVITY_REQUEST_LOGIN_QQ) {
            switch (resultCode) {
                case ACTIVITY_RESULT_LOGIN_QQ_SUCCESS:
                    updateTextView("QQ登录成功");
                    break;
                case ACTIVITY_RESULT_LOGIN_QQ_CANCEL:
                    updateTextView("QQ登录取消");
                    break;
                case ACTIVITY_RESULT_LOGIN_QQ_FAIL:
                    updateTextView("QQ登录失败");
                    break;
            }
        } else if (requestCode == Config.ACTIVITY_REQUEST_SHARE_QQ) {
            switch (resultCode) {
                case ACTIVITY_RESULT_LOGIN_WB_SUCCESS:
                    updateTextView("WB分享成功");
                    break;
                case ACTIVITY_RESULT_LOGIN_WB_CANCEL:
                    updateTextView("WB分享取消");
                    break;
                case ACTIVITY_RESULT_LOGIN_WB_FAIL:
                    updateTextView("WB分享失败");
                    break;
            }
        } else if (requestCode == Config.ACTIVITY_REQUEST_LOGIN_WB) {

        } else if (requestCode == Config.ACTIVITY_REQUEST_SHARE_WB) {
            switch (resultCode) {
                case ACTIVITY_RESULT_SHARE_WB_SUCCESS:
                    updateTextView("WB分享成功");
                    break;
                case ACTIVITY_RESULT_SHARE_WB_CANCEL:
                    updateTextView("WB分享取消");
                    break;
                case ACTIVITY_RESULT_SHARE_WB_FAIL:
                    updateTextView("WB分享失败");
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
