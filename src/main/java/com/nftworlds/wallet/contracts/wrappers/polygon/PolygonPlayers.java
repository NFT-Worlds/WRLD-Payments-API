package com.nftworlds.wallet.contracts.wrappers.polygon;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * Contract wrapper for NFT Worlds player wallet and state mapping
 * on the Polygon chain. Players contract version 1.4
 * Polygon Mainnet contract address: 0xF405Fb60690395D8d4d047Cc8916Df256270285f
 * Polygon Mainnet contract block explorer: https://polygonscan.com/address/0xF405Fb60690395D8d4d047Cc8916Df256270285f
 * Auto-generated with web3j version 4.1.1
 */

@SuppressWarnings("rawtypes")
public class PolygonPlayers extends Contract {
    public static final String BINARY = "Bin file was not provided";

    public static final String FUNC_ASSIGNEDWALLETUUID = "assignedWalletUUID";

    public static final String FUNC_CONVENIENCEGATEWAY = "convenienceGateway";

    public static final String FUNC_GETPLAYERPRIMARYWALLET = "getPlayerPrimaryWallet";

    public static final String FUNC_GETPLAYERSECONDARYWALLETS = "getPlayerSecondaryWallets";

    public static final String FUNC_GETPLAYERSTATEDATA = "getPlayerStateData";

    public static final String FUNC_GETPLAYERSTATEDATABATCH = "getPlayerStateDataBatch";

    public static final String FUNC_REMOVEPLAYERSECONDARYWALLET = "removePlayerSecondaryWallet";

    public static final String FUNC_REMOVEPLAYERSTATEDATA = "removePlayerStateData";

    public static final String FUNC_SETPLAYERPRIMARYWALLET = "setPlayerPrimaryWallet";

    public static final String FUNC_SETPLAYERSECONDARYWALLET = "setPlayerSecondaryWallet";

    public static final String FUNC_SETPLAYERSTATEDATA = "setPlayerStateData";

    public static final String FUNC_SETPLAYERSTATEDATABATCH = "setPlayerStateDataBatch";

