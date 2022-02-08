package com.nftworlds.wallet.contracts.nftworlds;

import com.nftworlds.wallet.contracts.wrappers.ethereum.EthereumWRLDToken;
import com.nftworlds.wallet.contracts.wrappers.polygon.PolygonWRLDToken;

public class WRLD {
    private EthereumWRLDToken ethereumWRLDTokenContract;
    private PolygonWRLDToken polygonWRLDTokenContract;

    public WRLD() {
        this.ethereumWRLDTokenContract = EthereumWRLDToken.load(/* TODO */);
        this.polygonWRLDTokenContract = PolygonWRLDToken.load(/* TODO */);
    }
}
