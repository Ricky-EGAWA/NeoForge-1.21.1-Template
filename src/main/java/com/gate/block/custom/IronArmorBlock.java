package com.gate.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class IronArmorBlock extends SwordGateBlock {

    public IronArmorBlock() {
        super();
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide && entity instanceof Player player) {
            // 各防具スロットをチェックして、空なら装備
            if (player.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
                player.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
            }
            if (player.getItemBySlot(EquipmentSlot.CHEST).isEmpty()) {
                player.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
            }
            if (player.getItemBySlot(EquipmentSlot.LEGS).isEmpty()) {
                player.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
            }
            if (player.getItemBySlot(EquipmentSlot.FEET).isEmpty()) {
                player.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
            }
        }
    }
}