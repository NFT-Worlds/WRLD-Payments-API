package com.nftworlds.wallet.api;

import com.nftworlds.wallet.NFTWorlds;
import com.nftworlds.wallet.contracts.wrappers.common.ERC20;
import com.nftworlds.wallet.objects.NFTPlayer;
import com.nftworlds.wallet.objects.Network;
import com.nftworlds.wallet.objects.TransactionObjects;
import com.nftworlds.wallet.objects.Wallet;
import org.bukkit.entity.Player;
import org.web3j.tx.gas.DefaultGasProvider;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class WalletAPI {

    /**
     * Get an NFT Player
     * @param uuid
     * @return NFT Player
     */
    public NFTPlayer getNFTPlayer(UUID uuid) {
        return NFTPlayer.getByUUID(uuid);
    }

    /**
     * Get an NFT Player
     * @param player
     * @return NFT Player
     */
    public NFTPlayer getNFTPlayer(Player player) {
        return NFTPlayer.getByUUID(player.getUniqueId());
    }

    /**
     * Get a player's wallet
     * @param player
     * @return player's wallet
     */
    public List<Wallet> getWallets(Player player) {
        return getWallets(player.getUniqueId());
    }

    /**
     * Get a player's wallet
     * @param uuid
     * @return player's wallet
     */
    public List<Wallet> getWallets(UUID uuid) {
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
    public Wallet getPrimaryWallet(Player player) {
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
     * @param network
     * @param reason
     * @param canDuplicate
     * @param payload
     */
    public <T> void requestWRLD(UUID uuid, double amount, Network network, String reason, boolean canDuplicate, T payload) throws IOException, InterruptedException {
        NFTPlayer player = NFTPlayer.getByUUID(uuid);
        if (player != null) {
            player.requestWRLD(amount, network, reason, canDuplicate, payload);
        }
    }

    /**
     * Send WRLD to a player's primary wallet
     * @param uuid
     * @param amount
     * @param network
     * @param reason
     */
    public void sendWRLD(UUID uuid, double amount, Network network, String reason) {
        NFTPlayer player = NFTPlayer.getByUUID(uuid);
        if (player != null) {
            player.sendWRLD(amount, network, reason);
        }
    }

    /**
     * Send WRLD to a player's primary wallet
     * @param player
     * @param amount
     * @param network
     * @param reason
     */
    public void sendWRLD(Player player, double amount, Network network, String reason) {
        NFTPlayer p = NFTPlayer.getByUUID(player.getUniqueId());
        if (p != null) {
            p.sendWRLD(amount, network, reason);
        }
    }

    /**
     * Create a peer to peer payment link
     * @param from
     * @param to
     * @param amount
     * @param network
     * @param reason
     */
    public void createPlayerPayment(Player from, Player to, double amount, Network network, String reason) {
        NFTPlayer nftPlayerFrom = NFTPlayer.getByUUID(from.getUniqueId());
        NFTPlayer nftPlayerTo = NFTPlayer.getByUUID(to.getUniqueId());
        if (nftPlayerFrom != null && nftPlayerTo != null) {
            nftPlayerFrom.createPlayerPayment(nftPlayerTo, amount, network, reason);
        }
    }

    /**
     * Create a peer to peer payment link
     * @param from
     * @param to
     * @param amount
     * @param network
     * @param reason
     * @param payload
     */
    public <T> void createPlayerPayment(Player from, Player to, double amount, Network network, String reason, T payload) {
        NFTPlayer nftPlayerFrom = NFTPlayer.getByUUID(from.getUniqueId());
        NFTPlayer nftPlayerTo = NFTPlayer.getByUUID(to.getUniqueId());
        if (nftPlayerFrom != null && nftPlayerTo != null) {
            nftPlayerFrom.createPlayerPayment(nftPlayerTo, amount, network, reason, payload);
        }
    }

    /**
     * Register a custom ERC20 token. This should be called during startup.
     * @param contractAddress
     */
    public void registerERC20(String contractAddress, Network network) {
        if (network.equals(Network.POLYGON)) {
            ERC20 newToken = ERC20.load(
                    contractAddress,
                    NFTWorlds.getInstance().getPolygonRPC().getPolygonWeb3j(),
                    TransactionObjects.polygonTransactionManager,
                    TransactionObjects.fastGasProviderPolygon
            );
            Wallet.getCustomPolygonTokenWrappers().put(contractAddress, newToken);
        } else if (network.equals(Network.ETHEREUM)) {
            ERC20 newToken = ERC20.load(
                    contractAddress,
                    NFTWorlds.getInstance().getEthereumRPC().getEthereumWeb3j(),
                    TransactionObjects.ethereumTransactionManager,
                    new DefaultGasProvider()
            );
            Wallet.getCustomPolygonTokenWrappers().put(contractAddress, newToken);
        }
    }

}
