package com.icecream.cc.ilovewords;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.icecream.cc.bean.BasicWordsInDB;
import com.icecream.cc.bean.User;
import com.icecream.cc.connection.MySQLiteOpenHelper;
import com.icecream.cc.logOp.FileSaveLoginLog;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private EditText et_loginAccount;
    private EditText et_loginPwd;
    private Button bt_login;
    private TextView tv_forgetPwd;
    private TextView tv_gotoRegister;
    private Button bt_gotoAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化单词库
        initWordDabase();

        //初始化控件
        init();

        //绑定登录按钮点击事件
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = et_loginAccount.getText().toString();
                String pwd = et_loginPwd.getText().toString();
                //检查输入是否为空
                if(account.trim().isEmpty() || pwd.trim().isEmpty()){
                    Toast.makeText(MainActivity.this,"请输入完整信息",Toast.LENGTH_LONG).show();
                    return;
                }
                //检查用户信息是否正确
                String sql = "select * from UserTable where UserTel=?";
                String[] condition = new String[]{account};
                User[] users = queryUsers(sql,condition);
                if(users.length == 0){
                    Toast.makeText(MainActivity.this,"用户不存在",Toast.LENGTH_LONG).show();
                    return;
                }else{
                    if(account.equals(users[0].getUtel()) && pwd.equals(users[0].getUpassword())){
                        //用户名和密码都正确，登录成功
                        //1.登录信息记入登录日志
                        Date date = new Date();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String dateStr = formatter.format(date);
                        if(new FileSaveLoginLog().saveLoginInfo(MainActivity.this,users[0].getUtel(),users[0].getUname(),dateStr)){
                            System.out.println("用户登录信息写入日志成功");
                        }else{
                            System.out.println("用户登录信息写入日志失败");
                        }
                        //2.打开用户对应的展示自己的界面
                        Intent intent = new Intent(MainActivity.this,UserPanelActivity.class);
                            //把uTel、uName传递过去
                        intent.putExtra("uName",users[0].getUname());
                        intent.putExtra("uTel",users[0].getUtel());
                        startActivity(intent);
                    }else{
                        Toast.makeText(MainActivity.this,"密码错误",Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }
        });

        //绑定注册按钮点击事件
        tv_gotoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        //进入管理员界面按钮
        bt_gotoAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AdminLoginActivity.class);
                startActivity(intent);
            }
        });
    }

    //初始化单词库
    private void initWordDabase() {
        //检查单词库内是否有单词，有的话说明已经初始化过了，不需要再次初始化
        MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(this);
        SQLiteDatabase db = mySQLiteOpenHelper.getReadableDatabase();
        String querySql = "select * from WordTable";
        Cursor cursor = db.rawQuery(querySql,null);
        if(cursor.getCount() != 0){
            System.out.println("单词库已经初始化过了，无需再次初始化");
            cursor.close();
            db.close();
            return;
        }else{
            cursor.close();
            BasicWordsInDB basicWordsInDB = new BasicWordsInDB();
            String[] wordsName = basicWordsInDB.getNames();
            String[] wordsIPA = basicWordsInDB.getIPA();
            String[] wordsMeaning = basicWordsInDB.getMeaning();
            String[] wordsExample = basicWordsInDB.getExample();
            for(int i = 0;i < wordsName.length;i ++){
                db.execSQL("insert into WordTable values(?,?,?,?)",new Object[]{wordsName[i],wordsIPA[i],wordsMeaning[i],wordsExample[i]});
            }
            System.out.println("单词库初始化完毕");
            //插入一个管理员账户
            db.execSQL("insert into AdministratorTable values(?,?,?)",new Object[]{"12138","admin001","12138"});
            System.out.println("管理员初始化完毕");
            db.close();
            return;
        }
    }

    private void init() {
        et_loginAccount = (EditText) findViewById(R.id.et_loginAccount);
        et_loginPwd = (EditText) findViewById(R.id.et_loginPassword);
        bt_login = (Button) findViewById(R.id.bt_login);
        tv_forgetPwd = (TextView) findViewById(R.id.tv_forgetPwd);
        tv_gotoRegister = (TextView) findViewById(R.id.tv_register);
        bt_gotoAdmin = (Button) findViewById(R.id.bt_gotoAdmin);
    }

    //查询用户
    private User[] queryUsers(String sql,String[] conditions){
        MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(this);
        SQLiteDatabase db = mySQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,conditions);
        User[] users = new User[cursor.getCount()];

        //没有查到结果，返回
        if(cursor.getCount() == 0){

        }else{
            int rowCount = 0;
            while (cursor.moveToNext()){
                users[rowCount++] = new User(cursor.getString(0),cursor.getString(1),cursor.getString(2));
            }
        }
        cursor.close();
        db.close();
        return users;
    }
}
