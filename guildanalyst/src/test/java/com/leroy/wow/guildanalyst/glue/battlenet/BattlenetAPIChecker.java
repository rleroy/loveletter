package com.leroy.wow.guildanalyst.glue.battlenet;

import java.util.Set;

import org.junit.Assert;

import com.leroy.wow.battlenet.BattleNetClient;
import com.leroy.wow.battlenet.BattleNetResponse;
import com.leroy.wow.beans.WowCharacter;
import com.leroy.wow.beans.WowGuildMember;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class BattlenetAPIChecker {
    
    private BattleNetClient client;
    private BattleNetResponse response;
    
    private String realm;
    private String guild;
    private Set<WowGuildMember> members;

    private String name;
    private WowCharacter character;
    
    @Given("^a battlenet client on zone \"(.*?)\"$")
    public void a_battlenet_client_on_zone(String zone) throws Throwable {
        client = new BattleNetClient(zone);
    }

    @Given("^realm name is \"(.*?)\"$")
    public void server_name_is(String realm) throws Throwable {
        this.realm = realm;
    }

    @Given("^guild name is \"(.*?)\"$")
    public void guild_name_is(String guild) throws Throwable {
        this.guild = guild;
    }
    
    @Given("^character name is \"(.*?)\"$")
    public void character_name_is(String character) throws Throwable {
        this.name = character;
    }

    @When("^I request data$")
    public void i_request_data() throws Throwable {
        response = client.getData("guild", "Sargeras", "La Meute");
    }

    @When("^I get the member list$")
    public void i_get_the_member_list() throws Throwable {
        this.members = client.getGuild(this.realm, this.guild).getMembers();
    }
    
    @When("^I get the character data$")
    public void i_get_the_character_data() throws Throwable {
        this.character = client.getCharacter(this.realm, name);
    }
    
    @Then("^I get data$")
    public void i_get_data() throws Throwable {
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getJSON());
    }
    
    @Then("^a character with name \"(.*?)\" is in the list$")
    public void character_with_name_is_in_the_list(String name) throws Throwable {
        Assert.assertEquals(1, this.members.stream().filter(c -> name.equals(c.getName())).count());
    }

    @Then("^I am able to know the ilvl of this character$")
    public void i_am_able_to_know_the_ilvl_of_this_character() throws Throwable {
        Assert.assertNotNull(this.character.getAverageItemLevel());
    }
}
