package com.rexchoppers.mchunt.exceptions;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.locale.LocalizationManager;

public class PlayerAlreadyInArenaSetupException extends Exception {
    public PlayerAlreadyInArenaSetupException() {
        super(new LocalizationManager(MCHunt.getCurrentLocale()).getMessage("player.already_in_area_setup"));
    }
}