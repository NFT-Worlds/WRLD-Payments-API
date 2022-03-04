package com.nftworlds.wallet.menus;

import com.nftworlds.wallet.objects.NFTPlayer;
import ninja.amp.ampmenus.events.ItemClickEvent;
import ninja.amp.ampmenus.items.MenuItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class BalanceItem extends MenuItem {
    private static final String DISPLAY_NAME = ChatColor.GOLD + "$WRLD Balance";
    private static final ItemStack ICON = new ItemStack(Material.EMERALD);

    public BalanceItem() {
        super(DISPLAY_NAME, ICON);
    }

    @Override
    public void onItemClick(ItemClickEvent event) {
        event.getPlayer().sendMessage("Clicked an item!");
    }

    @Override
    public ItemStack getFinalIcon(Player player) {
        ItemStack finalIcon = super.getFinalIcon(player);
        try {
            double balance = NFTPlayer.getByUUID(player.getUniqueId()).getPrimaryWallet().getPolygonWRLDBalance();
            ItemMeta meta = finalIcon.getItemMeta();
            List<String> lore = Arrays.asList("" + balance + " $WRLD");
            meta.setLore(lore);
            finalIcon.setItemMeta(meta);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalIcon;
    }
}
