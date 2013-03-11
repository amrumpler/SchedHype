package com.amrumpler.schedhype.filters;

import static com.amrumpler.schedhype.Constants.SESSION_USER;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;

import com.amrumpler.schedhype.util.LogUtils;

@Slf4j
public class AuthorizationFilter extends AbstractFilter {

    List<String> authUrls = new ArrayList<String>();
    List<String> picUrls = new ArrayList<String>();

    public boolean addAuthUrl(String urlString) {
        return authUrls.add(urlString.toLowerCase());
    }

    public boolean addPicUrl(String urlString) {
        return picUrls.add(urlString.toLowerCase());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
    	long startTime = System.currentTimeMillis();
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        
        if (isAuthRequired(request)) {
        	log.debug("Authorization required, redirecting to login");
        	//For AJAX, set response code, for jsps redirect
        	if(request.getRequestURI().endsWith(".jsp")) {
        		response.sendRedirect("/");
        	} else {
        		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        		PrintWriter writer = response.getWriter();  
                writer.write("{\"blank\":[],\"errors\":[\"Not Authorized\"]}");  
        	}
            return;
        }
        
        chain.doFilter(servletRequest, servletResponse);
        if("text/html".equals(response.getContentType()) || "application/json".equals(response.getContentType())) {
        	log.debug("URL:{}, Time:{}millis", request.getRequestURI(), (System.currentTimeMillis()-startTime));
        }
    }

    private boolean isAuthRequired(HttpServletRequest request) {
        String url = getPath(request);
        if (isProtectedUrl(url, authUrls)) {
            HttpSession session = request.getSession();
            if (session.getAttribute(SESSION_USER) == null) {
                LogUtils.debug(log, "Access denied to \"{}\" - protected URL but user not logged in", url);
                // Not even logged in
                return true;
            }
        }

        return false;
    }
    
}
