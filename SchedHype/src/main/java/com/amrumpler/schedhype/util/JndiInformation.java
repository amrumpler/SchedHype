package com.amrumpler.schedhype.util;

import com.google.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.google.inject.Singleton;
import java.lang.reflect.Constructor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class JndiInformation {

    private InitialContext context = null;

    @Inject
    public JndiInformation(InitialContext context) {
        this.context = context;
    }

    Object retrieve(String name) throws NamingException {
        Object result = null;

        try {
            result = context.lookup(name);
        } catch (NamingException e) {
            LogUtils.error(log, "Unable to do InitialContext.lookup() for {}", name, e);
            throw e;
        }
        return result;
    }

    public <T> T get(String name, Class<T> type) throws NamingException {
        T result = null;

        if (name == null || name.isEmpty()) {
            return result;
        }

        Object o = retrieve(name);

        if (o == null) {
            LogUtils.trace(log, "JNDI lookup for {} returned a null value.", name);
        } else if (type.isAssignableFrom(o.getClass())) {
            result = type.cast(o);

            if (name.toLowerCase().contains("password") && result instanceof String) {
                LogUtils.trace(log, "JNDI lookup for {} returned a string whose length is {}", name, ((String) result).length());
            } else {
                LogUtils.trace(log, "JNDI lookup for {} returned {}", name, result);
            }
        } else {
            try {
                Constructor<T> cons = type.getConstructor(o.getClass());
                result = cons.newInstance(o);
            } catch (Exception ex) {
                LogUtils.trace(log, "Failed to create instance of type {} from {} using a constructor", type.getClass(), o);
                LogUtils.trace(log, "JNDI lookup returned an unexpected value: {}", result);
            }
        }

        return result;
    }
}
