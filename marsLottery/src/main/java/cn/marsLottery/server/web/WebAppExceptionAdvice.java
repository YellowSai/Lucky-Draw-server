package cn.marsLottery.server.web;

import cn.jzcscw.commons.core.Result;
import cn.jzcscw.commons.exception.JzRuntimeException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.lang.reflect.UndeclaredThrowableException;
import java.sql.SQLException;

/**
 * 统一的RestController异常拦截处理
 *
 * @author tanghuang 2020年02月21日
 */
@Slf4j
@RestControllerAdvice
public class WebAppExceptionAdvice {

    @ExceptionHandler({Exception.class})
    @ResponseBody
    public Result<String> exceptionHandler(Exception ex) {
        log.error("ExceptionMsg:{}, Exception:{}", ex.getMessage(), ex);
        return Result.error("系统异常,请联系管理员");
    }

    @ExceptionHandler({UndeclaredThrowableException.class})
    @ResponseBody
    public Result<String> exceptionHandler(UndeclaredThrowableException ex) {
        log.error("ExceptionMsg:{}, Exception:{}", ex.getMessage(), ex);
        return Result.error("系统调用异常,请联系管理员");
    }

    /**
     * 自定义定义基础业务服务的异常
     */
    @ExceptionHandler(JzRuntimeException.class)
    @ResponseBody
    public Result<String> handlerBaseException(JzRuntimeException ex) {
        log.error("AidingRuntimeException:{}", ex.getMessage());
        return Result.error(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(InvalidFormatException.class)
    @ResponseBody
    public Result<String> handleInvalidFormatException(InvalidFormatException ex) {
        log.error("InvalidFormatException:{}", ex.getMessage());
        return Result.error(HttpStatus.BAD_REQUEST.value(), "参数类型错误，请检查输入");
    }

    /**
     * 使用@Valid 注解时校验数据，数据不合法时会抛出此异常
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public Result<String> handleBindingException(BindException ex) {
        StringBuilder stringBuilder = new StringBuilder("参数校验失败:");
        BindingResult bindingResult = ex.getBindingResult();
        for (ObjectError error : bindingResult.getAllErrors()) {
            stringBuilder.append(error.getDefaultMessage()).append("\n");
        }
        log.warn("BindException:{}", stringBuilder.toString());
        return Result.error(HttpStatus.BAD_REQUEST.value(), stringBuilder.toString());
    }

    /**
     * 使用@Valid + @ResponseBody注解时校验数据，数据不合法时会抛出此异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Result<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        StringBuilder stringBuilder = new StringBuilder("参数校验失败:");
        BindingResult bindingResult = ex.getBindingResult();
        for (ObjectError error : bindingResult.getAllErrors()) {
            stringBuilder.append(error.getDefaultMessage()).append("\n");
        }
        log.warn("MethodArgumentNotValidException:{}", stringBuilder.toString());
        return Result.error(HttpStatus.BAD_REQUEST.value(), stringBuilder.toString());
    }

    /**
     * ServletRequestBindingException
     */
    @ExceptionHandler(ServletRequestBindingException.class)
    @ResponseBody
    public Result<String> handleServletRequestBindingException(ServletRequestBindingException ex) {
        log.warn("ServletRequestBindingException:{}", ex.getMessage());
        return Result.error(HttpStatus.BAD_REQUEST.value(), "系统错误,请联系管理员");
    }

    /**
     * 未找到请求路径
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    public Result<String> handlerNotFoundException(HttpServletRequest req, NoHandlerFoundException ex) {
        log.warn("NoHandlerFoundException:{}", ex.getMessage());
        String errorURL = req.getRequestURL().toString();
        return Result.error(HttpStatus.NOT_FOUND.value(), "未找到路径 url:".concat(errorURL));
    }

    /**
     * 违反数据库约束异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public Result<String> handleConstraintViolationException(ConstraintViolationException ex) {
        log.warn("ConstraintViolationException:{}", ex.getMessage());
        return Result.error(HttpStatus.BAD_REQUEST.value(), "数据错误,请联系管理员");
    }

    /**
     * SQL异常
     */
    @ExceptionHandler(SQLException.class)
    @ResponseBody
    public Result<String> handleSqlException(SQLException ex) {
        log.error("SQLException:{}", ex.getMessage());
        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "数据库操作异常SQL异常,请联系管理员");
    }

    /**
     * Insert或Update数据时违反了完整性，例如违反了惟一性限制
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public Result<String> handleSqlException(DataIntegrityViolationException ex) {
        log.warn("DataIntegrityViolationException:{}", ex.getMessage());
        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "数据库操作异常,请联系管理员");
    }

    /**
     * 非法参数异常
     */
    @ExceptionHandler(IllegalStateException.class)
    @ResponseBody
    public Result<String> handleIllegalStateException(IllegalStateException ex) {
        log.warn("IllegalStateException:{}", ex.getMessage());
        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "参数校验异常,请联系管理员");
    }

}
