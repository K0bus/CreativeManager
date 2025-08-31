package fr.k0bus.creativemanager.type;

public enum CustomType {
    COMMANDS("commands"),
    PLACE("place"),
    BREAK("break"),
    ITEM_USE("item-use"),
    BLOCK_USE("block-use"),
    STORE_ITEM("store-item"),
    NBT("nbt");

    private final String id;

    CustomType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
