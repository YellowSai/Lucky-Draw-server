package cn.jzcscw.commons.exception;


import cn.jzcscw.commons.constant.Consts;

/**
 * scode运行期异常类
 */
public class JzRuntimeException extends RuntimeException {

    /**
     * 错误代码
     */
    private int code;

    /**
     * 只带异常的构造方法，code为-4
     *
     * @param e
     */
    public JzRuntimeException(Exception e) {
        super(e);
        this.code = Consts.ERROR_CODE;
    }

    /**
     * 只带错误信息的构造方法，code为-4
     *
     * @param message
     */
    public JzRuntimeException(String message) {
        super(message);
        this.code = Consts.ERROR_CODE;
    }

    public JzRuntimeException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
