package com.nftworlds.wallet.contracts.nftworlds;

import java.math.BigInteger;
import com.nftworlds.wallet.contracts.wrappers.ethereum.EthereumWRLDToken;
import com.nftworlds.wallet.contracts.wrappers.polygon.PolygonWRLDToken;

public class WRLD {
    private EthereumWRLDToken ethereumWRLDTokenContract;
    private PolygonWRLDToken polygonWRLDTokenContract;

    public WRLD() {
        this.ethereumWRLDTokenContract = EthereumWRLDToken.load(/* TODO */);
        this.polygonWRLDTokenContract = PolygonWRLDToken.load(/* TODO */);
    }

    public BigInteger ethereum_getBalance(String walletAddress) {
        // TODO: .send returns async and not a biginteger?
        return this.ethereumWRLDTokenContract.balanceOf(walletAddress).send();
    }

    public BigInteger polygon_getBalance(String walletAddress) {
        // TODO: .send returns async and not a biginteger?
        return this.polygonWRLDTokenContract.balanceOf(walletAddress).send();
    }

    private
}
