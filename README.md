# OWNER JSON-Loader

## JSON Loader для библиотеки OWNER

Позволяет минимизировать код для обработки сохраненной в формате JSON конфигурации приложения.

[OWNER Java library](https://github.com/lviggiano/owner)

```json
{
  "application": {
    "debug": "false",
    "suites": [
      "smoke",
      "master"
    ],
    "endpoints": {
      "fistapplication": {
        "application": "first-name",
        "baseuri": "/first-base-path/",
        "port": "9080"
      }
    },
    "was": {
      "login": "was_login",
      "password": "was_password"
    },
    "databases": [
      {
        "url": "jdbc:oracle:thin:@fistdbhost:1521:fdb",
        "login": "first_db_login",
        "password": "first_db_password"
      },
      {
        "url": "jdbc:oracle:thin:@seconddbhost:1521:sdb",
        "login": "second_db_login",
        "password": "second_db_password"
      }
    ]
  }
}
```

```java
@Config.Sources("classpath:config.json")
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
```

```java
Factory configFactory = ConfigFactory.newInstance();
configFactory.registerLoader(new JsonLoader());
JsonConfig jsonConfig = configFactory.create(JsonConfig.class);

List<String> suites = jsonConfig.suites();
boolean isDebug = jsonConfig.isDebug();
String firstDBPassword = jsonConfig.dbPassword0();
```
