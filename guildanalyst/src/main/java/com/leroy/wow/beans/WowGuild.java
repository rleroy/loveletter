package com.leroy.wow.beans;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class WowGuild {

    private Map<Long, Set<WowGuildMember>> members;
    
    public WowGuild(String json) {
        super();
        this.members = new HashMap<Long, Set<WowGuildMember>>();
        JSONObject obj = (JSONObject)JSONValue.parse(json);
        Object members = obj.get("members");
        for (Object member : (JSONArray)members){
            JSONObject cur = (JSONObject)member;

            Long rank = (Long)cur.get("rank");
            
            JSONObject character = (JSONObject)cur.get("character");
            String realm = (String)character.get("realm");
            String name = (String)character.get("name");
            
            WowGuildMember curMember = new WowGuildMember(rank, realm, name);
            Set<WowGuildMember> rankList = this.members.get(rank);
            if (rankList == null){
                rankList = new HashSet<WowGuildMember>();
                this.members.put(rank,  rankList);
            }
            rankList.add(curMember);
        }
    }

    public Set<WowGuildMember> getMembers() {
        return this.members.values()
                    .stream()
                    .flatMap(s -> s.stream())
                    .collect(Collectors.toSet());
    }

}
