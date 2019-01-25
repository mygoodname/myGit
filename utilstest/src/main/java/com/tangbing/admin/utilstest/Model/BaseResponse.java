package com.tangbing.admin.utilstest.Model;

/**
 * Created by liuhc on 2016/12/1.
 * 描述：
 * Version:1.0
 */
public class BaseResponse<T> {
    public boolean Success;
    public int Code;
    public String Description;
    public T Data;

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean success) {
        Success = success;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }
}
