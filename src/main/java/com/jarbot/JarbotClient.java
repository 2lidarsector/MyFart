package com.jarbot;

import com.jarbot.ui.JarbotScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;

public class JarbotClient implements ClientModInitializer {

    public static final String MODID = "jarbot";

    @Override
    public void onInitializeClient() {
        // Load config
        JarbotConfig.load();

        // Register keybinds
        Keybinds.register();

        // UI open and toggle handling
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Open UI
            while (Keybinds.OPEN_UI.wasPressed()) {
                MinecraftClient mc = MinecraftClient.getInstance();
                mc.setScreen(new JarbotScreen());
            }

            // Toggle action
            while (Keybinds.TOGGLE_FEATURE.wasPressed()) {
                JarbotConfig cfg = JarbotConfig.get();
                cfg.highlightEnabled = !cfg.highlightEnabled;
                JarbotConfig.save();
            }
        });

        // Render event: handles tracers and sets glowing flags
        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            OverlayRenderer.render(context);
        });
    }
}