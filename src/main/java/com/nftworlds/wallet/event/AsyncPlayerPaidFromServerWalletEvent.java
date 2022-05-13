package com.nftworlds.wallet.event;

import com.nftworlds.wallet.objects.Network;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class AsyncPlayerPaidFromServerWalletEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final Player receiver;
    private final double amount;
    private final Network network;
    private final String reason;

    private boolean defaultReceiveMessage = true;

    public AsyncPlayerPaidFromServerWalletEvent(Player receiver, double amount, Network network, String reason) {
        super(true);
        this.receiver = receiver;
        this.amount = amount;
        this.network = network;
        this.reason = reason;
    }

    public void disableDefaultReceiveMessage() {
        this.defaultReceiveMessage = false;
    }

    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
