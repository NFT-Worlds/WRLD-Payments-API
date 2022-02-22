package com.nftworlds.wallet.objects.payments;

import com.nftworlds.wallet.event.PlayerTransactEvent;
import com.nftworlds.wallet.objects.Network;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.web3j.abi.datatypes.generated.Uint256;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

@Getter
public class PaymentRequest<T> {

    @Getter
    private static ArrayList<PaymentRequest> paymentRequests = new ArrayList<>();

    private UUID associatedPlayer;
    private double amount;
    private Uint256 refid;
    private Network network;
    private String reason;
    private long timeout;
    private T payload;

    public PaymentRequest(UUID associatedPlayer, double amount, Uint256 refid, Network network, String reason, long timeout, T payload) {
        this.associatedPlayer = associatedPlayer;
        this.amount = amount;
        this.refid = refid;
        this.network = network;
        this.reason = reason;
        this.timeout = timeout;
        this.payload = payload;
        paymentRequests.add(this);
    }

    public void finalizeTransaction() {
        new PlayerTransactEvent(
                Objects.requireNonNull(Bukkit.getPlayer(this.getAssociatedPlayer())),
                this.getAmount(), this.getReason(), this.getRefid(), this.getPayload())
                .callEvent();

        PaymentRequest.getPaymentRequests().remove(this);
    }

    public static void removePaymentsFor(UUID uuid) {
        paymentRequests.removeIf(paymentRequest -> paymentRequest.getAssociatedPlayer().equals(uuid));
    }

    public static PaymentRequest getPayment(Uint256 refid, Network network) {
        for (PaymentRequest p : paymentRequests) {
            if (refid.equals(p.getRefid()) && network == p.getNetwork()) {
                return p;
            }
        }
        return null;
    }

}
