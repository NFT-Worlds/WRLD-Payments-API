package com.nftworlds.wallet;

import com.nftworlds.wallet.config.Config;
import com.nftworlds.wallet.contracts.nftworlds.Players;
import com.nftworlds.wallet.contracts.nftworlds.WRLD;
import com.nftworlds.wallet.listeners.PlayerListener;
import com.nftworlds.wallet.rpcs.Ethereum;
import com.nftworlds.wallet.rpcs.Polygon;
import lombok.Getter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class NFTWorlds extends JavaPlugin {
    private static NFTWorlds plugin;

    @Getter private Config nftConfig;
    @Getter private final ExecutorService executorService = Executors.newFixedThreadPool(5); //May or may not need eventually idk yet

    //Contracts
    @Getter private Players players;
    @Getter private WRLD wrld;

    //RPCs
    @Getter private Polygon polygonRPC;
    @Getter private Ethereum ethereumRPC;

    public void onEnable() {
        plugin = this;

        (nftConfig = new Config()).registerConfig();

        polygonRPC = new Polygon();
        ethereumRPC = new Ethereum();

        players = new Players();
        wrld = new WRLD();

        registerEvents();

        getServer().getConsoleSender().sendMessage("NFTWorlds Wallet API has been enabled");
    }

    public void onDisable() {
        plugin = null;
        getServer().getConsoleSender().sendMessage("NFTWorlds Wallet API has been disabled");
    }

    public void registerEvents() {
        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new PlayerListener(), this);
    }

    public static NFTWorlds getInstance() {
        return plugin;
    }
}
