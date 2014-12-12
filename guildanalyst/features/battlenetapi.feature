Feature: Gather data from the Battlenet API
As a program, I need to gather data from the battlenet api.

Scenario: Simple hit
Given a battlenet client
 When I request data
 Then I get data