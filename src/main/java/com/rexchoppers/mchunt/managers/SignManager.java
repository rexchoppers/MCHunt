package com.rexchoppers.mchunt.managers;

import com.rexchoppers.mchunt.signs.ScrollingSign;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class SignManager {
    private Map<Location, ScrollingSign> signs = new HashMap<>();

    public SignManager() {}

    public void addSign(Location location, ScrollingSign sign) {
        signs.put(location, sign);
    }
}
