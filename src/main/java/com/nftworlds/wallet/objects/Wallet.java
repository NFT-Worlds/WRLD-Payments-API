package com.nftworlds.wallet.objects;

import java.util.UUID;

public class Wallet {

    private String address;
    private double balance;

    public Wallet(String address, UUID player) {
        this.address = address;
    }
    /**
     * Get the wallet's WRLD balance
     */
    public void getBalance() {
        //TODO: Get the player's balance
        //We'll probably want an async scheduler constantly updating player balances so it's seamless and easy here
    }

    /**
     * Send a request for a WRLD transaction from this wallet
     * @param amount
     * @param reason
     */
    public void requestWRLD(int amount, String reason) {
        //TODO: Make a request for WRLD from this wallet
    }

    /**
     * Deposit WRLD into this wallet
     * @param amount
     * @param reason
     */
    public void payWRLD(int amount, String reason) {
        //TODO: Send WRLD to wallet
    }

}
