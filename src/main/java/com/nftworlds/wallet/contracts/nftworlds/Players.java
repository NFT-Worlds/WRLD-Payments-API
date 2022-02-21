package com.nftworlds.wallet.contracts.nftworlds;

import com.nftworlds.wallet.NFTWorlds;
import com.nftworlds.wallet.contracts.wrappers.polygon.PolygonPlayers;
import com.nftworlds.wallet.objects.NFTPlayer;
import com.nftworlds.wallet.objects.Wallet;
import com.nftworlds.wallet.rpcs.Polygon;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.json.JSONObject;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class Players {
    private PolygonPlayers polygonPlayersContract;

    public static final String PLAYER_PRIMARY_WALLET_SET = Hash.sha3String("PlayerPrimaryWalletSet(string,string,address)");
    public static final String PLAYER_SECONDARY_WALLET_SET = Hash.sha3String("PlayerSecondaryWalletSet(string,string,address)");
    public static final String PLAYER_SECONDARY_WALLET_REMOVED = Hash.sha3String("PlayerSecondaryWalletRemoved(string,string,address)");

    public Players() {
        NFTWorlds nftWorlds = NFTWorlds.getInstance();
        Polygon polygonRPC = nftWorlds.getPolygonRPC();
        Credentials credentials = null;

        try {
            credentials = Credentials.create("0x0000000000000000000000000000000000000000000000000000000000000000"); // We're only reading so this can be anything
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        this.polygonPlayersContract = PolygonPlayers.load(
                nftWorlds.getNftConfig().getPolygonPlayerContract(),
                polygonRPC.getPolygonWeb3j(),
                credentials,
                polygonRPC.getGasProvider()
        );

        startPlayerWalletUpdateListener();
    }

    public String getPlayerPrimaryWallet(String playerUUID) throws Exception {
        return this.polygonPlayersContract.getPlayerPrimaryWallet(playerUUID.replace("-", "")).send();
    }

    public CompletableFuture<String> getPlayerPrimaryWalletAsync(String playerUUID) throws Exception {
        return this.polygonPlayersContract.getPlayerPrimaryWallet(playerUUID.replace("-", "")).sendAsync();
    }

    public List<String> getPlayerSecondaryWallets(String playerUUID) throws Exception {
        return this.polygonPlayersContract.getPlayerSecondaryWallets(playerUUID.replace("-", "")).send();
    }

    public CompletableFuture<List> getPlayerSecondaryWalletsAsync(String playerUUID) throws Exception {
        return this.polygonPlayersContract.getPlayerSecondaryWallets(playerUUID.replace("-", "")).sendAsync();
    }

    public JSONObject getPlayerStateData(String playerUUID, String setterWalletAddress) throws Exception {
        String stateDataUrl = this.polygonPlayersContract.getPlayerStateData(playerUUID.replace("-", ""), setterWalletAddress, true).send();

        if (stateDataUrl.isEmpty()) {
            return null;
        }

        return new JSONObject(HttpClient.newHttpClient().send(HttpRequest.newBuilder().uri(URI.create(stateDataUrl)).build(), HttpResponse.BodyHandlers.ofString()).body());
    }

    public JSONObject getPlayerStateDataAsync(String playerUUID, String setterWalletAddress) throws Exception {
        CompletableFuture<String> stateDataUrl = this.polygonPlayersContract.getPlayerStateData(playerUUID.replace("-", ""), setterWalletAddress, true).sendAsync();

        if (stateDataUrl.get().isEmpty()) {
            return null;
        }

        return new JSONObject(HttpClient.newHttpClient().send(HttpRequest.newBuilder().uri(URI.create(stateDataUrl.get())).build(), HttpResponse.BodyHandlers.ofString()).body());
    }

    private void startPlayerWalletUpdateListener() {
        EthFilter transferFilter = new EthFilter(
                DefaultBlockParameterName.LATEST,
                DefaultBlockParameterName.LATEST,
                this.polygonPlayersContract.getContractAddress()
        ).addOptionalTopics(Players.PLAYER_PRIMARY_WALLET_SET, Players.PLAYER_SECONDARY_WALLET_SET, Players.PLAYER_SECONDARY_WALLET_REMOVED);

        NFTWorlds.getInstance().getPolygonRPC().getPolygonWeb3j().ethLogFlowable(transferFilter).subscribe(log -> {
            String eventHash = log.getTopics().get(0);

            if (eventHash.equals(PLAYER_PRIMARY_WALLET_SET)) {
                this.paymentListener_handlePrimaryWalletSetEvent(log);
            } else if (eventHash.equals(PLAYER_SECONDARY_WALLET_SET)) {
                this.paymentListener_handleSecondaryWalletSetEvent(log);
            } else if (eventHash.equals(PLAYER_SECONDARY_WALLET_REMOVED)) {
                this.paymentListener_handleSecondaryWalletRemovedEvent(log);
            }
        },
        error -> {
            error.printStackTrace();
        });
    }

    public void paymentListener_handlePrimaryWalletSetEvent(Log log) {
        Bukkit.getLogger().log(Level.INFO, "Primary wallet updated");

        List<String> topics = log.getTopics();
        List<Type> data = FunctionReturnDecoder.decode(log.getData(), PolygonPlayers.PLAYERPRIMARYWALLETSET_EVENT.getNonIndexedParameters());

        String playerUUID = (String) data.get(0).getValue();
        Address walletAddress = (Address) FunctionReturnDecoder.decodeIndexedValue(topics.get(2), new TypeReference<Address>(false) {});

        Bukkit.getLogger().log(Level.INFO, "Primary wallet of uuid " + playerUUID + " set to " + walletAddress);

        UUID uuid = java.util.UUID.fromString (playerUUID.replaceFirst("(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)","$1-$2-$3-$4-$5"));
        NFTPlayer nftPlayer = NFTPlayer.getByUUID(uuid);
        if (nftPlayer != null) {
            nftPlayer.getWallets().set(0, new Wallet(uuid, walletAddress.getValue()));
            Player p = Bukkit.getPlayer(uuid);
            if (p.isOnline()) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', " \n&7Your primary wallet has been set to &a" + walletAddress.getValue() + "&r\n "));
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            }
        }
    }

    public void paymentListener_handleSecondaryWalletSetEvent(Log log) {
        Bukkit.getLogger().log(Level.INFO, "Secondary wallet updated (addition)");

        List<String> topics = log.getTopics();
        List<Type> data = FunctionReturnDecoder.decode(log.getData(), PolygonPlayers.PLAYERSECONDARYWALLETSET_EVENT.getNonIndexedParameters());

        String playerUUID = (String) data.get(0).getValue();
        Address walletAddress = (Address) FunctionReturnDecoder.decodeIndexedValue(topics.get(2), new TypeReference<Address>(false) {});

        Bukkit.getLogger().log(Level.INFO, "Added secondary wallet of " +  walletAddress.toString() + " to uuid " + playerUUID);

        UUID uuid = java.util.UUID.fromString (playerUUID.replaceFirst("(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)","$1-$2-$3-$4-$5"));
        NFTPlayer nftPlayer = NFTPlayer.getByUUID(uuid);
        if (nftPlayer != null) {
            nftPlayer.getWallets().add(new Wallet(uuid, walletAddress.getValue()));
            Player p = Bukkit.getPlayer(uuid);
            if (p.isOnline()) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', " \n&7Your secondary wallets have been updated by adding &a" + walletAddress.getValue() + "&r\n "));
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            }
        }
    }

    public void paymentListener_handleSecondaryWalletRemovedEvent(Log log) {
        Bukkit.getLogger().log(Level.INFO, "Secondary wallet updated (removal)");

        List<String> topics = log.getTopics();
        List<Type> data = FunctionReturnDecoder.decode(log.getData(), PolygonPlayers.PLAYERSECONDARYWALLETREMOVED_EVENT.getNonIndexedParameters());

        String playerUUID = (String) data.get(0).getValue();
        Address walletAddress = (Address) FunctionReturnDecoder.decodeIndexedValue(topics.get(2), new TypeReference<Address>(false) {});

        Bukkit.getLogger().log(Level.INFO, "Removed secondary wallet of " +  walletAddress.toString() + " from uuid " + playerUUID);

        UUID uuid = java.util.UUID.fromString (playerUUID.replaceFirst("(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)","$1-$2-$3-$4-$5"));
        NFTPlayer nftPlayer = NFTPlayer.getByUUID(uuid);
        if (nftPlayer != null) {
            nftPlayer.getWallets().removeIf(wallet -> wallet.equals(new Wallet(uuid, walletAddress.getValue())));
            Player p = Bukkit.getPlayer(uuid);
            if (p.isOnline()) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', " \n&7Your secondary wallets have been updated by removing &c" + walletAddress.getValue() + "&r\n "));
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
            }
        }
    }
}
