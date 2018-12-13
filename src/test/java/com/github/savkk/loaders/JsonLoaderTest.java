package com.github.savkk.loaders;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;
import org.aeonbits.owner.Factory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

public class JsonLoaderTest {
    private Factory configFactory;

    @Config.Sources("classpath:data/data.json")
    public interface JsonConfig extends Config {
        String name();

        Integer age();

        @Key("family.children")
        List<String> children();

        @Key("family.wife")
        String wife();

        @DefaultValue("27")
        @Key("family.wife.age")
        int wifeAge();
    }

    @Test
    public void baseTest() {
        JsonConfig jsonConfig = configFactory.create(JsonConfig.class);
        assertEquals(jsonConfig.name(), "Anatoliy");
        assertEquals(jsonConfig.age(), Integer.valueOf(33));
        assertEquals(jsonConfig.children().get(0), "Andrey");
        assertEquals(jsonConfig.children().get(1), "Arseniy");
        assertEquals(jsonConfig.wife(), "Olga");
        assertEquals(jsonConfig.wifeAge(), 27);
    }

    @BeforeClass
    public void setUp() {
        configFactory = ConfigFactory.newInstance();
        configFactory.registerLoader(new JsonLoader());
    }
}