package com.nftworlds.wallet.menus;

import com.nftworlds.wallet.NFTWorlds;
import ninja.amp.ampmenus.menus.ItemMenu;

public class WalletGUI {
    public static ItemMenu menu;

    public static void setup() {
        menu = new ItemMenu("NFT Worlds Wallet", ItemMenu.Size.TWO_LINE, NFTWorlds.getInstance());
        menu.setItem(8, new CloseItem());
        menu.setItem(0, new BalanceItem());
    }
}
