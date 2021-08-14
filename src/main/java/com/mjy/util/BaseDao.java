package com.mjy.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author
 * @description
 * @create 2021-07-31 16:19
 */
public class BaseDao {

    public static ResultSet baseQuery(Connection conn,String sql,PreparedStatement pre,Object[] args) throws SQLException {
        pre = conn.prepareStatement(sql);

        //填充占位符,占位符起始位置为1
        for (int i = 0; i < args.length; i++) {
            pre.setObject(i+1,args[i]);
        }

        return pre.executeQuery();
    }

    public static int baseUpdate(Connection conn,String sql,PreparedStatement ps,Object[] args) throws SQLException {
        ps = conn.prepareStatement(sql);

        //填充占位符,占位符起始位置为1
        for (int i = 0; i < args.length; i++) {
            ps.setObject(i+1,args[i]);
        }

        return ps.executeUpdate();
    }
}
