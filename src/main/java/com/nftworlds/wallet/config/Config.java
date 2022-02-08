package com.nftworlds.wallet.config;

import com.nftworlds.wallet.NFTWorlds;
import com.nftworlds.wallet.util.AddressUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.logging.Level;

@Getter
public class Config {

    private String polygonHttpsRpc;
    private String ethereumHttpsRpc;
    private String serverWalletAddress;

    public void registerConfig() {
        NFTWorlds wallet = NFTWorlds.getInstance();
        FileConfiguration config = wallet.getConfig();
        config.options().copyDefaults(true);
        wallet.saveConfig();

        this.polygonHttpsRpc = config.getString("polygon_https_rpc");
        this.ethereumHttpsRpc = config.getString("ethereum_https_rpc");

        String address = config.getString("server_wallet_address");
        if (AddressUtil.validAddress(address.toLowerCase()) && AddressUtil.checksumAddress(address.toLowerCase())) {
            this.serverWalletAddress = address;
        } else {
            Bukkit.getLogger().log(Level.WARNING, "Server wallet address is an invalid format. Check config.yml.");
            wallet.getServer().getPluginManager().disablePlugin(wallet);
        }
    }

}
