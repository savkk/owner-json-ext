# OWNER  Json Loader
[OWNER Java library](https://github.com/lviggiano/owner)

```json
{
  "name": "Anatoliy",
  "age": 33,
  "family": {
    "children": [
      "Andrey",
      "Arseniy"
    ],
    "wife": "Olga"
  }
}
```

```java
    @Config.Sources("classpath:config.json")
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
```

```java
    Factory configFactory = ConfigFactory.newInstance();
    configFactory.registerLoader(new JsonPathLoader());
    JsonConfig jsonConfig = configFactory.create(JsonConfig.class);
```