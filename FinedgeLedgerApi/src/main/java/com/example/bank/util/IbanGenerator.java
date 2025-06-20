package com.example.bank.util;

import java.security.SecureRandom;

public class IbanGenerator {

    private static final SecureRandom RND = new SecureRandom();

    // valori demo
    private static final String ABI = "02008";   // banca
    private static final String CAB = "16517";   // filiale

    /** Ritorna un IBAN italiano realistico (27 caratteri) unico e valido. */
    public static String newItalian() {
        char cin = (char) ('A' + RND.nextInt(26));      // lettera A-Z
        String account = randomDigits(12);              // 12 cifre
        String body = "" + cin + ABI + CAB + account;   // 23 caratteri
        String check = calcCheckDigits("IT00" + body);  // 2 cifre di controllo
        return "IT" + check + body;
    }

    /* ---------- helper privati ------------------------------------------- */

    private static String randomDigits(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(RND.nextInt(10));
        return sb.toString();
    }

    private static String calcCheckDigits(String iban) {
        // sposta i primi 4 caratteri in coda
        String rearr = iban.substring(4) + iban.substring(0, 4);
        StringBuilder num = new StringBuilder();
        for (char ch : rearr.toCharArray())
            num.append(Character.isLetter(ch) ? ch - 'A' + 10 : ch);
        int mod = mod97(num.toString());
        int check = 98 - mod;
        return String.format("%02d", check);
    }

    private static int mod97(String s) {
        int rem = 0;
        for (char c : s.toCharArray())
            rem = (rem * 10 + (c - '0')) % 97;
        return rem;
    }
}
