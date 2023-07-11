
package com.xqxy.core.mybatis;

import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.xqxy.core.util.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectionException;

import java.util.Date;

/**
 * 自定义sql字段填充器，自动填充创建修改相关字段
 *
 * @author xiao jun
 * @date 2020/3/30 15:21
 */
public class CustomMetaObjectHandler implements MetaObjectHandler {

    private static final Log log = Log.get();

    private static final String CREATE_USER = "createUser";

    private static final String CREATE_TIME = "createTime";

    private static final String UPDATE_USER = "updateUser";

    private static final String UPDATE_TIME = "updateTime";

    public CustomMetaObjectHandler() {
        System.out.println("测试数据");
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        try {
            //设置createUser（BaseEntity)
            setFieldValByName(CREATE_USER, this.getUserUniqueId(), metaObject);

            //设置createTime（BaseEntity)
            /*SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateData = "2021-08-04 10:00:00";
            Date parse = simpleDateFormat.parse(dateData);
            setFieldValByName(CREATE_TIME, parse, metaObject);*/
            setFieldValByName(CREATE_TIME, new Date(), metaObject);
        } catch (ReflectionException e) {
            log.warn(">>> CustomMetaObjectHandler处理过程中无相关字段，不做处理");
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            //设置updateUser（BaseEntity)
//            setFieldValByName(UPDATE_USER, this.getUserUniqueId(), metaObject);
            //设置updateTime（BaseEntity)
            setFieldValByName(UPDATE_TIME, new Date(), metaObject);
        } catch (ReflectionException e) {
            log.warn(">>> CustomMetaObjectHandler处理过程中无相关字段，不做处理");
        }
    }

    /**
     * 获取用户唯一id
     */
    private Long getUserUniqueId() {
        try {
            return Long.valueOf(SecurityUtils.getCurrentUserInfo().getId());
        } catch (Exception e) {
            //如果获取不到就返回-1
            return -1L;
        }
    }
}