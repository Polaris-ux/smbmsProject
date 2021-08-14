package com.mjy.dao;

import com.mjy.entity.Role;
import com.mjy.entity.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @description
 * @create 2021-07-31 17:34
 */
public interface UserDao {

    //查询用户
    User getLoginUser(Connection conn,String userCode,String password);


    //修改密码
    boolean updatePassword(Connection conn,String userCode,String newPassword);

    //查询用户记录数
    int getUserCount(Connection conn,String username,int userRole);

    //获取用户列表
    List<User> getUserList(Connection conn,String userName,int userRole,int currentPage,int pageSize);

    //获取角色列表
    List<Role> getRoleList(Connection conn);

    User getUserById(Connection conn,int userId);

    boolean updateUserInfo(Connection conn,User user);

    int deleteUserById(Connection conn,int userId);

    int addUser(Connection conn, Map<String,Object> map);

    User getUserByCode(Connection conn,String userCode);
}
