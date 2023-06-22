package com.gaokao.common.meta;

import com.gaokao.common.enums.BusinessCode;
import org.springframework.http.HttpStatus;

/**
 * @author attack204
 * date:  2021/7/19
 * email: 757394026@qq.com
 */
public class AjaxResult<T> {
    private int code;
    private String msg;
    private T data;

    public AjaxResult() {
    }

    public AjaxResult(BusinessCode code, String msg, T data) {
        this.code = code.getCode();
        this.msg = msg;
        this.data = data;
    }

    public AjaxResult(BusinessCode code, String msg) {
        this.code = code.getCode();
        this.msg = msg;
    }

    public AjaxResult(BusinessCode code) {
        this.code = code.getCode();
    }

    public AjaxResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> AjaxResult<T> SUCCESSMSG(String msg) {
        return new AjaxResult<>(HttpStatus.OK.value(), msg, null);
    }

    public static <T> AjaxResult<T> SUCCESS(T data) {
        return new AjaxResult<>(BusinessCode.SUCCESS, "success", data);
    }

    public static <T> AjaxResult<T> FAIL(String msg) {
        return new AjaxResult<>(BusinessCode.FAIL, msg, null);
    }

    public static <T> AjaxResult<T> FORBIDDEN(String msg) {
        return new AjaxResult<>(BusinessCode.FORBIDDEN, msg, null);
    }

    public static <T> AjaxResult<T> UNAUTHORIZED(String msg) {
        return new AjaxResult<>(BusinessCode.UNAUTHORIZED, msg, null);
    }


    public static <T> AjaxResult<T> init(BusinessCode businessCode, String msg) {
        return new AjaxResult<>(businessCode, msg, null);
    }


    public static <T> AjaxResult<T> init(BusinessCode businessCode, String msg, T data) {
        return new AjaxResult<>(businessCode, msg, data);
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

