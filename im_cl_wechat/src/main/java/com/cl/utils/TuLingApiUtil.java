package com.cl.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Scanner;

@Component
public class TuLingApiUtil {
    //图灵api key
    private static final String key = "d3a63c004e614549a26beaa7fb9c53f5";
    public static  String  getResult(String question){
        //图灵接口
        //String apiUrl = "http://openapi.tuling123.com/openapi/api/v2"+key"&info=";
        String apiUrl = "http://www.tuling123.com/openapi/api?key="+key+"&info=";
        try {
            //问题要urf-8编码
            question = URLEncoder.encode(question,"utf-8");
            //拼接 url
            apiUrl = apiUrl + question;
        }catch (Exception e){
            e.printStackTrace();
        }
        //封装请求头
        HttpGet request = new HttpGet(apiUrl);
        HttpResponse response = null;
        String result = "";
        try {
            //发送http请求
           response = HttpClients.createDefault().execute(request);
//           //获取响应码
//            int code = response.getStatusLine().getStatusCode();
             result  = EntityUtils.toString(response.getEntity());

        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }


   public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        while(true) {
            String question = "";
            //控制台输入信息
            question = sc.nextLine();
            //把json格式的字符串转化为json对象
            String result = getResult(question);

            //System.out.println(result);
            JsonObject json = new JsonParser().parse(result).getAsJsonObject();
            //获得text键的内容，并转化为string
           // System.out.println(json);
            String back = json.get("text").toString();
            System.out.println(back);

            //System.out.println(getResult(question));
        }
     }

}
