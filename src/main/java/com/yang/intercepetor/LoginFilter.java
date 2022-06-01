package com.yang.intercepetor;

import com.alibaba.fastjson.JSON;
import com.yang.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "loginFilter", urlPatterns = "/*")
public class LoginFilter implements Filter {

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();


    /**
     * 判断请求是否是允许的路径
     * @param requestUri 请求的路径
     * @param enableURI 允许访问的路径
     * @return 是否放行
     */
    private boolean check(String requestUri, String[] enableURI) {
        for (String uri : enableURI) {
            if (PATH_MATCHER.match(uri, requestUri)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //获取请求的URI
        String requestURI = request.getRequestURI();
        //设置允许不登陆就访问的URI路径
        String[] urls = {
                "/backend/**",
                "/front/**",
                "/employee/login",
                "employee/logout"
        };
        //2、判断本次请求是否需要处理
        boolean check = check(requestURI, urls);
        //3、如果不需要处理，则直接放行
        if(check){
            log.info("本次请求{}不需要处理",requestURI);
            filterChain.doFilter(request,response);
            return;
        }
        //4、判断登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("employee") != null){
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("employee"));

            String empId = (String) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request,response);
            return;
        }

        log.info("用户未登录");
        //5、如果未登录则返回未登录结果，通过输出流方式向客户端页面响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));

        /*//当前访问的路径不是未登录允许的，判断是否登陆
        if (!check) {
            String loginId = (String) request.getSession().getAttribute("employee");
            if (loginId != null) {
                //当前用户已经登陆，放行
                filterChain.doFilter(request, response);
                BaseContext.setCurrentId(loginId);
                log.info("当前线程id：{}", Thread.currentThread().getId());
                return;
            }
            //当前路径是不允许直接访问且没有登陆
            response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
            return;
        }
        //当前路径是允许直接访问的
        filterChain.doFilter(request, response);*/
    }
}