    public static final Event PLAYERPRIMARYWALLETSET_EVENT = new Event("PlayerPrimaryWalletSet",
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>(true) {}, new TypeReference<Utf8String>() {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event PLAYERSECONDARYWALLETREMOVED_EVENT = new Event("PlayerSecondaryWalletRemoved",
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>(true) {}, new TypeReference<Utf8String>() {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event PLAYERSECONDARYWALLETSET_EVENT = new Event("PlayerSecondaryWalletSet",
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>(true) {}, new TypeReference<Utf8String>() {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event PLAYERSTATEDATAREMOVED_EVENT = new Event("PlayerStateDataRemoved",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Utf8String>(true) {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event PLAYERSTATEDATASET_EVENT = new Event("PlayerStateDataSet",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Utf8String>(true) {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

    @Deprecated
    protected PolygonPlayers(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected PolygonPlayers(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected PolygonPlayers(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected PolygonPlayers(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<PlayerPrimaryWalletSetEventResponse> getPlayerPrimaryWalletSetEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(PLAYERPRIMARYWALLETSET_EVENT, transactionReceipt);
        ArrayList<PlayerPrimaryWalletSetEventResponse> responses = new ArrayList<PlayerPrimaryWalletSetEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            PlayerPrimaryWalletSetEventResponse typedResponse = new PlayerPrimaryWalletSetEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.playerUUIDIndex = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.setWalletAddress = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.playerUUID = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<PlayerPrimaryWalletSetEventResponse> playerPrimaryWalletSetEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, PlayerPrimaryWalletSetEventResponse>() {
            @Override
            public PlayerPrimaryWalletSetEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(PLAYERPRIMARYWALLETSET_EVENT, log);
                PlayerPrimaryWalletSetEventResponse typedResponse = new PlayerPrimaryWalletSetEventResponse();
                typedResponse.log = log;
                typedResponse.playerUUIDIndex = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.setWalletAddress = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.playerUUID = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<PlayerPrimaryWalletSetEventResponse> playerPrimaryWalletSetEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PLAYERPRIMARYWALLETSET_EVENT));
        return playerPrimaryWalletSetEventFlowable(filter);
    }

    public List<PlayerSecondaryWalletRemovedEventResponse> getPlayerSecondaryWalletRemovedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(PLAYERSECONDARYWALLETREMOVED_EVENT, transactionReceipt);
        ArrayList<PlayerSecondaryWalletRemovedEventResponse> responses = new ArrayList<PlayerSecondaryWalletRemovedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            PlayerSecondaryWalletRemovedEventResponse typedResponse = new PlayerSecondaryWalletRemovedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.playerUUIDIndex = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.removedWalletAddress = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.playerUUID = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<PlayerSecondaryWalletRemovedEventResponse> playerSecondaryWalletRemovedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, PlayerSecondaryWalletRemovedEventResponse>() {
            @Override
            public PlayerSecondaryWalletRemovedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(PLAYERSECONDARYWALLETREMOVED_EVENT, log);
                PlayerSecondaryWalletRemovedEventResponse typedResponse = new PlayerSecondaryWalletRemovedEventResponse();
                typedResponse.log = log;
                typedResponse.playerUUIDIndex = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.removedWalletAddress = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.playerUUID = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<PlayerSecondaryWalletRemovedEventResponse> playerSecondaryWalletRemovedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PLAYERSECONDARYWALLETREMOVED_EVENT));
        return playerSecondaryWalletRemovedEventFlowable(filter);
    }

    public List<PlayerSecondaryWalletSetEventResponse> getPlayerSecondaryWalletSetEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(PLAYERSECONDARYWALLETSET_EVENT, transactionReceipt);
        ArrayList<PlayerSecondaryWalletSetEventResponse> responses = new ArrayList<PlayerSecondaryWalletSetEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            PlayerSecondaryWalletSetEventResponse typedResponse = new PlayerSecondaryWalletSetEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.playerUUIDIndex = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.setWalletAddress = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.playerUUID = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<PlayerSecondaryWalletSetEventResponse> playerSecondaryWalletSetEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, PlayerSecondaryWalletSetEventResponse>() {
            @Override
            public PlayerSecondaryWalletSetEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(PLAYERSECONDARYWALLETSET_EVENT, log);
                PlayerSecondaryWalletSetEventResponse typedResponse = new PlayerSecondaryWalletSetEventResponse();
                typedResponse.log = log;
                typedResponse.playerUUIDIndex = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.setWalletAddress = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.playerUUID = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<PlayerSecondaryWalletSetEventResponse> playerSecondaryWalletSetEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PLAYERSECONDARYWALLETSET_EVENT));
        return playerSecondaryWalletSetEventFlowable(filter);
    }

    public List<PlayerStateDataRemovedEventResponse> getPlayerStateDataRemovedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(PLAYERSTATEDATAREMOVED_EVENT, transactionReceipt);
        ArrayList<PlayerStateDataRemovedEventResponse> responses = new ArrayList<PlayerStateDataRemovedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            PlayerStateDataRemovedEventResponse typedResponse = new PlayerStateDataRemovedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.setterAddress = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.playerUUIDIndex = (byte[]) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.playerUUID = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.ipfsHash = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<PlayerStateDataRemovedEventResponse> playerStateDataRemovedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, PlayerStateDataRemovedEventResponse>() {
            @Override
            public PlayerStateDataRemovedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(PLAYERSTATEDATAREMOVED_EVENT, log);
                PlayerStateDataRemovedEventResponse typedResponse = new PlayerStateDataRemovedEventResponse();
                typedResponse.log = log;
                typedResponse.setterAddress = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.playerUUIDIndex = (byte[]) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.playerUUID = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.ipfsHash = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<PlayerStateDataRemovedEventResponse> playerStateDataRemovedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PLAYERSTATEDATAREMOVED_EVENT));
        return playerStateDataRemovedEventFlowable(filter);
    }

    public List<PlayerStateDataSetEventResponse> getPlayerStateDataSetEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(PLAYERSTATEDATASET_EVENT, transactionReceipt);
        ArrayList<PlayerStateDataSetEventResponse> responses = new ArrayList<PlayerStateDataSetEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            PlayerStateDataSetEventResponse typedResponse = new PlayerStateDataSetEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.setterAddress = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.playerUUIDIndex = (byte[]) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.playerUUID = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.ipfsHash = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<PlayerStateDataSetEventResponse> playerStateDataSetEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, PlayerStateDataSetEventResponse>() {
            @Override
            public PlayerStateDataSetEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(PLAYERSTATEDATASET_EVENT, log);
                PlayerStateDataSetEventResponse typedResponse = new PlayerStateDataSetEventResponse();
                typedResponse.log = log;
                typedResponse.setterAddress = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.playerUUIDIndex = (byte[]) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.playerUUID = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.ipfsHash = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<PlayerStateDataSetEventResponse> playerStateDataSetEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PLAYERSTATEDATASET_EVENT));
        return playerStateDataSetEventFlowable(filter);
    }

    public RemoteFunctionCall<String> assignedWalletUUID(String param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ASSIGNEDWALLETUUID,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> convenienceGateway() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_CONVENIENCEGATEWAY,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> getPlayerPrimaryWallet(String _playerUUID) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETPLAYERPRIMARYWALLET,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_playerUUID.replace("-", ""))),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<List> getPlayerSecondaryWallets(String _playerUUID) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETPLAYERSECONDARYWALLETS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_playerUUID.replace("-", ""))),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<String> getPlayerStateData(String _playerUUID, String _setterAddress, Boolean includeGateway) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETPLAYERSTATEDATA,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_playerUUID.replace("-", "")),
                        new org.web3j.abi.datatypes.Address(160, _setterAddress),
                        new org.web3j.abi.datatypes.Bool(includeGateway)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<List> getPlayerStateDataBatch(List<String> _playerUUIDs, String _setterAddress, Boolean includeGateway) {
        List<String> _playerUUIDsFixed = new ArrayList<>();
        for (String s : _playerUUIDs) {
            _playerUUIDsFixed.add(s.replace("-",""));
        }
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETPLAYERSTATEDATABATCH,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                                org.web3j.abi.datatypes.Utf8String.class,
                                org.web3j.abi.Utils.typeMap(_playerUUIDsFixed, org.web3j.abi.datatypes.Utf8String.class)),
                        new org.web3j.abi.datatypes.Address(160, _setterAddress),
                        new org.web3j.abi.datatypes.Bool(includeGateway)),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Utf8String>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> removePlayerSecondaryWallet(String _playerUUID) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REMOVEPLAYERSECONDARYWALLET,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_playerUUID.replace("-",""))),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> removePlayerStateData(String _playerUUID) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REMOVEPLAYERSTATEDATA,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_playerUUID.replace("-",""))),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setPlayerPrimaryWallet(String _playerUUID, byte[] _signature) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETPLAYERPRIMARYWALLET,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_playerUUID.replace("-","")),
                        new org.web3j.abi.datatypes.DynamicBytes(_signature)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setPlayerSecondaryWallet(String _playerUUID, byte[] _signature) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETPLAYERSECONDARYWALLET,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_playerUUID.replace("-","")),
                        new org.web3j.abi.datatypes.DynamicBytes(_signature)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setPlayerStateData(String _playerUUID, String _ipfsHash) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETPLAYERSTATEDATA,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_playerUUID.replace("-","")),
                        new org.web3j.abi.datatypes.Utf8String(_ipfsHash)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setPlayerStateDataBatch(List<String> _playerUUIDs, List<String> _ipfsHashes) {
        List<String> _playerUUIDsFixed = new ArrayList<>();
        for (String s : _playerUUIDs) {
            _playerUUIDsFixed.add(s.replace("-",""));
        }
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETPLAYERSTATEDATABATCH,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                                org.web3j.abi.datatypes.Utf8String.class,
                                org.web3j.abi.Utils.typeMap(_playerUUIDsFixed, org.web3j.abi.datatypes.Utf8String.class)),
                        new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                                org.web3j.abi.datatypes.Utf8String.class,
                                org.web3j.abi.Utils.typeMap(_ipfsHashes, org.web3j.abi.datatypes.Utf8String.class))),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static PolygonPlayers load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new PolygonPlayers(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static PolygonPlayers load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new PolygonPlayers(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static PolygonPlayers load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new PolygonPlayers(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static PolygonPlayers load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new PolygonPlayers(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static class OwnershipTransferredEventResponse extends BaseEventResponse {
        public String previousOwner;

        public String newOwner;
    }

    public static class PlayerPrimaryWalletSetEventResponse extends BaseEventResponse {
        public byte[] playerUUIDIndex;

        public String setWalletAddress;

        public String playerUUID;
    }

    public static class PlayerSecondaryWalletRemovedEventResponse extends BaseEventResponse {
        public byte[] playerUUIDIndex;

        public String removedWalletAddress;

        public String playerUUID;
    }

    public static class PlayerSecondaryWalletSetEventResponse extends BaseEventResponse {
        public byte[] playerUUIDIndex;

        public String setWalletAddress;

        public String playerUUID;
    }

    public static class PlayerStateDataRemovedEventResponse extends BaseEventResponse {
        public String setterAddress;

        public byte[] playerUUIDIndex;

        public String playerUUID;

        public String ipfsHash;
    }

    public static class PlayerStateDataSetEventResponse extends BaseEventResponse {
        public String setterAddress;

        public byte[] playerUUIDIndex;

        public String playerUUID;

        public String ipfsHash;
    }
}
