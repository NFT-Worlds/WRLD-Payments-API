package com.nftworlds.wallet.objects.payments;

import com.nftworlds.wallet.objects.NFTPlayer;
import com.nftworlds.wallet.objects.Network;
import lombok.Getter;
import lombok.Setter;
import org.web3j.abi.datatypes.generated.Uint256;

import java.util.ArrayList;
import java.util.UUID;

@Getter
public class PeerToPeerPayment<T> {

    @Getter
    private static ArrayList<PeerToPeerPayment> peerToPeerPayments = new ArrayList<>();

    private UUID to;
    private UUID from;
    @Setter private double amount;
    private Uint256 refid;
    private Network network;
    private String reason;
    private T payload;

    private long timeout;

    public PeerToPeerPayment(NFTPlayer to, NFTPlayer from, double amount, Uint256 refid, Network network, String reason, long timeout, T payload) {
        this.to = to.getUuid();
        this.from = from.getUuid();
        this.amount = amount;
        this.refid = refid;
        this.network = network;
        this.reason = reason;
        this.timeout = timeout;
        this.payload = payload;
        peerToPeerPayments.add(this);
    }

    public static void removePaymentsFor(UUID uuid) {
        peerToPeerPayments.removeIf(peerToPeerPayment -> peerToPeerPayment.getTo().equals(uuid) || peerToPeerPayment.getFrom().equals(uuid));
    }

    public static PeerToPeerPayment getPayment(Uint256 refid, Network network) {
        for (PeerToPeerPayment p : peerToPeerPayments) {
            if (refid.equals(p.getRefid()) && network == p.getNetwork()) {
                return p;
            }
        }
        return null;
    }

    public T getPayload() {
        return payload;
    }
}
