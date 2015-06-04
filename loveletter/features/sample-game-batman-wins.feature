Feature: winner
sample game :
Batman : 8-Princess
Joker : 1-Gard
Random : 1-Gard
Deck : 7655443322111 

Scenario: Sample game, Batman wins

Given a player is named "Batman"
Given a player is named "Joker"
Given a player is named "Random"
When the round is set up for my "sample" game
When 4 table turns
Then "Batman" should be the winner