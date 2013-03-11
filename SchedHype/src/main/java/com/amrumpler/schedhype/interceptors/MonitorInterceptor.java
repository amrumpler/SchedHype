package com.amrumpler.schedhype.interceptors;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.management.ManagementFactory;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import lombok.extern.slf4j.Slf4j;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.amrumpler.schedhype.mbeans.MonitorWrapper;
import com.amrumpler.schedhype.util.LogUtils;
import com.google.inject.BindingAnnotation;
import com.google.inject.Singleton;
import com.jamonapi.MonitorFactory;
import java.lang.reflect.Method;
import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

@Singleton
@Slf4j
public class MonitorInterceptor implements MethodInterceptor {

    private MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

    private static final String COULD_NOT_CREATE_MBEAN = "Could not create MBean {}";

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        com.jamonapi.Monitor m = getMonitor(invocation.getMethod().getDeclaringClass().getName() + "."
                + invocation.getMethod().getName());

        Object result;
        try {
            m.start();
            result = invocation.proceed();
        } finally {
            m.stop();
        }

        return result;
    }

    private com.jamonapi.Monitor getMonitor(String name) {
        createMonitorMBean(name);
        return getTimeMonitor(name);
    }

    private void createMonitorMBean(String name) {
        MonitorWrapper mBean = new MonitorWrapper(getTimeMonitor(name));
        try {
            ObjectName oName = new ObjectName("com.adt.selfservice:type=Monitors,name=" + name);
            if(mbs != null){
	            if (!mbs.isRegistered(oName)) {
	            	mbs.registerMBean(mBean, oName);
		        }
            }
            else{
            	LogUtils.warn(log, COULD_NOT_CREATE_MBEAN, name);
            }
        } catch (MalformedObjectNameException mone) {
            LogUtils.warn(log, COULD_NOT_CREATE_MBEAN, name, mone);
        } catch (InstanceAlreadyExistsException iaee) {
            LogUtils.warn(log, "Attempting to create duplicate MBean {}", name, iaee);
        } catch (MBeanRegistrationException mbre) {
            LogUtils.warn(log, COULD_NOT_CREATE_MBEAN, name, mbre);
        } catch (NotCompliantMBeanException ncmbe) {
            LogUtils.warn(log, COULD_NOT_CREATE_MBEAN, name, ncmbe);
        }
    }

    private com.jamonapi.Monitor getTimeMonitor(String name) {
        return MonitorFactory.getTimeMonitor(name);
    }

    /**
     * Indicates that the method should be monitored using JAMon.
     * In order for annotated methods to be monitored
     * successfully, the methods must adhere to the following rules:
     * <ol>
     * <li>Guice must create the instance on which the method resides</li>
     * <li>Neither the class nor the method can be marked final</li>
     * <li>The method should not be private</li>
     * </ol>
     * If one or more of the above conditions are not met, then the method
     * will be invoked but no monitoring will take place.
     */
    @BindingAnnotation
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Monitor {
    }

    public <T extends Annotation> Object addMonitorsToMethods(final Object targetObject, final Class<?> interfaceToReflect, final Class<T> annotationToLookFor) {

        if (!interfaceToReflect.isInterface()) {
            LogUtils.error(log, "interfaceToReflect argument was not an interface");
            return targetObject;
        }

        ProxyFactory pf = new ProxyFactory();
        pf.setInterfaces(new Class[] { interfaceToReflect });

        pf.setFilter(new MethodFilter() {
            @Override
            public boolean isHandled(Method method) {
                if (method.getAnnotation(annotationToLookFor) != null) {
                    LogUtils.debug(log, "will intercept method: " + method.getName());
                }
                return method.getAnnotation(annotationToLookFor) != null;
            }
        });

        Class<?> c = pf.createClass();
        MethodHandler mh = new MethodHandler() {
            @Override
            public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
                Object retVal = null;
                if (proceed != null) {
                    retVal = proceed.invoke(self, args);
                } else {
                    com.jamonapi.Monitor m = getMonitor(thisMethod.getDeclaringClass().getName() + "." + thisMethod.getName());
                    try {
                        m.start();
                        retVal = targetObject.getClass().getMethod(thisMethod.getName(), thisMethod.getParameterTypes()).invoke(targetObject, args);
                    } finally {
                        m.stop();
                    }
                }
                return retVal;
            }
        };

        Object proxiedObject = targetObject;
        try {
            proxiedObject = c.newInstance();
            ((ProxyObject) proxiedObject).setHandler(mh);
            LogUtils.debug(log, "proxied object name = {}", proxiedObject.getClass().getName());
        } catch (Exception ex) {
            LogUtils.error(log, "failed to create proxied object", ex);
        }

        return proxiedObject;
    }
}

