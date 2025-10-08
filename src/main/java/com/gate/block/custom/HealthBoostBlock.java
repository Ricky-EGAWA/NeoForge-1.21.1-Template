package com.gate.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class HealthBoostBlock extends SwordGateBlock {

    public HealthBoostBlock() {
        super();
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide && entity instanceof Player player) {
            AttributeInstance maxHealth = player.getAttribute(Attributes.MAX_HEALTH);
            if (maxHealth != null && maxHealth.getBaseValue() < 200.0) {
                // 最大体力を200（ハート100個）に設定
                maxHealth.setBaseValue(200.0);
                // 体力も全回復
                player.setHealth(200.0F);
            }
        }
        super.entityInside(state, level, pos, entity);
    }
}