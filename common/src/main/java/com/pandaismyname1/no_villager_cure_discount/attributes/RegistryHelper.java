package com.pandaismyname1.no_villager_cure_discount.attributes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;

import static com.pandaismyname1.no_villager_cure_discount.NoVillagerCureDiscount.MOD_ID;

public class RegistryHelper {

    public static ResourceLocation createAttributeIdentifier(String type, String name) {
        return createIdentifier(type + "." + name);
    }

    public static ResourceLocation createIdentifier(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static Attribute createDynamicAttribute(ResourceLocation id) {
        return new DynamicEntityAttribute(
                id.toLanguageKey("attribute")
        );
    }
}
