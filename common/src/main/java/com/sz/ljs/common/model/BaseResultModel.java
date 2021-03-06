package com.sz.ljs.common.model;

public class BaseResultModel {

    /**
     * Code : 1
     * Msg : OK
     * Data : 入库成功
     */

    private int Code;
    private String Msg;
    private String Data;

    public int getCode() {
        return Code;
    }

    public void setCode(int Code) {
        this.Code = Code;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public String getData() {
        return Data;
    }

    public void setData(String Data) {
        this.Data = Data;
    }
}
