package com.amrumpler.schedhype.util;

import lombok.extern.slf4j.Slf4j;
import com.google.inject.Inject;
import com.google.inject.Provider;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;
import java.util.Map;

@Slf4j
public class JndiDirNamingContextProvider implements Provider<DirContext> {

    private JndiInformation jndi;
    @Setter private String initialContextFactory;

    @Inject
    public JndiDirNamingContextProvider(JndiInformation jndi) {
        this.jndi = jndi;
        this.initialContextFactory = "com.sun.jndi.ldap.LdapCtxFactory";
    }

    @Override
    public DirContext get() {
        Map<String, String> env = new Hashtable<String, String>();

        DirContext ctx = null;

        env.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
        env.put(javax.naming.Context.SECURITY_AUTHENTICATION, "simple");
        
        try {
            ctx = new InitialDirContext((Hashtable<String, String>) env); // NOPMD - Map cannot be used here.
        } catch (NamingException e) {
            LogUtils.error(log, "Error occurred while accessing LDAP.", e);
            throw new RuntimeException(e);
        }

        return ctx;
    }
    
}