
package com.xqxy.core.util;

import com.xqxy.core.exception.ServiceException;
import com.xqxy.core.exception.enums.ServerExceptionEnum;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * HttpServlet工具类，获取当前request和response
 *
 * @author xiao jun
 * @date 2020/3/30 15:09
 */
public class HttpServletUtil {

    /**
     * 获取当前请求的request对象
     *
     * @author xiao jun
     * @date 2020/3/30 15:10
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            throw new ServiceException(ServerExceptionEnum.REQUEST_EMPTY);
        } else {
            return requestAttributes.getRequest();
        }
    }

    /**
     * 获取当前请求的response对象
     *
     * @author xiao jun
     * @date 2020/3/30 15:10
     */
    public static HttpServletResponse getResponse() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            throw new ServiceException(ServerExceptionEnum.REQUEST_EMPTY);
        } else {
            return requestAttributes.getResponse();
        }
    }
}
