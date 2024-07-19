package com.pandaismyname1.no_villager_cure_discount.fabric;

import com.pandaismyname1.no_villager_cure_discount.NoVillagerCureDiscount;
import net.fabricmc.api.ModInitializer;

public class NoVillagerCureDiscountFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        NoVillagerCureDiscount.init();
    }
}