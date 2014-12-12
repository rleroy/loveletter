package com.leroy.wow.battlenet;

public class BattleNetResponse {

    private String json;
    
    public BattleNetResponse(String json){
        super();
        this.json = json;
    }
    
    public String getJSON() {
        return json;
    }

}
