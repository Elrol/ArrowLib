package dev.elrol.arrowlib.libs;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.Nullable;

/**
 * 1.21.1 Data-Driven Enchantment helper methods.
 * Simplifies looking up registry entries and parsing dynamic level data via the current component systems.
 */
public class ArrowEnchantmentUtils {

    /**
     * Resolves an explicit {@link ResourceKey} enchantment definition into its standard runtime {@link Holder} wrapper.
     *
     * @param registryManager The dynamic world/server level registry controller wrapper.
     * @param enchantment     The resource indicator target identifier key.
     * @return The active {@link Holder} wrapper pointing to the loaded configuration element data, or {@code null} if missing.
     */
    @Nullable
    public static Holder<Enchantment> getEnchantmentEntry(RegistryAccess registryManager, ResourceKey<Enchantment> enchantment) {
        return registryManager.registryOrThrow(Registries.ENCHANTMENT)
                .getHolder(enchantment)
                .orElse(null);
    }

    /**
     * Identifies the current level of a specified enchantment applied to an item stack instance.
     *
     * @param registryManager The dynamic world/server level registry controller wrapper.
     * @param stack           The specific {@link ItemStack} instance being inspected.
     * @param enchantment     The resource indicator target identifier key.
     * @return The evaluation value level number found on the target item, or {@code -1} if the base registry entry fails resolution.
     */
    public static int getEnchantmentLevel(RegistryAccess registryManager, ItemStack stack, ResourceKey<Enchantment> enchantment) {
        Holder<Enchantment> entry = getEnchantmentEntry(registryManager, enchantment);
        return entry == null ? -1 : stack.getEnchantments().getLevel(entry);
    }

    /**
     * Helper check determining if a specified enchantment is loaded onto an item stack at level 1 or higher.
     *
     * @param registryManager The dynamic world/server level registry controller wrapper.
     * @param stack           The specific {@link ItemStack} instance being inspected.
     * @param enchantment     The resource indicator target identifier key.
     * @return {@code true} if an active reference match is discovered at a level greater than 0, otherwise {@code false}.
     */
    public static boolean hasEnchantment(RegistryAccess registryManager, ItemStack stack, ResourceKey<Enchantment> enchantment) {
        return getEnchantmentLevel(registryManager, stack, enchantment) > 0;
    }

}
