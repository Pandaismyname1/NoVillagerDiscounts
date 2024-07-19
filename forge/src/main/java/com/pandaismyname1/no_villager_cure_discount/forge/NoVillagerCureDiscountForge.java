package com.pandaismyname1.no_villager_cure_discount.forge;

import dev.architectury.platform.forge.EventBuses;
import com.pandaismyname1.no_villager_cure_discount.NoVillagerCureDiscount;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(NoVillagerCureDiscount.MOD_ID)
public class NoVillagerCureDiscountForge {
    public NoVillagerCureDiscountForge() {
		// Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(NoVillagerCureDiscount.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        NoVillagerCureDiscount.init();
    }
}