package com.cl.utils;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * 百度语音识别ASR() & 合成TTS()
 */
public class SpeechRec {
    private static final String serverURL = "http://vop.baidu.com/server_api";
    private static String token = "";

    private static final String apiKey = "oIhzjlpz6Fbi3TP3X76Y2uGg";
    private static final String secretKey = "r4ffetewhfv7kdmcyWko46n7TaEox9xq";
    private static final String cuid = "23966903";

    public static void main(String[] args) throws Exception {
        Record record = new Record();
        SpeechRec s = new SpeechRec();
        AuthService a = new AuthService();

        record.record();
       // s.getToken();
        a.getAuth();
        String str = s.ASR(new File("F:\\record.wav"));
        System.out.println(str);

    }

    //语音识别：音频-->文本，并返回解析后的文本
    public String ASR(File recordFile) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(serverURL
                + "?cuid=" + cuid + "&token=" + token).openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "audio/wav; rate=16000");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        // 发送请求
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.write(loadFile(recordFile));
        wr.flush();
        wr.close();

        String recordText = parseJson(printResponse(conn));//解析后的识别文本
        return recordText;
    }

    public void getToken() throws Exception {
        //初始化一个AipSpeech
        AipSpeech client = new AipSpeech(cuid, apiKey, secretKey);
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);


        String getTokenURL = "https://openapi.baidu.com/oauth/2.0/token?grant_type=client_credentials" +
                "&client_id=" + apiKey + "&client_secret=" + secretKey;
        HttpURLConnection conn = (HttpURLConnection) new URL(getTokenURL).openConnection();
        token = new JSONObject(printResponse(conn)).getString("access_token");
    }

    public String printResponse(HttpURLConnection conn) throws Exception {
        if (conn.getResponseCode() != 200) {
            System.out.println("conn.getResponseCode() = " + conn.getResponseCode());
            return "";
        }
        InputStream is = conn.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuffer response = new StringBuffer();
        while ((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        rd.close();
        return response.toString();
    }

    private byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            is.close();
            throw new IOException("Could not completely read file " + file.getName());
        }
        is.close();
        return bytes;
    }


    //解析后，得到百度语音识别后的文本
    public String parseJson(String response) {
        if (response == null) {
            return "";
        }
        net.sf.json.JSONObject ASRResult = net.sf.json.JSONObject.fromObject(response);

        String resultText = "";
        if (ASRResult.get("result") == null) {
            resultText = "[\"\"]";
        } else {
            resultText = ASRResult.get("result").toString();
        }

        //resultText = ASRResult.get("result").toString();
        resultText = resultText.substring(2, resultText.length() - 2);

        return resultText;
    }

    //语音合成，文本-->音频,返回得到的音频文件
    public File TTS(String text) {
        AipSpeech aipSpeech = new AipSpeech(cuid, apiKey, secretKey);
        HashMap options = new HashMap();
        options.put("spd", "5");
        options.put("pit", "5");
        options.put("per", "4");

        TtsResponse ttsResponse = aipSpeech.synthesis(text, "zh", 1, options);
        byte[] aa = ttsResponse.getData();
        getFile(aa, "F:\\play.wav");

        File file = new File("F:\\play.wav");

        return file;
    }

    //合成的二进制数据放入文件中
    private void getFile(byte[] binData, String filePathName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = new File(filePathName);
        try {
            fos = new FileOutputStream(file);
           //bos = new BufferedOutputStream(fos);
            bos.write(binData);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {//关闭流
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}