package com.leroy.game.loveletter.beans;

import org.junit.Assert;
import org.junit.Test;

public class TestPlayer {

    @Test
    public void testGetName(){
        Player p = new Player("Bob");
        Assert.assertEquals("Bob", p.getName());
    }
    @Test
    public void testToString(){
        Player p = new Player("Bob");
        Assert.assertEquals("Bob", p.toString());
    }
    
}
