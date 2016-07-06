package edu.feicui.treasuremap.user.account;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Administrator on 2016/7/5.
 */
public interface AccountView extends MvpView{
    //更新用户头像
    void updatePhoto(String url);
    //隐藏进度条
    void showProgress();
    //显示进度条
    void hideProgress();
    //显示信息
    void showMessage(String msg);
}
