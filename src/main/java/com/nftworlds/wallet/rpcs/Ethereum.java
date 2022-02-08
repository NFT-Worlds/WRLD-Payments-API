package com.nftworlds.wallet.rpcs;

import com.nftworlds.wallet.NFTWorlds;
import lombok.Getter;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

public class Ethereum {
    @Getter private Web3j ethereumWeb3j;
    @Getter private DefaultGasProvider gasProvider;

    public Ethereum() {
        this.ethereumWeb3j = Web3j.build(new HttpService(NFTWorlds.getInstance().getNftConfig().getEthereumHttpsRpc()));
        this.gasProvider = new DefaultGasProvider();
    }
}
