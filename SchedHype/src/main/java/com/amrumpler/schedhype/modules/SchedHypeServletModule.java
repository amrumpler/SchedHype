package com.amrumpler.schedhype.modules;

import static com.amrumpler.schedhype.Constants.ENVIRONMENT;
import static com.amrumpler.schedhype.Constants.THIRTY_DAYS;

import java.util.HashMap;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import lombok.extern.slf4j.Slf4j;

import org.joda.time.Period;

import com.amrumpler.schedhype.filters.AuthorizationFilter;
import com.amrumpler.schedhype.filters.LogFilter;
import com.amrumpler.schedhype.util.JndiInformation;
import com.amrumpler.schedhype.util.LogUtils;
import com.google.inject.servlet.ServletModule;
import com.samaxes.filter.CacheFilter;
import com.samaxes.filter.NoCacheFilter;

@Slf4j
public class SchedHypeServletModule extends ServletModule {
    
    @Override
    protected void configureServlets() {
        filter("/api/*", "*.jsp", "*.html").through(new LogFilter());
        LogUtils.info(log, "Log filter installed");

        filter("/*").through(getConfiguredAuthFilter());
        LogUtils.info(log, "Authorization filter installed");

        try {
            JndiInformation jndi = new JndiInformation(new InitialContext());
            if (jndi.get(ENVIRONMENT, String.class).equals("DEV")) {
                filter("/*").through(new NoCacheFilter());
                LogUtils.info(log, "No-cache filter installed");
            } else {
                filter("/api/*", "*.jsp", "*.html").through(new NoCacheFilter());
                LogUtils.info(log, "No-cache filter installed for api, jsp, and html");

                Map<String, String> params = new HashMap<String, String>();
                params.put("expirationTime", String.valueOf(Period.days(THIRTY_DAYS).toStandardSeconds().getSeconds()));
                params.put("static", "true");
                filter("*.json", "*.js", "*.css", "*.svg", "*.eot", "*.ttf", "*.gif", "*.png", "*.jpg").through(new CacheFilter(), params);
                LogUtils.info(log, "Cache filter installed for static resources");
            }
            
        } catch (NamingException ex) {
            LogUtils.error(log, ex.getMessage(), ex);
        }
        
    }

    private AuthorizationFilter getConfiguredAuthFilter() {
        AuthorizationFilter filter = new AuthorizationFilter();
        filter.addPicUrl("/api/pic/auth");
        filter.addPicUrl("/api/auth/alarmSystem");
        filter.addPicUrl("/api/auth/coi");
        filter.addPicUrl("/api/auth/contact");
        filter.addPicUrl("/api/auth/history");

        filter.addAuthUrl("/auth");
        filter.addAuthUrl("/api/auth");
        filter.addAuthUrl("/api/securityPasswords/submit");
        return filter;
    }

}
