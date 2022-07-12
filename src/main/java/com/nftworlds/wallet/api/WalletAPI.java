package com.nftworlds.wallet.api;

import com.nftworlds.wallet.NFTWorlds;
import com.nftworlds.wallet.contracts.wrappers.common.ERC20;
import com.nftworlds.wallet.objects.NFTPlayer;
import com.nftworlds.wallet.objects.Network;
import com.nftworlds.wallet.objects.TransactionObjects;
import com.nftworlds.wallet.objects.Wallet;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.web3j.tx.gas.DefaultGasProvider;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class WalletAPI {

    /**
     * Get an NFT Player
     *
     * @param uuid
     * @return NFT Player
     */
    @Nullable
    public NFTPlayer getNFTPlayer(@NotNull UUID uuid) {
        return NFTPlayer.getByUUID(uuid);
    }

    /**
     * Get an NFT Player
     *
     * @param player
     * @return NFT Player
     */
    @Nullable
    public NFTPlayer getNFTPlayer(@NotNull Player player) {
        return NFTPlayer.getByUUID(player.getUniqueId());
    }

    /**
     * Get a player's wallet
     *
     * @param player
     * @return player's wallet
     */
    @Nullable
    public List<Wallet> getWallets(@NotNull Player player) {
        return getWallets(player.getUniqueId());
    }

    /**
     * Get a player's wallet
     *
     * @param uuid
     * @return player's wallet
     */
    @Nullable
    public List<Wallet> getWallets(@NotNull UUID uuid) {
        NFTPlayer player = NFTPlayer.getByUUID(uuid);
        if (player != null) {
            return player.getWallets();
        }
        return null;
    }

    /**
     * Get a player's primary wallet
     *
     * @param player
     * @return player's wallet
     */
    @Nullable
    public Wallet getPrimaryWallet(@NotNull Player player) {
        return getPrimaryWallet(player.getUniqueId());
    }

    /**
     * Get a player's primary wallet
     *
     * @param uuid
     * @return player's wallet
     */
    @Nullable
    public Wallet getPrimaryWallet(@NotNull UUID uuid) {
        NFTPlayer player = NFTPlayer.getByUUID(uuid);
        if (player != null) {
            return player.getPrimaryWallet();
        }
        return null;
    }

    /**
     * Send a request for a WRLD transaction to a player
     *
     * @param uuid
     * @param amount
     * @param network
     * @param reason
     * @param canDuplicate
     * @param payload
     * @return If $WRLD request was successful
     */
    public <T> boolean requestWRLD(@NotNull UUID uuid, @NotNull double amount, @NotNull Network network, @NotNull String reason, @NotNull boolean canDuplicate, @NotNull T payload) {
        NFTPlayer player = NFTPlayer.getByUUID(uuid);
        if (player != null) {
            return player.requestWRLD(amount, network, reason, canDuplicate, payload);
        }
        return false;
    }

    /**
     * Send WRLD to a player's primary wallet
     *
     * @param uuid
     * @param amount
     * @param network
     * @param reason
     * @return If $WRLD send was successful
     */
    public boolean sendWRLD(@NotNull UUID uuid, @NotNull double amount, @NotNull Network network, @NotNull String reason) {
        NFTPlayer player = NFTPlayer.getByUUID(uuid);
        if (player != null) {
            return player.sendWRLD(amount, network, reason);
        }
        return false;
    }

    /**
     * Send WRLD to a player's primary wallet
     *
     * @param player
     * @param amount
     * @param network
     * @param reason
     * @return If $WRLD send was successful
     */
    public boolean sendWRLD(@NotNull Player player, @NotNull double amount, @NotNull Network network, @NotNull String reason) {
        NFTPlayer p = NFTPlayer.getByUUID(player.getUniqueId());
        if (p != null) {
            return p.sendWRLD(amount, network, reason);
        }
        return false;
    }

    /**
     * Create a peer to peer payment link
     *
     * @param from
     * @param to
     * @param amount
     * @param network
     * @param reason
     * @return If player payment link was successfully created
     */
    public boolean createPlayerPayment(@NotNull Player from, @NotNull Player to, @NotNull double amount, @NotNull Network network, @NotNull String reason) {
        NFTPlayer nftPlayerFrom = NFTPlayer.getByUUID(from.getUniqueId());
        NFTPlayer nftPlayerTo = NFTPlayer.getByUUID(to.getUniqueId());
        if (nftPlayerFrom != null && nftPlayerTo != null) {
            return nftPlayerFrom.createPlayerPayment(nftPlayerTo, amount, network, reason);
        }
        return false;
    }

    /**
     * Create a peer to peer payment link
     *
     * @param from
     * @param to
     * @param amount
     * @param network
     * @param reason
     * @param payload
     * @return If player payment link was successfully created
     */
    public <T> boolean createPlayerPayment(@NotNull Player from, @NotNull Player to, @NotNull double amount, @NotNull Network network, @NotNull String reason, @NotNull T payload) {
        NFTPlayer nftPlayerFrom = NFTPlayer.getByUUID(from.getUniqueId());
        NFTPlayer nftPlayerTo = NFTPlayer.getByUUID(to.getUniqueId());
        if (nftPlayerFrom != null && nftPlayerTo != null) {
            return nftPlayerFrom.createPlayerPayment(nftPlayerTo, amount, network, reason, payload);
        }
        return false;
    }

    /**
     * Register a custom ERC20 token. This should be called during startup.
     *
     * @param contractAddress
     */
    public void registerERC20(@NotNull String contractAddress, @NotNull Network network) {
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
