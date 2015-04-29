Feature: Gather data from the Battlenet API
As a program, I need to gather data from the battlenet api.

Background:
Given a battlenet client on zone "EU"

Scenario: Simple hit
 When I request data
 Then I get data
 
Scenario: Guild data
Given realm name is "Sargeras"
Given guild name is "La Meute"
 When I get the member list
 Then a character with name "Pamynx" is in the list
 
Scenario: Character data
Given realm name is "Sargeras"
Given character name is "Pamynx"
 When I get the character data
 Then I am able to know the ilvl of this character 
 Then I am able to know the achievementPoints of this character 

Scenario: Character data
Given realm name is "Sargeras"
Given character name is "Aphykith"
 When I get the character data
 Then I am able to know this character is a reroll of "Pamynx" 

Scenario: Accounts data
Given realm name is "Sargeras"
Given guild name is "La Meute"
 When I get the accounts list
 Then a main character is names "Pamynx" and have a reroll "Aphykith"
 