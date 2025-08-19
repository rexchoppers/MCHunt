package com.rexchoppers.mchunt.repositories;

import com.rexchoppers.mchunt.MCHunt;

import java.util.List;

public class Repository<T> {

    private final MCHunt plugin;
    private final String filePath;

    private List<T> data;

    public Repository(MCHunt plugin, String filePath) {
        this.plugin = plugin;
        this.filePath = filePath;
    }
}
