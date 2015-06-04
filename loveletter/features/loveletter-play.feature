Feature: Play

Scenario: Play
Given a player is named "Batman"
Given a player is named "Joker"
Given a player is named "Random"
Given the round is set up
Given "Batman" has drawn a card  
When "Batman" plays
Then "Batman" should have 1 cards in hand