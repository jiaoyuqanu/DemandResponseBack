package com.xqxy.dr.modular.event.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class JDBCUtil {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Value("${spring.datasource.mysql.username}")
    private String userName;

    @Value("${spring.datasource.mysql.jdbc-url}")
    private String dataurl;

    @Value("${spring.datasource.mysql.password}")
    private String datapassword;

    @Value("${spring.datasource.mysql.driver-class-name}")
    private String driver;

    public Connection getConn(){
        String url=dataurl;
        String user=userName;
        String password=datapassword;
        Connection conn = null;
        try {
            //2）、加载驱动，不需要显示注册驱动
            Class.forName(driver);
            conn= DriverManager.getConnection(url,user,password);
        } catch (Exception e) {
            log.error("数据库连接错误"+e);
        }
        //（3）、获取连接
        return conn;
    }

}