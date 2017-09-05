package com.qhbsq.grab.share;

import java.io.Serializable;

/**
 * Created by MSI05 on 2017/8/31.
 */

public class ShareRequest implements Serializable {

    private String title;
    private String description;
    private String imageUrl;
    private String webUrl;
    private String status; //0 分享成功，1 分享失败
    private String type; //0 登录，2 分享

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
