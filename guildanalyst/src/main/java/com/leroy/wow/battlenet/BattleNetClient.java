package com.leroy.wow.battlenet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.leroy.wow.beans.WowCharacter;
import com.leroy.wow.beans.WowGuild;


public class BattleNetClient {

    private String host;
    private String server;
    
    public BattleNetClient(String zone, String server) {
        if ("EU".equals(zone)){
            this.host = "eu.battle.net";
        }else{
            throw new IllegalArgumentException("Zone "+zone+" is not supported");
        }
        this.server = server;
        
        System.setProperty("http.proxyHost", "proxyusers.intranet");
        System.setProperty("http.proxyPort", "8080");
        System.setProperty("http.proxyUser", "leroyro1");
    }

    public BattleNetResponse getData(String type, String name) throws IOException, URISyntaxException{
        return this.getData(type, name, null);
    }
    
    public BattleNetResponse getData(String type, String name, Set<String> fields) throws IOException, URISyntaxException{
        String path = "/api/wow/"+type+"/"+this.server+"/"+name;
        String options = null;
        if (fields != null && fields.size() > 0){
            List<String> optionsList = fields.stream().map(s -> "fields=" + s).collect(Collectors.toList());
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

    public WowGuild getGuild(String name) throws Exception{
        Set<String> fields = new HashSet<String>();
        fields.add("members");
        BattleNetResponse data = this.getData("guild", name, fields);
        return new WowGuild(data.getJSON());
    }

    public WowCharacter getCharacter(String name) throws IOException, URISyntaxException {
        Set<String> fields = new HashSet<String>();
        fields.add("items");
        BattleNetResponse data = this.getData("character", name, fields);
        return new WowCharacter(data.getJSON());
    }

    public WowCharacter getSafeCharacter(String name) {
        WowCharacter res = null;
        try{
            res = this.getCharacter(name);
        }catch(Exception e){
            // TODO : Log4j
            e.printStackTrace();
        }
        return res;
    }

}
