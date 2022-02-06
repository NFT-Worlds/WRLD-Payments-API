package com.nftworlds.wallet.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Holds information for player transaction events
 */
public class PlayerTransactEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private String wallet;
    private int amount;
    private String reason;

    public PlayerTransactEvent(@NotNull final Player player, @NotNull final String wallet, @NotNull final int amount, @NotNull final String reason) {
        super(player);
        this.wallet = wallet;
        this.amount = amount;
    }

    /**
     * Gets the wallet used during the transaction
     *
     * @return Player's wallet
     */
    @NotNull
    public String getWallet() {
        return wallet;
    }

    /**
     * Gets the amount of $WRLD from the transaction
     *
     * @return Amount of $WRLD
     */
    @NotNull
    public int getAmount() {
        return amount;
    }

    /**
     * Gets the amount of $WRLD from the transaction
     *
     * @return Amount of $WRLD
     */
    @NotNull
    public int getWRLD() {
        return amount;
    }

    /**
     * Gets the reason for the transaction
     *
     * @return Reason
     */
    @NotNull
    public String getReason() {
        return reason;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
