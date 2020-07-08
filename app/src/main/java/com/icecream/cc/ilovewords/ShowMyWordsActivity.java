package com.icecream.cc.ilovewords;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.icecream.cc.bean.Word;
import com.icecream.cc.connection.MySQLiteOpenHelper;

public class ShowMyWordsActivity extends Activity {
    private ListView wordListView;
    private Word[] words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_my_words);

        //初始化控件
        init();

        //根据用户的tel从数据库中找单词数据
        Intent intent = getIntent();
        final String userTel = intent.getStringExtra("uTel");
                    //单词表和选择表内连接
        String sql = "select * from WordTable as wt inner join SelectionTable as st on wt.WordName=st.WordName where st.UserTel=?";
        String[] conditions = new String[]{userTel};
        words = queryWords(sql,conditions);

        //new 一个适配器
        MyBaseAdapter myBaseAdapter = new MyBaseAdapter();
        wordListView.setAdapter(myBaseAdapter);

        //为每个item添加点击事件
        //短按点击事件，进入单词详情界面
        wordListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //找到条目中控件
                TextView tv_word = (TextView) view.findViewById(R.id.tv_wordInItem);
                String name = tv_word.getText().toString();
                //跳转到单词详情页面
                Intent intent = new Intent(ShowMyWordsActivity.this,ShowWordDetailActivity.class);
                intent.putExtra("wordName",name);
                intent.putExtra("uTel",userTel);
                startActivity(intent);
            }
        });

        //长按item点击事件，删除单词
        wordListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //找到条目中控件
                TextView tv_word = (TextView) view.findViewById(R.id.tv_wordInItem);
                final String name = tv_word.getText().toString();
                //询问用户是否删除单词
                AlertDialog alertDialog = new AlertDialog.Builder(ShowMyWordsActivity.this)
                        .setTitle("删除提示框")
                        .setIcon(R.mipmap.ic_launcher)
                        .setMessage("是否确认删除此单词？")
                        .setPositiveButton("是",new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(ShowMyWordsActivity.this);
                                SQLiteDatabase db = mySQLiteOpenHelper.getReadableDatabase();
                                //db.execSQL("delete from SelectionTable where UserTel=? and WordName=?",new Object[]{userTel,name});
                                if(db.delete("SelectionTable","UserTel=? and WordName=?",new String[]{userTel,name}) == 1){
                                    Toast.makeText(ShowMyWordsActivity.this,"删除单词成功",Toast.LENGTH_LONG).show();
                                    refresh();
                                }else{
                                    Toast.makeText(ShowMyWordsActivity.this,"删除单词失败",Toast.LENGTH_LONG).show();
                                }
                                db.close();
                                return;
                            }
                        })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        })
                        .show();
                return false;
            }
        });
    }


    private void init() {
        wordListView = (ListView) findViewById(R.id.lv_wyWords);
    }

    //从数据库查询单词数据
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

    class MyBaseAdapter extends BaseAdapter{

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
            View view = View.inflate(ShowMyWordsActivity.this,R.layout.my_words_list_item,null);
            //找到条目中控件
            TextView tv_word = (TextView) view.findViewById(R.id.tv_wordInItem);
            TextView tv_IPA = (TextView) view.findViewById(R.id.tv_IPAInItem);
            TextView tv_meaning = (TextView) view.findViewById(R.id.tv_wordMeaningInItem);
            //给值
            tv_word.setText(words[position].getName());
            tv_IPA.setText(" " + words[position].getIPA());
            tv_meaning.setText(words[position].getMeaning());

            return view;
        }
    }

    //刷新activity
    private void refresh(){
        onCreate(null);
    }
}
