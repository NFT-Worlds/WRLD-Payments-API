package com.nftworlds.wallet.objects;

import com.nftworlds.wallet.qrmaps.LinkUtils;
import com.nftworlds.wallet.NFTWorlds;
import com.nftworlds.wallet.qrmaps.QRMapManager;
import com.nftworlds.wallet.contracts.wrappers.common.ERC20;
import com.nftworlds.wallet.contracts.wrappers.polygon.PolygonWRLDToken;
import com.nftworlds.wallet.objects.payments.PaymentRequest;
import com.nftworlds.wallet.objects.payments.PeerToPeerPayment;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.json.JSONArray;
import org.json.JSONObject;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class Wallet {

    @Getter
    private UUID associatedPlayer;
    @Getter
    private String address;
    @Getter
    @Setter
    private double polygonWRLDBalance;
    @Getter
    @Setter
    private double ethereumWRLDBalance;
    @Getter
    private static HashMap<String, ERC20> customPolygonTokenWrappers = new HashMap<String, ERC20>();
    @Getter
    private static HashMap<String, Double> customPolygonBalances = new HashMap<>();
    @Getter
    private static HashMap<String, ERC20> customEthereumTokenWrappers = new HashMap<String, ERC20>();
    @Getter
    private static HashMap<String, Double> customEthereumBalances = new HashMap<>();

    public Wallet(UUID associatedPlayer, String address) {
        this.associatedPlayer = associatedPlayer;
        this.address = address;

        //Get balance initially
        double polygonBalance = 0;
        double ethereumBalance = 0;
        try {
            BigInteger bigIntegerPoly = NFTWorlds.getInstance().getWrld().getPolygonBalance(address);
            BigInteger bigIntegerEther = NFTWorlds.getInstance().getWrld().getEthereumBalance(address);
            polygonBalance = Convert.fromWei(bigIntegerPoly.toString(), Convert.Unit.ETHER).doubleValue();
            ethereumBalance = Convert.fromWei(bigIntegerEther.toString(), Convert.Unit.ETHER).doubleValue();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.polygonWRLDBalance = polygonBalance;
        this.ethereumWRLDBalance = ethereumBalance;

    }

    /**
     * Get the wallet's WRLD balance
     */
    public double getWRLDBalance(Network network) {
        if (network == Network.POLYGON) {
            return polygonWRLDBalance;
        } else {
            return ethereumWRLDBalance;
        }
    }

    /**
     * Refresh the wallet's balance for an arbitrary ERC20 token defined at runtime.
     * This is a blocking call, do not run in main thread.
     */
    public void refreshERC20Balance(Network network, String tokenContract) throws Exception {
        if (network == Network.POLYGON) {
            ERC20 customToken = Wallet.getCustomPolygonTokenWrappers().get(tokenContract);
            if (customToken == null) {
                customToken = ERC20.load(tokenContract, NFTWorlds.getInstance().getPolygonRPC().getPolygonWeb3j(),
                        Credentials.create(NFTWorlds.getInstance().getNftConfig().getServerPrivateKey()), new DefaultGasProvider());
                Wallet.getCustomPolygonTokenWrappers().put(tokenContract, customToken);
            }
            BigInteger bigInteger = customToken.balanceOf(address).send();
            customPolygonBalances.put(tokenContract,
                    Convert.fromWei(bigInteger.toString(), Convert.Unit.ETHER).doubleValue());

        } else if (network == Network.ETHEREUM) {
            ERC20 customToken = Wallet.getCustomEthereumTokenWrappers().get(tokenContract);
            if (customToken == null) {
                customToken = ERC20.load(tokenContract, NFTWorlds.getInstance().getEthereumRPC().getEthereumWeb3j(),
                        Credentials.create(NFTWorlds.getInstance().getNftConfig().getServerPrivateKey()), new DefaultGasProvider());
                Wallet.getCustomPolygonTokenWrappers().put(tokenContract, customToken);
            }
            BigInteger bigInteger = customToken.balanceOf(address).send();

            customEthereumBalances.put(tokenContract,
                    Convert.fromWei(bigInteger.toString(), Convert.Unit.ETHER).doubleValue());
        }
    }

    /**
     * Get a list of all the account's owned NFTs. Does not return metadata.
     * This is a blocking call, do not run in main thread.
     */
    public JSONObject getOwnedNFTs(Network network) throws IOException, InterruptedException {
        String baseURL;
        if (network.equals(Network.ETHEREUM)) {
            baseURL = NFTWorlds.getInstance().getNftConfig().getEthereumHttpsRpc();
        } else if (network.equals(Network.POLYGON)) {
            baseURL = NFTWorlds.getInstance().getNftConfig().getPolygonHttpsRpc();
        } else {
            return null;
        }
        String url = baseURL + "/getNFTs?owner=" + address + "&withMetadata=false";
        return new JSONObject(HttpClient.newHttpClient().send(HttpRequest.newBuilder().uri(URI.create(url)).build(), HttpResponse.BodyHandlers.ofString()).body());
    }

    /**
     * Get a list of all the account's owned NFTs. Returns metadata.
     */
    public JSONObject getOwnedNFTsFromContract(Network network, String contractAddress) throws IOException, InterruptedException {
        String baseURL;
        if (network.equals(Network.ETHEREUM)) {
            baseURL = NFTWorlds.getInstance().getNftConfig().getEthereumHttpsRpc();
        } else if (network.equals(Network.POLYGON)) {
            baseURL = NFTWorlds.getInstance().getNftConfig().getPolygonHttpsRpc();
        } else {
            return null;
        }
        String url = baseURL + "/getNFTs?owner=" + address + "&contractAddresses=" + contractAddress;
        return new JSONObject(HttpClient.newHttpClient().send(HttpRequest.newBuilder().uri(URI.create(url)).build(), HttpResponse.BodyHandlers.ofString()).body());
    }

    public boolean doesPlayerOwnNFTInCollection(Network network, String contractAddress) throws IOException, InterruptedException {
        String baseURL;
        if (network.equals(Network.ETHEREUM)) {
            baseURL = NFTWorlds.getInstance().getNftConfig().getEthereumHttpsRpc();
        } else if (network.equals(Network.POLYGON)) {
            baseURL = NFTWorlds.getInstance().getNftConfig().getPolygonHttpsRpc();
        } else {
            return false;
        }
        String url = baseURL + "/getNFTs?owner=" + address + "&contractAddresses=" + contractAddress;
        JSONObject payload = new JSONObject(HttpClient.newHttpClient().send(HttpRequest.newBuilder().uri(URI.create(url)).build(), HttpResponse.BodyHandlers.ofString()).body());
        JSONArray ownedNFTs = (JSONArray) payload.get("ownedNfts");
        return ownedNFTs.length() > 0;
    }

    /**
     * Send a request for a WRLD transaction from this wallet
     *
     * @param amount
     * @param network
     * @param reason
     * @param canDuplicate
     * @param payload
     */
    public <T> void requestWRLD(double amount, Network network, String reason, boolean canDuplicate, T payload) throws IOException, InterruptedException {
        NFTWorlds nftWorlds = NFTWorlds.getInstance();
        NFTPlayer nftPlayer = NFTPlayer.getByUUID(associatedPlayer);
        if (nftPlayer != null) {
            Player player = Bukkit.getPlayer(nftPlayer.getUuid());
            if (player != null) {
                Uint256 refID = new Uint256(new BigInteger(256, new Random())); //NOTE: This generates a random Uint256 to use as a reference. Don't know if we want to change this or not.
                long timeout = Instant.now().plus(nftWorlds.getNftConfig().getLinkTimeout(), ChronoUnit.SECONDS).toEpochMilli();
                new PaymentRequest(associatedPlayer, amount, refID, network, reason, timeout, canDuplicate, payload);
                String paymentLink = "https://nftworlds.com/pay/?to=" + nftWorlds.getNftConfig().getServerWalletAddress() + "&amount=" + amount + "&ref=" + refID.getValue().toString() + "&expires=" + (int) (timeout / 1000) + "&duplicate=" + canDuplicate;


                MapView view = Bukkit.createMap(player.getWorld());
                view.getRenderers().clear();

                QRMapManager renderer = new QRMapManager();
                player.sendMessage(ChatColor.GOLD + "Incoming payment request for: " + ChatColor.WHITE + reason);
                if (Bukkit.getServer().getPluginManager().getPlugin("Geyser-Spigot") != null && org.geysermc.connector.GeyserConnector.getInstance().getPlayerByUuid(player.getUniqueId()) != null) {
                    String shortLink = LinkUtils.shortenURL(paymentLink);
                    renderer.load(shortLink);
                    // TODO: Better error handling
                    view.addRenderer(renderer);
                    ItemStack map = new ItemStack(Material.FILLED_MAP);
                    MapMeta meta = (MapMeta) map.getItemMeta();

                    meta.setMapView(view);
                    map.setItemMeta(meta);

                    QRMapManager.playerPreviousItem.put(player.getUniqueId(), player.getInventory().getItem(0));
                    player.getInventory().setItem(0, map);
                    player.getInventory().setHeldItemSlot(0);

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f&lScan the QR code on your map! Right click to exit."));

                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f&lPAY HERE: ") + ChatColor.GREEN + paymentLink);
                }
            }
        }
    }

    /**
     * Deposit WRLD into this wallet
     *
     * @param amount
     * @param network
     * @param reason
     */
    public void payWRLD(double amount, Network network, String reason) {
        if (!NFTPlayer.getByUUID(getAssociatedPlayer()).isLinked()) return;
        if (!network.equals(Network.POLYGON)) {
            Bukkit.getLogger().warning("Attempted to call Wallet.payWRLD with unsupported network. " +
                    "Only Polygon is supported in this plugin at the moment.");
            return;
        }

        BigDecimal sending = Convert.toWei(BigDecimal.valueOf(amount), Convert.Unit.ETHER);
        Player paidPlayer = Objects.requireNonNull(Bukkit.getPlayer(this.getAssociatedPlayer()));
        paidPlayer.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                new TextComponent(ChatColor.DARK_GREEN + "Incoming " + amount + " WRLD payment pending"));
        try {
            final PolygonWRLDToken polygonWRLDTokenContract = NFTWorlds.getInstance().getWrld().getPolygonWRLDTokenContract();
            polygonWRLDTokenContract.transfer(this.getAddress(), sending.toBigInteger()).sendAsync().thenAccept((c) -> {
                paidPlayer.sendMessage(
                        ChatColor.translateAlternateColorCodes('&',
                                "&6You've been paid! &7Reason&f: " + reason + "\n" +
                                        "&a&nhttps://polygonscan.com/tx/" +
                                        c.getTransactionHash() + "&r\n "));
            });
        } catch (Exception e) {
            Bukkit.getLogger().warning("caught error in payWrld:");
            e.printStackTrace();
        }
    }

    /**
     * Create a peer to peer payment link for player
     *
     * @param to
     * @param amount
     * @param network
     * @param reason
     */
    public void createPlayerPayment(NFTPlayer to, double amount, Network network, String reason) {
        NFTWorlds nftWorlds = NFTWorlds.getInstance();
        NFTPlayer nftPlayer = NFTPlayer.getByUUID(associatedPlayer);
        if (nftPlayer != null && to != null) {
            Player player = Bukkit.getPlayer(nftPlayer.getUuid());
            if (player != null) {
                if (!to.isLinked()) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis player does not have a wallet linked."));
                    return;
                }
                Uint256 refID = new Uint256(new BigInteger(256, new Random()));
                long timeout = Instant.now().plus(nftWorlds.getNftConfig().getLinkTimeout(), ChronoUnit.SECONDS).toEpochMilli();
                new PeerToPeerPayment(to, nftPlayer, amount, refID, network, reason, timeout);
                String paymentLink = "https://nftworlds.com/pay/?to=" + to.getPrimaryWallet().getAddress() + "&amount=" + amount + "&ref=" + refID.getValue().toString() + "&expires=" + (int) (timeout / 1000);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f&lPAY HERE: ") + ChatColor.GREEN + paymentLink);
            }
        }
    }

}
