package com.nftworlds.wallet.listeners;

import com.nftworlds.wallet.NFTWorlds;
import com.nftworlds.wallet.objects.NFTPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private NFTWorlds plugin;

    public PlayerListener() {
        this.plugin = NFTWorlds.getInstance();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(AsyncPlayerPreLoginEvent event) {
        new NFTPlayer(event.getUniqueId());
    }

    public void onQuit(PlayerQuitEvent event) {
        NFTPlayer.remove(event.getPlayer().getUniqueId());
    }

}
