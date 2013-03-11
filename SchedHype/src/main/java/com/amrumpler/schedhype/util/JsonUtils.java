package com.amrumpler.schedhype.util;

import java.io.IOException;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.codehaus.jackson.map.ObjectMapper;

@Slf4j
public final class JsonUtils {
	
	// private constructor - fixes sonar major violation - Hide Utility Class Constructor
	private JsonUtils() { }

    public static String toJson(Object fromObject) {
        return toJson(fromObject, "{\"errors\": [\"Generic.error\"], \"blank\": []}");
    }

    public static String toJson(Object fromObject, String errorString) {
        try {
            return new ObjectMapper().writeValueAsString(fromObject);
        } catch (IOException ex) {
            LogUtils.error(log, "Failed to generate JSON", ex);
        }

        return errorString;
    }

    public static String toJson(Object fromObject, String... errorSections) {
        try {
            return new ObjectMapper().writeValueAsString(fromObject);
        } catch (IOException ex) {
            LogUtils.error(log, "Failed to generate JSON", ex);
        }

        StringBuilder sb = new StringBuilder();
        for (String errorSection : errorSections) {
            sb.append(errorSection);
        }
        return sb.toString();
    }

    
    @SuppressWarnings("unchecked")
	public static Object getValue(String json, String propertyName) {
    	if (json == null || propertyName == null) {
    		return null;
    	}
    	
		Map<String, Object> map;
		try {
			map = new ObjectMapper().readValue(json, Map.class);
		} catch (Exception e) {
			return null;
		} 
		
		return map.get(propertyName);
    }
}
