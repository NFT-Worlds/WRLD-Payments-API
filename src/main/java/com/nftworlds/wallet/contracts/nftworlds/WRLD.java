package com.nftworlds.wallet.contracts.nftworlds;

import java.math.BigInteger;

import com.nftworlds.wallet.NFTWorlds;
import com.nftworlds.wallet.config.Config;
import com.nftworlds.wallet.contracts.wrappers.ethereum.EthereumWRLDToken;
import com.nftworlds.wallet.contracts.wrappers.polygon.PolygonWRLDToken;

public class WRLD {
    private EthereumWRLDToken ethereumWRLDTokenContract;
    private PolygonWRLDToken polygonWRLDTokenContract;

    public WRLD() {
        Config config = NFTWorlds.getInstance().getNftConfig();
        this.ethereumWRLDTokenContract = EthereumWRLDToken.load(config.getEthereumWrldContract());
        this.polygonWRLDTokenContract = PolygonWRLDToken.load(config.getPolygonWrldContract());
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
