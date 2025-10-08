package com.gate.item;

import com.gate.TutorialMod;
import com.gate.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TutorialMod.MOD_ID);

//    public static final Supplier<CreativeModeTab> BISMUTH_ITEMS_TAB = CREATIVE_MODE_TAB.register("bismuth_items_tab",
//            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.BISMUTH.get()))
//                    .title(Component.translatable("creativetab.template.bismuth_items"))
//                    .displayItems((itemDisplayParameters, output) -> {
//                        output.accept(ModItems.BISMUTH);
//                        output.accept(ModItems.RAW_BISMUTH);
//                    }).build());

    public static final Supplier<CreativeModeTab> BISMUTH_BLOCK_TAB = CREATIVE_MODE_TAB.register("bismuth_blocks_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(Blocks.DIAMOND_BLOCK))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(TutorialMod.MOD_ID, "bismuth_items_tab"))
                    .title(Component.translatable("creativetab.template.bismuth_blocks"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModBlocks.SWORD_GATE);
                        output.accept(ModBlocks.WOOD_SWORD_GATE);
                        output.accept(ModBlocks.IRON_ARMOR_BLOCK);
                        output.accept(ModBlocks.NETHERITE_ARMOR_BLOCK);
                        output.accept(ModBlocks.HEALTH_BLOCK);
                        output.accept(ModBlocks.POWER_BLOCK);
                    }).build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
