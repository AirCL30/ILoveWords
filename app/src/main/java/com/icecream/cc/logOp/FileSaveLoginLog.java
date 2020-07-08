package com.icecream.cc.logOp;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

/**
 * Created by 陈湘龙 on 2020/5/14.
 */
public class FileSaveLoginLog {
    private static String fileName = "loginLog.txt";
    //保存登录信息到loginLog.txt中
    public static boolean saveLoginInfo(Context context,String userAccount,String userName,String date){

        try {
            //1.通过上下文获取文件输出流
            FileOutputStream fos = context.openFileOutput(fileName,context.MODE_APPEND);
            //2.把登录账号、登录时间写入loginLog.txt
            fos.write(("登录账户:" + userAccount + ",用户名:" + userName + ",登录时间:" + date + "\r\n").getBytes());
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //取出登录信息
    public static String getLoginInfo(Context context){
        String loginStr = "";
        try {
            FileInputStream fis = context.openFileInput(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis));
            String strTemp = null;
            while ((strTemp = bufferedReader.readLine()) != null){
                //System.out.println(strTemp);
                loginStr += strTemp + "\n\n";
            }
            bufferedReader.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loginStr;
    }
}
