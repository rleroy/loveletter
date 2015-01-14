package com.leroy.wow.battlenet.services;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.leroy.wow.battlenet.BattleNetResponse;
import com.leroy.wow.battlenet.BattleNetType;

public class BattleNetClientPersistenceService {

    private String root;
    private LocalDateTime minBirth;
    private Duration maxAge;
    
    public BattleNetClientPersistenceService(String root, LocalDateTime minBirth, Duration maxAge) {
        this.root = root;
        this.minBirth = minBirth;
        this.maxAge = maxAge;
    }

	public String getPersistantPath() {
		return this.root;
	}
    
    public BattleNetResponse getData(BattleNetType type, String realm, String name) throws IOException {
        BattleNetResponse res = null;
        String filePath = buildFilePath(type, realm, name);
        File f = new File(filePath);
        if (f.exists() && isValid(f)){
            res = new BattleNetResponse(new String(Files.readAllBytes(Paths.get(filePath)), "utf-8"));
        }
        return res;
    }
    
    public void putData(BattleNetType type, String realm, String name, String json) throws UnsupportedEncodingException, IOException {
        String filePath = buildFilePath(type, realm, name);
        File f = new File(filePath);
        f.getParentFile().mkdirs();
        if (f.exists()){
            f.delete();
        }
        f.createNewFile();
        Files.write(Paths.get(filePath), json.getBytes("utf-8"));
    }
    
    private String buildFilePath(BattleNetType type, String realm, String name) throws UnsupportedEncodingException{
        return root+"/"+type.name()+"/"+URLEncoder.encode(realm, "utf-8")+"/"+URLEncoder.encode(name, "utf-8")+".json";
    }
    
    private boolean isValid(File f) {
        boolean res = false;
        if (f.exists()){
            LocalDateTime lastModif = LocalDateTime.ofInstant(Instant.ofEpochMilli(f.lastModified()), ZoneId.systemDefault());
            if (minBirth.isBefore(lastModif)){
                if (Duration.between(lastModif, LocalDateTime.now()).compareTo(maxAge) < 0){
                    res = true;
                }
            }
        }
        return res;
    }


}
