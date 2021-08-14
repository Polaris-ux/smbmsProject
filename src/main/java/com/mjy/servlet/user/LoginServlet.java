package com.mjy.servlet.user;

import com.mjy.entity.User;
import com.mjy.service.UserService;
import com.mjy.service.impl.UserServiceImpl;
import com.mjy.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author
 * @description
 * @create 2021-07-31 18:55
 */
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userCode = req.getParameter("userCode");
        String userPassword = req.getParameter("userPassword");

        UserService us=new UserServiceImpl();
        User user = us.login(userCode, userPassword);
        System.out.println(user);

        if(user!=null){
            req.getSession().setAttribute(Constants.USER_SESSION,user);

            //跳转到主页
            resp.sendRedirect("/smbms/jsp/frame.jsp");
        }else{

            req.setAttribute("error","用户名或密码错误");
            req.getRequestDispatcher("/login.jsp").forward(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
