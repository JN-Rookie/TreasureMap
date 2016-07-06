package edu.feicui.treasuremap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.feicui.treasuremap.R;
import edu.feicui.treasuremap.commons.ActivityUtils;
import edu.feicui.treasuremap.user.login.LoginActivity;
import edu.feicui.treasuremap.user.register.RegisterActivity;


/**
 * 入口界面
 */
public class MainActivity extends AppCompatActivity {

    private ActivityUtils mActivityUtils;

    //广播接收器(当登陆注册成功后入口页面进行关闭操作)
    private final BroadcastReceiver mReceiver =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityUtils=new ActivityUtils(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //注册本地广播(当登陆注册成功后入口页面进行关闭操作)
        IntentFilter intentFilter=new IntentFilter(Constants.ACTION_ENTER_HOME);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,intentFilter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消广播注册
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.btn_login, R.id.btn_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                mActivityUtils.startActivity(LoginActivity.class);
                break;
            case R.id.btn_register:
                mActivityUtils.startActivity(RegisterActivity.class);
                break;
        }
    }
}
