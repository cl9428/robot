package com.cl.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cl.utils.JSONResult;
import com.cl.utils.TuLingApiUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


@RestController
@RequestMapping("/chat")
public class ChatbotController {
    @Autowired
    TuLingApiUtil tuLingApiUtil;
    @RequestMapping("/bot")
    public String chatBot(String question) throws IOException {
        // String APIKEY = "d3a63c004e614549a26beaa7fb9c53f5";//官网注册  76bf4eb4ff774f55a174f76ef0d151f4
        String APIKEY = "b6cc890990a04f23a6c6c71a4c36fb4c";
        String apiUrl = "http://www.tuling123.com/openapi/api?key=" + APIKEY + "&info=";
        try {
            //问题要urf-8编码
            question = URLEncoder.encode(question, "utf-8");
            //拼接 url
            apiUrl = apiUrl + question;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //请求的url
        URL url = null;
        //建立的http链接
        HttpURLConnection httpConn = null;
        //请求的输入流
        BufferedReader in = null;
        //输入流的缓冲
        StringBuffer sb = new StringBuffer();

        try {
            url = new URL(apiUrl);
            in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String str = null;
            //一行一行进行读入
            while ((str = in.readLine()) != null) {
                sb.append(str);
            }
        } catch (Exception ex) {
        } finally {
            try {
                if (in != null) {
                    in.close(); //关闭流
                }
            } catch (IOException ex) {

            }
        }
        String result = sb.toString();
        JsonObject json = new JsonParser().parse(result).getAsJsonObject();
        //获得text键的内容，并转化为string
        // System.out.println(json);
        String back = json.get("text").toString();
        return back;

    }

    @RequestMapping("/weChat")
    //获取响应json字符串
    public String robot(String message){
         String url = "http://www.tuling123.com/openapi/";
         String api_key = "29f09ec51fdd4633a7a8f514a52175d5";
         //String api_key = "76bf4eb4ff774f55a174f76ef0d151f4";

            //图灵api接口
            String apiUrl = url+"api?key="+api_key+"&info=";
            try {
                //设置编码格式
                message = URLEncoder.encode(message,"utf-8");
                //拼接url
                apiUrl = apiUrl + message;

            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }
            //封装http请求头
            HttpPost request = new HttpPost(apiUrl);
            String result = "";
            try {
                //发送http请求
                HttpResponse response = HttpClients.createDefault().execute(request);
                //获取响应码
                int code = response.getStatusLine().getStatusCode();
                if(code == 200){
                    //获取返回结果
                    result = EntityUtils.toString(response.getEntity());

                }else {
                    System.out.println("code="+code);
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;//返回结果{"code":xxx,"text":xxx}
        }

}
