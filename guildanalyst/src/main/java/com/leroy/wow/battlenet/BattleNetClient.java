package com.leroy.wow.battlenet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.leroy.wow.battlenet.services.BattleNetClientCacheService;
import com.leroy.wow.battlenet.services.BattleNetClientPersistenceService;
import com.leroy.wow.battlenet.services.BattleNetClientWebService;
import com.leroy.wow.beans.WowCharacter;
import com.leroy.wow.beans.WowGuild;


public class BattleNetClient {

    private final static Logger logger = Logger.getLogger(BattleNetClient.class);
    private long apiCallCount;
    
    private BattleNetClientWebService web;
    private BattleNetClientPersistenceService file;
    private BattleNetClientCacheService mem;
    
    public BattleNetClient(String zone) {
        this.apiCallCount = 0;
        this.web = new BattleNetClientWebService(zone);
        this.file = new BattleNetClientPersistenceService("/users/leroyro1/perso/tmp", LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT), Duration.ofHours(1));
        this.mem = new BattleNetClientCacheService();

        System.setProperty("http.proxyHost", "proxyusers.intranet");
        System.setProperty("http.proxyPort", "8080");
        System.setProperty("http.proxyUser", "leroyro1");
    }

    public long getApiCallCount() {
        return apiCallCount;
    }
    
    public boolean isPersisted(BattleNetType type, String realm, String name) {
        boolean res = false;
        try{
            res = file.getData(type, realm, name) != null;
        }catch(Exception e){
            logger.error("Cannot check persistency of "+type.name()+" whith name "+name+" on realm "+realm);
            logger.debug(e.getMessage(), e);
        }
        return res;
    }
    
    public BattleNetResponse getData(BattleNetType type, String realm, String name) throws IOException, URISyntaxException{
        BattleNetResponse res = mem.getData(type, realm, name);
        if (res == null){
            res = file.getData(type, realm, name);
            if (res == null){
                res = web.getData(type, realm, name);
                apiCallCount++;
                file.putData(type, realm, name, res.getJSON());
            }
            mem.putData(type, realm, name, res.getJSON());
        }
        return res;
    }

    public WowGuild getGuild(String realm, String name) throws Exception{
        BattleNetResponse data = this.getData(BattleNetType.guild, realm, name);
        return new WowGuild(data.getJSON());
    }

    public WowCharacter getCharacter(String realm, String name) throws IOException, URISyntaxException {
        BattleNetResponse data = this.getData(BattleNetType.character, realm, name);
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
