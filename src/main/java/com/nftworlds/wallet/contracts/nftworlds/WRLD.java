package com.nftworlds.wallet.contracts.nftworlds;

import com.nftworlds.wallet.NFTWorlds;
import com.nftworlds.wallet.config.Config;
import com.nftworlds.wallet.contracts.wrappers.ethereum.EthereumWRLDToken;
import com.nftworlds.wallet.contracts.wrappers.polygon.PolygonWRLDToken;
import com.nftworlds.wallet.event.PlayerTransactEvent;
import com.nftworlds.wallet.objects.Network;
import com.nftworlds.wallet.objects.PaymentRequest;
import com.nftworlds.wallet.rpcs.Ethereum;
import com.nftworlds.wallet.rpcs.Polygon;
import org.bukkit.Bukkit;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.utils.Convert;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class WRLD {
    private EthereumWRLDToken ethereumWRLDTokenContract;
    private PolygonWRLDToken polygonWRLDTokenContract;

    public static final String TRANSFER_EVENT_TOPIC = Hash.sha3String("Transfer(address,address,uint256)");
    public static final String TRANSFER_REF_EVENT_TOPIC = Hash.sha3String( "TransferRef(address,address,uint256,uint256)");

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

        polygonPaymentListener();
    }

    public void polygonPaymentListener() {
        EthFilter transferFilter = new EthFilter(
            DefaultBlockParameterName.EARLIEST,
            DefaultBlockParameterName.LATEST,
            this.polygonWRLDTokenContract.getContractAddress()
        ).addSingleTopic(WRLD.TRANSFER_REF_EVENT_TOPIC);

        NFTWorlds.getInstance().getPolygonRPC().getPolygonWeb3j().ethLogFlowable(transferFilter).subscribe(log -> {
            List<String> topics = log.getTopics();
            String eventHash = topics.get(0);

            if (eventHash.equals(TRANSFER_REF_EVENT_TOPIC)) {
                List<Type> data = FunctionReturnDecoder.decode(log.getData(), PolygonWRLDToken.TRANSFERREF_EVENT.getNonIndexedParameters());
                TypeReference<Address> addressTypeReference = new TypeReference<Address>() {};
                TypeReference<Uint256> uint256TypeReference = new TypeReference<Uint256>() {};

                Address fromAddress = (Address) FunctionReturnDecoder.decodeIndexedValue(topics.get(1), addressTypeReference);
                Address toAddress = (Address) FunctionReturnDecoder.decodeIndexedValue(topics.get(2), addressTypeReference);
                Uint256 amount = (Uint256) data.get(0);
                Uint256 ref = (Uint256) data.get(1);

                PaymentRequest paymentRequest = PaymentRequest.getPayment(ref, Network.POLYGON);
                double received = Convert.fromWei(amount.getValue().toString(), Convert.Unit.ETHER).doubleValue();
                if (paymentRequest.getAmount() == received) {
                    PaymentRequest.getPaymentRequests().remove(paymentRequest);
                    if (paymentRequest != null) {
                        new PlayerTransactEvent(Bukkit.getPlayer(paymentRequest.getAssociatedPlayer()), received, paymentRequest.getReason(), ref) ; //TODO: Test if works for offline players
                    }
                } else {
                    Bukkit.getLogger().log(Level.WARNING,
                            "Payment with REFID " + ref.getValue().toString() +" was receive but amount was " + received + ". Expected " + paymentRequest.getAmount());
                }
                // Map "fromAddress" back to a player?
                // Trigger callback or hook of some kind devs can build off of when getting valid incoming payments with ref?
            }
        },
        error -> {
            error.printStackTrace();
        });
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
