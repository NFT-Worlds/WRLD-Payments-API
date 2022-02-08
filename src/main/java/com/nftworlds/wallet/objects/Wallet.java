package com.nftworlds.wallet.objects;

import java.util.UUID;

public class Wallet {

    private String address;
    private double balance;

    public Wallet(String address, UUID player) {
        this.address = address;
    }

    public void pay(int amount, String reason) {
        //TODO: Send WRLD to wallet
    }

    public void request(int amount, String reason) {
        //TODO: Make a request for WRLD from this wallet
    }

    public void getBalance() {
        //TODO: Get the player's balance
        //We'll probably want an async scheduler constantly updating player balances so it's seamless and easy here
    }

}
