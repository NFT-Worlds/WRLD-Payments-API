package com.nftworlds.wallet.config;

import com.nftworlds.wallet.NFTWorlds;
import lombok.Getter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

@Getter
public class LangConfig {

    private File langFile;
    private FileConfiguration lang;

    private String NoLinkedWallet;

    private String IncomingRequest;

    private String IncomingPending;

    private String PayHere;

    private String Paid;

    private String SetPrimaryWallet;

    private String SetSecondaryWallet;

    private String RemoveSecondaryWallet;

    private String ScanQRCode;

    private String PlayerNoLinkedWallet;

    public void registerConfig() {
        langFile = new File(NFTWorlds.getInstance().getDataFolder(), "lang.yml");
        if (!langFile.exists()) {
            NFTWorlds.getInstance().saveResource("lang.yml", false);
        }

        lang = YamlConfiguration.loadConfiguration(langFile);

        this.NoLinkedWallet = lang.getString("NoLinkedWallet");
        this.IncomingRequest = lang.getString("IncomingRequest");
        this.IncomingPending = lang.getString("IncomingPending");
        this.PayHere = lang.getString("PayHere");
        this.Paid = lang.getString("Paid");
        this.SetPrimaryWallet = lang.getString("SetPrimaryWallet");
        this.SetSecondaryWallet = lang.getString("SetSecondaryWallet");
        this.RemoveSecondaryWallet = lang.getString("RemoveSecondaryWallet");
        this.ScanQRCode = lang.getString("ScanQRCode");
        this.PlayerNoLinkedWallet = lang.getString("PlayerNoLinkedWallet");
    }

}
