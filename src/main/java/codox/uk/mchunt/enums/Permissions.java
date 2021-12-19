package codox.uk.mchunt.enums;

public enum Permissions {
    PERMISSION_ADMIN("mchunt.admin");

    private final String stringValue;

    Permissions(final String s) {
        stringValue = s;
    }

    public String toString() {
        return stringValue;
    }
}
