package edu.feicui.treasuremap.user.register;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/7/1.
 */
public final class RegisterResult {
//    {
//        "errcode": 1,                  //状态值
//            "errmsg": "登录成功！",        //返回信息
//    }

    @SerializedName("tokenid")
    private int tokenId;//用户令牌



    @SerializedName("errcode")
    private int code;//状态值

    @SerializedName("errmsg")
    private String msg;//返回信息

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public int getTokenId() {
        return tokenId;
    }
}
