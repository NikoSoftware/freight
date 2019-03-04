package net.xiaomotou.freight.net;

import com.alibaba.fastjson.JSON;
import com.mysql.cj.x.protobuf.Mysqlx;

public class ResultModel<T> {

    public static final int OK = 1;
    public static final int ERROR = 0;

    private int code= OK;
    private String msg;
    private T data;

    public ResultModel() {
    }

    public ResultModel(T data) {
        this.data = data;
    }

    public ResultModel(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public ResultModel setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ResultModel setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public ResultModel setData(T data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
