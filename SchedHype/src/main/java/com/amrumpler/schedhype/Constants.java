package com.amrumpler.schedhype;

import java.util.ResourceBundle;

public final class Constants {
	static{
		String prefix = ResourceBundle.getBundle("configuration").getString("jndi.context.prefix");
		ENVIRONMENT = prefix + "environment";
	}
	public static final String ENVIRONMENT;
	public static final String SESSION_USER = "user";
	public static final int THIRTY_DAYS=30;
	public static final String RELEASE_VERSION = ResourceBundle.getBundle("configuration").getString("build.number");
}
