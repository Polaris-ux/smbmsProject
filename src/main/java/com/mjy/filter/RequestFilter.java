package com.mjy.filter;

import com.mjy.entity.User;
import com.mjy.util.Constants;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author
 * @description
 * @create 2021-07-31 20:39
 */
public class RequestFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req=(HttpServletRequest)request;
        HttpServletResponse resp=(HttpServletResponse)response;
        HttpSession session = req.getSession();
        User user=(User)session.getAttribute(Constants.USER_SESSION);
        //检测是否登录，若未登录，跳转到登录页面
        if(user==null){
            req.getRequestDispatcher("/error.jsp").forward(request,response);
        }
        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}
