package edu.feicui.treasuremap.user.login;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Administrator on 2016/6/30.
 */
public interface LoginView extends MvpView{
    /**显示登陆中进度视图*/
    void showProgress();
    /**隐藏登陆中进度视图*/
    void hideProgress();
    /**显示提示信息*/
    void showMessage(String msg);
    /**导航到Home界面*/
    void navigateToHome();
    /** 清理*/
    void clearEditView();
}
