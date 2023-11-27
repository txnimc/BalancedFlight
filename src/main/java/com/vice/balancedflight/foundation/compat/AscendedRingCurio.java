package com.vice.balancedflight.foundation.compat;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.vice.balancedflight.BalancedFlight;
import com.vice.balancedflight.content.angelRing.FlightRing;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.InterModComms;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.type.capability.ICurio;

public class AscendedRingCurio implements ICurio
{
    private final FlightRing parent;

    public AscendedRingCurio(FlightRing parent)
    {
        this.parent = parent;
    }

    public static ICapabilityProvider initCapabilities(FlightRing stack) {
        ICurio curio = new AscendedRingCurio(stack);

        return new ICapabilityProvider() {
            private final LazyOptional<ICurio> curioOpt = LazyOptional.of(() -> curio);

            @Nonnull
            @Override
            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
                return CuriosCapability.ITEM.orEmpty(cap, curioOpt);
            }
        };
    }

    public static void sendImc() {
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("flight_ring").build());
    }

    public static boolean HasAscendedRing(Player player) { return CuriosApi.getCuriosHelper().findEquippedCurio(BalancedFlight.ASCENDED_FLIGHT_RING.get(), player).isPresent(); }


    @Override public boolean canEquip(String identifier, LivingEntity entityLivingBase) { return !HasAscendedRing((Player) entityLivingBase);}

    @Override public boolean canEquipFromUse(SlotContext slotContext) {
        return true;
    }

    @Override public ItemStack getStack()
    {
        return new ItemStack(parent);
    }

    @Override public boolean showAttributesTooltip(String identifier) {
        return true;
    }

    @Override public void playRightClickEquipSound(LivingEntity entityLivingBase) { entityLivingBase.playSound(SoundEvents.ARMOR_EQUIP_ELYTRA, 1.0F, 1.0F); }

}
