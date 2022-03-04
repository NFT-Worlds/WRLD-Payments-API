package com.nftworlds.wallet.menus;

import ninja.amp.ampmenus.events.ItemClickEvent;
import ninja.amp.ampmenus.items.StaticMenuItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CloseItem extends StaticMenuItem {
    public CloseItem() {
        super(ChatColor.RED + "Close", new ItemStack(Material.CRIMSON_BUTTON), new String[0]);
    }

    public void onItemClick(ItemClickEvent event) {
        event.setWillClose(true);
    }
}

