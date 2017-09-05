package com.qhbsq.grab;

/**
 * Created by MSI05 on 2017/8/30.
 */

public class Config {

    public static final String WEICHAT_APP_ID = "wxc6c35014c41047a3";

    //2045436852
    /** 当前 DEMO 应用的 APP_KEY，第三方应用应该使用自己的 APP_KEY 替换该 APP_KEY */
    public static final String WEIBO_APP_KEY = "2045436852";

    /**
     * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
     *
     * <p>
     * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响，
     * 但是没有定义将无法使用 SDK 认证登录。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     * </p>
     */
    public static final String WEIBO_REDIRECT_URL = "http://www.sina.com";

    /**
     * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
     * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利
     * 选择赋予应用的功能。
     *
     * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的
     * 使用权限，高级权限需要进行申请。
     *
     * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
     *
     * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
     * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
     */
    public static final String WEIBO_SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";

    public static final String QQ_APP_ID = "1104732758";

    public static final String BUNDLE_SHARE_REQUEST = "BUNDLE_SHARE_REQUEST";
    public static final String BUNDLE_WX_SCENE = "BUNDLE_WX_SCENE";
    public static final String BUNDLE_WX_TYPE = "BUNDLE_WX_TYPE";

    public static final String BUNDLE_WB_ACTION = "BUNDLE_WB_ACTION";
    public static final String BUNDLE_QQ_ACTION = "BUNDLE_QQ_ACTION";

    public static final String BUNDLE_QQ_SCENE = "BUNDLE_QQ_SCENE";

    public static final int ACTIVITY_REQUEST_SHARE_WX = 60001;
    public static final int ACTIVITY_RESULT_SHARE_WX_SUCCESS = 60002;
    public static final int ACTIVITY_RESULT_SHARE_WX_CANCEL = 60003;
    public static final int ACTIVITY_RESULT_SHARE_WX_FAIL = 60004;

    public static final int ACTIVITY_REQUEST_LOGIN_WX = 60005;
    public static final int ACTIVITY_RESULT_LOGIN_WX_SUCCESS = 60006;
    public static final int ACTIVITY_RESULT_LOGIN_WX_CANCEL = 60007;
    public static final int ACTIVITY_RESULT_LOGIN_WX_FAIL = 60008;

    public static final int ACTIVITY_REQUEST_LOGIN_WB = 60009;
    public static final int ACTIVITY_RESULT_LOGIN_WB_SUCCESS = 60010;
    public static final int ACTIVITY_RESULT_LOGIN_WB_CANCEL = 60011;
    public static final int ACTIVITY_RESULT_LOGIN_WB_FAIL = 60012;

    public static final int ACTIVITY_REQUEST_SHARE_WB = 60013;
    public static final int ACTIVITY_RESULT_SHARE_WB_SUCCESS = 60014;
    public static final int ACTIVITY_RESULT_SHARE_WB_CANCEL = 60015;
    public static final int ACTIVITY_RESULT_SHARE_WB_FAIL = 60016;

    public static final int ACTIVITY_REQUEST_LOGIN_QQ = 60017;
    public static final int ACTIVITY_RESULT_LOGIN_QQ_SUCCESS = 60018;
    public static final int ACTIVITY_RESULT_LOGIN_QQ_CANCEL = 60019;
    public static final int ACTIVITY_RESULT_LOGIN_QQ_FAIL = 60020;

    public static final int ACTIVITY_REQUEST_SHARE_QQ = 60021;
    public static final int ACTIVITY_RESULT_SHARE_QQ_SUCCESS = 60022;
    public static final int ACTIVITY_RESULT_SHARE_QQ_CANCEL = 60023;
    public static final int ACTIVITY_RESULT_SHARE_QQ_FAIL = 60024;
}
