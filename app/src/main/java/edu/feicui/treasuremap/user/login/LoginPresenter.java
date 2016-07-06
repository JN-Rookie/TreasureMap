package edu.feicui.treasuremap.user.login;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;

import edu.feicui.treasuremap.net.NetClient;
import edu.feicui.treasuremap.user.User;
import edu.feicui.treasuremap.user.UserPrefs;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2016/6/30.
 */
public class LoginPresenter extends MvpNullObjectBasePresenter<LoginView> {

    private final String    URL        = "http://admin.syfeicuiedu.com/Handler/UserHandler.ashx?action=login";
    private final MediaType mMediaType = MediaType.parse("text/*");
    private Gson mGson;

    private Call loginCall;

    public LoginPresenter() {
        mGson = new GsonBuilder().setLenient().create();//非严格模式,避免服务器错误
    }

    public void login(User user) {
        getView().showProgress();
        if (loginCall != null) loginCall.cancel();
        RequestBody requestBody = RequestBody.create(mMediaType, mGson.toJson(user));
        loginCall = NetClient.getInstance().getUserAPI().login(requestBody);
        loginCall.enqueue(mCallback);
    }

    private final Callback<ResponseBody> mCallback = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            getView().hideProgress();
            try {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    LoginResult result = mGson.fromJson(body, LoginResult.class);
                    String photoUrl=NetClient.BASE_URL+result.getIconUrl();
                    UserPrefs.getInstance().setPhoto(photoUrl);
                    UserPrefs.getInstance().setTokenid(result.getTokenId());
                    if (result != null) {
                        switch (result.getCode()) {
                            case 1:
                                getView().showMessage("登陆成功");
                                getView().navigateToHome();
                                break;
                            case 2:
                                getView().clearEditView();
                                getView().showMessage("此用户已被锁住！无法正常登录！");
                                break;
                            case 3:
                                getView().clearEditView();
                                getView().showMessage("用户名不存在!请先注册成会员再登录!");
                                break;
                            case 4:
                                getView().clearEditView();
                                getView().showMessage("密码错误！");
                                break;
                            case 5:
                                getView().clearEditView();
                                getView().showMessage("此用户已登录!");
                                break;
                            default:
                                getView().clearEditView();
                                getView().showMessage("发生异常,登录失败!");
                                break;
                        }
                    }
                }
            } catch (IOException e) {
                onFailure(call, e);
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            getView().hideProgress();
            getView().showMessage(t.getMessage());
        }
    };
}
