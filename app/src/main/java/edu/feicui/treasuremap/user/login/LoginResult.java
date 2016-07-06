package edu.feicui.treasuremap.user.login;

import com.google.gson.annotations.SerializedName;

/**
 * 登陆相应结果实体
 * Created by Administrator on 2016/6/30.
 */
public final class LoginResult {
//    {
//        "errcode": 1,                  //状态值
//            "errmsg": "登录成功！",        //返回信息
//            "headpic": "add.jpg",          //头像地址
//            "tokenid": 171                 //用户令牌
//    }

    @SerializedName("headpic")
    private String iconUrl;//用户头像

    @SerializedName("tokenid")
    private int tokenId;//用户令牌

    @SerializedName("errcode")
    private int code;//状态值

    @SerializedName("errmsg")
    private String msg;//返回信息

    public String getIconUrl() {
        return iconUrl;
    }

    public int getTokenId() {
        return tokenId;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
