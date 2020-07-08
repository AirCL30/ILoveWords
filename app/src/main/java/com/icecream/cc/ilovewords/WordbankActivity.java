package com.icecream.cc.ilovewords;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.icecream.cc.bean.Word;
import com.icecream.cc.connection.MySQLiteOpenHelper;

public class WordbankActivity extends AppCompatActivity {
    private ListView wordbankListView;
    private Word[] words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordbank);

        //初始化控件
        init();

        //接受utel
        Intent intent = getIntent();
        final String userTel = intent.getStringExtra("uTel");

        //取单词
        String sql = "select * from WordTable";
        words = queryWords(sql,null);

        //new 一个适配器
        MyBaseAdapter myBaseAdapter = new MyBaseAdapter();
        wordbankListView.setAdapter(myBaseAdapter);

        //为每个item设置点击事件
        wordbankListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //找到条目中控件
                TextView tv_word = (TextView) view.findViewById(R.id.tv_wordInItem);
                String name = tv_word.getText().toString();
                //跳转到单词详情页面
                Intent intent = new Intent(WordbankActivity.this,ShowWordDetailActivity.class);
                intent.putExtra("wordName",name);
                intent.putExtra("uTel",userTel);
                startActivity(intent);
            }
        });
    }

    private void init() {
        wordbankListView = (ListView) findViewById(R.id.lv_wordbank);
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
            queryWords = new Word[1];
            queryWords[0] = new Word("空空如也","","","");
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

    class MyBaseAdapter extends BaseAdapter {

        //返回item总数
        @Override
        public int getCount() {
            return words.length;
        }

        @Override
        public Object getItem(int position) {
            return words[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        //得到item的视图
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //将item转化为视图
            View view = View.inflate(WordbankActivity.this,R.layout.my_words_list_item,null);
            //找到条目中控件
            TextView tv_word = (TextView) view.findViewById(R.id.tv_wordInItem);
            TextView tv_IPA = (TextView) view.findViewById(R.id.tv_IPAInItem);
            TextView tv_meaning = (TextView) view.findViewById(R.id.tv_wordMeaningInItem);
            //给值
            tv_word.setText(words[position].getName());
            tv_IPA.setText("  " + words[position].getIPA());
            tv_meaning.setText(words[position].getMeaning());

            return view;
        }
    }
}
