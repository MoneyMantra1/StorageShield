package com.moneymantra.storageshield.client;

public final class StorageShieldStats {
    private static int renderedBlockEntities;
    private static int skippedBlockEntities;
    private static int renderedItemFrames;
    private static int skippedItemFrames;
    private static int wallChecks;
    private static int wallCheckLimitHits;

    private StorageShieldStats() {}

    public static void beginTick() {
        renderedBlockEntities = 0;
        skippedBlockEntities = 0;
        renderedItemFrames = 0;
        skippedItemFrames = 0;
        wallChecks = 0;
        wallCheckLimitHits = 0;
    }

    public static void blockEntityRendered() {
        renderedBlockEntities++;
    }

    public static void blockEntitySkipped() {
        skippedBlockEntities++;
    }

    public static void itemFrameRendered() {
        renderedItemFrames++;
    }

    public static void itemFrameSkipped() {
        skippedItemFrames++;
    }

    public static void wallCheck() {
        wallChecks++;
    }

    public static void wallCheckLimitHit() {
        wallCheckLimitHits++;
    }

    public static int wallChecks() {
        return wallChecks;
    }

    public static String summary() {
        return "BE rendered=" + renderedBlockEntities
                + ", BE skipped=" + skippedBlockEntities
                + ", frames rendered=" + renderedItemFrames
                + ", frames skipped=" + skippedItemFrames
                + ", wall checks=" + wallChecks
                + ", wall-limit hits=" + wallCheckLimitHits;
    }

    public static int renderedBlockEntities() {
        return renderedBlockEntities;
    }

    public static int skippedBlockEntities() {
        return skippedBlockEntities;
    }

    public static int renderedItemFrames() {
        return renderedItemFrames;
    }

    public static int skippedItemFrames() {
        return skippedItemFrames;
    }

    public static int wallCheckLimitHits() {
        return wallCheckLimitHits;
    }
}
