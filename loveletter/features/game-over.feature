Feature: game over

@wip
Scenario: empty deck
Given a player is named "Batman"
Given a player is named "Joker"
Given a player is named "Random"
When the round is set up
When 12 players has drawn a card and play
Then game over