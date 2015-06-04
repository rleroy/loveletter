package com.leroy.game.loveletter.glue;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(features="features",
                 format={"pretty"},
                 monochrome=true,
                 strict=true
                 ,tags = {"@wip"}
                )
public class RunTestsLoveLetter {

}
