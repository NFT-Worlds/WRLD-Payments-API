package com.nftworlds.wallet.objects;

import com.nftworlds.wallet.NFTWorlds;
import com.nftworlds.wallet.contracts.wrappers.common.ERC20;
import com.nftworlds.wallet.contracts.wrappers.common.ERC721;
import com.nftworlds.wallet.contracts.wrappers.polygon.PolygonWRLDToken;
import com.nftworlds.wallet.event.AsyncPlayerPaidFromServerWalletEvent;
import com.nftworlds.wallet.objects.payments.PaymentRequest;
import com.nftworlds.wallet.objects.payments.PeerToPeerPayment;
import com.nftworlds.wallet.qrmaps.LinkUtils;
import com.nftworlds.wallet.qrmaps.QRMapManager;
import com.nftworlds.wallet.util.ColorUtil;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;
import org.json.HTTP;
import org.json.JSONObject;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class Wallet {

    @Getter
    private final NFTPlayer owner;
    @Getter
    private final String address;
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

    public Wallet(UUID uuid, String address) {
        this.owner = NFTPlayer.getByUUID(uuid);
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

        NFTWorlds.getInstance().addWallet(this);
    }

    public Wallet(NFTPlayer owner, String address) {
        this.owner = owner;
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

        NFTWorlds.getInstance().addWallet(this);
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
     * Alternative API for NFT fetching that seems to provide better data than Alchemy.
     * Returns NFTs on both Polygon and Ethereum chains.
     */
    public JSONObject getOwnedNFTsByContactWithSimpleHash(String contractAddress) throws URISyntaxException, IOException, InterruptedException {
        String url =
                "https://api.simplehash.com/api/v0/nfts/owners?chains=polygon,ethereum&wallet_addresses=" + address +
                        "&contract_addresses=" + contractAddress;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url)).header("X-API-KEY", "worldql_sk_ssga6syqc1eo5eyn")
                .build();
        return new JSONObject(client.send(request, HttpResponse.BodyHandlers.ofString()).body());
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
        String url = baseURL + "/getNFTs?owner=" + address + "&contractAddresses[]=" + contractAddress;
        return new JSONObject(HttpClient.newHttpClient().send(HttpRequest.newBuilder().uri(URI.create(url)).build(), HttpResponse.BodyHandlers.ofString()).body());
    }

    public boolean doesPlayerOwnNFTInCollection(Network network, String contractAddress) {
        ERC721 erc721 = null;
        if (network.equals(Network.ETHEREUM)) {
            erc721 = ERC721.load(
                    contractAddress,
                    NFTWorlds.getInstance().getEthereumRPC().getEthereumWeb3j(),
                    Credentials.create(NFTWorlds.getInstance().getNftConfig().getServerPrivateKey()),
                    new DefaultGasProvider()
            );
        } else if (network.equals(Network.POLYGON)) {
            erc721 = ERC721.load(
                    contractAddress,
                    NFTWorlds.getInstance().getPolygonRPC().getPolygonWeb3j(),
                    Credentials.create(NFTWorlds.getInstance().getNftConfig().getServerPrivateKey()),
                    new DefaultGasProvider()
            );
        } else {
            return false;
        }

        try {
            BigInteger balance = erc721.balanceOf(address).send();
            return balance.compareTo(BigInteger.ZERO) > 0; //return true if address owns at least 1 NFT from this contract
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Send a request for a WRLD transaction from this wallet
     *
     * @param amount
     * @param network
     * @param reason
     * @param canDuplicate
     * @param payload
     * @return If $WRLD request was successful
     */
    public <T> boolean requestWRLD(@NotNull double amount, @NotNull Network network, @NotNull String reason, @NotNull boolean canDuplicate, @NotNull T payload) {
        NFTWorlds nftWorlds = NFTWorlds.getInstance();

        UUID uuid = owner.getUuid();
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            Uint256 refID = new Uint256(new BigInteger(256, new Random())); //NOTE: This generates a random Uint256 to use as a reference. Don't know if we want to change this or not.
            long timeout = Instant.now().plus(nftWorlds.getNftConfig().getLinkTimeout(), ChronoUnit.SECONDS).toEpochMilli();
            new PaymentRequest(uuid, amount, refID, network, reason, timeout, canDuplicate, payload);
            String paymentLink = "https://nftworlds.com/pay/?to=" + nftWorlds.getNftConfig().getServerWalletAddress() + "&amount=" + amount + "&ref=" + refID.getValue().toString() + "&expires=" + (int) (timeout / 1000) + "&duplicate=" + canDuplicate;


            MapView view = Bukkit.createMap(player.getWorld());
            view.getRenderers().clear();

            QRMapManager renderer = new QRMapManager();
            player.sendMessage(ColorUtil.rgb(MessageFormat.format(NFTWorlds.getInstance().getLangConfig().getIncomingRequest(), reason)));
            if (Bukkit.getServer().getPluginManager().getPlugin("Geyser-Spigot") != null && org.geysermc.connector.GeyserConnector.getInstance().getPlayerByUuid(player.getUniqueId()) != null) {
                String shortLink;
                try {
                    shortLink = LinkUtils.shortenURL(paymentLink);
                } catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                    return false;
                }
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

                player.sendMessage(ColorUtil.rgb(NFTWorlds.getInstance().getLangConfig().getScanQRCode()));

            } else {
                player.sendMessage(MessageFormat.format(ColorUtil.rgb(NFTWorlds.getInstance().getLangConfig().getPayHere()), paymentLink));
            }
            player.sendMessage(MessageFormat.format(ColorUtil.rgb(NFTWorlds.getInstance().getLangConfig().getPayHere()), paymentLink));
            return true;
        }
        return false;
    }

    public boolean mintERC1155NFT(String contractAddress, Network network, String data, int id) {
        if (!owner.isLinked()) {
            NFTWorlds.getInstance().getLogger().warning("Skipped outgoing transaction because wallet was not linked!");
            return false;
        }
        if (!network.equals(Network.POLYGON) || !NFTWorlds.getInstance().getNftConfig().isUseHotwalletForOutgoingTransactions()) {
            NFTWorlds.getInstance().getLogger().warning("Attempted to call Wallet.mintERC1155NFT with unsupported network." +
                    "Only Polygon is supported at the moment when using Hotwallet backend.");
            return false;
        }
        Player paidPlayer = Bukkit.getPlayer(owner.getUuid());

        JSONObject json = new JSONObject();
        json.put("network", "Polygon");
        json.put("contract_address", contractAddress);
        json.put("recipient_address", this.getAddress());
        // https://forum.openzeppelin.com/t/erc1155-data-parameter-on-mint-method/4393/4
        // We recommend using a JSON format. The token id will be automatically chosen.
        json.put("data", data);
        json.put("id", id);

        String requestBody = json.toString();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(NFTWorlds.getInstance().getNftConfig().getHotwalletHttpsEndpoint() + "/mint_erc1155"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        try {
            JSONObject response = new JSONObject(HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body());
            final String receiptLink = "https://app.economykit.com/hotwallet/transaction/" + response.getInt("outgoing_tx_id");
            Bukkit.getScheduler().runTaskAsynchronously(NFTWorlds.getInstance(), () -> {
                if (paidPlayer != null) {
                    paidPlayer.sendMessage(ColorUtil.rgb(
                            MessageFormat.format(NFTWorlds.getInstance().getLangConfig().getMinted(), receiptLink)));
                } else {
                    NFTWorlds.getInstance().getLogger().info("Sent offline ERC-1155 mint: " + receiptLink);
                }
            });
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * Deposit WRLD into this wallet
     *
     * @param amount
     * @param network
     * @param reason
     * @return If $WRLD send was successful
     */
    public boolean payWRLD(@NotNull double amount, @NotNull Network network, @NotNull String reason) {
        if (!owner.isLinked()) {
            NFTWorlds.getInstance().getLogger().warning("Skipped outgoing transaction because wallet was not linked!");
            return false;
        }
        if (!network.equals(Network.POLYGON)) {
            NFTWorlds.getInstance().getLogger().warning("Attempted to call Wallet.payWRLD with unsupported network. " +
                    "Only Polygon is supported in this plugin at the moment.");
            return false;
        }

        BigDecimal sending = Convert.toWei(BigDecimal.valueOf(amount), Convert.Unit.ETHER);
        Player paidPlayer = Bukkit.getPlayer(owner.getUuid());

        if (NFTWorlds.getInstance().getNftConfig().isUseHotwalletForOutgoingTransactions()) {
            // TODO: Add support for other outgoing currencies through Hotwallet.
            JSONObject json = new JSONObject();
            json.put("network", "Polygon");
            json.put("token", "POLYGON_WRLD");
            json.put("recipient_address", this.getAddress());
            json.put("amount", sending.toBigInteger());
            String requestBody = json.toString();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(NFTWorlds.getInstance().getNftConfig().getHotwalletHttpsEndpoint() + "/send_tokens"))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            try {
                JSONObject response = new JSONObject(HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body());
                final String receiptLink = "https://app.economykit.com/hotwallet/transaction/" + response.getInt("outgoing_tx_id");
                Bukkit.getScheduler().runTaskAsynchronously(NFTWorlds.getInstance(), () -> {
                    postPaymentEvent(amount, network, reason, paidPlayer, receiptLink);
                });
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            try {
                NFTWorlds.getInstance().getLogger().info("Sending outgoing transaction using PK to " + paidPlayer + " for " + amount);
                final PolygonWRLDToken polygonWRLDTokenContract = NFTWorlds.getInstance().getWrld().getPolygonWRLDTokenContract();
                polygonWRLDTokenContract.transfer(this.getAddress(), sending.toBigInteger()).sendAsync().thenAccept((c) -> {
                    final String receiptLink = "https://polygonscan.com/tx/" + c.getTransactionHash();
                    postPaymentEvent(amount, network, reason, paidPlayer, receiptLink);
                }).exceptionally(error -> {
                    NFTWorlds.getInstance().getLogger().warning("Caught error in transfer function exceptionally: " + error);
                    return null;
                });
            } catch (Exception e) {
                NFTWorlds.getInstance().getLogger().warning("caught error in payWrld:");
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private void postPaymentEvent(double amount, Network network, String reason, Player paidPlayer, String receiptLink) {
        if (paidPlayer != null) {
            AsyncPlayerPaidFromServerWalletEvent walletEvent = new AsyncPlayerPaidFromServerWalletEvent(paidPlayer, amount, network, reason, receiptLink);
            walletEvent.callEvent();

            if (walletEvent.isDefaultReceiveMessage()) {
                paidPlayer.sendMessage(ColorUtil.rgb(
                        MessageFormat.format(NFTWorlds.getInstance().getLangConfig().getPaid(), reason, receiptLink)));
            }
        } else {
            NFTWorlds.getInstance().getLogger().info("Paid offline player: " + receiptLink);
        }
    }

    /**
     * Create a peer to peer payment link for player
     *
     * @param to
     * @param amount
     * @param network
     * @param reason
     * @param payload
     * @return If player payment link was successfully created
     */
    public <T> boolean createPlayerPayment(@NotNull NFTPlayer to, @NotNull double amount, @NotNull Network network, @NotNull String reason, @NotNull T payload) {
        NFTWorlds nftWorlds = NFTWorlds.getInstance();
        Player player = Bukkit.getPlayer(owner.getUuid());
        if (player != null) {
            if (!to.isLinked()) {
                player.sendMessage(ColorUtil.rgb(NFTWorlds.getInstance().getLangConfig().getPlayerNoLinkedWallet()));
                return false;
            }
            Uint256 refID = new Uint256(new BigInteger(256, new Random()));
            long timeout = Instant.now().plus(nftWorlds.getNftConfig().getLinkTimeout(), ChronoUnit.SECONDS).toEpochMilli();
            new PeerToPeerPayment(to, owner, amount, refID, network, reason, timeout, payload);
            String paymentLink = "https://nftworlds.com/pay/?to=" + to.getPrimaryWallet().getAddress() + "&amount=" + amount + "&ref=" + refID.getValue().toString() + "&expires=" + (int) (timeout / 1000);
            player.sendMessage(MessageFormat.format(ColorUtil.rgb(NFTWorlds.getInstance().getLangConfig().getPayHere()), paymentLink));
            return true;
        }
        return false;
    }

    /**
     * Create a peer to peer payment link for player
     *
     * @param to
     * @param amount
     * @param network
     * @param reason
     * @return If player payment link was successfully created
     */
    public <T> boolean createPlayerPayment(@NotNull NFTPlayer to, @NotNull double amount, @NotNull Network network, @NotNull String reason) {
        NFTWorlds nftWorlds = NFTWorlds.getInstance();
        Player player = Bukkit.getPlayer(owner.getUuid());
        if (player != null) {
            if (!to.isLinked()) {
                player.sendMessage(ColorUtil.rgb(NFTWorlds.getInstance().getLangConfig().getPlayerNoLinkedWallet()));
                return false;
            }
            Uint256 refID = new Uint256(new BigInteger(256, new Random()));
            long timeout = Instant.now().plus(nftWorlds.getNftConfig().getLinkTimeout(), ChronoUnit.SECONDS).toEpochMilli();
            new PeerToPeerPayment(to, owner, amount, refID, network, reason, timeout, null);
            String paymentLink = "https://nftworlds.com/pay/?to=" + to.getPrimaryWallet().getAddress() + "&amount=" + amount + "&ref=" + refID.getValue().toString() + "&expires=" + (int) (timeout / 1000);
            player.sendMessage(MessageFormat.format(ColorUtil.rgb(NFTWorlds.getInstance().getLangConfig().getPayHere()), paymentLink));
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Wallet wallet = (Wallet) object;
        if (!Objects.equals(owner, wallet.owner)) return false;
        return Objects.equals(address, wallet.address);
    }

    @Override
    public int hashCode() {
        int result = owner != null ? owner.hashCode() : 0;
        result = 31 * result + (address != null ? address.hashCode() : 0);
        return result;
    }

}
