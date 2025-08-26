package com.rexchoppers.mchunt.exceptions;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.managers.LocalizationManager;

public class ArenaExistsException extends Exception {
    public ArenaExistsException(String name) {
        super(MCHunt.getLocalization().getMessage("arena.exists", name));
    }
}