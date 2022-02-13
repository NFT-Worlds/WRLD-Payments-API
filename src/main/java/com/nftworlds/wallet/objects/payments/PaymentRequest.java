package com.nftworlds.wallet.objects.payments;

import com.nftworlds.wallet.objects.Network;
import lombok.Getter;
import org.web3j.abi.datatypes.generated.Uint256;

import java.util.ArrayList;
import java.util.UUID;

@Getter
public class PaymentRequest {

    @Getter
    private static ArrayList<PaymentRequest> paymentRequests = new ArrayList<>();

    private UUID associatedPlayer;
    private double amount;
    private Uint256 refid;
    private Network network;
    private String reason;

    private long timeout;

    public PaymentRequest(UUID associatedPlayer, double amount, Uint256 refid, Network network, String reason, long timeout) {
        this.associatedPlayer = associatedPlayer;
        this.amount = amount;
        this.refid = refid;
        this.network = network;
        this.reason = reason;
        this.timeout = timeout;
        paymentRequests.add(this);
    }

    public static void removePaymentsFor(UUID uuid) {
        paymentRequests.removeIf(paymentRequest -> paymentRequest.getAssociatedPlayer().equals(uuid));
    }

    public static PaymentRequest getPayment(Uint256 refId, Network network) {
        for (PaymentRequest p : paymentRequests) {
            if (refId.equals(p.getRefid()) && network == p.getNetwork()) {
                return p;
            }
        }
        return null;
    }

}
