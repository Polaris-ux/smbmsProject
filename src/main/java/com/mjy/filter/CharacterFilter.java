package com.mjy.filter;



import javax.servlet.*;
import java.io.IOException;

/**
 * @author
 * @description
 * @create 2021-07-31 16:25
 */
public class CharacterFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}
