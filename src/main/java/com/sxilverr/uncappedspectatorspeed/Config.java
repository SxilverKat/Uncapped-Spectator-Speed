package com.sxilverr.uncappedspectatorspeed;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = UncappedSpectatorSpeed.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue CAP_SPECTATOR_SPEED;
    private static final ForgeConfigSpec.DoubleValue MAXIMUM_SPECTATOR_SPEED;
    private static final ForgeConfigSpec.DoubleValue SCROLL_SPEED_MULTIPLIER;
    private static final ForgeConfigSpec.BooleanValue PREVENT_SPECTATOR_DEATH;

    static
    {
        CLIENT_BUILDER.push("Client");
        CAP_SPECTATOR_SPEED = CLIENT_BUILDER
                .comment("Cap spectator speed.")
                .define("cap_spectator_speed", false);
        MAXIMUM_SPECTATOR_SPEED = CLIENT_BUILDER
                .comment("Maximum spectator speed.")
                .defineInRange("maximum_spectator_speed", 0.2D, 0.0D, Double.MAX_VALUE);
        SCROLL_SPEED_MULTIPLIER = CLIENT_BUILDER
                .comment("Speed gained per scroll tick. Higher = faster.")
                .defineInRange("scroll_speed_multiplier", 1.0D, 0.0D, Double.MAX_VALUE);
        CLIENT_BUILDER.pop();

        COMMON_BUILDER.push("Common");
        PREVENT_SPECTATOR_DEATH = COMMON_BUILDER
                .comment("Prevent spectators from dying in the void.")
                .define("prevent_spectator_death", true);
        COMMON_BUILDER.pop();
    }

    static final ForgeConfigSpec CLIENT_SPEC = CLIENT_BUILDER.build();
    static final ForgeConfigSpec COMMON_SPEC = COMMON_BUILDER.build();

    public static boolean capSpectatorSpeed;
    public static double maximumSpectatorSpeed;
    public static double scrollSpeedMultiplier;
    public static boolean preventSpectatorDeath;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        if (event.getConfig().getSpec() == CLIENT_SPEC)
        {
            capSpectatorSpeed = CAP_SPECTATOR_SPEED.get();
            maximumSpectatorSpeed = MAXIMUM_SPECTATOR_SPEED.get();
            scrollSpeedMultiplier = SCROLL_SPEED_MULTIPLIER.get();
        }
        else if (event.getConfig().getSpec() == COMMON_SPEC)
        {
            preventSpectatorDeath = PREVENT_SPECTATOR_DEATH.get();
        }
    }
}
