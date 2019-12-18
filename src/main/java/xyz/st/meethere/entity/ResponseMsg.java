package xyz.st.meethere.entity;

import java.util.HashMap;

/*
* 封装返回信息
* 需要带上返回状态码 用于向前端进行一些通知工作
* */
public class ResponseMsg {
    private int status;
    private HashMap<String, Object> responseMap = new HashMap<>();

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public HashMap<String, Object> getResponseMap() {
        return responseMap;
    }
}
