package edu.feicui.treasuremap.user.account;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.File;
import java.io.IOException;


import edu.feicui.treasuremap.net.NetClient;
import edu.feicui.treasuremap.user.UserPrefs;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2016/7/5.
 */
public class AccountPresenter extends MvpNullObjectBasePresenter<AccountView> {

    private Call<ResponseBody> uploadCall;
    private Call<ResponseBody> updataCall;
    private Gson               mGson;
    private String             mPhotoUrl;

    public AccountPresenter() {
        mGson = new GsonBuilder().setLenient().create();//非严格模式
    }

    /**
     * 上传头像
     */
    public void uploadPhoto(File file) {
        getView().showProgress();
        if (uploadCall != null) {
            uploadCall.cancel();
        }

        MediaType mediaType = MediaType.parse("image/png");
        RequestBody requestBody = RequestBody.create(mediaType, file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("image", "photo.png", requestBody);
        uploadCall = NetClient.getInstance().getUserAPI().upload(part);
        uploadCall.enqueue(uploadCallback);
    }

    //上传头像的callback
    private Callback<ResponseBody> uploadCallback = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            getView().hideProgress();
            try {
                if (response != null && response.isSuccessful()) {
                    String body = response.body().string();
                    //上传响应结果
                    UploadResult result = mGson.fromJson(body, UploadResult.class);
                    getView().showMessage(result.getMsg());
                    if (result.getCount() == 1) {
                        mPhotoUrl = result.getUrl();
                        // 截取出后面图像文件名称(在更新头像时，服务器接口只要求传过去文件的名称)
                        String photoName=mPhotoUrl;
                        //成功上传后--更新头像
                        Update update=new Update();
                        update.setTokenId(UserPrefs.getInstance().getTokenid());
                        update.setphotoUrl(UserPrefs.getInstance().getPhoto());
                        MediaType mediaType=MediaType.parse("text/json");
                        RequestBody requestBody=RequestBody.create(mediaType,mGson.toJson(update));
                        updataCall=NetClient.getInstance().getUserAPI().update(requestBody);
                        updataCall.enqueue(updateCallback);
                        return;
                    }else{
                        getView().showMessage(result.getMsg());
                    }
                }
            } catch (IOException e) {
                onFailure(uploadCall,e);
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            getView().hideProgress();
            getView().showMessage(t.getMessage());
        }
    };

    //更新头像的callback
    private Callback<ResponseBody> updateCallback=new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            getView().hideProgress();
            try {
                if(response!=null&&response.isSuccessful()){
                    String body=response.body().string();
                    UpdateResult result=mGson.fromJson(body,UpdateResult.class);
                    getView().showMessage(result.getMessage());
                    UserPrefs.getInstance().setPhoto(NetClient.BASE_URL + mPhotoUrl);
                    getView().updatePhoto(NetClient.BASE_URL + mPhotoUrl);
                }
            } catch (IOException e) {
                onFailure(updataCall,e);
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            getView().hideProgress();
            getView().showMessage(t.getMessage());
        }
    };
}
