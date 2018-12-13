package com.github.savkk.loaders;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.aeonbits.owner.loaders.Loader;
import org.testng.reporters.Files;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
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
        File file = new File(uri);
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(Files.readFile(file));
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
            for (JsonElement el : jsonArray) {
                if (!el.isJsonPrimitive()) {
                    throw new IllegalStateException("Array value isn't primitive");
                }
                list.add(el.getAsString());
            }
            properties.put(targetKey, String.join(", ", list));
        } else {
            properties.put(targetKey, element.getAsString());
        }
    }
}
