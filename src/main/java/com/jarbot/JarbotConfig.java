package com.jarbot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;

public class JarbotConfig {
    public boolean highlightEnabled = true;
    public boolean tracersEnabled = false;
    // store keycode + scancode where possible
    public int toggleKeyCode = GLFW.GLFW_KEY_G;
    public int toggleScanCode = 0;

    private static JarbotConfig INSTANCE;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = Path.of("config", "jarbot.json");

    public static JarbotConfig get() {
        if (INSTANCE == null) load();
        return INSTANCE;
    }

    public static void load() {
        try {
            File f = CONFIG_PATH.toFile();
            if (f.exists()) {
                try (FileReader fr = new FileReader(f)) {
                    INSTANCE = GSON.fromJson(fr, JarbotConfig.class);
                }
            } else {
                INSTANCE = new JarbotConfig();
                save();
            }
        } catch (Exception e) {
            INSTANCE = new JarbotConfig();
            e.printStackTrace();
        }
    }

    public static void save() {
        try {
            File f = CONFIG_PATH.toFile();
            f.getParentFile().mkdirs();
            try (FileWriter fw = new FileWriter(f)) {
                GSON.toJson(get(), fw);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void applyKeybinds() {
        JarbotConfig cfg = get();
        try {
            if (Keybinds.TOGGLE_FEATURE != null) {
                Keybinds.TOGGLE_FEATURE.setBoundKey(InputUtil.fromKeyCode(cfg.toggleKeyCode, cfg.toggleScanCode));
            }
        } catch (Throwable t) {
            // best-effort; ignore if the current platform/mappings don't support scancode mapping
        }
    }
}