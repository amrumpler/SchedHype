package com.amrumpler.schedhype.util;

import java.lang.reflect.Field;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class StringUtils {

    private static final int STANDARD_US_PHONE_SIZE = 10;
    private static final int AREA_CODE_SIZE = 3;
    private static final int EXCHANGE_CODE_SIZE = 3;
    public static final String EMPTY_STRING = "";

    private StringUtils() {
    }

	public static String stripLeadingAndTrailingQuotes(String str) {
		String retVal = str;
		if (retVal.startsWith("\"")) {
			retVal = retVal.substring(1, retVal.length());
		}
		if (retVal.endsWith("\"")) {
			retVal = retVal.substring(0, retVal.length() - 1);
		}
		return retVal;
	}

	public static void trimAllStringProperties(Object obj, String packageQualifier) {
	    if(obj !=null){
			Field[] fields = obj.getClass().getDeclaredFields();
		    for(Field field : fields) {
		        if (field.getType().equals(String.class)) {
		            field.setAccessible(true);
		            try {
		                String currentValue = (String)field.get(obj);
		                if(currentValue != null) {
		                    field.set(obj, ((String)field.get(obj)).trim());
		                }
	                } catch (Exception e) {
	                    LogUtils.error(log, e.getMessage(), e);
	                }
		        } else if (!isPrimitiveType(field.getType())) {
		            if(field.getType().getPackage().toString().endsWith(packageQualifier) && !isEnum(field.getType())) {
		                try {
		                    field.setAccessible(true);
	                        trimAllStringProperties(field.get(obj), packageQualifier);
	                    } catch (Exception e) {
	                        LogUtils.error(log, e.getMessage(), e);
	                    }
		            }
		        }
		    }
	    }
	}

	private static boolean isEnum(Class<?> clazz) {
	    return Enum.class.isAssignableFrom(clazz);
	}

	private static boolean isPrimitiveType(Class<?> clazz) {
        return clazz.getPackage() == null;
    }
	
	public static String toUpperCase(String string) {
	    return string == null ? null : string.toUpperCase();
	}

    public static String capitalizeWords(String string) {
        String [] parts = string.split("\\s");

        for (int i=0; i<parts.length; i++) {
            parts[i] = org.apache.commons.lang3.StringUtils.capitalize(parts[i]);
        }

        return org.apache.commons.lang3.StringUtils.join(parts, " ");
    }
    
	public static boolean isValidEmailAddress(String email) {
		boolean result = true;
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		} catch (AddressException ex) {
			result = false;
		}
		return result;
	}

    public static String trimToMaxLength(String string, int maxLength) {
        if (string == null) {
            return null;
        }
        String newValue = string.trim();
        return newValue.length() <= maxLength ? newValue : newValue.substring(0, maxLength).intern();
    }
    
    public static String formatPhoneNumber(String phoneNumber) {
        if (phoneNumber!=null && !phoneNumber.isEmpty() && phoneNumber.length() == STANDARD_US_PHONE_SIZE) {
            return String.format("(%s) %s-%s", phoneNumber.substring(0, AREA_CODE_SIZE), phoneNumber.substring(AREA_CODE_SIZE, AREA_CODE_SIZE + EXCHANGE_CODE_SIZE),
                    phoneNumber.substring(AREA_CODE_SIZE + EXCHANGE_CODE_SIZE, STANDARD_US_PHONE_SIZE));
       }
        return phoneNumber;
    }

    public static String unformatPhone(String formattedPhone) {
        return formattedPhone.replaceAll("[\\(\\)\\-\\s]+","");
    }
    
    //evalutes the Boolean or returns false if the value is null
    // TODO: this isn't a String util ... and I question it's purpose entirely
	public static boolean isTrue(Boolean val) {
		if (val == null) {
			return false;
		}
		return val.booleanValue() ? true : false;
	}
}
