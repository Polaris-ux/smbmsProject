package com.mjy.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @author
 * @description
 * @create 2021-07-31 16:07
 */
public class JDBCUtils {

    private static String url;
    private static String driver;
    private static String username;
    private static String password;

    static{
        try {
            ClassLoader classLoader = JDBCUtils.class.getClassLoader();
            InputStream is = classLoader.getResourceAsStream("jdbc.properties");
            Properties prop = new Properties();
            prop.load(is);

            url=prop.getProperty("url");
            driver = prop.getProperty("driver");
            username=prop.getProperty("username");
            password=prop.getProperty("password");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url,username,password);
        return conn;
    }

    public static void closeResource(PreparedStatement pre, Connection conn, ResultSet rs){
        if(rs!=null){
            try {
                rs.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        if(pre!=null){
            try {
                pre.close();
            } catch (SQLException throwables) {

                throwables.printStackTrace();
            }
        }

        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
