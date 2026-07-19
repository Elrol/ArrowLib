package dev.elrol.arrowlib.libs;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Global Mojang Serialization Codecs for dynamic configuration structures.
 * Provides {@link Codec} patterns utilizing {@link Either} to smoothly decode custom JSON setups
 * that accept either a direct {@link ResourceKey} (singular object) or a {@link TagKey} (group filter).
 */
public class ArrowCodecs {

    /** Codec to handle input that is either a specific {@link Item} registry key or an item tag definition. */
    public static final Codec<Either<ResourceKey<Item>, TagKey<Item>>>                          TARGET_ITEM_CODEC           = Codec.either(ResourceKey.codec(Registries.ITEM), TagKey.codec(Registries.ITEM));

    /** Codec to handle input that is either a specific {@link Block} registry key or a block tag definition. */
    public static final Codec<Either<ResourceKey<Block>, TagKey<Block>>>                        TARGET_BLOCK_CODEC          = Codec.either(ResourceKey.codec(Registries.BLOCK), TagKey.codec(Registries.BLOCK));

    /** Codec to handle input that is either a specific {@link Enchantment} registry key or an enchantment tag definition. */
    public static final Codec<Either<ResourceKey<Enchantment>, TagKey<Enchantment>>>            TARGET_ENCHANTMENT_CODEC    = Codec.either(ResourceKey.codec(Registries.ENCHANTMENT), TagKey.codec(Registries.ENCHANTMENT));

    /** Codec to handle input that is either a specific {@link DamageType} registry key or a damage type tag definition. */
    public static final Codec<Either<ResourceKey<DamageType>, TagKey<DamageType>>>              TARGET_DAMAGE_TYPE_CODEC    = Codec.either(ResourceKey.codec(Registries.DAMAGE_TYPE), TagKey.codec(Registries.DAMAGE_TYPE));

    /** Codec to handle input that is either a specific {@link MobEffect} registry key or a status effect tag definition. */
    public static final Codec<Either<ResourceKey<MobEffect>, TagKey<MobEffect>>>                TARGET_STATUS_EFFECT_CODEC  = Codec.either(ResourceKey.codec(Registries.MOB_EFFECT), TagKey.codec(Registries.MOB_EFFECT));

    /** Codec to handle input that is either a specific {@link EntityType} registry key or an entity type tag definition. */
    public static final Codec<Either<ResourceKey<EntityType<?>>, TagKey<EntityType<?>>>>        TARGET_ENTITY_TYPE_CODEC    = Codec.either(ResourceKey.codec(Registries.ENTITY_TYPE), TagKey.codec(Registries.ENTITY_TYPE));

    /**
     * A codec that serializes a {@link UUID} as a standard canonical string.
     * <p>
     * Useful for human-readable JSON configs where a UUID needs to be represented as
     * a string (e.g., "123e4567-e89b-12d3-a456-426614174000") rather than an int-array.
     */
    public static final Codec<UUID> UUID_CODEC = Codec.STRING.xmap(UUID::fromString, UUID::toString);

    /**
     * A codec that serializes a {@link Set} of {@link UUID}s as a JSON array of strings.
     * <p>
     * Automatically deserializes the data into a mutable {@link HashSet} for efficient
     * lookup operations, and collects it into an {@link ArrayList} when saving out to disk.
     */
    public static final Codec<Set<UUID>> UUID_SET_CODEC = UUID_CODEC.listOf().xmap(HashSet::new, ArrayList::new);

}
