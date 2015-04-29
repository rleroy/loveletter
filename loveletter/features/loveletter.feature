Feature: Play a round of love letter
As a team of 3 players, we want to play a round of love letter.

Scenario: Playing a round
Given a player is named "Batman"
Given a player is named "Joker"
Given a player is named "Random"
When they play a round
 And once the round is over
Then one of them should be the winner

