package me.degeron.questplugin.NPC;

// Настройки второго слоя NPC

public enum ModelPart {
    CAPE(0x01),
    JACKET(0x02),
    LEFT_SLEEVE(0x04),
    RIGHT_SLEEVE(0x08),
    LEFT_PANTS(0x10),
    RIGHT_PANTS(0x20),
    HAT(0x40);

    private final byte mask;

    ModelPart(int mask) {
        this.mask = (byte) mask;
    }

    public byte getMask() {
        return mask;
    }

    public static byte getMasks(ModelPart... parts) {
        byte bytes = 0;
        for (ModelPart part : parts) {
            bytes += part.getMask();
        }
        return bytes;
    }

    public static byte getAllBitMasks() {
        byte bytes = 0;
        for (ModelPart modelPart : values()) {
            bytes += modelPart.getMask();
        }
        return bytes;
    }
}
