package com.big_face.head;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.big_face.head.network.NetworkHandler;
import com.big_face.head.network.SyncHeadScalePacket;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public class HeadScaleCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("headscale")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.argument("scale", FloatArgumentType.floatArg(0.1F, 10.0F))
                                        .executes(ctx -> setHeadScale(
                                                ctx,
                                                EntityArgument.getPlayers(ctx, "targets"),
                                                FloatArgumentType.getFloat(ctx, "scale")
                                        ))
                                )
                                .then(Commands.literal("reset")
                                        .executes(ctx -> setHeadScale(
                                                ctx,
                                                EntityArgument.getPlayers(ctx, "targets"),
                                                1.0F
                                        ))
                                )
                        )
                        .then(Commands.literal("get")
                                .then(Commands.argument("target", EntityArgument.player())
                                        .executes(ctx -> getHeadScale(ctx, EntityArgument.getPlayer(ctx, "target")))
                                )
                        )
        );
    }

    private static int setHeadScale(CommandContext<CommandSourceStack> ctx, Collection<ServerPlayer> players, float scale) {
        for (ServerPlayer player : players) {
            player.setData(HeadScaleAttachment.HEAD_SCALE, scale);

            // クライアントに同期
            NetworkHandler.sendToPlayer(new SyncHeadScalePacket(player.getId(), scale), player);

            // 他のプレイヤーにも同期（見えるように）
            NetworkHandler.sendToAllPlayers(new SyncHeadScalePacket(player.getId(), scale), player.serverLevel());
        }

        if (players.size() == 1) {
            ServerPlayer player = players.iterator().next();
            ctx.getSource().sendSuccess(
                    () -> Component.translatable("command.headscale.set.single", player.getName().getString(), scale),
                    true
            );
        } else {
            final int count = players.size();
            ctx.getSource().sendSuccess(
                    () -> Component.translatable("command.headscale.set.multiple", count, scale),
                    true
            );
        }

        return players.size();
    }

    private static int getHeadScale(CommandContext<CommandSourceStack> ctx, ServerPlayer player) {
        float scale = player.getData(HeadScaleAttachment.HEAD_SCALE);
        ctx.getSource().sendSuccess(
                () -> Component.translatable("command.headscale.get", player.getName().getString(), scale),
                false
        );
        return 1;
    }
}
