package com.mjy.dao.impl;

import com.mjy.dao.UserDao;
import com.mjy.entity.Role;
import com.mjy.entity.User;
import com.mjy.util.BaseDao;
import com.mjy.util.JDBCUtils;
import com.mysql.cj.util.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @description
 * @create 2021-07-31 17:45
 */
public class UserDaoImpl implements UserDao {

    @Override
    public User getLoginUser(Connection conn, String userCode,String password) {
        PreparedStatement ps=null;
        ResultSet rs=null;
        User user =null;

        if(conn!=null){
            Object[] params={userCode,password};
            String sql="select * from smbms_user where userCode=? and userPassword=?";
            try {

                rs=BaseDao.baseQuery(conn,sql,ps,params);
                if(rs.next()){
                    int id = rs.getInt("id");
                    String userName = rs.getString("userName");
                    int gender = rs.getInt("gender");
                    Date birthday = rs.getDate("birthday");
                    String address = rs.getString("address");
                    int userRole = rs.getInt("userRole");
                    int createdBy = rs.getInt("createdBy");
                    Date creationDate = rs.getDate("creationDate");
                    int modifyBy = rs.getInt("modifyBy");
                    Date modifyDate = rs.getDate("modifyDate");

                    user=new User();
                    user.setId(id);
                    user.setUserCode(userCode);
                    user.setUserName(userName);
                    user.setUserPassword(password);
                    user.setGender(gender);
                    user.setBirthday(birthday);
                    user.setAddress(address);
                    user.setUserRole(userRole);
                    user.setCreatedBy(createdBy);
                    user.setCreationDate(creationDate);
                    user.setModifyBy(modifyBy);
                    user.setModifyDate(modifyDate);

                    return user;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                JDBCUtils.closeResource(ps,null,rs);
            }
        }




        return null;
    }

    @Override
    public boolean updatePassword(Connection conn, String userCode, String newPassword) {
        PreparedStatement pre=null;

        if(conn!=null){
            try {
                Object[] args={newPassword,userCode};
                String sql="update smbms_user set userPassword=? where userCode=?";
                int flag = BaseDao.baseUpdate(conn, sql, pre, args);

                if(flag>0){
                    return true;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {

                JDBCUtils.closeResource(pre,conn,null);
            }

        }

        return false;
    }

    @Override
    public int getUserCount(Connection conn, String username, int userRole){
        PreparedStatement pre=null;
        ResultSet rs=null;
        int count=0;
        if(conn!=null){
            try {
                List<Object> list=new ArrayList<>();
                //拼接sql字符串
                StringBuffer sbf = new StringBuffer();
                sbf.append("select count(1) count from smbms_user u,smbms_role r where u.userRole=r.id");
                if(!StringUtils.isNullOrEmpty(username)){
                    sbf.append(" and u.userName like ?");
                    list.add("%"+username+"%");
                }

                System.out.println(sbf.toString());
                if(userRole>0){
                    sbf.append(" and u.userRole= ? ");
                    list.add(userRole);
                }

                Object[] params = list.toArray();
                rs=BaseDao.baseQuery(conn,sbf.toString(),pre,params);
                if(rs.next()){
                    count=rs.getInt("count");
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                JDBCUtils.closeResource(pre,null,rs);
            }

        }

        return count;
    }

    @Override
    public List<User> getUserList(Connection conn, String userName, int userRole, int currentPage, int pageSize) {
        PreparedStatement pre=null;
        ResultSet rs=null;
        List<User> list=new ArrayList<>();
        List<Object> list1=new ArrayList<>();
        if(conn!=null){
            try {
                StringBuffer sbf = new StringBuffer();
                sbf.append("select u.*,r.roleName from smbms_user u,smbms_role r where u.userRole=r.id");
                if(!StringUtils.isNullOrEmpty(userName)){
                    sbf.append(" and u.userName like ?");
                    list1.add("%"+userName+"%");
                }

                if(userRole>0){
                    sbf.append(" and u.userRole= ?");
                    list1.add(userRole);
                }

                sbf.append(" order by creationDate desc limit ?,?");
                currentPage=(currentPage-1)*pageSize;
                list1.add(currentPage);
                list1.add(pageSize);

                Object[] params = list1.toArray();

                rs=BaseDao.baseQuery(conn,sbf.toString(),pre,params);
                while(rs.next()){
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUserCode(rs.getString("userCode"));
                    user.setUserName(rs.getString("userName"));
                    user.setUserPassword(rs.getString("userPassword"));
                    user.setGender(rs.getInt("gender"));
                    user.setBirthday(rs.getDate("birthday"));
                    user.setPhone(rs.getString("phone"));
                    user.setAddress(rs.getString("address"));
                    user.setUserRole(rs.getInt("userRole"));
                    user.setCreatedBy(rs.getInt("createdBy"));
                    user.setCreationDate(rs.getDate("creationDate"));
                    user.setModifyBy(rs.getInt("modifyBy"));
                    user.setModifyDate(rs.getDate("modifyDate"));
                    user.setUserRoleName(rs.getString("roleName"));

                    list.add(user);

                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                JDBCUtils.closeResource(pre,null,rs);
            }

        }

        return list;
    }

    @Override
    public List<Role> getRoleList(Connection conn) {
        PreparedStatement pre=null;
        ResultSet rs=null;
        List<Role> list=new ArrayList<>();

        if(conn!=null){
            try {
                String sql="select * from smbms_role";
                Object[] params=new Object[]{};
                rs=BaseDao.baseQuery(conn,sql,pre,params);

                while(rs.next()){

                    Role role = new Role();
                    role.setId(rs.getInt("id"));
                    role.setRoleCode(rs.getString("roleCode"));
                    role.setRoleName(rs.getString("roleName"));
                    role.setCreatedBy(rs.getInt("createdBy"));
                    role.setCreationDate(rs.getDate("creationDate"));
                    role.setModifyBy(rs.getInt("modifyBy"));
                    role.setModifyDate(rs.getDate("modifyDate"));

                    list.add(role);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                JDBCUtils.closeResource(pre,null,rs);
            }

        }

        return list;
    }

    @Override
    public User getUserById(Connection conn, int userId) {
        PreparedStatement pre=null;
        ResultSet rs=null;
        User user =null;
        if(conn!=null){
            try {
                String sql="select u.*,r.roleName from smbms_user u,smbms_role r where u.userRole=r.id and u.id= ?";
                Object[] params=new Object[]{userId};
                rs=BaseDao.baseQuery(conn,sql,pre,params);

                if(rs.next()){
                    user=new User();
                    user.setId(rs.getInt("id"));
                    user.setUserCode(rs.getString("userCode"));
                    user.setUserName(rs.getString("userName"));
                    user.setUserPassword(rs.getString("userPassword"));
                    user.setGender(rs.getInt("gender"));
                    user.setBirthday(rs.getDate("birthday"));
                    user.setPhone(rs.getString("phone"));
                    user.setAddress(rs.getString("address"));
                    user.setUserRole(rs.getInt("userRole"));
                    user.setCreatedBy(rs.getInt("createdBy"));
                    user.setCreationDate(rs.getDate("creationDate"));
                    user.setModifyBy(rs.getInt("modifyBy"));
                    user.setModifyDate(rs.getDate("modifyDate"));
                    user.setUserRoleName(rs.getString("roleName"));
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                JDBCUtils.closeResource(pre,null,rs);
            }
        }
        return user;
    }

    @Override
    public boolean updateUserInfo(Connection conn, User user) {

        PreparedStatement pre=null;
        int flag=0;
        if(conn!=null){
            try {
                String sql="update smbms_user set userName=? ,gender= ?, birthday= ?,phone= ?, address=?, userRole= ? " +
                        "where id=?";

                Object[] params=new Object[]{user.getUserName(),user.getGender(),user.getBirthday(),user.getPhone(),user.getAddress(),
                user.getUserRole(),user.getId()};

                flag= BaseDao.baseUpdate(conn, sql, pre, params);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                JDBCUtils.closeResource(pre,null,null);
            }

        }
        return flag>0? true:false;
    }

    @Override
    public int deleteUserById(Connection conn, int userId) {
        PreparedStatement pre=null;
        int flag=0;
        if(conn!=null){
            try {
                String sql="delete from smbms_user where id= ? ";
                Object[] params=new Object[]{userId};

                flag= BaseDao.baseUpdate(conn, sql, pre, params);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {

                JDBCUtils.closeResource(pre,null,null);
            }


        }

        return flag;
    }

    @Override
    public int addUser(Connection conn, Map<String, Object> map) {
        PreparedStatement pre=null;
        int flag=0;
        if(conn!=null){
            try {
                String sql="insert into smbms_user(userCode,userName,userPassword,gender,birthday,phone,address,userRole) values" +
                        "(?,?,?,?,?,?,?,?)";
                List<Object> list=new ArrayList<>();
                list.add(map.get("userCode"));
                list.add(map.get("userName"));
                list.add(map.get("userPassword"));
                list.add(map.get("gender"));
                list.add(map.get("birthday"));
                list.add(map.get("phone"));
                list.add(map.get("address"));
                list.add(map.get("userRole"));
                Object[] params = list.toArray();

                flag=BaseDao.baseUpdate(conn,sql,pre,params);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                JDBCUtils.closeResource(pre,null,null);
            }

        }

        return flag;
    }

    @Override
    public User getUserByCode(Connection conn, String userCode) {


        PreparedStatement pre=null;
        ResultSet rs=null;
        User user=null;
        if(conn!=null){
            try {

                String sql="select u.*,r.roleName from smbms_user u,smbms_role r where u.userRole=r.id and u.userCode= ?";
                Object[] params=new Object[]{userCode};
                rs=BaseDao.baseQuery(conn,sql,pre,params);

                if(rs.next()){
                    user= new User();
                    user.setId(rs.getInt("id"));
                    user.setUserCode(rs.getString("userCode"));
                    user.setUserName(rs.getString("userName"));
                    user.setUserPassword(rs.getString("userPassword"));
                    user.setGender(rs.getInt("gender"));
                    user.setBirthday(rs.getDate("birthday"));
                    user.setPhone(rs.getString("phone"));
                    user.setAddress(rs.getString("address"));
                    user.setUserRole(rs.getInt("userRole"));
                    user.setCreatedBy(rs.getInt("createdBy"));
                    user.setCreationDate(rs.getDate("creationDate"));
                    user.setModifyBy(rs.getInt("modifyBy"));
                    user.setModifyDate(rs.getDate("modifyDate"));
                    user.setUserRoleName(rs.getString("roleName"));
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                JDBCUtils.closeResource(pre,null,rs);
            }
        }
        return user;
    }
}
