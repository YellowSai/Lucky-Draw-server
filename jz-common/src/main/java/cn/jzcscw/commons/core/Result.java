package cn.jzcscw.commons.core;

import cn.jzcscw.commons.constant.Consts;
import lombok.Data;

/**
 * 返回结果类
 */
@Data
public class Result<T> {
    private int code;
    private String msg;
    private T data;
    private String traceId;

    /**
     * 默认构造器，成功
     */
    public Result() {
        this.code = Consts.SUCCESS_CODE;
        this.msg = "success";
    }

    public static <T> Result<T> error() {
        return error(Consts.ERROR_CODE, "未知异常，请联系管理员");
    }

    public static <T> Result<T> error(String msg) {
        return error(Consts.ERROR_CODE, msg);
    }

    public static <T> Result<T> error(int code, String msg) {
        Result<T> r = new Result<T>();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }

    public static <T> Result<T> ok(String msg) {
        Result<T> r = new Result<T>();
        r.setMsg(msg);
        return r;
    }

    public static <T> Result<T> data(T data) {
        Result<T> r = new Result<T>();
        r.setData(data);
        return r;
    }

    public static <T> Result<T> ok() {
        return ok("success");
    }

}
