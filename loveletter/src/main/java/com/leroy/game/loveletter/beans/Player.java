package com.leroy.game.loveletter.beans;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Player {
    
    private String name;
    private Set<Card> hand;
    private List<Card> discard;
    
    public Player(String name) {
        this.name = name;
        this.hand = new HashSet<>();
        this.discard = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public boolean isKnockedOut() {
        return false;
    }

    public List<Card> getDiscardedCards() {
        return this.discard;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    public void draw(Card c) {
        this.hand.add(c);
    }

    public Object getHandSize() {
        return this.hand.size();
    }

}
