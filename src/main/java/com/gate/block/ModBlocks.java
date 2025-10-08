package com.gate.block;

import com.gate.TutorialMod;
import com.gate.block.custom.*;
import com.gate.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(TutorialMod.MOD_ID);
    public static final DeferredBlock<Block> SWORD_GATE = registerBlock("sword_gate",
            SwordGateBlock::new);
    public static final DeferredBlock<Block> WOOD_SWORD_GATE = registerBlock("wood_sword_gate",
            WoodSwordBlock::new);
    public static final DeferredBlock<Block> IRON_ARMOR_BLOCK = registerBlock("iron_armor_block",
            IronArmorBlock::new);
    public static final DeferredBlock<Block> NETHERITE_ARMOR_BLOCK = registerBlock("netherite_armor_block",
            NetheriteArmorBlock::new);
    public static final DeferredBlock<Block> HEALTH_BLOCK = registerBlock("health_block",
            HealthBoostBlock::new);
    public static final DeferredBlock<Block> POWER_BLOCK = registerBlock("power_block",
            AttackBoostBlock::new);

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
