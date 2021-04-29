package com.cl.controller;

import com.cl.utils.AuthService;
import com.cl.utils.Record;
import com.cl.utils.SpeechRec;
import com.cl.utils.TulingRobot;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


@RestController
@RequestMapping("/chat")
public class ChatbotController {

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
    public String robot(String message) {
        String url = "http://www.tuling123.com/openapi/";
        String api_key = "29f09ec51fdd4633a7a8f514a52175d5";
        //String api_key = "76bf4eb4ff774f55a174f76ef0d151f4";

        //图灵api接口
        String apiUrl = url + "api?key=" + api_key + "&info=";
        try {
            //设置编码格式
            message = URLEncoder.encode(message, "utf-8");
            //拼接url
            apiUrl = apiUrl + message;

        } catch (UnsupportedEncodingException e) {
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
            if (code == 200) {
                //获取返回结果
                result = EntityUtils.toString(response.getEntity());

            } else {
                System.out.println("code=" + code);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;//返回结果{"code":xxx,"text":xxx}
    }


    /**
     * 机器人圆圆
     */

    public static class YuanYuan {
        private static File recordFile = new File("F:\\record.wav");
        private static String playFileName = "F:\\play.wav";

        public static void main(String[] args) {
            /**
             * 录音-->百度语音识别，获取到文本-->文本传入到图灵机器人与之对话-->拿到图灵机器人的回话
             * -->百度语音合成，将回话转为音频文件-->播放音频文件
             * 以上完成一次完整的对话
             * 若我说：退出-->百度语音识别为“退出”-->百度语音合成文本“好的，那下次再见了”-->播放音频文件-->退出程序
             * 若我说的是歌名-->播放音乐（前提是音乐已经下载好了）
             */

            Record record = new Record();
            SpeechRec speechRec = new SpeechRec();
            TulingRobot tulingRobot = new TulingRobot();

            while (true) {
                record.record();//录音
                try {
                    AuthService.getAuth();
                    String recordText = speechRec.ASR(recordFile);//百度语音识别
                    if (recordText.equals("退出。")) {
                        speechRec.TTS("好的，那下次再见了");
                        System.out.println("(我):退出");
                        System.out.println("(圆圆):" + "好的，那下次再见了");
                        record.play(playFileName);//播放合成的音频文件
                        System.exit(0);//退出程序
                    }
                    if (recordText.equals("")) {
                        speechRec.TTS("你啥也不说，我如何作答呀");
                        System.out.println("(我):" + recordText);
                        System.out.println("(圆圆):" + "你啥也不说，我如何作答呀");
                        record.play(playFileName);//播放合成的音频文件
                        continue;
                    }
                    /**
                     *  播放音乐（可配置）
                     *  配置过程：在指定文件夹下添加下载好的音乐-->如果识别的是音乐名，播放音频文件即可
                     */
                    if (recordText.equals("意外。")) {
                        System.out.println("音乐播放中...");
                        record.play("F:\\music\\意外.mp3");
                        continue;
                    }
                    if (recordText.equals("告白之夜。")) {
                        System.out.println("音乐播放中...");
                        record.play("F:\\music\\告白之夜.mp3");
                        continue;
                    }
                    if (recordText.equals("工资向北走。")) {//百度语音识别总是将“公子”识别成“工资”，可能是我发音不对吧哈哈                   record.play("F:\\music\\公子向北走.mp3");
                        System.out.println("音乐播放中...");
                        record.play("F:\\music\\公子向北走.mp3");
                        continue;
                    }//按以上格式添加配置即可

                    String result = tulingRobot.parseJson(tulingRobot.getResult(recordText));//得到图灵机器人的回话
                    speechRec.TTS(result);//百度语音合成
                    System.out.println("(我):" + recordText);
                    System.out.println("(圆圆):" + result);
                    record.play(playFileName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
