package com.amrumpler.schedhype.filters;

import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;

import com.google.inject.Singleton;

@Singleton
public abstract class AbstractFilter implements Filter {

    @Override
    public void init(FilterConfig fc) {
    }

    @Override
    public void destroy() {
    }

    protected String getPath(HttpServletRequest request) {
        return request.getServletPath() + (request.getPathInfo() == null ? "" : request.getPathInfo());
    }

    protected boolean isProtectedUrl(String url, List<String> protectedUrls) {
        if (protectedUrls.contains(url.toLowerCase()) || protectedUrls.contains("/")) {
            return true;
        }

        // Match on parts of the URL...if "/api/some" is a protected URL, then we should
        // protect it and anything within it, (eg "/api/some/work" but not "/api/something")
        String[] urlParts = url.toLowerCase().split("/");
        StringBuilder sb = new StringBuilder();
        for (String part : urlParts) {
            if (part.trim().length() > 0) {
                sb.append("/").append(part.trim());
            }
            if (protectedUrls.contains(sb.toString())) {
                return true;
            }
        }

        return false;
    }

}
