package com.leroy.wow.battlenet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


public class BattleNetClient {

    public BattleNetResponse getData() throws IOException {
        
        System.setProperty("http.proxyHost", "proxyusers.intranet");
        System.setProperty("http.proxyPort", "8080");
        System.setProperty("http.proxyUser", "leroyro1");
        
        String url = "http://eu.battle.net/api/wow/character/Sargeras/Pamynx";
        URL dataUrl = new URL(url);
        URLConnection conn = dataUrl.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String data = "";
        String inputLine;
        while ((inputLine = br.readLine()) != null) {
            data += inputLine;
        }
        br.close();
        return new BattleNetResponse(data);
    }
}
