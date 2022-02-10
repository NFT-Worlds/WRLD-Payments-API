package com.nftworlds.wallet.objects;

import com.nftworlds.wallet.NFTWorlds;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

public class Wallet {

    private UUID associatedPlayer;
    private String address;

    public Wallet(String address, UUID player) {
        this.address = address;
        this.associatedPlayer = player;
    }
    /**
     * Get the wallet's WRLD balance
     */
    public double getWRLDBalance(Network network) {
        //TODO: We'll probably want an async scheduler constantly updating player balances because calling this consistently on the main thread would cause lag
        double balance = 0;
        try {
            BigInteger bigInteger = network == Network.ETHEREUM ? NFTWorlds.getInstance().getWrld().getEthereumBalance(address) : NFTWorlds.getInstance().getWrld().getPolygonBalance(address);
            balance = Convert.fromWei(bigInteger.toString(), Convert.Unit.ETHER).doubleValue();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return balance;
    }

    /**
     * Send a request for a WRLD transaction from this wallet
     * @param amount
     * @param network
     * @param reason
     */
    public void requestWRLD(double amount, Network network, String reason) {
        BigDecimal requesting = Convert.toWei(BigDecimal.valueOf(amount), Convert.Unit.ETHER);
        //TODO: Make a request for WRLD from this wallet
    }

    /**
     * Deposit WRLD into this wallet
     * @param amount
     * @param network
     * @param reason
     */
    public void payWRLD(double amount, Network network, String reason) {
        BigDecimal sending = Convert.toWei(BigDecimal.valueOf(amount), Convert.Unit.ETHER);
        //TODO: Send WRLD to wallet
    }

}
