package com.rexchoppers.mchunt.exceptions;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.managers.LocalizationManager;

public class PlayerAlreadyInArenaSetupException extends Exception {
    public PlayerAlreadyInArenaSetupException() {
        super(MCHunt.getLocalization().getMessage("player.already_in_area_setup"));
    }
}