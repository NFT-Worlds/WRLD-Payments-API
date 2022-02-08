package com.nftworlds.wallet.rpcs;

import lombok.Getter;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

public class Ethereum {
    @Getter private static Web3j ethereumWeb3j;

    public Ethereum() {
        /* TODO Get eth rpc endpoint from yml config */
        this.ethereumWeb3j = Web3j.build(new HttpService(/* eth rpc endpoint from config */));
    }
}
