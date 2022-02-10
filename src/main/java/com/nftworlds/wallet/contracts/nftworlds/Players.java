package com.nftworlds.wallet.contracts.nftworlds;

import com.nftworlds.wallet.NFTWorlds;
import com.nftworlds.wallet.contracts.wrappers.polygon.PolygonPlayers;
import com.nftworlds.wallet.rpcs.Polygon;
import org.json.JSONObject;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Keys;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class Players {
    private PolygonPlayers polygonPlayersContract;

    public Players() {
        NFTWorlds nftWorlds = NFTWorlds.getInstance();
        Polygon polygonRPC = nftWorlds.getPolygonRPC();
        Credentials credentials = null;

        try {
            credentials = Credentials.create(Keys.createEcKeyPair()); // We're only reading so this can be anything
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        this.polygonPlayersContract = PolygonPlayers.load(
                nftWorlds.getNftConfig().getPolygonPlayerContract(),
                polygonRPC.getPolygonWeb3j(),
                credentials,
                polygonRPC.getGasProvider()
        );
    }

    //NOTE: All of these lookups can initially be done async on the join event (When a new instance of NFTPlayer is created). Then they'll be cached.

    public String getPlayerPrimaryWallet(String playerUUID) throws Exception {
        // TODO: returns async? or?
        return this.polygonPlayersContract.getPlayerPrimaryWallet(playerUUID).send();
    }

    public List<String> getPlayerSecondaryWallets(String playerUUID) throws Exception {
        // TODO: returns async? or?
        return this.polygonPlayersContract.getPlayerSecondaryWallets(playerUUID).send();
    }

    public JSONObject getPlayerStateData(String playerUUID, String setterWalletAddress) throws Exception {
        // TODO: returns async? or?
        String stateDataUrl = this.polygonPlayersContract.getPlayerStateData(playerUUID, setterWalletAddress, true).send();

        if (stateDataUrl.isEmpty()) {
            return null;
        }

        JSONObject json = new JSONObject(HttpClient.newHttpClient().send(HttpRequest.newBuilder().uri(URI.create(stateDataUrl)).build(), HttpResponse.BodyHandlers.ofString()).body());
        return json;
    }
}
