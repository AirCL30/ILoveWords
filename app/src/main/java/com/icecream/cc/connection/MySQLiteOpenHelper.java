package com.icecream.cc.connection;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by 陈湘龙 on 2020/5/13.
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    public MySQLiteOpenHelper(Context context) {
        //上下文、数据库名、游标工厂、版本
        super(context,"ILoveWords.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建4张表，UserTable,WordTable,SelectionTable,AdministratorTable
        Log.e("MySQLiteOpenHelper","MySQLiteOpenHelperMySQLiteOpenHelperMySQLiteOpenHelperMySQLiteOpenHelper");
        db.execSQL("CREATE TABLE UserTable (UserTel varchar(30) primary key,UserName varchar(30) not null,UserPassword varchar(30) not null)");
        System.out.println("创建用户表");
        db.execSQL("CREATE TABLE WordTable (WordName varchar(30) primary key,WordIPA varchar(50) not null,WordMeaning varchar(50) not null,Example varchar(200) not null)");
        System.out.println("创建单词表");
        db.execSQL("CREATE TABLE SelectionTable (UserTel varchar(30) not null,WordName varchar(30) not null)");
        System.out.println("创建选择表");
        db.execSQL("CREATE TABLE AdministratorTable (AdminCount varchar(30) primary key,AdminName varchar(30) not null,AdminPassword varchar(30) not null)");
        System.out.println("创建管理员表");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
