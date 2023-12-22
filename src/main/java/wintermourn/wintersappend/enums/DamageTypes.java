package wintermourn.wintersappend.enums;

public enum DamageTypes {
    SWEET_BERRY("sweetBerryBush"),
    THORNS("thorns"),
    ELYTRA("flyIntoWall"),
    FALLING("fall"),
    CACTUS("cactus");

    public final String id;
    DamageTypes(String s) {
        id = s;
    }

    @Override
    public String toString() {
        return id;
    }
}
