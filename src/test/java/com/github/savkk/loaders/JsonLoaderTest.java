package com.github.savkk.loaders;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;
import org.aeonbits.owner.Factory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

public class JsonLoaderTest {
    private Factory configFactory;

    @Config.Sources("classpath:data/data.json")
    public interface JsonConfig extends Config {
        @Key("application.debug")
        boolean isDebug();

        @Key("application.suites")
        List<String> suites();

        @Key("application.endpoints.fistapplication.application")
        String firstAppName();

        @Key("application.endpoints.fistapplication.baseuri")
        String firstAppBaseUri();

        @Key("application.endpoints.fistapplication.port")
        int firstAppBasePort();

        @Key("application.was.login")
        String wasLogin();

        @Key("application.was.password")
        String wasPassword();

        @Key("application.databases[0].url")
        String dbUrl0();

        @Key("application.databases[0].login")
        String dbLogin0();

        @Key("application.databases[0].password")
        String dbPassword0();

        @Key("application.databases[1].url")
        String dbUrl1();

        @Key("application.databases[1].login")
        String dbLogin1();

        @Key("application.databases[1].password")
        String dbPassword1();
    }

    @Test
    public void baseTest() {
        JsonConfig jsonConfig = configFactory.create(JsonConfig.class);
        assertFalse(jsonConfig.isDebug());
        assertEquals(jsonConfig.suites().size(), 2);
        assertEquals(jsonConfig.suites().get(0), "smoke");
        assertEquals(jsonConfig.suites().get(1), "master");
        assertEquals(jsonConfig.firstAppName(), "first-name");
        assertEquals(jsonConfig.firstAppBaseUri(), "/first-base-path/");
        assertEquals(jsonConfig.firstAppBasePort(), 9080);
        assertEquals(jsonConfig.wasLogin(), "was_login");
        assertEquals(jsonConfig.wasPassword(), "was_password");
        assertEquals(jsonConfig.dbUrl0(), "jdbc:oracle:thin:@fistdbhost:1521:fdb");
        assertEquals(jsonConfig.dbLogin0(), "first_db_login");
        assertEquals(jsonConfig.dbPassword0(), "first_db_password");
        assertEquals(jsonConfig.dbUrl1(), "jdbc:oracle:thin:@seconddbhost:1521:sdb");
        assertEquals(jsonConfig.dbLogin1(), "second_db_login");
        assertEquals(jsonConfig.dbPassword1(), "second_db_password");
    }

    @BeforeClass
    public void setUp() {
        configFactory = ConfigFactory.newInstance();
        configFactory.registerLoader(new JsonLoader());
    }
}