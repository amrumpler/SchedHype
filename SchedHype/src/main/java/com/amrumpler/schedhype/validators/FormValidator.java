package com.amrumpler.schedhype.validators;

import static com.amrumpler.schedhype.Constants.*;

import java.util.regex.Pattern;

import com.amrumpler.schedhype.domain.ErrorList;
import com.amrumpler.schedhype.util.StringUtils;

public class FormValidator {

    private static Pattern zipCodePattern = Pattern.compile("^\\d{5}$");
    private static Pattern nineDigitZipCodePattern = Pattern.compile("^\\d{9}$");
    private static Pattern passwordPattern = Pattern.compile("^(?=(?:[\\w\\d]*[a-zA-Z]){4})(?=(?:[\\w\\d]*\\d){1})[\\w\\d]{8,20}$");
    private static Pattern usPhonePattern = Pattern.compile("\\d{10}");
    private static Pattern emailAddressPattern = Pattern.compile("^[A-Za-z0-9._%+-]+@(?:[A-Za-z0-9-_]+\\.)+[A-Za-z]{2,4}$");
    private static Pattern phoneExtensionPattern = Pattern.compile("\\d{0,10}?");


    public boolean isValidZipCode(String zipCode) {
        return zipCodePattern.matcher(zipCode).matches();
    }

    public boolean isValid9DigitZipCode(String zipCode) {
        return nineDigitZipCodePattern.matcher(zipCode).matches();
    }

    public boolean isEmpty(String str) {
        return (str == null || str.trim().isEmpty());
    }

    public boolean isValidEmail(String emailAddress) {
        return emailAddressPattern.matcher(emailAddress).matches();
    }

    public boolean isValidPassword(String password) {
        return passwordPattern.matcher(password).matches();
    }

    public boolean isValidUSPhone(String sitePhone) {
        return usPhonePattern.matcher(StringUtils.unformatPhone(sitePhone)).matches();
    }

    public boolean isValidPhoneExtension(String extension) {
        return extension == null || phoneExtensionPattern.matcher(extension).matches();
    }


    public void doBasicValidation(boolean isEmpty, String blankMessage, boolean isValid, String errorMessage,
                                  ErrorList errors) {
        if (isEmpty) {
            errors.addBlankField(blankMessage);
        } else if (!isValid) {
            errors.addError(errorMessage);
        }
    }
}
