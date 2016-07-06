package edu.feicui.treasuremap.user.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.feicui.treasuremap.R;
import edu.feicui.treasuremap.Constants;
import edu.feicui.treasuremap.commons.ActivityUtils;
import edu.feicui.treasuremap.commons.RegexUtils;
import edu.feicui.treasuremap.components.AlertDialogFragment;
import edu.feicui.treasuremap.treasure.HomeActivity;
import edu.feicui.treasuremap.user.User;


public class LoginActivity extends MvpActivity<LoginView, LoginPresenter> implements LoginView {

    @NonNull
    @Override
    public LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @BindView(R.id.toolbar_login)
    Toolbar  mToolbar;
    @BindView(R.id.et_username)
    EditText mEtUsername;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.tv_forgetPassword)
    TextView mTvForgetPassword;
    @BindView(R.id.btn_Login)
    Button   mBtnLogin;

    private ActivityUtils mActivityUtils; // Activity常用工具集

    private String username; // 用来保存编辑框内的用户名
    private String password; // 用来保存编辑框内的密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityUtils = new ActivityUtils(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    /**
     * 设置toolbar标题
     */
    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getTitle());
        mEtPassword.addTextChangedListener(mTextWatcher);//密码监听
        mEtUsername.addTextChangedListener(mTextWatcher); //用户名监听
    }

    /**
     * 设置返回键功能
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_Login)
    public void onClick() {
        // 正则进行判断输入的用户名是否有效
        if (RegexUtils.verifyUsername(username) != RegexUtils.VERIFY_SUCCESS) {
            showUsernameError();
            return;
        }
        // 正则进行判断输入的密码是否有效
        if (RegexUtils.verifyPassword(password) != RegexUtils.VERIFY_SUCCESS) {
            showPasswordError();
            return;
        }
        getPresenter().login(new User(username,password));
    }

    /**
     * 监听输入框的方法
     */
    private final TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            username = mEtUsername.getText().toString();
            password = mEtPassword.getText().toString();
            boolean canLogin = TextUtils.isEmpty(username) || TextUtils.isEmpty(password);
            if (!canLogin) {
                mBtnLogin.setEnabled(true);
            } else {
                mBtnLogin.setEnabled(false);
            }
        }
    };



    private void showUsernameError() {
        String msg = getString(R.string.username_rules);
        AlertDialogFragment fragment = AlertDialogFragment.newInstance(R.string.username_error, msg);
        fragment.show(getSupportFragmentManager(), "showUsernameError");
    }

    private void showPasswordError() {
        String msg = getString(R.string.password_rules);
        AlertDialogFragment fragment = AlertDialogFragment.newInstance(R.string.password_error, msg);
        fragment.show(getSupportFragmentManager(), "showPasswordError");
    }

    private ProgressDialog progressDialog;

    @Override
    public void showProgress() {
        mActivityUtils.hideSoftKeyboard();
        progressDialog = ProgressDialog.show(this, "", "登陆中,请稍后...");
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

    @Override
    public void navigateToHome() {
        mActivityUtils.startActivity(HomeActivity.class);
        finish();
        // 发送广播
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(Constants.ACTION_ENTER_HOME));
    }

    @Override
    public void clearEditView() {
        mEtPassword.setText("");
    }
}
