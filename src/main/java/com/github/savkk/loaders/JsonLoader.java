package com.github.savkk.loaders;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.aeonbits.owner.loaders.Loader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class JsonLoader implements Loader {
    public boolean accept(URI uri) {
        try {
            URL url = uri.toURL();
            return url.getFile().toLowerCase().endsWith(".json");
        } catch (MalformedURLException ex) {
            return false;
        }
    }

    public void load(Properties properties, URI uri) throws IOException {
        Path path = Paths.get(uri);
        List<String> list = Files.readAllLines(path);
        String json = String.join(System.lineSeparator(), list);
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(json);
        jsonElementToProperties(element, properties, "");
    }

    public String defaultSpecFor(String uriPrefix) {
        return uriPrefix + ".json";
    }

    private void jsonElementToProperties(JsonElement element, Properties properties, String targetKey) {
        if (element.isJsonObject()) {
            for (String key : element.getAsJsonObject().keySet()) {
                jsonElementToProperties(
                        element.getAsJsonObject().get(key),
                        properties,
                        targetKey + (targetKey.isEmpty() ? "" : ".") + key);
            }
        } else if (element.isJsonArray()) {
            JsonArray jsonArray = element.getAsJsonArray();
            List<String> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonElement jsonElement = jsonArray.get(i);
                if (jsonElement.isJsonObject()) {
                    jsonElementToProperties(jsonElement, properties, targetKey + "[" + i + "]");
                } else if (jsonElement.isJsonPrimitive()) {
                    list.add(jsonElement.getAsString());
                }
            }
            if (!list.isEmpty()) {
                properties.put(targetKey, String.join(", ", list));
            }
        } else {
            properties.put(targetKey, element.getAsString());
        }
    }
}
