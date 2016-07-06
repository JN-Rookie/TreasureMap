package edu.feicui.treasuremap.user.account;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pkmmte.view.CircularImageView;

import org.hybridsquad.android.library.CropHandler;
import org.hybridsquad.android.library.CropHelper;
import org.hybridsquad.android.library.CropParams;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.feicui.treasuremap.R;
import edu.feicui.treasuremap.commons.ActivityUtils;
import edu.feicui.treasuremap.components.IconSelectWindow;
import edu.feicui.treasuremap.user.UserPrefs;


public class AccountActivity extends MvpActivity<AccountView,AccountPresenter>implements AccountView{

    @BindView(R.id.toolbar)
    Toolbar           mToolbar;
    @BindView(R.id.iv_userIcon)
    CircularImageView mIvUserIcon;

    private ActivityUtils mActivityUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityUtils=new ActivityUtils(this);
        setContentView(R.layout.activity_account);
        //取出最新的photoUrl
        String photoUrl= UserPrefs.getInstance().getPhoto();
        if(photoUrl!=null){
            ImageLoader.getInstance().displayImage(photoUrl,mIvUserIcon);
        }
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getTitle());
    }

    @NonNull
    @Override
    public AccountPresenter createPresenter() {
        return new AccountPresenter();
    }

    private IconSelectWindow mIconSelectWindow;

    //在当前用户中心界面按下图片后,弹出popwindow
    @OnClick(R.id.iv_userIcon)
    public void onClick(){
        if(mIconSelectWindow==null){
            mIconSelectWindow=new IconSelectWindow(this,mListener);
        }
        if(mIconSelectWindow.isShowing()){
            mIconSelectWindow.dismiss();
        }else{
            mIconSelectWindow.show();
        }
    }

    private CropHandler mCropHandler=new CropHandler() {
        @Override
        public void onPhotoCropped(Uri uri) {
            File file=new File(uri.getPath());
            getPresenter().uploadPhoto(file);
            //这里收到的uri就是处理完成的photo,等下做业务逻辑
            // 剪切成功 - 进行图像上传工作
            mActivityUtils.showToast("剪切成功");
        }

        @Override
        public void onCropCancel() {
            mActivityUtils.showToast("CropCancel");
        }

        @Override
        public void onCropFailed(String message) {
            mActivityUtils.showToast("CropFailed");
        }

        @Override
        public CropParams getCropParams() {
            CropParams params=new CropParams();
            //设置图片格式为300*300
            params.aspectX=300;
            params.aspectY=300;
            return params;
        }

        @Override
        public Activity getContext() {
            return AccountActivity.this;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //帮助我们处理结果(剪切出来的图片)
        CropHelper.handleResult(mCropHandler,requestCode, resultCode, data);
    }

    private final IconSelectWindow.Listener mListener=new IconSelectWindow.Listener() {
        @Override
        public void toGallery() {
            //按下相册按钮时(打开相册-到相册选择图像-队图像进行剪切-将图像传回来-将图像传回到服务器)
            mActivityUtils.showToast("使用相册图片");
            CropHelper.clearCachedCropFile(mCropHandler.getCropParams().uri);//清理现有图片
            Intent intent= CropHelper.buildCropFromGalleryIntent(mCropHandler.getCropParams());
            startActivityForResult(intent,CropHelper.REQUEST_CROP);
        }

        @Override
        public void toCamera() {
            //按下相机按钮时(打开相机-拍照-打开相册-到相册选择图像-队图像进行剪切-将图像传回来-将图像传回到服务器)
            mActivityUtils.showToast("调用相机");
            CropHelper.clearCachedCropFile(mCropHandler.getCropParams().uri);//清理现有图片
            Intent intent=CropHelper.buildCaptureIntent(mCropHandler.getCropParams().uri);
            startActivityForResult(intent,CropHelper.REQUEST_CAMERA);
        }
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private ProgressDialog progressDialog;

    @Override
    public void showProgress() {
        mActivityUtils.hideSoftKeyboard();
        progressDialog = ProgressDialog.show(this, "", "头像上传中,请稍后...");
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null&&progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showMessage(String msg) {
        mActivityUtils.showToast(msg);
    }

    //当头像更新操作完成了得到结果(url),presenter就会通知这边调用
    @Override
    public void updatePhoto(String url) {
        //imageloader使用前一定要初始化
        ImageLoader.getInstance().displayImage(url,mIvUserIcon);
    }

}
