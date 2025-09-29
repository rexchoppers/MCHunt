package com.rexchoppers.mchunt.managers;

import java.util.ResourceBundle;
import java.util.Locale;
import java.util.MissingResourceException;
import java.text.MessageFormat;

public class LocalizationManager {
    private final ResourceBundle messages;

    public LocalizationManager(Locale locale) {
        ResourceBundle bundle;
        try {
            // Try to load the requested locale

            bundle = ResourceBundle.getBundle("language", locale);
        } catch (MissingResourceException e) {
            // Fallback to English if not found (language_en.properties)
            bundle = ResourceBundle.getBundle("language", Locale.ENGLISH);
        }
        this.messages = bundle;
    }

    public String getMessage(String key, Object... params) {
        String pattern;
        try {
            pattern = messages.getString(key);
        } catch (MissingResourceException e) {
            // In case the key itself is missing, just return the key name
            pattern = key;
        }
        return MessageFormat.format(pattern, params);
    }
}
