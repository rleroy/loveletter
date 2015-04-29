package com.leroy.game.loveletter.beans;

public enum CardType {
    PRINCESS(8),
    COUNTESS(7),
    KING(6),
    PRINCE(5),
    HANDMAID(4),
    BARON(3),
    PRIEST(2),
    GUARD(1),
    ;
    
    private int rank;

    private CardType(int rank){
        this.rank = rank;
    }
    
    public int getRank() {
        return rank;
    }
}
