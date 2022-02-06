package com.nftworlds.wallet;

import com.nftworlds.wallet.listeners.PlayerListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Wallet extends JavaPlugin {

    private Wallet plugin;
    
    public void onEnable() {
        plugin = this;

        registerEvents();

        getServer().getConsoleSender().sendMessage("NFTWorlds Wallet API has been enabled");
    }

    public void onDisable() {
        plugin = null;
        getServer().getConsoleSender().sendMessage("NFTWorlds Wallet API has been disabled");
    }

    public void registerEvents() {
        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new PlayerListener(this), this);
    }

}
