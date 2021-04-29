package com.cl.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**y
 * 图灵机器人
 */
public class TulingRobot {
    private static final String url = "http://www.tuling123.com/openapi/";
    private static final String api_key = "29f09ec51fdd4633a7a8f514a52175d5";


    //获取响应，得到响应的json字符串
    public String getResult( String message ) {
        //图灵api接口
        String apiUrl = url+"api?key="+api_key+"&info=";
        try {
            //设置编码格式
            message = URLEncoder.encode(message, "utf-8");
            //拼接url
            apiUrl = apiUrl + message;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //封装请求头
        HttpPost request = new HttpPost(apiUrl);
        String result = "";
        try {
            //发送http请求
            HttpResponse response = HttpClients.createDefault().execute(request);
            //获取响应码
            int code = response.getStatusLine().getStatusCode();

            if (code == 200) {
                //获取返回的结果
                result = EntityUtils.toString(response.getEntity());
            } else {
                System.out.println("code=" + code);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result; //返回结果{"code":xxx,"text":xxx}
    }

    //解析Json字符串
    public String parseJson(String jsonStirng){
        if(jsonStirng == null){
            return "";
        }
        //json字符串 --> json对象
        JSONObject results = JSONObject.fromObject(jsonStirng);

        String result = results.getString("text");
        return result;
    }
}