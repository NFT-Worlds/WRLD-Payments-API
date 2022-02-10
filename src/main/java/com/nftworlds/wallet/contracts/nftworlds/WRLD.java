package com.nftworlds.wallet.contracts.nftworlds;

import com.nftworlds.wallet.NFTWorlds;
import com.nftworlds.wallet.config.Config;
import com.nftworlds.wallet.contracts.wrappers.ethereum.EthereumWRLDToken;
import com.nftworlds.wallet.contracts.wrappers.polygon.PolygonWRLDToken;
import com.nftworlds.wallet.rpcs.Ethereum;
import com.nftworlds.wallet.rpcs.Polygon;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Keys;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

public class WRLD {
    private EthereumWRLDToken ethereumWRLDTokenContract;
    private PolygonWRLDToken polygonWRLDTokenContract;

    public WRLD() {
        NFTWorlds nftWorlds = NFTWorlds.getInstance();
        Ethereum ethereumRPC = nftWorlds.getEthereumRPC();
        Polygon polygonRPC = nftWorlds.getPolygonRPC();
        Credentials credentials = null;

        try {
            credentials = Credentials.create(Keys.createEcKeyPair()); //We're only reading so this can be anything
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Config config = nftWorlds.getNftConfig();

        this.ethereumWRLDTokenContract = EthereumWRLDToken.load(
            config.getEthereumWrldContract(),
            nftWorlds.getEthereumRPC().getEthereumWeb3j(),
            credentials,
            ethereumRPC.getGasProvider()
        );

        this.polygonWRLDTokenContract = PolygonWRLDToken.load(
            config.getPolygonWrldContract(),
            nftWorlds.getPolygonRPC().getPolygonWeb3j(),
            credentials,
            polygonRPC.getGasProvider()
        );
    }

    public BigInteger getEthereumBalance(String walletAddress) throws Exception {
        return this.ethereumWRLDTokenContract.balanceOf(walletAddress).send();
    }

    public CompletableFuture<BigInteger> getEthereumBalanceAsync(String walletAddress) throws Exception {
        return this.ethereumWRLDTokenContract.balanceOf(walletAddress).sendAsync();
    }

    public BigInteger getPolygonBalance(String walletAddress) throws Exception {
        return this.polygonWRLDTokenContract.balanceOf(walletAddress).send();
    }

    public CompletableFuture<BigInteger> getPolygonBalanceAsync(String walletAddress) throws Exception {
        return this.polygonWRLDTokenContract.balanceOf(walletAddress).sendAsync();
    }



}
