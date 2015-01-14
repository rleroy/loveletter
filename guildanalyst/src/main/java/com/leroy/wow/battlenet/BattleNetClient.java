package com.leroy.wow.battlenet;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

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
    	String persistentPath = "/tmp";
    	if ("leroyro1".equals(System.getProperty("user.name"))){
            System.setProperty("http.proxyHost", "proxyusers.intranet");
            System.setProperty("http.proxyPort", "8080");
            System.setProperty("http.prox yUser", "leroyro1");
            persistentPath = "/users/leroyro1/perso/tmp";
    	}else{
    		persistentPath = "/Volumes/SeagateUSBDrive/Ronan/wow/tmp";
    	}
    	
        this.apiCallCount = 0;
        this.web = new BattleNetClientWebService(zone);
        this.file = new BattleNetClientPersistenceService(persistentPath, LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT), Duration.ofHours(1));
        this.mem = new BattleNetClientCacheService();

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
    
	public String getPersistantPath() {
		return this.file.getPersistantPath();
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

    public <T> Map<T, Set<WowCharacter>> getGuildMembersBy(String realm, String name, Function<WowCharacter, T> f) throws Exception{
        WowGuild guild = this.getGuild(realm, name);
        
        Map<T, Set<WowCharacter>> res = new HashMap<T, Set<WowCharacter>>(){

            @Override
            public Set<WowCharacter> get(Object key) {
                Set<WowCharacter> res = super.get(key);
                if (res == null){
                    res = new HashSet<WowCharacter>();
                    this.put((T)key, res);
                }
                return res;
            }
            
        };
        
        guild.getMembers()
                .stream()
                .map(member -> this.getSafeCharacter(member.getRealm(), member.getName()))
                .filter(member -> member != null)
                .forEach(character -> res.get(f.apply(character)).add(character))
            ;
        
        return res;
    }
    
    public String getMainName(String realm, String guild, Long achievementsPoints) throws Exception {
        Map<Long, Set<WowCharacter>> membersByAchievementPoints = getGuildMembersBy(realm, guild, WowCharacter::getAchievementPoints);
        String res = membersByAchievementPoints
                            .get(achievementsPoints)
                            .stream()
                            .sorted(Comparator.comparing(c -> c.getRealm()+"-"+c.getName()))
                            .sorted(Comparator.comparingLong(WowCharacter::getAverageItemLevel).reversed())
                            .findFirst()
                            .get()
                            .getName();
        return res;
    }

}
