package com.pandaismyname1.no_villager_cure_discount.attributes;

import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;

import static com.pandaismyname1.no_villager_cure_discount.NoVillagerCureDiscount.MOD_ID;

public class VillagerAttributes {

    public static final ResourceLocation CURE_COUNT_ID
            = RegistryHelper.createAttributeIdentifier("entity", "");
    public static final Attribute CURE_COUNT = RegistryHelper.createDynamicAttribute(
            CURE_COUNT_ID
    ).setSyncable(true);


    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(MOD_ID, Registries.ATTRIBUTE);

    public static void setup() {
        ATTRIBUTES.register(CURE_COUNT_ID.getPath(), () -> CURE_COUNT);
    }
}


