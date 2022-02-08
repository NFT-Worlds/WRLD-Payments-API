package com.nftworlds.wallet.contracts.nftworlds;

import com.nftworlds.wallet.NFTWorlds;
import com.nftworlds.wallet.config.Config;
import com.nftworlds.wallet.contracts.wrappers.ethereum.EthereumWRLDToken;
import com.nftworlds.wallet.contracts.wrappers.polygon.PolygonWRLDToken;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Keys;

import java.math.BigInteger;

public class WRLD {
    private EthereumWRLDToken ethereumWRLDTokenContract;
    private PolygonWRLDToken polygonWRLDTokenContract;

    public WRLD() {
        NFTWorlds nftWorlds = NFTWorlds.getInstance();
        Credentials credentials = null;
        try {
            credentials = Credentials.create(Keys.createEcKeyPair()); //We're only reading so this can be anything
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Config config = nftWorlds.getNftConfig();
        this.ethereumWRLDTokenContract = EthereumWRLDToken.load(config.getEthereumWrldContract(), nftWorlds.getEthereumRPC().getEthereumWeb3j(), credentials);
        this.polygonWRLDTokenContract = PolygonWRLDToken.load(config.getPolygonWrldContract(), nftWorlds.getPolygonRPC().getPolygonWeb3j(), credentials);
    }

    public BigInteger ethereum_getBalance(String walletAddress) throws Exception {
        // TODO: .send returns async and not a biginteger?
        return this.ethereumWRLDTokenContract.balanceOf(walletAddress).send();
    }

    public BigInteger polygon_getBalance(String walletAddress) throws Exception {
        // TODO: .send returns async and not a biginteger?
        return this.polygonWRLDTokenContract.balanceOf(walletAddress).send();
    }



}
