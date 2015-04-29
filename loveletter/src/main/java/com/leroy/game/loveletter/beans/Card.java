package com.leroy.game.loveletter.beans;

public class Card {

    private CardType type;
    
    public Card(CardType type) {
        super();
        this.type = type;
    }

    public CardType getType() {
        return this.type;
    }

}
