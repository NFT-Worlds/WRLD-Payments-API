package com.nftworlds.wallet.listeners;

import com.nftworlds.wallet.NFTWorlds;
import com.nftworlds.wallet.objects.NFTPlayer;
import com.nftworlds.wallet.qrmaps.QRMapManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

    private NFTWorlds plugin;

    public PlayerListener() {
        this.plugin = NFTWorlds.getInstance();
    }

    // No need to retrieve the wallet here tbh just do regular player join.
    // Async event is called when player is rejected such as from a ban or a whitelist
    
    /*@EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(AsyncPlayerPreLoginEvent event) {
        new NFTPlayer(event.getUniqueId());
    }
    */

    @EventHandler(priority = EventPriority.LOWEST)
    public void postJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        new BukkitRunnable() {
        	@Override
        	public void run() {
                new NFTPlayer(p.getUniqueId());
                if (!NFTPlayer.getByUUID(p.getUniqueId()).isLinked()) {
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        if (!p.isOnline()) return;
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', " \n&f&lIMPORTANT: &cYou do not have a wallet linked!\n&7Link your wallet at &a&nhttps://nftworlds.com/login&r\n "));
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                    }, 20L);
                }
        	}
        }.runTaskAsynchronously(plugin);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        NFTPlayer.remove(event.getPlayer().getUniqueId());
        restoreItemReplacedWithMap(event.getPlayer());
    }

    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            restoreItemReplacedWithMap(event.getPlayer());
        }
    }

    private void restoreItemReplacedWithMap(Player player) {
        ItemStack previousItem = QRMapManager.playerPreviousItem.get(player.getUniqueId());
        if (previousItem != null) {
            player.getInventory().setItem(0, previousItem);
            QRMapManager.playerPreviousItem.remove(player.getUniqueId());
        }
    }

}
