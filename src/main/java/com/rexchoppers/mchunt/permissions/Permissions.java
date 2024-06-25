package com.rexchoppers.mchunt.permissions;

public enum Permissions {
    PERMISSION_ADMIN("mchunt.admin");

    private final String permission;

    Permissions(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
