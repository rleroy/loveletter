package com.leroy.wow.battlenet;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum BattleNetType {
    
    guild("members"),
    character("items", "guild"),
    ;
    
    private Set<String> fields;
    
    private BattleNetType(String...fields){
        this.fields = new HashSet<String>(Arrays.asList(fields));
    }

    public Set<String> getFields() {
        return fields;
    }
    
}
