package edu.feicui.treasuremap.user.register;


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

/**
 * Created by Administrator on 2016/6/30.
 */
public class RegisterPresenter extends MvpNullObjectBasePresenter<RegisterView> {

    private final String    URL        ="http://admin.syfeicuiedu.com/Handler/UserHandler.ashx?action=login";
    private final MediaType mMediaType =MediaType.parse("text/*");
    private Gson    mGson;
    private Call    registerCall;

    public RegisterPresenter() {
        mGson=new GsonBuilder().setLenient().create();//非严格模式
    }

    public void register(User user){
        getView().showProgress();
        if (registerCall != null) registerCall.cancel();
        RequestBody requestBody=RequestBody.create(mMediaType,mGson.toJson(user));
        registerCall= NetClient.getInstance().getUserAPI().register(requestBody);
        registerCall.enqueue(mCallback);
    }

    private Callback<ResponseBody> mCallback=new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
            getView().hideProgress();
            if(response.isSuccessful()){
                try {
                    String body = response.body().string();
                    RegisterResult result=mGson.fromJson(body,RegisterResult.class);
                    if(result!=null){
                        switch (result.getCode()){
                            case 1:
                                UserPrefs.getInstance().setTokenid(result.getTokenId());
                                getView().showMessage("注册成功");
                                getView().navigateToHome();
                                break;
                            case 2:
                                getView().showMessage("注册此用户名已存在！");
                                break;
                            default:
                                getView().showMessage("发生异常,注册失败!");
                                break;
                        }
                    }
                } catch (IOException e) {
                    onFailure(call,e);
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            getView().hideProgress();
            getView().showMessage(t.getMessage());
        }
    };
}
