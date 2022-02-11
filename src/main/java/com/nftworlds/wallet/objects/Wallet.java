package com.nftworlds.wallet.objects;

import com.nftworlds.wallet.NFTWorlds;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;
import java.util.UUID;

public class Wallet {

    private UUID associatedPlayer;
    private String address;

    public Wallet(UUID associatedPlayer, String address) {
        this.associatedPlayer = associatedPlayer;
        this.address = address;
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
        NFTWorlds nftWorlds = NFTWorlds.getInstance();
        //BigDecimal requesting = Convert.toWei(BigDecimal.valueOf(amount), Convert.Unit.ETHER);
        NFTPlayer nftPlayer = NFTPlayer.getByUUID(associatedPlayer);
        if (nftPlayer != null) {
            Player player = Bukkit.getPlayer(nftPlayer.getUuid());
            if (player != null) {
                Uint256 refID = new Uint256(new Random().nextLong()); //NOTE: This generates a random Uint256 to use as a reference. Don't know if we want to change this or not.
                new PaymentRequest(associatedPlayer, amount, refID, network, reason);
                String paymentLink = "https://nftworlds.com/pay/?to="+nftWorlds.getNftConfig().getServerWalletAddress()+"&amount="+amount+"&ref="+refID;
                player.sendMessage("Pay here: " + paymentLink); //NOTE: Yeah this will look nicer and we'll do QR codes as well
            }
        }
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
