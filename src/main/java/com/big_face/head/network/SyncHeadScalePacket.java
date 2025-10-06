package com.big_face.head.network;

import com.big_face.head.HeadScaleAttachment;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncHeadScalePacket(int playerId, float scale) implements CustomPacketPayload {

    public static final Type<SyncHeadScalePacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("headscale", "sync_head_scale"));

    public static final StreamCodec<ByteBuf, SyncHeadScalePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            SyncHeadScalePacket::playerId,
            ByteBufCodecs.FLOAT,
            SyncHeadScalePacket::scale,
            SyncHeadScalePacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.level != null) {
                Entity entity = minecraft.level.getEntity(playerId);
                if (entity instanceof Player player) {
                    player.setData(HeadScaleAttachment.HEAD_SCALE, scale);
                }
            }
        });
    }
}