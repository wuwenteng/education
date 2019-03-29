package com.example.education.contrroller;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author ASUS
 */
@WebFilter(urlPatterns = "/haveLogin/*")
public class LogFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        System.out.println("FILTER");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
//        response.setHeader("Access-Control-Allow-Origin",request.getHeader("Origin"));
//        response.setHeader("Access-Control-Allow-Methods","POST,GET,OPTIONS,DELETE");
//        response.setHeader("Access-Control-Max-Age","3600");
//        response.setHeader("Access-Control-Allow-Headers","x-requested-with");
//        response.setHeader("Access-Control-Allow-Credentials","true"); //是否支持cookie跨域
        HttpSession session = request.getSession();

        if (session.getAttribute("username") != null) {
            filterChain.doFilter(servletRequest,servletResponse);
        }else {
            System.out.println("没有登录");

//            return "redirect:/login.html";
        }

    }

    @Override
    public void destroy() {
        System.out.println("------------销毁------------");
    }
}
