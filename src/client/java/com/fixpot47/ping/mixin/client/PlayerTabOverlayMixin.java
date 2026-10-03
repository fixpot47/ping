package com.fixpot47.ping.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerTabOverlay.class)
public abstract class PlayerTabOverlayMixin {
    @ModifyConstant(method = "extractRenderState", constant = @Constant(intValue = 13))
    private int ping$makeRoomForPingText(int original) {
        return 45;
    }

    @Inject(method = "extractPingIcon", at = @At("HEAD"), cancellable = true)
    private void ping$renderExactPing(
            GuiGraphicsExtractor graphics,
            int width,
            int x,
            int y,
            PlayerInfo playerInfo,
            CallbackInfo ci
    ) {
        int latency = playerInfo.getLatency();
        String text = latency < 0 ? "? ms" : latency + " ms";

        Minecraft minecraft = Minecraft.getInstance();
        int textWidth = minecraft.font.width(text);

        graphics.text(
                minecraft.font,
                text,
                x + width - textWidth,
                y,
                0xFFFFFF,
                true
        );

        ci.cancel();
    }
}
