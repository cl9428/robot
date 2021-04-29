package com.cl.utils;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
/**
 * 获取token类
 *
 * token过期处理 2018.10.24 16:34 参考官方jar
 */
public class AuthService {

    /**
     * 判断token是否过期
     */
    private static Calendar expireDate = null;
    private static boolean flag = false; // 是否已经获取过了

    public static Boolean needAuth() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, +1); // 当前日期加一天
        return Boolean.valueOf(!flag || c.after(expireDate));
    }

    /**
     * 获取权限token
     *
     * @return 返回示例： { "access_token":
     *         "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567",
     *         "expires_in": 2592000 }
     */
    public static String getAuth() {
        // 官网获取的 API Key 更新为你注册的
        String clientId = "oIhzjlpz6Fbi3TP3X76Y2uGg";
        // 官网获取的 Secret Key 更新为你注册的
        String clientSecret = "r4ffetewhfv7kdmcyWko46n7TaEox9xq";

        flag = true;

        return getAuth(clientId, clientSecret);
    }

    /**
     * 获取API访问token 该token有一定的有效期，需要自行管理，当失效时需重新获取.
     * @param clientId  - 百度云官网获取的 API Key
     * @param clientSecret - 百度云官网获取的 Securet Key
     * @return assess_token
     */
    private static String getAuth(String clientId, String clientSecret) {
        // 获取token地址
        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
        String getAccessTokenUrl = authHost
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + clientId
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + clientSecret;

        try {
            URL realUrl = new URL(getAccessTokenUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.connect();

            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.err.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = "";
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            /**
             * 返回结果示例
             */
            System.err.println("result:" + result);
            JSONObject jsonObject = new JSONObject(result);
            String access_token = jsonObject.getString("access_token");

            Integer expires_in = Integer.valueOf(jsonObject.getInt("expires_in"));

            System.out.println("expires_in:" + expires_in);

            Calendar c = Calendar.getInstance();
            System.out.println("现在日期：" + c.get(c.YEAR) + "/" + c.get(c.MONTH) + "/" + c.get(c.DAY_OF_MONTH));
            c.add(13, expires_in.intValue());
            System.out.println("过期日期：" + c.get(c.YEAR) + "/" + c.get(c.MONTH) + "/" + c.get(c.DAY_OF_MONTH));
            expireDate = c;

            return access_token;

        } catch (Exception e) {
            System.err.printf("获取token失败！");
            e.printStackTrace(System.err);
        }
        return null;
    }

    public static void main(String[] args) {

        System.out.println("flag:" + flag);

        System.out.println("needAuth():" + needAuth().booleanValue());

        //第一次请求
        if (needAuth().booleanValue()) {
            String access_token = getAuth();

            System.out.println("flag:" + flag);

            System.out.println("access_token:" + access_token);
        } else {
            System.out.println("token未过期，不需要重新获取");
        }

        //第二次请求
        if (needAuth().booleanValue()) {
            String access_token = getAuth();

            System.out.println("flag:" + flag);

            System.out.println("access_token:" + access_token);
        } else {
            System.out.println("token未过期，不需要重新获取");
        }

    }

}