package com.gate.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class AttackBoostBlock extends SwordGateBlock {
    // ResourceLocationで識別子を作成
    private static final ResourceLocation MODIFIER_ID =
            ResourceLocation.fromNamespaceAndPath("gate", "attack_boost");

    public AttackBoostBlock() {
        super();
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide && entity instanceof Player player) {
            AttributeInstance attackDamage = player.getAttribute(Attributes.ATTACK_DAMAGE);
            if (attackDamage != null) {
                // 既存のモディファイアを削除（重複防止）
                attackDamage.removeModifier(MODIFIER_ID);

                // 攻撃力を10倍にするモディファイア（+900%）
                AttributeModifier modifier = new AttributeModifier(
                        MODIFIER_ID,
                        999.0, // +900%（元の値×10にするため）
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                );
                attackDamage.addPermanentModifier(modifier);
            }
        }
    }
}