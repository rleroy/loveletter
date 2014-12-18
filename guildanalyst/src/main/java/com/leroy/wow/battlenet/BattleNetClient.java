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

import org.apache.log4j.Logger;

import com.leroy.wow.beans.WowCharacter;
import com.leroy.wow.beans.WowGuild;


public class BattleNetClient {

    private final static Logger logger = Logger.getLogger(BattleNetClient.class);

    private String host;
    
    public BattleNetClient(String zone) {
        if ("EU".equals(zone)){
            this.host = "eu.battle.net";
        }else{
            throw new IllegalArgumentException("Zone "+zone+" is not supported");
        }
        System.setProperty("http.proxyHost", "proxyusers.intranet");
        System.setProperty("http.proxyPort", "8080");
        System.setProperty("http.proxyUser", "leroyro1");
    }

    public BattleNetResponse getData(String type, String realm, String name) throws IOException, URISyntaxException{
        return this.getData(type, realm, name, null);
    }
    
    public BattleNetResponse getData(String type, String realm, String name, Set<String> fields) throws IOException, URISyntaxException{
        String path = "/api/wow/"+type+"/"+realm+"/"+name;
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

    public WowGuild getGuild(String realm, String name) throws Exception{
        Set<String> fields = new HashSet<String>();
        fields.add("members");
        BattleNetResponse data = this.getData("guild", realm, name, fields);
        return new WowGuild(data.getJSON());
    }

    public WowCharacter getCharacter(String realm, String name) throws IOException, URISyntaxException {
        Set<String> fields = new HashSet<String>();
        fields.add("items");
        BattleNetResponse data = this.getData("character", realm, name, fields);
        return new WowCharacter(data.getJSON());
    }

    public WowCharacter getSafeCharacter(String realm, String name) {
        WowCharacter res = null;
        try{
            res = this.getCharacter(realm, name);
        }catch(Exception e){
            logger.error("Cannot load character whith name "+name+" on realm "+realm);
            logger.debug(e.getMessage(), e);
        }
        return res;
    }

}
