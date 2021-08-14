package com.mjy.service.impl;

import com.mjy.dao.UserDao;
import com.mjy.dao.impl.UserDaoImpl;
import com.mjy.entity.Role;
import com.mjy.entity.User;
import com.mjy.service.UserService;
import com.mjy.util.BaseDao;
import com.mjy.util.JDBCUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @description
 * @create 2021-07-31 18:50
 */
public class UserServiceImpl implements UserService {

    private UserDao userDao;

    public UserServiceImpl(){
        userDao=new UserDaoImpl();
    }
    @Override
    public User login(String userCode, String password) {
        Connection conn=null;

        try {
            conn= JDBCUtils.getConnection();
            User loginUser = userDao.getLoginUser(conn, userCode,password);
            return loginUser;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null,conn,null);
        }

        return null;
    }

    @Override
    public boolean resetPassword(String userCode, String newPassword) {
        Connection conn=null;

        try {
            conn=JDBCUtils.getConnection();
            return userDao.updatePassword(conn,userCode,newPassword);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null,conn,null);
        }

        return false;
    }

    @Override
    public int getUserNum(String username, int userRole) {

        Connection conn=null;
        int userCount=0;

        try {
            conn=JDBCUtils.getConnection();
            userCount= userDao.getUserCount(conn, username, userRole);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null,conn,null);
        }


        return userCount;
    }

    @Override
    public List<User> getUsers(String username, int userRole, int currentPage, int pageSize) {
        Connection conn=null;

        try {
            conn=JDBCUtils.getConnection();
            List<User> userList = userDao.getUserList(conn, username, userRole, currentPage, pageSize);
            return userList;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null,conn,null);
        }

        return null;
    }

    @Override
    public List<Role> getRoles() {

        Connection conn=null;
        try {
            conn=JDBCUtils.getConnection();
            List<Role> roleList = userDao.getRoleList(conn);
            return roleList;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null,conn,null);
        }

        return null;
    }

    @Override
    public User getUserById(int userId) {
        Connection conn=null;
        try {
            conn=JDBCUtils.getConnection();
            User user = userDao.getUserById(conn, userId);
            return user;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null,conn,null);
        }

        return null;
    }

    @Override
    public boolean updateUser(User user) {
        Connection conn=null;

        try {
            conn=JDBCUtils.getConnection();
            return userDao.updateUserInfo(conn,user);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null,conn,null);
        }

        return false;
    }

    @Override
    public boolean deleteUser(int userId) {
        Connection conn=null;

        try {
            conn=JDBCUtils.getConnection();
            int count = userDao.deleteUserById(conn, userId);
            if(count>0){
                return true;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null,conn,null);
        }
        return false;
    }

    @Override
    public boolean addUser(Map<String, Object> map) {
        Connection conn=null;
        try {
            conn=JDBCUtils.getConnection();
            int flag = userDao.addUser(conn, map);
            if(flag>0){
                return true;
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null,conn,null);
        }

        return false;
    }

    @Override
    public User getUserByCode(String userCode) {
        Connection conn=null;
        try {
            conn=JDBCUtils.getConnection();
            User user = userDao.getUserByCode(conn, userCode);
            return user;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null,conn,null);
        }

        return null;
    }
}
