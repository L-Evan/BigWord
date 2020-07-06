package com.example.levan.myapplication.tools;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 废弃中
 */
public class saveInfoFile {

    private saveInfoFile(){
    }
    //待继续完善
    public static boolean saveOutputStream(Context context, String username, String password) {
        try {
            // 使用Android上下问获取当前项目的路径
            File file = new File(context.getFilesDir(), "userinfo.txt");
            //调试
            //System.out.print(context.getFilesDir());
            // 创建输出流对象
            FileOutputStream fos = new FileOutputStream(file);
            // 向文件中写入信息
            fos.write((username + ":" + password+";").getBytes());
            // 关闭输出流对象
            fos.close();
            return true;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
    //登录
    public static Map<String, String> readUserInfo(Context context){
        try {
            // 创建FIle对象
            File file = new File(context.getFilesDir(), "userinfo.txt");
            // 创建BufferedReader对象
            BufferedReader br = new BufferedReader(new FileReader(file));
            Map<String, String> map = new HashMap<String, String>();
            while( br.ready()){
                String content = br.readLine();
                String [] contents0= content.split(";");
                String[] contents = contents0[0].split(":");
                map.put(contents[0], contents[1]);
            }
            br.close();
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
    //注册
    public static boolean saveUserInfo(Context context, String username, String password) {
        try {
            // 使用Android上下问获取当前项目的路径
            File file = new File(context.getFilesDir(), "userinfo.txt");
            // 创建输出流对象
            FileOutputStream fos = new FileOutputStream(file);
            // 向文件中写入信息
            String outinfo = username + ":" + password+";";
            System.out.println(outinfo);
            fos.write(outinfo.getBytes());
            // 关闭输出流对象
            fos.close();
            return true;
        } catch (Exception e) {
//            throw new RuntimeException();
            Toast.makeText(context,"注册失败",Toast.LENGTH_LONG).show();
        }
        return false;
    }
}
