package com.nftworlds.wallet.api;

import com.nftworlds.wallet.objects.NFTPlayer;
import com.nftworlds.wallet.objects.Wallet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class WalletAPI {

    /**
     * Get a player's wallet
     * @param player
     * @return player's wallet
     */
    public Wallet[] getWallets(Player player) {
        return getWallets(player.getUniqueId());
    }

    /**
     * Get a player's wallet
     * @param uuid
     * @return player's wallet
     */
    public Wallet[] getWallets(UUID uuid) {
        NFTPlayer player = NFTPlayer.getByUUID(uuid);
        if (player != null) {
            return player.getWallets();
        }
        return null;
    }

    /**
     * Get a player's primary wallet
     * @param player
     * @return player's wallet
     */
    public String getPrimaryWallet(Player player) {
        return getPrimaryWallet(player.getUniqueId());
    }

    /**
     * Get a player's primary wallet
     * @param uuid
     * @return player's wallet
     */
    public Wallet getPrimaryWallet(UUID uuid) {
        NFTPlayer player = NFTPlayer.getByUUID(uuid);
        if (player != null) {
            return player.getPrimaryWallet();
        }
        return null;
    }

    /**
     * Send a request for a WRLD transaction to a player
     * @param uuid
     * @param amount
     */
    public void requestWRLD(UUID uuid, int amount, String reason) {
        Player p = Bukkit.getPlayer(uuid);
        if (p != null) {
            //TODO: Send QR code to player on map
        }
    }

    /**
     * Send WRLD to a player's primary wallet
     * @param uuid
     * @param amount
     */
    public void sendWRLD(UUID uuid, int amount, String reason) {
        NFTPlayer player = NFTPlayer.getByUUID(uuid);
        if (player != null) {
            Wallet wallet = player.getPrimaryWallet();
            //TODO: Send WRLD to wallet
        }
    }



}
