package com.easyhome.commands;

import com.easyhome.EasyHome;
import com.easyhome.config.HomeConfig;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.permissions.PermissionsModule;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import java.awt.Color;

/**
 * /easyhome admin - Admin configuration commands for EasyHome.
 *
 * Subcommands:
 *   /easyhome admin config              - Show current settings
 *   /easyhome admin set <key> <value>   - Change a setting
 *   /easyhome admin reload              - Reload config from file
 */
public class HomeAdminCommand extends AbstractPlayerCommand {
    private final EasyHome plugin;

    private static final Color GOLD = new Color(255, 170, 0);
    private static final Color GREEN = new Color(85, 255, 85);
    private static final Color RED = new Color(255, 85, 85);
    private static final Color GRAY = new Color(170, 170, 170);
    private static final Color AQUA = new Color(85, 255, 255);
    private static final Color YELLOW = new Color(255, 255, 85);

    public HomeAdminCommand(EasyHome plugin) {
        super("easyhome", "EasyHome commands");
        this.plugin = plugin;
        setAllowsExtraArguments(true);
    }

    private String[] parseArgs(CommandContext ctx) {
        String input = ctx.getInputString().trim();
        if (input.isEmpty()) {
            return new String[0];
        }
        String[] allArgs = input.split("\\s+");
        if (allArgs.length > 0 && allArgs[0].equalsIgnoreCase("easyhome")) {
            String[] args = new String[allArgs.length - 1];
            System.arraycopy(allArgs, 1, args, 0, allArgs.length - 1);
            return args;
        }
        return allArgs;
    }

    @Override
    protected void execute(@Nonnull CommandContext ctx,
                          @Nonnull Store<EntityStore> store,
                          @Nonnull Ref<EntityStore> playerRef,
                          @Nonnull PlayerRef playerData,
                          @Nonnull World world) {

        String[] args = parseArgs(ctx);

        if (args.length == 0) {
            showUsage(playerData);
            return;
        }

        String subcommand = args[0];

        if (subcommand.equalsIgnoreCase("admin")) {
            handleAdmin(playerData, args);
        } else {
            showUsage(playerData);
        }
    }

    private void showUsage(PlayerRef playerData) {
        playerData.sendMessage(Message.raw("=== EasyHome ===").color(GOLD));
        playerData.sendMessage(Message.raw("/easyhome admin - Admin settings").color(GRAY));
    }

    private void handleAdmin(PlayerRef playerData, String[] args) {
        if (!hasAdminPermission(playerData)) {
            return;
        }

        // args[0] is "admin", so admin subcommand is args[1]
        if (args.length < 2) {
            showAdminHelp(playerData);
            return;
        }

        String adminCmd = args[1];
        String arg1 = args.length > 2 ? args[2] : null;
        String arg2 = args.length > 3 ? args[3] : null;

        switch (adminCmd.toLowerCase()) {
            case "config":
                showConfig(playerData);
                break;
            case "set":
                handleSet(playerData, arg1, arg2);
                break;
            case "reload":
                handleReload(playerData);
                break;
            default:
                playerData.sendMessage(Message.raw("Unknown command: " + adminCmd).color(RED));
                showAdminHelp(playerData);
        }
    }

    private boolean hasAdminPermission(PlayerRef playerData) {
        if (!PermissionsModule.get().hasPermission(playerData.getUuid(), "homes.admin")) {
            playerData.sendMessage(Message.raw("You don't have permission for this command.").color(RED));
            return false;
        }
        return true;
    }

    private void showAdminHelp(PlayerRef playerData) {
        playerData.sendMessage(Message.raw("=== EasyHome Admin ===").color(GOLD));
        playerData.sendMessage(Message.raw("/easyhome admin config - Show current settings").color(GRAY));
        playerData.sendMessage(Message.raw("/easyhome admin set default 3 - Give everyone 3 homes").color(GRAY));
        playerData.sendMessage(Message.raw("/easyhome admin set max 10 - Set max homes allowed").color(GRAY));
        playerData.sendMessage(Message.raw("/easyhome admin set warmup 0 - Disable teleport delay").color(GRAY));
        playerData.sendMessage(Message.raw("/easyhome admin reload - Reload settings").color(GRAY));
    }

