package org.redrock.gobang.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "loginFilter",urlPatterns = {"/go"})
public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpReq=(HttpServletRequest)req;
        HttpServletResponse httpResp=(HttpServletResponse)resp;
        Object o=httpReq.getSession().getAttribute("user");
        if(o==null){
            httpResp.sendRedirect(httpReq.getContextPath() +"/view/login");
        }else {
            chain.doFilter(req,resp);
        }
    }

    @Override
    public void destroy() {
    }
}

