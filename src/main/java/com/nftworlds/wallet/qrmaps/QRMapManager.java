package com.nftworlds.wallet.qrmaps;

import com.google.zxing.WriterException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.UUID;

public class QRMapManager extends MapRenderer {
    private BufferedImage image;
    private boolean loaded;
    public static HashMap<UUID, ItemStack> playerPreviousItem = new HashMap<>();

    public QRMapManager() {
        this.loaded = false;
    }

    public QRMapManager(final String url) {
        this.loaded = false;
        this.load(url);
    }

    public boolean load(final String url) {
        try {
            this.image = LinkUtils.createQRCode(url, 128);
            this.image = MapPalette.resizeImage(this.image);
        } catch (WriterException e) {
            return false;
        }

        return true;
    }

    @Override
    public void render(@NotNull MapView view, @NotNull MapCanvas canvas, @NotNull Player player) {
        if (this.loaded) return;

        canvas.drawImage(0, 0, this.image);
        view.setTrackingPosition(false);

        this.loaded = true;
    }
}
