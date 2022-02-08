package com.nftworlds.wallet.contracts.nftworlds;

import com.nftworlds.wallet.contracts.wrappers.polygon.PolygonPlayers;
import com.nftworlds.wallet.providers.Polygon;
import org.json.simple.JSONObject;

public class Players {
    private PolygonPlayers polygonPlayersContract;

    public Players() {
        /*
            TODO: Contract loading should use the corresponding contract addresses set in the plugin's
            Config yml file.
         */

        this.polygonPlayersContract = PolygonPlayers.load(/* TODO */);
    }

    public String getPlayerPrimaryWallet(String playerUUID) {
        // TODO: returns async? or?
        return this.polygonPlayersContract.getPlayerPrimaryWallet(playerUUID).send();
    }

    public String[] getPlayerSecondaryWallets(String playerUUID) {
        // TODO: returns async? or?
        return this.polygonPlayersContract.getPlayerSecondaryWallets(playerUUID).send();
    }

    public JSONObject getPlayerStateData(String playerUUID, String setterWalletAddress) {
        // TODO: returns async? or?
        String stateDataUrl = this.polygonPlayersContract.getPlayerStateData(playerUUID, setterWalletAddress, true).send();

        if (stateDataUrl.isEmpty()) {
            // TODO: no state data was set for player by the setter address, return?
        }

        /*
            TODO:
            Do HTTP request for JSON string returned from retrieving stateDataUrl contents.
            Convert JSON string to JSON Object and return.
         */
    }
}
