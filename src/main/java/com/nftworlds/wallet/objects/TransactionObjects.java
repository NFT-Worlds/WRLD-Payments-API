package com.nftworlds.wallet.objects;

import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.StaticGasProvider;

public class TransactionObjects {
    public static TransactionManager polygonTransactionManager;
    public static TransactionManager ethereumTransactionManager;

    public static StaticGasProvider fastGasProviderPolygon;
}
