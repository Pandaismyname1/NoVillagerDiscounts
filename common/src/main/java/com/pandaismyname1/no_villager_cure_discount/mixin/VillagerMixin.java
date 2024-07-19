package com.pandaismyname1.no_villager_cure_discount.mixin;

import com.pandaismyname1.no_villager_cure_discount.NoVillagerCureDiscount;
import com.pandaismyname1.no_villager_cure_discount.attributes.VillagerAttributes;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.gossip.GossipContainer;
import net.minecraft.world.entity.ai.gossip.GossipType;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.MerchantOffer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;

@Mixin(Villager.class)
public class VillagerMixin {

    @Final
    @Shadow
    private GossipContainer gossips;


    @Inject(method = "createAttributes", at = @At("RETURN"))
    private static void createAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        cir.getReturnValue().add(VillagerAttributes.CURE_COUNT);
    }

    @Inject(method = "onReputationEventFrom", at = @At("HEAD"), cancellable = true)
    public void disableCureDiscount(ReputationEventType eventType, Entity player, CallbackInfo ci) {
        if (eventType == ReputationEventType.ZOMBIE_VILLAGER_CURED) {
            if (NoVillagerCureDiscount.CONFIG.disableCureDiscount || NoVillagerCureDiscount.CONFIG.nerfCureDiscount == 0) {
                ci.cancel();
                return;
            }
            if (NoVillagerCureDiscount.CONFIG.nerfCureDiscount == 1) {
                return;
            }
            if (NoVillagerCureDiscount.CONFIG.nerfCureDiscount > 1) {
                NoVillagerCureDiscount.CONFIG.nerfCureDiscount = 1;
                System.err.println("nerfCureDiscount cannot be greater than 1");
            }
            if (NoVillagerCureDiscount.CONFIG.nerfCureDiscount < 0) {
                NoVillagerCureDiscount.CONFIG.nerfCureDiscount = 0;
                System.err.println("nerfCureDiscount cannot be less than 0");
            }
            var majorPositiveNewValue = NoVillagerCureDiscount.CONFIG.nerfCureDiscount * 20;
            var minorPositiveNewValue = NoVillagerCureDiscount.CONFIG.nerfCureDiscount * 25;
            Villager villager = (Villager)(Object)this;
            var attribute = VillagerAttributes.CURE_COUNT;
            var attributeInstance = villager.getAttribute(VillagerAttributes.CURE_COUNT);
            if (attributeInstance == null) {
                return;
            }
            var cureCount = attributeInstance.getBaseValue();
            if (NoVillagerCureDiscount.CONFIG.capCureAtempts && cureCount >= NoVillagerCureDiscount.CONFIG.maxCureAttempts) {
                ci.cancel();
                return;
            }
            this.gossips.add(player.getUUID(), GossipType.MAJOR_POSITIVE, Math.round(majorPositiveNewValue));
            this.gossips.add(player.getUUID(), GossipType.MINOR_POSITIVE, Math.round(minorPositiveNewValue));
            attributeInstance.setBaseValue(cureCount + 1);
            ci.cancel();
        }
    }

    @Inject(method = "updateSpecialPrices", at = @At("HEAD"), cancellable = true)
    private void updateSpecialPrices(Player p_35541_, CallbackInfo ci) {
        AbstractVillager abstractVillager = (AbstractVillager)(Object)this;
        Villager villager = (Villager)(Object)this;
        int i = villager.getPlayerReputation(p_35541_);
        if (i != 0) {
            Iterator var3 = abstractVillager.getOffers().iterator();

            while(var3.hasNext()) {
                MerchantOffer merchantoffer = (MerchantOffer)var3.next();
                merchantoffer.addToSpecialPriceDiff(-Mth.floor((float)i * merchantoffer.getPriceMultiplier()));
            }
        }

        if (NoVillagerCureDiscount.CONFIG.disableHeroOfTheVillage || NoVillagerCureDiscount.CONFIG.nerfHeroOfTheVillage == 0) {
            ci.cancel();
            return;
        }
        if (p_35541_.hasEffect(MobEffects.HERO_OF_THE_VILLAGE)) {
            MobEffectInstance mobeffectinstance = p_35541_.getEffect(MobEffects.HERO_OF_THE_VILLAGE);
            int k = mobeffectinstance.getAmplifier();
            Iterator var5 = abstractVillager.getOffers().iterator();

            if (NoVillagerCureDiscount.CONFIG.nerfHeroOfTheVillage > 1) {
                NoVillagerCureDiscount.CONFIG.nerfHeroOfTheVillage = 1;
                System.err.println("nerfHeroOfTheVillage cannot be greater than 1");
            }
            if (NoVillagerCureDiscount.CONFIG.nerfHeroOfTheVillage < 0) {
                NoVillagerCureDiscount.CONFIG.nerfHeroOfTheVillage = 0;
                System.err.println("nerfHeroOfTheVillage cannot be less than 0");
            }

            while(var5.hasNext()) {
                MerchantOffer merchantoffer1 = (MerchantOffer)var5.next();
                double d0 = (0.3 + 0.0625 * (double)k) * NoVillagerCureDiscount.CONFIG.nerfHeroOfTheVillage;
                int j = (int)Math.floor(d0 * (double)merchantoffer1.getBaseCostA().getCount());
                merchantoffer1.addToSpecialPriceDiff(-Math.max(j, 1));
            }
        }
        ci.cancel();
    }
}
