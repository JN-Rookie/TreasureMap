package edu.feicui.treasuremap.user.account;

import com.google.gson.annotations.SerializedName;

/**
 * 更新用户头像的请求体
 * Created by Administrator on 2016/7/6.
 */
public class Update {

    @SerializedName("Tokenid")
    private int    tokenId;
    @SerializedName("HeadPic")
    private String photoUrl;

    public void setTokenId(int tokenId) {
        this.tokenId = tokenId;
    }

    public void setphotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
