package edu.feicui.treasuremap;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import edu.feicui.treasuremap.user.UserPrefs;


/**
 * Created by Administrator on 2016/7/5.
 */
public class TreasureApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //userprefs初始化配置
        UserPrefs.init(this);
        //ImageLoader初始化配置
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
        //百度地图初始化
//        SDKInitializer.initialize(getApplicationContext());

    }
}
