package com.nftworlds.wallet.config;

import com.nftworlds.wallet.NFTWorlds;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.web3j.crypto.Keys;
import org.web3j.crypto.WalletUtils;

import java.util.logging.Level;

@Getter
public class Config {
    private String polygonHttpsRpc;
    private String ethereumHttpsRpc;
    private String serverWalletAddress;

    private String polygonPlayerContract;
    private String polygonWrldContract;
    private String ethereumWrldContract;

    public void registerConfig() {
        NFTWorlds wallet = NFTWorlds.getInstance();
        FileConfiguration config = wallet.getConfig();
        config.options().copyDefaults(true);
        wallet.saveConfig();

        this.polygonHttpsRpc = config.getString("polygon_https_rpc");
        this.ethereumHttpsRpc = config.getString("ethereum_https_rpc");

        String address = config.getString("server_wallet_address");
        if (validateAddress(address, "Server Wallet Address")) {
            this.serverWalletAddress = address;
        }

        String polygonPlayerContract = config.getString("polygon_player_contract");
        if (validateAddress(polygonPlayerContract, "Polygon Player Contract")) {
            this.polygonPlayerContract = polygonPlayerContract;
        }

        String polygonWrldContract = config.getString("polygon_wrld_contract");
        if (validateAddress(polygonWrldContract, "Polygon WRLD Contract")) {
            this.polygonWrldContract = polygonWrldContract;
        }

        String ethereumWrldContract = config.getString("ethereum_wrld_contract");
        if (validateAddress(ethereumWrldContract, "Ethereum WRLD Contract")) {
            this.ethereumWrldContract = ethereumWrldContract;
        }
    }

    private boolean validateAddress(String address, String name) {
        if (!WalletUtils.isValidAddress(address) || !Keys.toChecksumAddress(address).equals(address)) {
            Bukkit.getLogger().log(Level.WARNING, name + " is an invalid format. Check config.yml.");
            Bukkit.getServer().getPluginManager().disablePlugin(NFTWorlds.getInstance());
            return false;
        }

        return true;
    }

}
