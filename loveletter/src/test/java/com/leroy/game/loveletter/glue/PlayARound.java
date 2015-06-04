package com.leroy.game.loveletter.glue;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Assert;

import com.leroy.game.loveletter.LoveLetterRound;
import com.leroy.game.loveletter.beans.Player;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PlayARound {

	private Set<Player> players = new HashSet<>();
	private LoveLetterRound game;

	@Given("^a player is named \"(.*?)\"$")
	public void a_player_is_named(String name) throws Throwable {
		players.add(new Player(name));
	}
	@Given("^\"(.*?)\" has drawn a card$")
	public void has_drawn_a_card(String player) throws Throwable {
		game.draw(player);
	}

	@When("^\"(.*?)\" plays$")
	public void plays(String player) throws Throwable {
		game.play(player);
	}

	@When("^they play a round$")
	public void they_play_a_game_of_love_letter() throws Throwable {
		game = LoveLetterRound.playWith(players.stream().collect(Collectors.toList()).toArray(new Player[0]));
	}

	@When("^once the round is over$")
	public void once_the_game_is_over() throws Throwable {
		game.terminate();
	}

	@When("^the round is set up$")
	public void the_round_is_set_up() throws Throwable {
		game = LoveLetterRound.playWith(players.stream().collect(Collectors.toList()).toArray(new Player[0]));
		game.setupRound();
	}

	@When("^the round is set up for my \"(.*?)\" game$")
	public void the_round_is_set_up_for_my_game(String arg1) throws Throwable {
		game = LoveLetterRound.playWith(players.stream().collect(Collectors.toList()).toArray(new Player[0]));
		game.setupRound("sample");
	}
	
	@When("^\"(.*?)\" draws a card$")
	public void draws_a_card(String name) throws Throwable {
		game.draw(name);
	}

	@Then("^one of them should be the winner$")
	public void one_of_them_is_the_winner() throws Throwable {
		Player winner = game.getWinner();
		players.stream().anyMatch(p -> p.getName().equals(winner.getName()));
	}

	@Then("^(\\d+) card should be removed hidden$")
	public void card_should_be_removed_hidden(int nbCards) throws Throwable {
		Assert.assertEquals(nbCards, game.getRemovedHiddenCards().size());
	}

	@Then("^(\\d+) cards should be removed and shown$")
	public void cards_should_be_removed_and_shown(int nbCards) throws Throwable {
		Assert.assertEquals(nbCards, game.getRemovedShownCards().size());
	}

	@Then("^(\\d+) should be in the deck$")
	public void should_be_in_the_deck(int nbCards) throws Throwable {
		Assert.assertEquals(nbCards, game.getCardsInDeck().size());
	}

	@Then("^\"(.*?)\" should have (\\d+) card in hand and played no cards$")
	public void should_have_card_in_hand_and_played_no_cards(String player, int nbCards) throws Throwable {
		Assert.assertEquals(nbCards, game.getPlayer(player).getHandSize());
		Assert.assertEquals(0, game.getPlayer(player).getDiscardedCards().size());
	}

	@Then("^\"(.*?)\" should have (\\d+) cards in hand$")
	public void should_have_cards_in_hand(String player, int nbCards) throws Throwable {
		Assert.assertEquals(nbCards, game.getPlayer(player).getHandSize());
	}

	@When("^(\\d+) players has drawn a card and play$")
	public void players_has_drawn_a_card_and_play(int number) throws Throwable {
	    for (int i = 0; i <number; i++) {
	    	String player = players.stream().findAny().get().getName();
			game.draw(player);
			game.play(player);

	    }
	}

	@When("^(\\d+) table turns happens$")
	public void table_turns_happens(int nb) throws Throwable {
		throw new RuntimeException("TODO");
	}

	@Then("^game over$")
	public void game_over() throws Throwable {
		Assert.assertEquals(game.isOver(),true);
	}
	

	@Then("^\"(.*?)\" should be the winner$")
	public void should_be_the_winner(String arg1) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	    throw new PendingException();
	}


}
