package com.leroy.game.loveletter;

import org.junit.Assert;
import org.junit.Test;

import com.leroy.game.loveletter.beans.Player;

public class TestLoveLetterRound {

    @Test
    public void testGetPlayer(){
        Player toto = new Player("toto");
        Player titi = new Player("titi");
        Player tata = new Player("tata");
        LoveLetterRound round = LoveLetterRound.playWith(toto, titi, tata);
        Player p = round.getPlayer("toto");
        Assert.assertEquals("toto", p.getName());
    }
}
