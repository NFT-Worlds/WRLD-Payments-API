package com.nftworlds.wallet.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class AddressUtil {

    /**
     * Ekesh Bahuguna 2018
     */

    public static boolean validAddress(String ethereumAddress) {
        String regex = "^0x[0-9a-f]{40}$";
        if (ethereumAddress.matches(regex)) {
            return true;
        }
        return false;
    }

    public static boolean checksumAddress(String ethereumAddress) {
        String subAddr = ethereumAddress.substring(2);
        String subAddrLower = subAddr.toLowerCase();
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        byte[] encodedhash = digest.digest(subAddrLower.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
        for (int i = 0; i < encodedhash.length; i++) {
            String hex = Integer.toHexString(0xff & encodedhash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        String digestMessage = hexString.toString();

        for (short i = 0; i < subAddr.length(); i++) {
            if (subAddr.charAt(i) >= 65 && subAddr.charAt(i) <= 91) {
                String ss = Character.toString(digestMessage.charAt(i));
                if (!(Integer.parseInt(ss, 16) > 7)) {
                    return false;
                }
            }
        }
        return true;
    }

}
