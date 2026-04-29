package com.sxilverr.uncappedspectatorspeed;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(UncappedSpectatorSpeed.MODID)
public class UncappedSpectatorSpeed
{
    public static final String MODID = "uncappedspectatorspeed";

    public UncappedSpectatorSpeed(FMLJavaModLoadingContext context)
    {
        context.registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_SPEC);
        context.registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC);
    }

    @Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientEvents
    {
        @SubscribeEvent
        public static void onMouseScroll(InputEvent.MouseScrollingEvent event)
        {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null || !mc.player.isSpectator()) return;
            if (mc.gui.getSpectatorGui().isMenuActive()) return;

            double scrollDelta = event.getScrollDelta();
            Abilities abilities = mc.player.getAbilities();
            float current = abilities.getFlyingSpeed();
            float step = (float) (scrollDelta * 0.005D * Config.scrollSpeedMultiplier);
            float updated;

            if (Config.capSpectatorSpeed)
            {
                float max = (float) Config.maximumSpectatorSpeed;
                updated = Mth.clamp(current + step, 0.0F, max);
            }
            else
            {
                updated = Math.max(0.0F, current + step);
            }

            abilities.setFlyingSpeed(updated);
            event.setCanceled(true);
        }
    }

    @Mod.EventBusSubscriber(modid = MODID)
    public static class CommonEvents
    {
        @SubscribeEvent
        public static void onLivingAttack(LivingAttackEvent event)
        {
            if (!Config.preventSpectatorDeath) return;
            if (!(event.getEntity() instanceof Player player)) return;
            if (!player.isSpectator()) return;
            if (event.getSource().is(DamageTypes.FELL_OUT_OF_WORLD))
            {
                event.setCanceled(true);
            }
        }
    }
}
