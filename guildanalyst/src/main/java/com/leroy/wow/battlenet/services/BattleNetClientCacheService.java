package com.leroy.wow.battlenet.services;

import java.util.HashMap;
import java.util.Map;

import com.leroy.wow.battlenet.BattleNetResponse;
import com.leroy.wow.battlenet.BattleNetType;

public class BattleNetClientCacheService {

    Map<String, String> cache;
    
    public BattleNetClientCacheService(){
        cache = new HashMap<String, String>();
    }
    
    public BattleNetResponse getData(BattleNetType type, String realm, String name) {
        BattleNetResponse res = null;
        String key = buildkey(type, realm, name);
        if (cache.containsKey(key)){
            String json = cache.get(key);
            res = new BattleNetResponse(json);
        }
        return res;
    }
    
    public void putData(BattleNetType type, String realm, String name, String json) {
        String key = buildkey(type, realm, name);
        cache.put(key, json);
    }
    
    private String buildkey(BattleNetType type, String realm, String name){
        return type.name() + "/" + realm + "/" + name;
    }
}
