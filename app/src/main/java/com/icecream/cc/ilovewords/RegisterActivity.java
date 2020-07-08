package com.icecream.cc.ilovewords;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.icecream.cc.bean.User;
import com.icecream.cc.connection.MySQLiteOpenHelper;

public class RegisterActivity extends Activity {
    private EditText et_uname;
    private EditText et_utel;
    private EditText et_upwd;
    private EditText et_upwdAgain;
    private Button bt_register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //初始化控件
        init();

        //绑定监听事件
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //检查输入的合法性
                String uName = et_uname.getText().toString();
                String uTel = et_utel.getText().toString();
                String uPwd = et_upwd.getText().toString();
                String uPwdAgain = et_upwdAgain.getText().toString();
                //如果有一项输入为空
                if(uName.trim().isEmpty() || uTel.trim().isEmpty() || uPwd.trim().isEmpty() || uPwdAgain.trim().isEmpty()){
                    Toast.makeText(RegisterActivity.this,"请输入完整内容",Toast.LENGTH_LONG).show();
                    return;
                }
                //如果手机号不是11位
                if(uTel.length() != 11){
                    Toast.makeText(RegisterActivity.this,"手机号不足11位",Toast.LENGTH_LONG).show();
                    return;
                }
                //如果密码和确认密码不一致
                if(!uPwd.equals(uPwdAgain)){
                    Toast.makeText(RegisterActivity.this,"两次输入密码不一致",Toast.LENGTH_LONG).show();
                    return;
                }
                //检查此用户是否已存在
                String sql = "select * from UserTable where UserTel=?";
                String[] condition = new String[]{uTel};
                boolean userExist = userIsExist(sql, condition);
                if(userExist){
                    Toast.makeText(RegisterActivity.this,"该用户已存在，请勿重复注册",Toast.LENGTH_LONG).show();
                    return;
                }else{
                    insertUser(new User(uTel, uName, uPwd));
                    Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });
    }

    private void init() {
        et_uname = (EditText) findViewById(R.id.et_registerUname);
        et_utel = (EditText) findViewById(R.id.et_registerUTel);
        et_upwd = (EditText) findViewById(R.id.et_registerPassword);
        et_upwdAgain = (EditText) findViewById(R.id.et_registerPasswordAgain);
        bt_register = (Button) findViewById(R.id.bt_register);
    }

    //查询用户
    private boolean userIsExist(String sql,String[] conditions){
        MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(this);
        SQLiteDatabase db = mySQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,conditions);

        //没有查到结果，返回
        if(cursor.getCount() == 0){
            cursor.close();
            db.close();
            return false;
        }else{
            cursor.close();
            db.close();
            return true;
        }
    }

    //插入一个用户
    public void insertUser(User user){
        MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(this);
        SQLiteDatabase db = mySQLiteOpenHelper.getReadableDatabase();
        ContentValues contentValues  = new ContentValues();
        contentValues.put("UserTel",user.getUtel());
        contentValues.put("UserName",user.getUname());
        contentValues.put("UserPassword",user.getUpassword());
        db.insert("UserTable",null,contentValues);
        db.close();
    }
}
