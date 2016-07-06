package edu.feicui.treasuremap.net;

import java.util.concurrent.TimeUnit;

import edu.feicui.treasuremap.user.UserAPI;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * 网络连接的类
 * Created by Administrator on 2016/6/30.
 */
public class NetClient {
    public static final String BASE_URL ="http://admin.syfeicuiedu.com";

    private static NetClient sClient;

    private final OkHttpClient mClient;
    private final Retrofit     mRetrofit;
    private       UserAPI      mUserAPI;

    public NetClient() {
        mClient=new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .build();

        mRetrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(mClient)
                .build();

    }

    public UserAPI getUserAPI(){
        if(mUserAPI==null){
            mUserAPI=mRetrofit.create(UserAPI.class);
        }
        return mUserAPI;
    }

    public OkHttpClient getClient(){
        return mClient;
    }

    public static NetClient getInstance(){
        if(sClient==null){
            sClient=new NetClient();
        }
        return sClient;
    }




}
