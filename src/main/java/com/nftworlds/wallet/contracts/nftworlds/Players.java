package com.nftworlds.wallet.contracts.nftworlds;

import com.nftworlds.wallet.contracts.wrappers.polygon.PolygonPlayers;
import org.json.*;
import org.json.simple.JSONObject;

public class Players {
    private PolygonPlayers polygonPlayersContract;

    public Players() {
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

    public JSONObject getPlayerStateData(String playerUUID, String setterAddress) {
        // TODO: returns async? or?
        String stateDataUrl = this.polygonPlayersContract.getPlayerStateData(playerUUID, setterAddress, true).send();

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
