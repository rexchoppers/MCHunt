package com.rexchoppers.mchunt.models;

import com.google.gson.annotations.Expose;
import com.rexchoppers.mchunt.enums.ArenaStatus;

public class Arena {
    @Expose
    private String id;

    @Expose
    private String name;

    private ArenaStatus status;


    public Arena(String id, String name) {
        this.id = id;
        this.name = name;
        this.status = ArenaStatus.OFFLINE;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArenaStatus getStatus() {
        return status;
    }

    public void setStatus(ArenaStatus status) {
        this.status = status;
    }
}
