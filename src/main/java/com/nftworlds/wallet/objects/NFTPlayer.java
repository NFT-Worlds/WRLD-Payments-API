package com.nftworlds.wallet.objects;

import com.nftworlds.wallet.NFTWorlds;
import com.nftworlds.wallet.contracts.nftworlds.Players;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Getter @Setter
public class NFTPlayer {

    private static HashSet<NFTPlayer> players = new HashSet<>();

    private UUID uuid;
    private Wallet wallets[]; //Primary is wallets[0]

    @SneakyThrows
    public NFTPlayer(UUID uuid) {
        this.uuid = uuid;

        Players playerContract = NFTWorlds.getInstance().getPlayers();

        String primary = playerContract.getPlayerPrimaryWallet(uuid.toString());
        List<String> secondary = playerContract.getPlayerSecondaryWallets(uuid.toString());

        wallets = new Wallet[secondary.size()+1];
        wallets[0] = new Wallet(primary, uuid);
        for (int i = 0; i < secondary.size(); i++) {
            wallets[i+1] = new Wallet(secondary.get(i), uuid);
        }

        players.add(this);
    }

    /**
     * Get a player's wallet(s)
     * @return player's wallet(s)
     */
    public Wallet[] getWallets() {
        return wallets;
    }

    /**
     * Get a player's primary wallet
     * @return player's wallet
     */
    public Wallet getPrimaryWallet() {
        return wallets[0];
    }

    /**
     * Send a request for a WRLD transaction to a player
     * @param amount
     * @param reason
     */
    public void requestWRLD(int amount, String reason) {

    }

    /**
     * Send WRLD to a player's primary wallet
     * @param amount
     * @param reason
     */
    public void sendWRLD(int amount, String reason) {

    }

    public static NFTPlayer getByUUID(UUID uuid) {
        for (NFTPlayer player : players) {
            if (player.getUuid().equals(uuid)) {
                return player;
            }
        }
        return null;
    }

}
