package com.empcraft.srl;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.empcraft.srl.listener.PlayerListener;
import com.empcraft.srl.util.MainUtil;

public class SignRanksLite extends JavaPlugin {
    public SignRanksLite plugin;
    public String version;
    public static YamlConfiguration language = null;
    public static YamlConfiguration languageFile = null;
    public static FileConfiguration config;

    @Override
    public void onEnable() {
        this.plugin = this;
        this.version = getDescription().getVersion();
        
        saveResource("en-US.yml", true);
        getConfig().options().copyDefaults(true);
        final Map<String, Object> options = new HashMap<String, Object>();
        getConfig().set("version", version);
        options.put("create.expand-vert",true);
        options.put("language","en-US");
        options.put("create.add-owner",true);
        options.put("max-region-count-per-player",7);
        options.put("max-claim-area", 1024);
        List<String> ignore = Arrays.asList("PlotMe","PlotWorld");
        options.put("ignore-worlds",ignore);
        for (final Entry<String, Object> node : options.entrySet()) {
             if (!getConfig().contains(node.getKey())) {
                 getConfig().set(node.getKey(), node.getValue());
             }
        }
        saveConfig();
        this.saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);   
        File yamlFile = new File(getDataFolder(), getConfig().getString("language").toLowerCase()+".yml");
        language = YamlConfiguration.loadConfiguration(yamlFile);
        
        for (C c : C.values()) {
            if (!language.contains(c.name())) {
                language.set(c.name(), c.d());
            }
            c.s(language.getString(c.name()));
        }
        
        config = getConfig();
    }
    
    @Override
    public void onDisable() {
        this.reloadConfig();
        this.saveConfig();
        MainUtil.sendMessage(null,"&d&oThanks for using &aSignRanksLite&d by &5Empire92&d!");
    }
}
