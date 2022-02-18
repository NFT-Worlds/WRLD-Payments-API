package com.nftworlds.wallet.handlers;

import com.nftworlds.wallet.NFTWorlds;
import com.nftworlds.wallet.objects.payments.PaymentRequest;
import com.nftworlds.wallet.objects.payments.PeerToPeerPayment;
import org.bukkit.Bukkit;

public class TimeoutHandler {

    public void handleTimeouts() {
        Bukkit.getScheduler().runTaskTimer(NFTWorlds.getInstance(), () -> {
            PaymentRequest.getPaymentRequests().removeIf(paymentRequest -> paymentRequest.getTimeout() < System.currentTimeMillis());
            PeerToPeerPayment.getPeerToPeerPayments().removeIf(peerToPeerPayment -> peerToPeerPayment.getTimeout() < System.currentTimeMillis());
        }, 20L, 20L);
    }

}
