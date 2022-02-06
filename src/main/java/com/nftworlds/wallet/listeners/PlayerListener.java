package com.nftworlds.wallet.listeners;

import com.nftworlds.wallet.Wallet;
import com.nftworlds.wallet.objects.NFTPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class PlayerListener implements Listener {

    private Wallet plugin;

    public PlayerListener(Wallet instance) {
        this.plugin = instance;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(AsyncPlayerPreLoginEvent event) {
        new NFTPlayer(event.getUniqueId());
    }

}
