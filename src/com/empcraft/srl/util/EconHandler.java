package com.empcraft.srl.util;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.UUID;

/**
 * Also handles some permissions stuff
 */
public class EconHandler {

    private static Permission perms = null;
    private static Economy    econ  = null;

    public EconHandler(final Plugin vaultPlugin) {
        setupPermissions();
        setupEconomy();
    }

    private static boolean setupEconomy() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        final RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private static boolean setupPermissions() {
        final RegisteredServiceProvider<Permission> rsp = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    /**
     * Get a user's group
     * @param world
     * @param uuid
     * @return String (group name)
     */
    public static String getGroup(final World world, final UUID uuid) {
        return perms.getPrimaryGroup(world.getName(), Bukkit.getOfflinePlayer(uuid));
    }
    
    public static boolean isGroup(String check) {
        String[] groups = perms.getGroups();
        for (String group : groups) {
            if (group.equals(check)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean hasSubgroup(Player player, String check) {
        String[] groups = perms.getPlayerGroups(player);
        for (String group : groups) {
            if (group.equals(check)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if a user has money
     * @param player (Player)
     * @param amount (double)
     * @return boolean
     */
    public static boolean hasMoney(final Player player, final double amount) {
        return econ.has(player, amount);
    }
    
    /**
     * Check if a user has money
     * @param player (UUID)
     * @param amount (double)
     * @return boolean
     */
    public static boolean hasMoney(final UUID player, final double amount) {
        return econ.has(Bukkit.getOfflinePlayer(player), amount);
    }

    /**
     * Withdraw money from a user
     * @param player
     * @param amount
     */
    public static void takeMoney(final Player player, final double amount) {
        econ.withdrawPlayer(player, amount);
    }
}
