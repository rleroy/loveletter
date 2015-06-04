package com.leroy.game.loveletter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import com.leroy.game.loveletter.beans.Card;
import com.leroy.game.loveletter.beans.CardType;
import com.leroy.game.loveletter.beans.Player;

public class LoveLetterRound {
    
    private Set<Player> players;
    private Set<Card> removedHiddenCards;
    private Set<Card> removedShownCards;
    private List<Card> deck;
    
    public static LoveLetterRound playWith(Player...players) {
        return new LoveLetterRound(players);
    }

	public void draw(String name) {
		Player player = players.stream().filter(p -> name.equals(p.getName())).findAny().get();
		this.draw(c -> player.draw(c));
	}

    public void terminate() {
        // TODO Auto-generated method stub
    }

    public Player getWinner() {
        return players.stream().findAny().get();
    }

    public void setupRound() {
        buildDeck();
        shuffleDeck();
        this.drawCards(1, c -> this.removedHiddenCards.add(c));
        if (this.players.size() <= 2){
            this.drawCards(3, c -> this.removedShownCards.add(c));
        }
        this.players.stream().forEach(p -> this.drawCards(1, c -> p.draw(c)));
    }

    private void buildDeck() {
        for (int i = 0; i < 5; i++){
            this.deck.add(new Card(CardType.GUARD));
        }
        for (int i = 0; i < 2; i++){
            this.deck.add(new Card(CardType.PRIEST));
        }
        for (int i = 0; i < 2; i++){
            this.deck.add(new Card(CardType.BARON));
        }
        for (int i = 0; i < 2; i++){
            this.deck.add(new Card(CardType.HANDMAID));
        }
        for (int i = 0; i < 2; i++){
            this.deck.add(new Card(CardType.PRINCE));
        }
        this.deck.add(new Card(CardType.KING));
        this.deck.add(new Card(CardType.COUNTESS));
        this.deck.add(new Card(CardType.PRINCESS));
    }
    
    private void shuffleDeck() {
        Collections.shuffle(this.deck);
    }
    
    private void drawCards(int i, Consumer<Card> consumer){
        for (int j = 0; j < i; j++){
            this.draw(consumer);
        }
    }
    
    private void draw(Consumer<Card> consumer){
        Card c = this.deck.get(0);
        this.deck.remove(0);
        consumer.accept(c);
    }
    
    public Player getPlayer(String name) {
        return this.players.stream().filter(p -> name.equals(p.getName())).findAny().get();
    }

    public Set<Card> getRemovedHiddenCards() {
        return removedHiddenCards;
    }

    public Set<Card> getRemovedShownCards() {
        return removedShownCards;
    }

    public List<Card> getCardsInDeck() {
        return this.deck;
    }
    
    private LoveLetterRound(Player...players) {
        this.players = new HashSet<>();
        for (Player p : players){
            this.players.add(p);
        }
        this.removedHiddenCards = new HashSet<>();
        this.removedShownCards = new HashSet<>();
        this.deck = new ArrayList<>();
    }
}
