package com.icecream.cc.ilovewords;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.icecream.cc.bean.Administrator;
import com.icecream.cc.connection.MySQLiteOpenHelper;
import com.icecream.cc.logOp.FileSaveLoginLog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.InflaterInputStream;

public class AdminLoginActivity extends AppCompatActivity {
    private EditText et_adminLoginAccount;
    private EditText et_adminLoginPwd;
    private Button bt_adminLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        //初始化控件
        init();

        //登录点击
        bt_adminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String adminAccount = et_adminLoginAccount.getText().toString();
                String adminPwd = et_adminLoginPwd.getText().toString();
                if(adminAccount.trim().isEmpty() || adminPwd.trim().isEmpty()){
                    Toast.makeText(AdminLoginActivity.this,"请填写完整信息",Toast.LENGTH_LONG).show();
                    return;
                }
                String sql = "select * from AdministratorTable where AdminCount=?";
                String[] conditions = new String[]{adminAccount};
                Administrator[] administrators = queryAdmin(sql,conditions);
                if(administrators.length == 0){
                    Toast.makeText(AdminLoginActivity.this,"该管理员不存在",Toast.LENGTH_LONG).show();
                    return;
                }else{
                    if(adminAccount.equals(administrators[0].getAccount()) && adminPwd.equals(administrators[0].getPwd())){
                        //1.登录信息记入登录日志
                        Date date = new Date();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String dateStr = formatter.format(date);
                        if(new FileSaveLoginLog().saveLoginInfo(AdminLoginActivity.this,administrators[0].getAccount(),administrators[0].getName(),dateStr)){
                            System.out.println("管理员登录信息写入日志成功");
                        }else{
                            System.out.println("管理员登录信息写入日志失败");
                        }
                        Intent intent = new Intent(AdminLoginActivity.this,AdministratorActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(AdminLoginActivity.this,"密码错误",Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }
        });
    }

    private void init() {
        et_adminLoginAccount = (EditText) findViewById(R.id.et_adminLoginAccount);
        et_adminLoginPwd = (EditText) findViewById(R.id.et_adminLoginPassword);
        bt_adminLogin = (Button) findViewById(R.id.bt_adminLogin);
    }

    //查管理员表
    private Administrator[] queryAdmin(String sql,String[] conditions){
        MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(this);
        SQLiteDatabase db = mySQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,conditions);
        Administrator[] admins = new Administrator[cursor.getCount()];

        //没有查到结果，返回
        if(cursor.getCount() == 0){

        }else{
            int rowCount = 0;
            while (cursor.moveToNext()){
                admins[rowCount++] = new Administrator(cursor.getString(0),cursor.getString(1),cursor.getString(2));
            }
        }
        cursor.close();
        db.close();
        return admins;
    }
}
