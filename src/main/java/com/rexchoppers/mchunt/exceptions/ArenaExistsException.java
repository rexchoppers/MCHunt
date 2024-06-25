package com.rexchoppers.mchunt.exceptions;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.locale.LocalizationManager;

import java.util.Locale;

public class ArenaExistsException extends Exception {
    public ArenaExistsException(String name) {
        super(new LocalizationManager(MCHunt.getCurrentLocale()).getMessage("arena.exists", name));
    }
}