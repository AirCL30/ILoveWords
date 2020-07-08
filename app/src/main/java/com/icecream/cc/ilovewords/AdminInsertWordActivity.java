package com.icecream.cc.ilovewords;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.icecream.cc.bean.Word;
import com.icecream.cc.connection.MySQLiteOpenHelper;

public class AdminInsertWordActivity extends AppCompatActivity {
    private EditText et_wordName;
    private EditText et_wordIPA;
    private EditText et_wordMeaning;
    private EditText et_wordExampe;
    private Button bt_addToWorkBank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_insert_word);

        //初始化控件
        init();

        //添加按钮点击
        bt_addToWorkBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取内容
                String name = et_wordName.getText().toString();
                String ipa = et_wordIPA.getText().toString();
                String meaning  = et_wordMeaning.getText().toString();
                String example = et_wordExampe.getText().toString();
                if(name.trim().isEmpty() || ipa.trim().isEmpty() || meaning.trim().isEmpty() || example.trim().isEmpty()){
                    Toast.makeText(AdminInsertWordActivity.this,"内容不完整",Toast.LENGTH_LONG).show();
                    return;
                }
                Word newWord = new Word(name,ipa,meaning,example);
                //查询单词库是否已经有此单词
                String sql = "select * from WordTable";
                Word[] words = queryWords(sql,null);
                //词库中没有单词
                if(words.length == 0){
                    MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(AdminInsertWordActivity.this);
                    SQLiteDatabase db = mySQLiteOpenHelper.getReadableDatabase();
                    db.execSQL("insert into WordTable values(?,?,?,?)",new Object[]{newWord.getName(),newWord.getIPA(),newWord.getMeaning(),newWord.getExample()});
                    db.close();
                    Toast.makeText(AdminInsertWordActivity.this,"添加单词成功",Toast.LENGTH_LONG).show();
                    return;
                }
                //词库中有单词，检查是否已存在
                boolean isExist = false;
                for(int i = 0;i < words.length;i ++){
                    if(words[i].getName().equals(newWord.getName())){
                        isExist = true;
                        break;
                    }
                }
                if(isExist){
                    Toast.makeText(AdminInsertWordActivity.this,"该单词已存在，不需要重复添加",Toast.LENGTH_LONG).show();
                    return;
                }else{
                    MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(AdminInsertWordActivity.this);
                    SQLiteDatabase db = mySQLiteOpenHelper.getReadableDatabase();
                    db.execSQL("insert into WordTable values(?,?,?,?)",new Object[]{newWord.getName(),newWord.getIPA(),newWord.getMeaning(),newWord.getExample()});
                    db.close();
                    Toast.makeText(AdminInsertWordActivity.this,"添加单词成功",Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });
    }

    private void init() {
        et_wordName = (EditText) findViewById(R.id.et_addWordName);
        et_wordIPA = (EditText) findViewById(R.id.et_addWordIPA);
        et_wordMeaning = (EditText) findViewById(R.id.et_addWordMeaning);
        et_wordExampe = (EditText) findViewById(R.id.et_addWordExample);
        bt_addToWorkBank = (Button) findViewById(R.id.bt_addWordToWordBank);
    }

    //从单词表里查出单词
    private Word[] queryWords(String sql,String[] conditions){
        MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(this);
        SQLiteDatabase db = mySQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,conditions);
        Word[] queryWords = new Word[cursor.getCount()];

        //该用户没有单词
        if(cursor.getCount() == 0){
            cursor.close();
            db.close();
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
}
