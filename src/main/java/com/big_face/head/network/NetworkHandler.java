package com.big_face.head.network;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class NetworkHandler {
    
    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(NetworkHandler::registerPackets);
    }
    
    private static void registerPackets(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        
        registrar.playToClient(
            SyncHeadScalePacket.TYPE,
            SyncHeadScalePacket.STREAM_CODEC,
            SyncHeadScalePacket::handle
        );
    }
    
    public static void sendToPlayer(SyncHeadScalePacket packet, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, packet);
    }
    
    public static void sendToAllPlayers(SyncHeadScalePacket packet, ServerLevel level) {
        PacketDistributor.sendToPlayersInDimension(level, packet);
    }
}