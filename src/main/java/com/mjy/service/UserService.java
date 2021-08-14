package com.mjy.service;

import com.mjy.entity.Role;
import com.mjy.entity.User;

import java.util.List;
import java.util.Map;

/**
 * @author
 * @description
 * @create 2021-07-31 18:49
 */
public interface UserService {

    User login(String userCode,String password);

    boolean resetPassword(String userCode,String newPassword);

    int getUserNum(String username,int userRole);

    List<User> getUsers(String username,int userRole,int currentPage,int pageSize);

    List<Role> getRoles();

    User getUserById(int userId);

    boolean updateUser(User user);

    boolean deleteUser(int userId);

    boolean addUser(Map<String,Object> map);

    User getUserByCode(String userCode);
}
