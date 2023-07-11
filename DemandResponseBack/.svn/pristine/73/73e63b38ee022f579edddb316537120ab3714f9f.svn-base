package com.xqxy.core.util;

import com.alibaba.fastjson.JSONObject;
import com.xqxy.core.pojo.login.CurrenUserInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;

/**
 * 用电框架获取用户信息
 */
public final class SecurityUtils {

    private static final String X_CLIENT_TOKEN_USER = "x-client-token-user";
    private static final String X_CLIENT_TOKEN_USER2 = "x-client-token-user2";

    private SecurityUtils() {
    }

    //获取当前登录用户信息
    @Deprecated
    public static CurrenUserInfo getCurrentUserInfo() {
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String claims = request.getHeader(X_CLIENT_TOKEN_USER);
        if (StringUtils.isEmpty(claims)) {
            return null;
        }
        return JSONObject.parseObject(claims, CurrenUserInfo.class);
    }

    public static CurrenUserInfo getCurrentUserInfoUTF8() {
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String claims = request.getHeader(X_CLIENT_TOKEN_USER2);
        if(org.apache.commons.lang.StringUtils.isEmpty(claims)){
            System.out.println("get X_CLIENT_TOKEN_USER = null");
            return null;
        }
        try {
            CurrenUserInfo currenUserInfo = JSONObject.parseObject(URLDecoder.decode(claims,"utf-8"), CurrenUserInfo.class);
            return currenUserInfo;
        }catch (Exception e){
            System.out.println("trans user info error!");
            e.printStackTrace();
        }
        return null;
    }
}
