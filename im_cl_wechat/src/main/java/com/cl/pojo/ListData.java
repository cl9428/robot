package com.cl.pojo;


/**
 *
 * @author cl 封装数据
 */
public class ListData {
    public static final int SEND = 1;
    public static final int RECEIVE = 2;
    private int flag;
    private String content;// text
    private String time;
    private String url;// 链接类地址

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // 文字类
    public ListData(String contentt, int flag, String time) {
        super();
        setContent(contentt);
        setFlag(flag);
        setTime(time);
    }

    // 链接类
    public ListData(String contentt,String url, int flag, String time) {
        super();
        setContent(contentt);
        setFlag(flag);
        setTime(time);
        setUrl(url);
    }

}
