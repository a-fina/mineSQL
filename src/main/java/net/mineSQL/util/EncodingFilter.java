package net.mineSQL.util;

import java.io.IOException;
import java.util.Date;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.log4j.Logger;

public class EncodingFilter implements Filter {
    private String encodingReq;
    private String encodingRes;
    private FilterConfig filterConfig;
    private static final Logger log = Logger.getLogger(FirstFilter.class);

    /**
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig fc) throws ServletException {
	this.filterConfig = fc;
	this.encodingReq = filterConfig.getInitParameter("encodingReq");
	this.encodingRes = "text/html;charset=" + filterConfig.getInitParameter("encodingRes");
	//this.encodingRes = "text/html;charset=ISO-8859-1";
    }

    /**
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain) 
    throws IOException, ServletException {
        log.info("Encoding req: "+this.encodingReq+" res: "+this.encodingRes);
        HttpServletRequest  req = (HttpServletRequest)request;
        HttpServletResponse res = (HttpServletResponse)response;

        req.setCharacterEncoding(encodingReq);
        res.addHeader("Content-Type",encodingRes);
        chain.doFilter(request, response);
    }

    /**
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy() {
    }
}

