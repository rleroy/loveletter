Feature: Gather data from the Battlenet API
As a program, I need to gather data from the battlenet api.

Background:
Given a battlenet client on server "EU"/"Sargeras"

Scenario: Simple hit
 When I request data
 Then I get data
 
Scenario: Guild data
Given guild name is "La Meute"
 When I get the member list
 Then a character with name "Pamynx" is in the list
 
Scenario: Guild data
Given character name is "Pamynx"
 When I get the character data
 Then I am able to know the ilvl of this character 