    private void showConfig(PlayerRef playerData) {
        HomeConfig config = plugin.getConfig();
        playerData.sendMessage(Message.raw("=== EasyHome Settings ===").color(GOLD));
        playerData.sendMessage(Message.raw("").color(GRAY));
        playerData.sendMessage(Message.raw("Default homes: " + config.getDefaultHomeLimit()).color(AQUA));
        playerData.sendMessage(Message.raw("Max homes: " + config.getMaxHomeLimit()).color(AQUA));
        playerData.sendMessage(Message.raw("Teleport delay: " + config.getWarmupSeconds() + " seconds").color(AQUA));
        playerData.sendMessage(Message.raw("Permission mode: " + (config.isPermissionOverridesEnabled() ? "on" : "off")).color(AQUA));
        playerData.sendMessage(Message.raw("").color(GRAY));
        playerData.sendMessage(Message.raw("Change settings with:").color(GRAY));
        playerData.sendMessage(Message.raw("  /easyhome admin set default 5").color(YELLOW));
        playerData.sendMessage(Message.raw("  /easyhome admin set max 25").color(YELLOW));
        playerData.sendMessage(Message.raw("  /easyhome admin set warmup 0").color(YELLOW));
        playerData.sendMessage(Message.raw("  /easyhome admin set permissions on").color(YELLOW));
    }

    private void handleSet(PlayerRef playerData, String key, String valueStr) {
        if (key == null || valueStr == null) {
            playerData.sendMessage(Message.raw("How to change settings:").color(GOLD));
            playerData.sendMessage(Message.raw("  /easyhome admin set default 3").color(YELLOW));
            playerData.sendMessage(Message.raw("    Give everyone 3 homes").color(GRAY));
            playerData.sendMessage(Message.raw("  /easyhome admin set max 25").color(YELLOW));
            playerData.sendMessage(Message.raw("    Max homes anyone can have").color(GRAY));
            playerData.sendMessage(Message.raw("  /easyhome admin set warmup 0").color(YELLOW));
            playerData.sendMessage(Message.raw("    Teleport delay (0 = instant)").color(GRAY));
            playerData.sendMessage(Message.raw("  /easyhome admin set permissions on").color(YELLOW));
            playerData.sendMessage(Message.raw("    Let permissions override default").color(GRAY));
            return;
        }

        HomeConfig config = plugin.getConfig();

        switch (key.toLowerCase()) {
            case "default":
            case "defaultlimit":
                try {
                    int value = Integer.parseInt(valueStr);
                    config.setDefaultHomeLimit(value);
                    playerData.sendMessage(Message.raw("Default home limit set to " + value + "!").color(GREEN));
                } catch (NumberFormatException e) {
                    playerData.sendMessage(Message.raw("Please enter a number!").color(RED));
                }
                break;

            case "max":
            case "maxlimit":
                try {
                    int value = Integer.parseInt(valueStr);
                    config.setMaxHomeLimit(value);
                    playerData.sendMessage(Message.raw("Maximum home limit set to " + value + "!").color(GREEN));
                } catch (NumberFormatException e) {
                    playerData.sendMessage(Message.raw("Please enter a number!").color(RED));
                }
                break;

            case "warmup":
                try {
                    int value = Integer.parseInt(valueStr);
                    config.setWarmupSeconds(value);
                    if (value == 0) {
                        playerData.sendMessage(Message.raw("Teleport warmup disabled (instant teleport)!").color(GREEN));
                    } else {
                        playerData.sendMessage(Message.raw("Teleport warmup set to " + value + " seconds!").color(GREEN));
                    }
                } catch (NumberFormatException e) {
                    playerData.sendMessage(Message.raw("Please enter a number!").color(RED));
                }
                break;

            case "permissions":
            case "perms":
                boolean enabled = valueStr.equalsIgnoreCase("on") ||
                                  valueStr.equalsIgnoreCase("true") ||
                                  valueStr.equalsIgnoreCase("yes") ||
                                  valueStr.equalsIgnoreCase("enabled");
                config.setPermissionOverridesEnabled(enabled);
                if (enabled) {
                    playerData.sendMessage(Message.raw("Permission overrides enabled!").color(GREEN));
                    playerData.sendMessage(Message.raw("Players with homes.limit.X permissions can exceed default.").color(GRAY));
                } else {
                    playerData.sendMessage(Message.raw("Permission overrides disabled!").color(GREEN));
                    playerData.sendMessage(Message.raw("All players now get " + config.getDefaultHomeLimit() + " homes.").color(GRAY));
                }
                break;

            default:
                playerData.sendMessage(Message.raw("Unknown setting! Try: default, max, warmup, permissions").color(RED));
        }
    }

    private void handleReload(PlayerRef playerData) {
        plugin.getConfig().reload();
        playerData.sendMessage(Message.raw("Configuration reloaded!").color(GREEN));
        showConfig(playerData);
    }
}
