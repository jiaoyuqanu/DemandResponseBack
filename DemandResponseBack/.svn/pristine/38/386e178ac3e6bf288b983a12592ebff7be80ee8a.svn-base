
package com.xqxy.core.error;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.Log;
import com.xqxy.core.consts.AopSortConstant;
import com.xqxy.core.consts.CommonConstant;
import com.xqxy.core.exception.ServiceException;
import com.xqxy.core.exception.enums.ServerExceptionEnum;
import com.xqxy.core.exception.enums.abs.AbstractBaseExceptionEnum;
import com.xqxy.core.pojo.response.ResponseData;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理器
 *
 * @author xiao jun, fengshuonan
 * @date 2020/3/18 19:03
 */
@Order(AopSortConstant.GLOBAL_EXP_HANDLER_AOP)
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Log log = Log.get();

    /**
     * 拦截业务异常
     *
     * @author xiao jun
     * @date 2020/3/18 19:41
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public ResponseData businessError(ServiceException e) {
        return renderJson(e.getCode(), e.getErrorMessage(), e);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ResponseData IllegalArgumentException(IllegalArgumentException e) {
        return renderJson(-2, e.getMessage(), e);
    }

    /**
     * 渲染异常json
     *
     * @author stylefeng
     * @date 2020/5/5 16:22
     */
    private ResponseData renderJson(Integer code, String message) {
        return renderJson(code, message, null);
    }

    /**
     * 渲染异常json
     *
     * @author stylefeng
     * @date 2020/5/5 16:22
     */
    private ResponseData renderJson(AbstractBaseExceptionEnum baseExceptionEnum) {
        return renderJson(baseExceptionEnum.getCode(), baseExceptionEnum.getMessage(), null);
    }

    /**
     * 渲染异常json
     *
     * @author stylefeng
     * @date 2020/5/5 16:22
     */
    private ResponseData renderJson(Throwable throwable) {
        return renderJson(((AbstractBaseExceptionEnum) ServerExceptionEnum.SERVER_ERROR).getCode(),
                ((AbstractBaseExceptionEnum) ServerExceptionEnum.SERVER_ERROR).getMessage(), throwable);
    }

    /**
     * 渲染异常json
     * <p>
     * 根据异常枚举和Throwable异常响应，异常信息响应堆栈第一行
     *
     * @author stylefeng
     * @date 2020/5/5 16:22
     */
    private ResponseData renderJson(Integer code, String message, Throwable e) {
        if (ObjectUtil.isNotNull(e)) {

            //获取所有堆栈信息
            StackTraceElement[] stackTraceElements = e.getStackTrace();

            //默认的异常类全路径为第一条异常堆栈信息的
            String exceptionClassTotalName = stackTraceElements[0].toString();

            //遍历所有堆栈信息，找到cn.stylefeng开头的第一条异常信息
            for (StackTraceElement stackTraceElement : stackTraceElements) {
                if (stackTraceElement.toString().contains(CommonConstant.DEFAULT_PACKAGE_NAME)) {
                    exceptionClassTotalName = stackTraceElement.toString();
                    break;
                }
            }
            return ResponseData.fail("-1", message, exceptionClassTotalName);
        } else {
            return ResponseData.fail("-1", message, null);
        }
    }

}
