package com.nftworlds.wallet.objects;

import java.util.UUID;

public class Wallet {

    private String address;

    public Wallet(String address, UUID player) {
        this.address = address;
    }

    public void pay(int amount, String reason) {
        //TODO: Send WRLD to wallet
    }

    public void request(int amount, String reason) {
        //TODO: Make a request for WRLD from this wallet
    }

}
