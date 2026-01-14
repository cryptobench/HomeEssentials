package com.easyhome;

import com.easyhome.commands.DelHomeCommand;
import com.easyhome.commands.HomeAdminCommand;
import com.easyhome.commands.HomeCommand;
import com.easyhome.commands.HomeHelpCommand;
import com.easyhome.commands.HomesCommand;
import com.easyhome.commands.SetHomeCommand;
import com.easyhome.config.HomeConfig;
import com.easyhome.data.HomeStorage;
import com.easyhome.util.WarmupManager;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;

/**
 * EasyHome - A user-friendly home management plugin for Hytale.
 */
public class EasyHome extends JavaPlugin {

    private HomeConfig config;
    private HomeStorage storage;
    private WarmupManager warmupManager;

    public EasyHome(JavaPluginInit init) {
        super(init);
    }

    @Override
    public void setup() {
        // Initialize configuration
        config = new HomeConfig(getDataDirectory());

        // Initialize storage
        storage = new HomeStorage(getDataDirectory());

        // Initialize warmup manager
        warmupManager = new WarmupManager();

        // Register commands
        getCommandRegistry().registerCommand(new SetHomeCommand(this));
        getCommandRegistry().registerCommand(new HomeCommand(this));
        getCommandRegistry().registerCommand(new DelHomeCommand(this));
        getCommandRegistry().registerCommand(new HomesCommand(this));
        getCommandRegistry().registerCommand(new HomeHelpCommand(this));
        getCommandRegistry().registerCommand(new HomeAdminCommand(this));
    }

    @Override
    public void start() {
        // Plugin started
    }

    @Override
    public void shutdown() {
        // Save all data
        if (storage != null) {
            storage.saveAll();
        }

        // Shutdown warmup manager
        if (warmupManager != null) {
            warmupManager.shutdown();
        }
    }

    public HomeConfig getConfig() {
        return config;
    }

    public HomeStorage getStorage() {
        return storage;
    }

    public WarmupManager getWarmupManager() {
        return warmupManager;
    }

    /**
     * Get the home limit for a player.
     * Uses config-based defaults with optional permission overrides.
     */
    public int getHomeLimit(Player player) {
        // Check for unlimited permission first
        if (player.hasPermission("homes.limit.unlimited")) {
            return config.getMaxHomeLimit();
        }

        // If permission overrides are disabled, everyone gets the default
        if (!config.isPermissionOverridesEnabled()) {
            return config.getDefaultHomeLimit();
        }

        // Check for specific permission-based limits (highest first)
        // These override the default if enabled
        if (player.hasPermission("homes.limit.50")) {
            return Math.min(50, config.getMaxHomeLimit());
        }
        if (player.hasPermission("homes.limit.25")) {
            return Math.min(25, config.getMaxHomeLimit());
        }
        if (player.hasPermission("homes.limit.10")) {
            return Math.min(10, config.getMaxHomeLimit());
        }
        if (player.hasPermission("homes.limit.5")) {
            return Math.min(5, config.getMaxHomeLimit());
        }
        if (player.hasPermission("homes.limit.3")) {
            return Math.min(3, config.getMaxHomeLimit());
        }
        if (player.hasPermission("homes.limit.1")) {
            return Math.min(1, config.getMaxHomeLimit());
        }

        // Fall back to config default
        return config.getDefaultHomeLimit();
    }
}
