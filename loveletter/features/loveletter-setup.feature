Feature: Setup a round
Shuffle the 16 cards to form a face-down draw deck.
Remove the top card of the deck from the game without looking at it. 
If you are playing a two-player game, take three more cards from the top of the deck and place them to the side, face up.
They will not be used during this round. 
Each player draws one card from the deck.
This is the player's hand, and is kept secret from the others.
The winner of the last round goes first.
For the first round, whoever was most recently on a date goes first (if tied, the younger player wins the tie). 

Scenario: Setting up with 2 players
Given a player is named "Batman"
Given a player is named "Joker"
When the round is set up
Then "Batman" should have 1 card in hand and played no cards
Then "Joker" should have 1 card in hand and played no cards
Then 1 card should be removed hidden
Then 3 cards should be removed and shown
Then 10 should be in the deck

Scenario: Setting up with 3 players
Given a player is named "Batman"
Given a player is named "Joker"
Given a player is named "Random"
When the round is set up
Then "Batman" should have 1 card in hand and played no cards
Then "Joker" should have 1 card in hand and played no cards
Then "Random" should have 1 card in hand and played no cards
Then 1 card should be removed hidden
Then 12 should be in the deck
