package com.icecream.cc.ilovewords;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.icecream.cc.logOp.FileSaveLoginLog;

public class AdminViewLoginLogActivity extends AppCompatActivity {
    private TextView showLoginLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_login_log);

        //初始化控件
        init();

        //取出登录日志的信息
        String loginString = new FileSaveLoginLog().getLoginInfo(this);
        //登录信息为空，什么也不做
        //不为空，给Textview赋值
        if(loginString == ""){

        }else{
            showLoginLog.setText(loginString);
        }
    }

    private void init() {
        showLoginLog = (TextView) findViewById(R.id.tv_showLoginLog);
    }
}
