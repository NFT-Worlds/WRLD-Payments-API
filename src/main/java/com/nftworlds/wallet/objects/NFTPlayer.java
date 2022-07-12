package com.nftworlds.wallet.objects;

import com.nftworlds.wallet.NFTWorlds;
import com.nftworlds.wallet.contracts.nftworlds.Players;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class NFTPlayer {

    private static final String EMPTY_ADDRESS = "0x0000000000000000000000000000000000000000";

    @Getter
    private static final ConcurrentHashMap<UUID, NFTPlayer> players = new ConcurrentHashMap<>();

    @Getter
    private final UUID uuid;
    private final List<Wallet> wallets;
    private boolean linked = false;

    @SneakyThrows
    public NFTPlayer(UUID uuid) {
        this.uuid = uuid;

        Players playerContract = NFTWorlds.getInstance().getPlayers();

        String primary = playerContract.getPlayerPrimaryWallet(uuid.toString().replace("-", ""));
        List<String> secondary = playerContract.getPlayerSecondaryWallets(uuid.toString().replace("-", ""));

        linked = !primary.equalsIgnoreCase(EMPTY_ADDRESS);

        wallets = new ArrayList<>();
        wallets.add(new Wallet(this, primary));
        for (String wallet : secondary) {
            wallets.add(new Wallet(this, wallet));
        }

        players.put(uuid, this);
    }

    /**
     * Get a player's wallet(s)
     *
     * @return player's wallet(s)
     */
    public List<Wallet> getWallets() {
        return wallets;
    }

    /**
     * Get a player's primary wallet
     *
     * @return player's wallet
     */
    public Wallet getPrimaryWallet() {
        return wallets.get(0);
    }

    public void setPrimaryWallet(Wallet wallet) {
        Wallet previousWallet = wallets.set(0, wallet);
        if (previousWallet != null) {
            NFTWorlds.getInstance().removeWallet(previousWallet);
        }
        linked = !wallet.getAddress().equalsIgnoreCase(EMPTY_ADDRESS);
    }

    /**
     * Send a request for a WRLD transaction to a player
     *
     * @param amount
     * @param network
     * @param reason
     * @param canDuplicate
     * @param payload
     * @return If $WRLD request was successful
     */
    public <T> boolean requestWRLD(@NotNull double amount, @NotNull Network network, @NotNull String reason, @NotNull boolean canDuplicate, @NotNull T payload) {
        return getPrimaryWallet().requestWRLD(amount, network, reason, canDuplicate, payload);
    }


    /**
     * Send WRLD to a player's primary wallet
     *
     * @param amount
     * @param network
     * @param reason
     * @return If $WRLD send was successful
     */
    public boolean sendWRLD(@NotNull double amount, @NotNull Network network, @NotNull String reason) {
        return getPrimaryWallet().payWRLD(amount, network, reason);
    }

    /**
     * Mint an ERC-1155 NFT to a player's primary wallet
     *
     * @param contractAddress
     * @param network
     * @param data
     * @return If NFT was minted
     */
    public boolean mintNFT(@NotNull String contractAddress, @NotNull Network network, @NotNull String data, @NotNull int id) {
        return getPrimaryWallet().mintERC1155NFT(contractAddress, network, data, id);
    }

    /**
     * Create a peer to peer player payment link
     *
     * @param to
     * @param amount
     * @param network
     * @param reason
     * @return If player payment link was successfully created
     */
    public boolean createPlayerPayment(@NotNull NFTPlayer to, @NotNull double amount, @NotNull Network network, @NotNull String reason) {
        return getPrimaryWallet().createPlayerPayment(to, amount, network, reason);
    }

    /**
     * Create a peer to peer player payment link
     *
     * @param to
     * @param amount
     * @param network
     * @param reason
     * @param payload
     * @return If player payment link was successfully created
     */
    public <T> boolean createPlayerPayment(@NotNull NFTPlayer to, @NotNull double amount, @NotNull Network network, @NotNull String reason, @NotNull T payload) {
        return getPrimaryWallet().createPlayerPayment(to, amount, network, reason, payload);
    }

    /**
     * Check if player has their wallet linked
     *
     * @return if player has wallet linked
     */
    public boolean isLinked() {
        return linked;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        NFTPlayer player = (NFTPlayer) object;
        return Objects.equals(uuid, player.uuid);
    }

    @Override
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }

    public static void remove(UUID uuid) {
        NFTPlayer player = players.remove(uuid);
        if (player != null) {
            NFTWorlds plugin = NFTWorlds.getInstance();
            for (Wallet wallet : player.wallets) {
                plugin.removeWallet(wallet);
            }
        }
    }

    public static NFTPlayer getByUUID(UUID uuid) {
        return players.getOrDefault(uuid, null);
    }

}
