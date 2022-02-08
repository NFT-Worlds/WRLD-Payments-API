package com.nftworlds.wallet.objects;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.UUID;

@Getter @Setter
public class NFTPlayer {

    private static HashSet<NFTPlayer> players = new HashSet<>();

    private UUID uuid;
    private Wallet wallets[]; //Primary is wallets[0]

    public NFTPlayer(UUID uuid) {
        this.uuid = uuid;
        //TODO: Get player's address(s) from database
        players.add(this);
    }

    public Wallet getPrimaryWallet() {
        return wallets[0];
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
