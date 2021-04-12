package com.cl;

import com.alibaba.fastjson.JSONObject;
import com.cl.utils.JSONResult;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class test {
    public static void main(String[] args) throws IOException {

        Test("重庆大学附近的酒店");

    }
    public static JSONResult Test(String question) throws IOException {

        String info = URLEncoder.encode(question, "utf-8");
        String apiKey = "b6cc890990a04f23a6c6c71a4c36fb4c";
        String userId = "123456";
        String url = "http://www.tuling123.com/openapi/api?key="+apiKey
                +"&info="+info+"&userid="+userId;
        URL getUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection)getUrl.openConnection();
        connection.connect();
        //		获得输入流，并使用Reader读取
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream(),"utf-8"));
        StringBuffer stringBuffer = new StringBuffer();
        String line = "";
        while((line = reader.readLine()) != null){
            stringBuffer.append(line);
        }
//		断开链接
        reader.close();
        connection.disconnect();

        //把json格式的字符串转化为json对象
        JsonObject json = new JsonParser().parse(String.valueOf(stringBuffer)).getAsJsonObject();
        //获得text键的内容，并转化为string
        String back = json.get("text").toString();;
        return JSONResult.ok(back);
    }

}
