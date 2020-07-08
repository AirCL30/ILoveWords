package com.icecream.cc.ilovewords;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.ToneGenerator;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.icecream.cc.bean.User;
import com.icecream.cc.bean.Word;
import com.icecream.cc.connection.MySQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AdministratorActivity extends Activity {
    private ListView wordListView;
    private Word[] words;
    private Button insertWordButton;
    private Button sendUserInfoButton;
    private Button downloadWordsButton;
    private Button viewLoginLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //初始化控件
        init();

        //取单词
        String sql = "select * from WordTable";
        words = queryWords(sql,null);

        //new 一个适配器
        MyBaseAdapter myBaseAdapter = new MyBaseAdapter();
        wordListView.setAdapter(myBaseAdapter);

        //长按item点击事件，删除单词
        wordListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //找到条目中控件
                TextView tv_word = (TextView) view.findViewById(R.id.tv_wordInItem);
                final String name = tv_word.getText().toString();
                //询问用户是否删除单词
                AlertDialog alertDialog = new AlertDialog.Builder(AdministratorActivity.this)
                        .setTitle("删除提示框")
                        .setIcon(R.mipmap.ic_launcher)
                        .setMessage("是否确认删除此单词？")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(AdministratorActivity.this);
                                SQLiteDatabase db = mySQLiteOpenHelper.getReadableDatabase();
                                if (db.delete("WordTable", "WordName=?", new String[]{name}) == 1) {
                                    Toast.makeText(AdministratorActivity.this, "删除单词成功", Toast.LENGTH_LONG).show();
                                    //从选择表中删除选择了这个单词的用户记录
                                    db.delete("SelectionTable", "WordName=?", new String[]{name});
                                    Toast.makeText(AdministratorActivity.this, "删除单词选择该单词用户成功", Toast.LENGTH_LONG).show();
                                    refresh();
                                } else {
                                    Toast.makeText(AdministratorActivity.this, "删除单词失败", Toast.LENGTH_LONG).show();
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

        //添加单词按钮
        insertWordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdministratorActivity.this,AdminInsertWordActivity.class);
                startActivity(intent);
            }
        });

        //发送用户信息按钮
        sendUserInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserInfo();
            }
        });

        //下载单词按钮
        downloadWordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadWords();
            }
        });

        //打开登录日志界面
        viewLoginLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdministratorActivity.this,AdminViewLoginLogActivity.class);
                startActivity(intent);
            }
        });
    }

    private void init() {
        wordListView = (ListView) findViewById(R.id.lv_admin);
        insertWordButton = (Button) findViewById(R.id.bt_adminAddWord);
        sendUserInfoButton = (Button) findViewById(R.id.bt_sendUserInfoToServer);
        downloadWordsButton = (Button) findViewById(R.id.bt_downloadWords);
        viewLoginLog = (Button) findViewById(R.id.bt_aminViewLoginLog);
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
            View view = View.inflate(AdministratorActivity.this,R.layout.my_words_list_item,null);
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

    //刷新activity
    private void refresh(){
        onCreate(null);
    }

    //将用户信息封装成json
    private void sendUserInfo(){
        //预定一个通知项，userCount = 0表示没有用户，不等于0才有用户
        //没有用户，通知管理员，不发送数据到服务器
        //有用户，才发送用户信息到服务器
        String sql = "select * from UserTable";
        User[] users = queryUsers(sql,null);
        if(users.length == 0){
            Toast.makeText(this,"当前没有用户数据，无法发送用户信息",Toast.LENGTH_LONG).show();
            return;
        }
        //有用户
        JsonArray userJsonArray = new JsonArray();
        for(int i = 0;i < users.length;i ++){
            JsonObject userJson = new JsonObject();
            userJson.addProperty("uTel",users[i].getUtel());
            userJson.addProperty("uName",users[i].getUname());
            userJson.addProperty("uPassword",users[i].getUpassword());
            //加入Json数组
            userJsonArray.add(userJson);
        }
        System.out.println(userJsonArray.toString());
        postUserInfo(userJsonArray);
    }

    //从用户库里查用户信息
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

    //post发送Json给服务器
    private void postUserInfo(JsonArray userJsonArray){
        String path = "http://192.168.1.100:8080/ILoveWordsServer/servlet/GetUserInfoServlet";
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置超时时间
            conn.setConnectTimeout(5000);
            //请求方式
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);//设置向服务器写数据。
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.getOutputStream().write(String.valueOf(userJsonArray).getBytes());//把数据以流的方式写给服务器。

            //状态码
            int code = conn.getResponseCode();
            //请求成功
            if(code == 200){
                System.out.println("请求成功");
            }else{
                System.out.println("请求失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //向服务器请求单词Json
    private JSONArray getWordsJson() {
        JSONArray jsonArray = null;
        String path = "http://192.168.1.100:8080/ILoveWordsServer/servlet/DownloadWordsServlet";
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置超时时间
            conn.setConnectTimeout(5000);
            //请求方式
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);//设置向服务器写数据。
            conn.setDoInput(true);
            conn.setUseCaches(false);

            //接受数据
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] data = new byte[1024];
            int len = 0;
            InputStream is = conn.getInputStream();
            while ((len = is.read(data)) != -1) {
                outStream.write(data, 0, len);
            }
            is.close();
            //通过out.Stream.toByteArray获取服务器传过来的Json串
            String josnString = new String(outStream.toByteArray());
            System.out.println(josnString);

            //将json串转换成JsonArray
            jsonArray = new JSONArray(josnString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    //使用服务器传递过来的Json向单词库中写入数据
    private void downloadWords(){
        JSONArray wordsJsonArray = getWordsJson();
        //将wordsJsonArray转换成对象
        Word[] newWords = new Word[wordsJsonArray.length()];
        for(int i = 0;i < wordsJsonArray.length();i ++){
            //取出一组
            try {
                JSONObject jsonObject = wordsJsonArray.getJSONObject(i);
                //取出一个单词
                newWords[i] = new Word(jsonObject.getString("name"),jsonObject.getString("IPA"),jsonObject.getString("meaning"),jsonObject.getString("example"));
                //向单词表中插入单词，如果该单词已存在，跳过该单词
                //查询是否存在该单词
                String sql = "select * from WordTable where WordName=?";
                String[] conditons = new String[]{newWords[i].getName()};
                Word[] wordInBank = queryWords(sql,conditons);
                if(!wordInBank[0].getName().equals("空空如也")){
                    //跳过
                    continue;
                }else{
                    MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(this);
                    SQLiteDatabase db = mySQLiteOpenHelper.getReadableDatabase();
                    db.execSQL("insert into WordTable values(?,?,?,?)",new Object[]{newWords[i].getName(),newWords[i].getIPA(),newWords[i].getMeaning(),newWords[i].getExample()});
                    db.close();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Toast.makeText(this,"下载单词完毕，已自动添加到单词库",Toast.LENGTH_LONG).show();
        refresh();
    }
}
