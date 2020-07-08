package com.icecream.cc.ilovewords;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.icecream.cc.bean.User;
import com.icecream.cc.bean.Word;
import com.icecream.cc.connection.MySQLiteOpenHelper;

public class ShowWordDetailActivity extends AppCompatActivity {
    private TextView tv_wordName;
    private TextView tv_wordIPA;
    private TextView tv_wordMeaning;
    private TextView tv_wordExample;
    private ImageView iv_add;
    private ImageView iv_share;
    private String wordName;
    private String userTel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_word_detail);

        //初始化控件
        init();

        //接受单词名称
        Intent intent = getIntent();
        wordName = intent.getStringExtra("wordName");
        userTel = intent.getStringExtra("uTel");

        //根据单词名称查询详情
        String sql = "select * from WordTable where WordName=?";
        final String[] conditions = new String[]{wordName};
        Word[] words = queryWordDetails(sql,conditions);

        //给各个控件赋值
        tv_wordName.setText(words[0].getName());
        tv_wordIPA.setText(words[0].getIPA());
        tv_wordMeaning.setText(words[0].getMeaning());
        tv_wordExample.setText(words[0].getExample());

        //添加图片点击事件
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先检查这个单词是否已经被该用户添加过了
                String sql = "select * from SelectionTable where UserTel=? and WordName=?";
                String[] conditions = new String[]{userTel,wordName};
                MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(ShowWordDetailActivity.this);
                SQLiteDatabase db = mySQLiteOpenHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery(sql,conditions);
                //添加过了
                if(cursor.getCount() != 0){
                    cursor.close();
                    db.close();
                    Toast.makeText(ShowWordDetailActivity.this,"此单词已经添加过了",Toast.LENGTH_LONG).show();
                    return;
                }else{
                    cursor.close();
                    db.execSQL("insert into SelectionTable values(?,?)", new Object[]{userTel, wordName});
                    db.close();
                    Toast.makeText(ShowWordDetailActivity.this,"添加成功",Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });

        //分享图片点击事件
        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //展示当前的所有用户
                String sql = "select * from UserTable";
                User[] users = queryUser(sql, null);
                if(users.length == 0){
                    Toast.makeText(ShowWordDetailActivity.this,"没用用户可进行分享",Toast.LENGTH_LONG).show();
                    return;
                }else{
                    final String[] userSelections = new String[users.length - 1];
                    for(int i = 0, j = 0;i < users.length;i ++) {
                        //跳过自己
                        if(users[i].getUtel().equals(userTel)){
                            continue;
                        }
                        userSelections[j] = new String();
                        userSelections[j++] = users[i].getUtel();
                    }
                    //选中的用户
                    final User selectedUser = new User();
                    //显示可分享用户的单选对话框
                    AlertDialog alertDialog = new AlertDialog.Builder(ShowWordDetailActivity.this)
                            .setTitle("请选择想要分享的用户")
                            .setIcon(R.mipmap.ic_launcher)
                            .setSingleChoiceItems(userSelections, -1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    selectedUser.setUtel(userSelections[which]);
                                }
                            })
                            .setPositiveButton("分享", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //未选择的话，提示
                                    if (selectedUser.getUtel() == null) {
                                        Toast.makeText(ShowWordDetailActivity.this,"未选择分享用户",Toast.LENGTH_LONG).show();
                                    } else {
                                        shareWord(selectedUser);
                                    }
                                    return;
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            })
                            .show();
                }
            }
        });
    }

    private void init() {
        tv_wordName = (TextView) findViewById(R.id.tv_wordNameInDetail);
        tv_wordIPA = (TextView) findViewById(R.id.tv_wordIPAInDetail);
        tv_wordMeaning = (TextView) findViewById(R.id.tv_wordMeaningInDetail);
        tv_wordExample = (TextView) findViewById(R.id.tv_wordExampleInDetail);
        iv_add = (ImageView) findViewById(R.id.iv_addImg);
        iv_share = (ImageView) findViewById(R.id.iv_shareImg);
    }

    //从单词库中查询这个单词的详情
    private Word[] queryWordDetails(String sql,String[] conditions){
        MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(this);
        SQLiteDatabase db = mySQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,conditions);
        Word[] queryWords = new Word[cursor.getCount()];

        //该用户没有单词
        if(cursor.getCount() == 0){
            cursor.close();
            db.close();
            queryWords = new Word[1];
            queryWords[0] = new Word("","","词库中暂时没有这个单词，管理员正在快马加鞭更新中......","");
            return queryWords;
        }else{
            //取单词
            int rowCount = 0;
            while (cursor.moveToNext()){
                queryWords[rowCount++] = new Word(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3));
            }
            cursor.close();
            db.close();
            return queryWords;
        }
    }

    //查询用户
    private User[] queryUser(String sql,String[] conditions){
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

    //分享单词
    private void shareWord(User selectUser){
        //先检查这个单词是否已经被该用户添加过了
        String sql = "select * from SelectionTable where UserTel=? and WordName=?";
        String[] conditions = new String[]{selectUser.getUtel(),wordName};
        MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(ShowWordDetailActivity.this);
        SQLiteDatabase db = mySQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, conditions);
        //添加过了
        if(cursor.getCount() != 0){
            cursor.close();
            db.close();
            Toast.makeText(ShowWordDetailActivity.this,"此单词该用户已经添加过了",Toast.LENGTH_LONG).show();
            return;
        }else{
            cursor.close();
            db.execSQL("insert into SelectionTable values(?,?)", new Object[]{selectUser.getUtel(), wordName});
            db.close();
            Toast.makeText(ShowWordDetailActivity.this,"分享成功",Toast.LENGTH_LONG).show();
            return;
        }
    }
}
