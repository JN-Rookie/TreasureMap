package edu.feicui.treasuremap.user.account;

import com.google.gson.annotations.SerializedName;

/**
 * 登陆相应结果实体
 * Created by Administrator on 2016/6/30.
 */
public final class UploadResult {
    @SerializedName("error")
    private String msg;

    @SerializedName("urlcount")
    private int count;

    @SerializedName("smallImgUrl")
    private String url;

    public int getCount() {
        return count;
    }

    public String getUrl() {
        return url;
    }

    public String getMsg() {
        return msg;
    }
}
