package com.nftworlds.wallet.rpcs;

import com.nftworlds.wallet.NFTWorlds;
import lombok.Getter;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

public class Ethereum {

    @Getter private Web3j ethereumWeb3j;

    public Ethereum() {
        this.ethereumWeb3j = Web3j.build(new HttpService(NFTWorlds.getInstance().getNftConfig().getEthereumHttpsRpc()));
    }
}
