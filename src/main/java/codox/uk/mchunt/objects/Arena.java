package codox.uk.mchunt.objects;

import java.util.UUID;

public class Arena {

    private String id;

    public Arena() {
        this.id = UUID.randomUUID().toString();
    }
}
