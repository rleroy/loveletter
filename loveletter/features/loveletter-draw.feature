Feature: Draw

Scenario: Draw
Given a player is named "Batman"
Given a player is named "Joker"
Given a player is named "Random"
When the round is set up
When "Batman" draws a card
Then "Batman" should have 2 cards in hand