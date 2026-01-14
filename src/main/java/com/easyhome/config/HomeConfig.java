package com.easyhome.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Configuration manager for EasyHome plugin.
 * Supports in-game configuration via /easyhome admin command.
 */
public class HomeConfig {
    private final Path configFile;
    private final Gson gson;
    private ConfigData config;

    public HomeConfig(Path dataDirectory) {
        this.configFile = dataDirectory.resolve("config.json");
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.config = new ConfigData();
        load();
    }

    /**
     * Load configuration from file.
     */
    public void load() {
        if (Files.exists(configFile)) {
            try {
                String json = Files.readString(configFile);
                ConfigData loaded = gson.fromJson(json, ConfigData.class);
                if (loaded != null) {
                    config = loaded;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            save();
        }
    }

    /**
     * Reload configuration from file.
     */
    public void reload() {
        load();
    }

    /**
     * Save current configuration to file.
     */
    public void save() {
        try {
            Files.createDirectories(configFile.getParent());
            Files.writeString(configFile, gson.toJson(config));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ===== GETTERS =====

    public int getDefaultHomeLimit() {
        return config.defaultHomeLimit;
    }

    public int getMaxHomeLimit() {
        return config.maxHomeLimit;
    }

    public int getWarmupSeconds() {
        return config.warmupSeconds;
    }

    public double getMovementThreshold() {
        return config.movementThreshold;
    }

    public boolean isPermissionOverridesEnabled() {
        return config.permissionOverridesEnabled;
    }

    // ===== SETTERS (auto-save) =====

    public void setDefaultHomeLimit(int value) {
        config.defaultHomeLimit = Math.max(1, value);
        save();
    }

    public void setMaxHomeLimit(int value) {
        config.maxHomeLimit = Math.max(1, value);
        save();
    }

    public void setWarmupSeconds(int value) {
        config.warmupSeconds = Math.max(0, value);
        save();
    }

    public void setMovementThreshold(double value) {
        config.movementThreshold = Math.max(0.1, value);
        save();
    }

    public void setPermissionOverridesEnabled(boolean value) {
        config.permissionOverridesEnabled = value;
        save();
    }

    /**
     * Configuration data structure for JSON serialization.
     */
    private static class ConfigData {
        // Default number of homes for all players
        int defaultHomeLimit = 1;

        // Maximum homes a player can have (even with permissions)
        int maxHomeLimit = 50;

        // Teleport warmup time in seconds (0 = instant)
        int warmupSeconds = 3;

        // How far player can move before teleport cancels (in blocks)
        double movementThreshold = 0.5;

        // If true, permission nodes (homes.limit.X) can override defaultHomeLimit
        // If false, all players get defaultHomeLimit regardless of permissions
        boolean permissionOverridesEnabled = true;
    }
}
