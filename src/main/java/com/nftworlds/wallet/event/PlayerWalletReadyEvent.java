package com.nftworlds.wallet.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;
import org.web3j.abi.datatypes.generated.Uint256;

/**
 * Holds information for player transaction events
 */
public class PlayerWalletReadyEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    public PlayerWalletReadyEvent(@NotNull final Player player) {
        super(player);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
