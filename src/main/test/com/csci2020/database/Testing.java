package com.csci2020.database;

import com.csci2020.database.integration.DBIntegrationTests;
import com.csci2020.database.unit.AuthenticationTest;
import com.csci2020.database.unit.MatchTest;
import com.csci2020.database.unit.PlayerTest;
import com.csci2020.database.unit.TeamTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({PlayerTest.class, TeamTest.class, MatchTest.class, AuthenticationTest.class, DBIntegrationTests.class})
public class Testing {
}
