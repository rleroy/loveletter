package com.leroy.wow.battlenet.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.stream.Collectors;

import com.leroy.wow.battlenet.BattleNetResponse;
import com.leroy.wow.battlenet.BattleNetType;

public class BattleNetClientWebService {

    private String host;

    public BattleNetClientWebService(String zone) {
        if ("EU".equals(zone)){
            this.host = "eu.battle.net";
        }else{
            throw new IllegalArgumentException("Zone "+zone+" is not supported");
        }
    }

    public BattleNetResponse getData(BattleNetType type, String realm, String name) throws URISyntaxException, IOException {
        String path = "/api/wow/"+type+"/"+realm+"/"+name;
        String options = null;
        if (type.getFields() != null && type.getFields().size() > 0){
            List<String> optionsList = type.getFields().stream().map(s -> "fields=" + s).collect(Collectors.toList());
            options = String.join("&", optionsList); 
        }
        URI uri = new URI("http", host, path, options, null);
        URL url = uri.toURL();
        URLConnection conn = url.openConnection();
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
