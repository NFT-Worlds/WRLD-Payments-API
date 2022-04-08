package com.nftworlds.wallet.menus;

import com.nftworlds.wallet.util.ColorUtil;
import ninja.amp.ampmenus.events.ItemClickEvent;
import ninja.amp.ampmenus.items.StaticMenuItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CloseItem extends StaticMenuItem {
    public CloseItem() {
        super(ColorUtil.rgb("&c&lClose"), new ItemStack(Material.BARRIER), new String[0]);
    }

    public void onItemClick(ItemClickEvent event) {
        event.setWillClose(true);
    }
}

