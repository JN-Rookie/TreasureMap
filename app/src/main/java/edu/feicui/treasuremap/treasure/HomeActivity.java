package edu.feicui.treasuremap.treasure;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.feicui.treasuremap.R;
import edu.feicui.treasuremap.commons.ActivityUtils;
import edu.feicui.treasuremap.user.UserPrefs;
import edu.feicui.treasuremap.user.account.AccountActivity;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar_home)
    Toolbar                 mToolbar;
    @BindView(R.id.nav_home)
    NavigationView          mNavHome;
    @BindView(R.id.drawer_layout)
    DrawerLayout            mDrawerLayout;

    private ImageView mIvUserIcon;

    private ActivityUtils mActivityUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityUtils=new ActivityUtils(this);
        setContentView(R.layout.activity_home);
    }

    @Override
    protected void onStart() {
        super.onStart();
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
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);//不显示左侧返回键
        mNavHome.setNavigationItemSelectedListener(this);//对NavigationView设置menu监听
        //drawerlayout监听(toolbar 和 drawerlayout),实现左侧按键的点击和动画效果
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,
                mDrawerLayout,mToolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();//同步
        //注意:不能用butterknife拿到
        mIvUserIcon= (ImageView) mNavHome.getHeaderView(0).findViewById(R.id.iv_userIcon);//关联头部view里的控件id
        mIvUserIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivityUtils.showToast("进入个人中心");
                mActivityUtils.startActivity(AccountActivity.class);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_hide:
                mActivityUtils.showToast(R.string.hide_treasure);
            break;
            case R.id.menu_item_my_list:
                mActivityUtils.showToast(R.string.my_list);
                break;
            case R.id.menu_item_help:
                mActivityUtils.showToast(R.string.about_help);
                break;
            case R.id.menu_item_logout:
                mActivityUtils.showToast(R.string.log_out);
                break;
        }
        return false;
    }
}
