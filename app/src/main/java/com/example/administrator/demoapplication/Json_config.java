package com.example.administrator.demoapplication;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Json_config {
    private static String title="XXXX";
    private static String ip="XXXX";
    private static String port="XXXX";

    private static String background="/sdcard/dengdai/1.png";
    private static Integer columns_number=2;
    private static final List<String> TITLE =new ArrayList<String>();
    private static final List<String> IMGurl = new ArrayList<String>();
    private static final List<Integer> Length=new ArrayList<Integer>();
    private static final List<Integer> ID=new ArrayList<Integer>();
    private static String text;


    public Json_config() throws IOException, JSONException {
        InputStreamReader isr = new InputStreamReader(new FileInputStream("/sdcard/dengdai//setting.json"), "utf8");
        BufferedReader read = new BufferedReader(isr);
        String s=read.readLine();
        JSONObject jsonObject = new JSONObject(s);
        Log.e("ee", String.valueOf(jsonObject));
        this.title = jsonObject.getString("title");
        this.text = jsonObject.getString("text");
        this.columns_number = jsonObject.getInt("columns_number");
        JSONArray img_list = (JSONArray) jsonObject.get("img_list");
        this.background = jsonObject.getString("background");

        for (int i = 0; i< img_list.length(); i++) {
            //循环遍历，依次取出JSONObject对象
            //用getInt和getString方法取出对应键值
            JSONObject imgjsonObject = img_list.getJSONObject(i);
            Log.d("MainActivity", String.valueOf(imgjsonObject));
            String img_path = imgjsonObject.getString("img_path");
            int time = imgjsonObject.getInt("time");
            int id=imgjsonObject.getInt("id");
            Log.d("MainActivity","img_path: " + img_path);
            Log.d("MainActivity","time: " + time);
            this.IMGurl.add("/sdcard/dengdai/"+img_path);
            this.Length.add(time*1000);
            this.ID.add(id);

            this.ip = jsonObject.getString("comserverip");
            this.port = jsonObject.getString("comserverport");
        }
        isr.close();
        read.close();
    }

    public static Integer getColumns_number() {
        return columns_number;
    }

    public static List<Integer> getID() {
        return ID;
    }

    public static List<Integer> getLength() {
        return Length;
    }

    public static List<String> getIMGurl() {
        return IMGurl;
    }

    public static List<String> getTITLE() {
        return TITLE;
    }

    public static String getBackground() {
        return background;
    }

    public static String getText() {
        return text;
    }

    public static String getTitle() {
        return title;
    }

    public String getIp(){
        return ip;
    }

    public static String getPort() {
        return port;
    }
}
