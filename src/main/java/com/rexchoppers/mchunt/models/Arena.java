package com.rexchoppers.mchunt.models;

import com.rexchoppers.mchunt.enums.ArenaStatus;

public class Arena {
    private String id;
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
