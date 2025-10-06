package com.big_face.head;

import com.big_face.TutorialMod;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;

@EventBusSubscriber(modid = TutorialMod.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class HeadScaleRenderer {
    
    @SubscribeEvent
    public static void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
        Player player = event.getEntity();
        float headScale = player.getData(HeadScaleAttachment.HEAD_SCALE);
        
        if (headScale != 1.0F) {
            PlayerModel<AbstractClientPlayer> model = event.getRenderer().getModel();
            
            // 頭の位置を調整（頭の底部を基準にスケール）
            float yOffset = (1.0F - headScale) * 0.5F;
            
            // 頭パーツのスケーリング
            model.head.xScale = headScale;
            model.head.yScale = headScale;
            model.head.zScale = headScale;
            model.head.y = 0.0F + yOffset * 16.0F;
            
            // ハット（第2レイヤー）も同様にスケール
            model.hat.xScale = headScale;
            model.hat.yScale = headScale;
            model.hat.zScale = headScale;
            model.hat.y = 0.0F + yOffset * 16.0F;
        }
    }
    
    @SubscribeEvent
    public static void onRenderPlayerPost(RenderPlayerEvent.Post event) {
        Player player = event.getEntity();
        float headScale = player.getData(HeadScaleAttachment.HEAD_SCALE);
        
        if (headScale != 1.0F) {
            PlayerModel<AbstractClientPlayer> model = event.getRenderer().getModel();
            
            // スケールをリセット
            model.head.xScale = 1.0F;
            model.head.yScale = 1.0F;
            model.head.zScale = 1.0F;
            model.head.y = 0.0F;
            
            model.hat.xScale = 1.0F;
            model.hat.yScale = 1.0F;
            model.hat.zScale = 1.0F;
            model.hat.y = 0.0F;
        }
    }
}