package com.leroy.wow.guildanalyst.glue.battlenet;

import org.junit.Assert;

import com.leroy.wow.battlenet.BattleNetClient;
import com.leroy.wow.battlenet.BattleNetResponse;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class BattlenetAPIChecker {
    
    private BattleNetClient client;
    private BattleNetResponse response;
    
    @Given("^a battlenet client$")
    public void a_battlenet_client() throws Throwable {
        client = new BattleNetClient();
    }

    @When("^I request data$")
    public void i_request_data() throws Throwable {
        response = client.getData();
    }

    @Then("^I get data$")
    public void i_get_data() throws Throwable {
        Assert.assertNotNull(response);
    }
}
