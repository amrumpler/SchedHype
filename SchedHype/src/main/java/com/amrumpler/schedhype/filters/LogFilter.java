package com.amrumpler.schedhype.filters;

import static com.amrumpler.schedhype.Constants.*;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.amrumpler.schedhype.util.LogUtils;

/**
 * Filters all content to obtain the logged in username, and appends a unique sequence number per request for use by by
 * {@link com.adt.selfservice.util.LogUtils LogUtils}.
 * 
 */
public class LogFilter extends AbstractFilter {

    private AtomicLong counter;

    public LogFilter() {
        counter = new AtomicLong();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        StringBuilder sb = new StringBuilder();

        if (request.getSession().getAttribute(SESSION_USER) != null) {
            //WebUser user = (WebUser) request.getSession().getAttribute(SESSION_USER);
            //sb.append(user.getUsername()).append("(").append(counter.incrementAndGet()).append(") - ");
        }

        try {
            LogUtils.TRACKING_PREFIX.set(sb.toString());
            chain.doFilter(servletRequest, servletResponse);
        } finally {
            LogUtils.TRACKING_PREFIX.remove();
        }
    }

}
