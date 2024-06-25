package com.rexchoppers.mchunt.locale;

import java.util.ResourceBundle;
import java.util.Locale;
import java.text.MessageFormat;

public class LocalizationManager {
    private ResourceBundle messages;

    public LocalizationManager(Locale locale) {
        this.messages = ResourceBundle.getBundle("language", locale);
    }

    public String getMessage(String key, Object... params) {
        String pattern = messages.getString(key);
        return MessageFormat.format(pattern, params);
    }
}
