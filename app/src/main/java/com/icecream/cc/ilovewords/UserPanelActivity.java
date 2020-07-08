package com.icecream.cc.ilovewords;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UserPanelActivity extends Activity {
    private TextView tv_showTel;
    private TextView tv_showName;
    private Button bt_openMywords;
    private Button bt_openWordDB;
    private Button bt_logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_panel);

        //初始化控件
        init();

        //初始化tel、name
        Intent intent = getIntent();
        final String userTel = intent.getStringExtra("uTel");
        String userName = intent.getStringExtra("uName");
        tv_showTel.setText(userTel);
        tv_showName.setText(userName);

        //我的单词点击事件
        bt_openMywords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(UserPanelActivity.this, ShowMyWordsActivity.class);
                intent2.putExtra("uTel", userTel);
                startActivity(intent2);
            }
        });

        //词库点击事件
        bt_openWordDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(UserPanelActivity.this,WordbankActivity.class);
                intent3.putExtra("uTel", userTel);
                startActivity(intent3);
            }
        });

        //退出按钮点击事件
        bt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        tv_showTel = (TextView) findViewById(R.id.tv_tel);
        tv_showName = (TextView) findViewById(R.id.tv_name);
        bt_openMywords = (Button) findViewById(R.id.bt_myWords);
        bt_openWordDB = (Button) findViewById(R.id.bt_wordDB);
        bt_logout = (Button) findViewById(R.id.bt_logout);
    }
}
