package com.rexchoppers.mchunt.exceptions;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.managers.LocalizationManager;

public class ArenaSetupNotFoundException extends Exception {
    public ArenaSetupNotFoundException() {
        super(MCHunt.getLocalization().getMessage("arena_setup.not_found"));
    }
}