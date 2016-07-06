package edu.feicui.treasuremap.user;

import android.content.Context;
import android.content.SharedPreferences;

/**
 *
 * Created by Administrator on 2016/7/5.
 */
public class UserPrefs {

    private static final String PREFS_NAME  ="user_info";//用户信息
    private static final String KEY_PHOTO   ="key_photo";//用户头像
    private static final String KEY_TOKENID ="key_tokenid";//用户令牌

    private final SharedPreferences mPreferences;

    private static UserPrefs mUserPrefs;

    public static void init(Context context){
        mUserPrefs=new UserPrefs(context);
    }

    public static UserPrefs getInstance(){
        return mUserPrefs;
    }

    private UserPrefs(Context context){
        mPreferences=context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    //存放照片
    public void setPhoto(String photoUrl){
        mPreferences.edit().putString(KEY_PHOTO,photoUrl).apply();
    }

    //去除存放的照片
    public String getPhoto(){
        return mPreferences.getString(KEY_PHOTO,null);
    }

    //存放用户令牌
    public void setTokenid(int tokenid){
        mPreferences.edit().putInt(KEY_TOKENID,tokenid).apply();
    }

    //获取用户令牌
    public int getTokenid(){
        return mPreferences.getInt(KEY_TOKENID,-1);
    }


}
