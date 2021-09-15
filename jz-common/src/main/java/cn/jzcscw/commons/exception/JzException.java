package cn.jzcscw.commons.exception;


import cn.jzcscw.commons.constant.Consts;

/**
 * jz异常类
 */
public class JzException extends Exception {

    /**
     * 错误代码
     */
    private int code;

    public JzException(int code, String message) {
        super(message);
        this.code = code;
    }

    public JzException(Exception e) {
        super(e);
        this.code = Consts.ERROR_CODE;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
