package com.moneymantra.storageshield;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class StorageShieldConfig {
    private StorageShieldConfig() {}

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final General GENERAL = new General(BUILDER);
    public static final TargetRule CHESTS = new TargetRule(BUILDER, "chests", true, 6, 18, true, false);
    public static final TargetRule TRAPPED_CHESTS = new TargetRule(BUILDER, "trapped_chests", true, 6, 18, true, false);
    public static final TargetRule ENDER_CHESTS = new TargetRule(BUILDER, "ender_chests", true, 6, 18, true, false);
    public static final TargetRule SHULKER_BOXES = new TargetRule(BUILDER, "shulker_boxes", true, 6, 16, true, false);
    public static final TargetRule BARRELS = new TargetRule(BUILDER, "barrels", true, 6, 18, true, false);
    public static final TargetRule ITEM_FRAMES = new TargetRule(BUILDER, "item_frames", true, 4, 12, true, false);
    public static final TargetRule SIGNS = new TargetRule(BUILDER, "signs", true, 6, 16, true, false);
    public static final TargetRule HANGING_SIGNS = new TargetRule(BUILDER, "hanging_signs", true, 6, 16, true, false);
    public static final TargetRule HOPPERS = new TargetRule(BUILDER, "hoppers", true, 4, 16, true, false);

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static final class General {
        public final ModConfigSpec.BooleanValue enabled;
        public final ModConfigSpec.BooleanValue debugOverlay;
        public final ModConfigSpec.BooleanValue showStartupMessage;
        public final ModConfigSpec.IntValue extremeRenderDistance;
        public final ModConfigSpec.IntValue maxWallChecksPerTick;
        public final ModConfigSpec.ConfigValue<String> defaultRuntimeProfile;

        private General(ModConfigSpec.Builder builder) {
            builder.push("general");
            enabled = builder
                    .comment("Master switch. If false, StorageShield never skips any supported renderer.")
                    .define("enabled", true);
            debugOverlay = builder
                    .comment("Draw a small debug overlay showing how many renderers were allowed/skipped during the current tick.")
                    .define("debugOverlay", true);
            showStartupMessage = builder
                    .comment("Show a short chat message when a world is joined.")
                    .define("showStartupMessage", true);
            defaultRuntimeProfile = builder
                    .comment("Initial runtime profile: NORMAL, PERFORMANCE, EXTREME, or OFF.")
                    .define("defaultRuntimeProfile", "PERFORMANCE");
            extremeRenderDistance = builder
                    .comment("When runtime profile is EXTREME, this caps every supported target's hideBeyondDistance to this value.")
                    .defineInRange("extremeRenderDistance", 8, 0, 128);
            maxWallChecksPerTick = builder
                    .comment("Safety limit for occlusion raycasts per client tick. Higher is more aggressive behind-wall culling; lower is safer for weak CPUs.")
                    .defineInRange("maxWallChecksPerTick", 768, 0, 20000);
            builder.pop();
        }
    }

    public static final class TargetRule {
        public final ModConfigSpec.BooleanValue enabled;
        public final ModConfigSpec.BooleanValue hideAll;
        public final ModConfigSpec.IntValue normalRenderDistance;
        public final ModConfigSpec.IntValue hideBeyondDistance;
        public final ModConfigSpec.BooleanValue hideBehindWalls;

        private TargetRule(ModConfigSpec.Builder builder, String name, boolean defaultEnabled, int defaultNormalDistance, int defaultHideDistance, boolean defaultHideBehindWalls, boolean defaultHideAll) {
            builder.push(name);
            enabled = builder
                    .comment("Enable StorageShield optimization for this target type.")
                    .define("enabled", defaultEnabled);
            hideAll = builder
                    .comment("Nuke-button test setting. If true, this target type is never rendered client-side.")
                    .define("hideAll", defaultHideAll);
            normalRenderDistance = builder
                    .comment("Distance in blocks where this target always renders normally. Behind-wall checks begin beyond this range.")
                    .defineInRange("normalRenderDistance", defaultNormalDistance, 0, 128);
            hideBeyondDistance = builder
                    .comment("Distance in blocks beyond which this target is not rendered.")
                    .defineInRange("hideBeyondDistance", defaultHideDistance, 0, 256);
            hideBehindWalls = builder
                    .comment("If true, targets beyond normalRenderDistance are hidden when a solid block is between the camera and target.")
                    .define("hideBehindWalls", defaultHideBehindWalls);
            builder.pop();
        }
    }
}
