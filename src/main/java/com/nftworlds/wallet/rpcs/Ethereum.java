package com.nftworlds.wallet.rpcs;

import com.nftworlds.wallet.NFTWorlds;
import lombok.Getter;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

public class Ethereum {
    @Getter private static Web3j ethereumWeb3j;

    public Ethereum() {
        String ethRpcEndpoint = NFTWorlds.getInstance().getNftConfig().getEthereumHttpsRpc();
        this.ethereumWeb3j = Web3j.build(new HttpService(/* eth rpc endpoint from config */));
    }
}
