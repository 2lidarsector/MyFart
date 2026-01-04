package com.jarbot;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OverlayRenderer {

    // store previous glowing state for players we modify so we can restore it later
    private static final Map<UUID, Boolean> previousGlowing = new ConcurrentHashMap<>();

    public static void render(WorldRenderContext context) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.world == null || mc.player == null) return;
        MatrixStack matrices = context.matrixStack();

        double camX = context.camera().getPos().x;
        double camY = context.camera().getPos().y;
        double camZ = context.camera().getPos().z;

        // Handle highlighting: set glowing on other players and record previous state.
        JarbotConfig cfg = JarbotConfig.get();
        for (PlayerEntity p : mc.world.getPlayers()) {
            if (p == mc.player) continue;

            UUID id = p.getUuid();
            if (cfg.highlightEnabled) {
                // record previous state once
                previousGlowing.putIfAbsent(id, p.isGlowing());
                // set glowing (client-side)
                p.setGlowing(true);
            } else {
                // if we previously changed it, restore the original state
                if (previousGlowing.containsKey(id)) {
                    Boolean prev = previousGlowing.remove(id);
                    p.setGlowing(prev != null && prev);
                } else {
                    // do nothing to avoid interfering with server-set glowing
                }
            }
        }

        // Clean up map for players who left
        previousGlowing.keySet().removeIf(uuid -> mc.world.getPlayerByUuid(uuid) == null);

        // Tracer rendering
        if (!cfg.tracersEnabled) return;

        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.lineWidth(2.0F);

        VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        VertexConsumer consumer = immediate.getBuffer(RenderLayer.getLines());
        Matrix4f matrix = matrices.peek().getPositionMatrix();

        for (PlayerEntity p : mc.world.getPlayers()) {
            if (p == mc.player) continue;

            Vec3d eye = p.getPos().add(0, p.getEyeHeight(p.getPose()) * 0.5, 0);
            double x = eye.x - camX;
            double y = eye.y - camY;
            double z = eye.z - camZ;

            int r = 255, g = 100, b = 100, a = 255;

            consumer.vertex(matrix, 0f, 0f, 0f).color(r, g, b, a).next();
            consumer.vertex(matrix, (float) x, (float) y, (float) z).color(r, g, b, a).next();
        }

        immediate.draw();

        RenderSystem.lineWidth(1.0F);
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
    }
}