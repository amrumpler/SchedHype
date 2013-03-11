package com.amrumpler.schedhype.modules;

import lombok.extern.slf4j.Slf4j;

import com.amrumpler.schedhype.exceptions.ModuleInitializationException;
import com.amrumpler.schedhype.interceptors.MonitorInterceptor;
import com.amrumpler.schedhype.interceptors.MonitorInterceptor.Monitor;
import com.amrumpler.schedhype.util.GenericExceptionHandler;
import com.amrumpler.schedhype.util.JndiDirNamingContextProvider;
import com.amrumpler.schedhype.util.LogUtils;
import com.amrumpler.schedhype.util.WebApplicationExceptionHandler;
import com.amrumpler.schedhype.validators.FormValidator;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.name.Names;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;
import com.google.inject.name.Names;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.CompactWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import freemarker.template.Configuration;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Provider;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.directory.DirContext;
import javax.sql.DataSource;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;

import static com.google.inject.jndi.JndiIntegration.fromJndi;
import static com.google.inject.matcher.Matchers.*;

@Slf4j
public class Bootstrap implements Module {
	
	public static final String CDATA = "CDATA";

	@Override
    public void configure(final Binder binder) {

        // configure exception handlers
        binder.bind(WebApplicationExceptionHandler.class);
        binder.bind(GenericExceptionHandler.class);
        
        binder.bind(XStream.class).annotatedWith(Names.named(CDATA)).toInstance(new XStream(
                new XppDriver() {
                    public HierarchicalStreamWriter createWriter(Writer out) {
                        return new PrettyPrintWriter(out) {

                            protected void writeText(QuickWriter writer, String text) {
                                writer.write("<![CDATA[");
                                writer.write(text.trim());
                                writer.write("]]>");
                            }
                        };
                    }
                }));

        binder.bind(XStream.class).annotatedWith(Names.named("APPLOG")).toInstance(new XStream(
                new XppDriver() {

                    public HierarchicalStreamWriter createWriter(Writer out) {
                        try {
                            out.write("<?xml version=\"1.0\"?>");
                        } catch (Exception e) {
                            throw new ModuleInitializationException(e.getMessage(), e);
                        }
                        return new CompactWriter(out) {

                            protected void writeText(QuickWriter writer, String text) {
                                writer.write(text.trim());
                            }
                        };
                    }
                }));

        binder.bind(DirContext.class).annotatedWith(Names.named("ldap")).toProvider(JndiDirNamingContextProvider.class);
        
        // configure validators
        binder.bind(FormValidator.class);
        
        // freemarker
        binder.bind(Configuration.class).toInstance(getFreemarkerConfiguration());

        // configure interceptors
        binder.bind(MonitorInterceptor.class).in(Scopes.SINGLETON);
        
        binder.bindInterceptor(any(), annotatedWith(Monitor.class), new MonitorInterceptor());

    }
	
	Configuration getFreemarkerConfiguration() {
        Configuration config = new Configuration();

        try {
            URL url = getClass().getResource("/email");
            config.setDirectoryForTemplateLoading(new File(url.getFile()));
        } catch (IOException ex) {
            LogUtils.error(log, "Failed to set freemarker template dir", ex);
        }

        return config;
    }


}
