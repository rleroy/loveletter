package com.leroy.wow.beans;

public class WowGuildMember {

    private long rank;
    private String realm;
    private String name;
    
    public WowGuildMember(long rank, String realm, String name) {
        super();
        this.rank = rank;
        this.realm = realm;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public long getRank() {
        return rank;
    }

    public String getRealm() {
        return realm;
    }

}
