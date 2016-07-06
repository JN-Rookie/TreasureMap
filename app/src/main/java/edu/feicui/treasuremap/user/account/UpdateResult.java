package edu.feicui.treasuremap.user.account;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/7/6.
 */
public class UpdateResult {

    @SerializedName("errcode")
    private int code;

    @SerializedName("errmsg")
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
