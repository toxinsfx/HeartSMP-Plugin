package me.nottoxinsfx.heartsmp.utils;

import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class YamlReader {
    private final JavaPlugin plugin;

    public YamlReader(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public Map<String, Object> loadYaml(String fileName) {
        File file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            return null; // File doesn't exist
        }

        Yaml yaml = new Yaml();
        try {
            FileInputStream input = new FileInputStream(file);
            return yaml.load(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null; // Failed to load YAML
    }

    public void setValue(String fileName, String key, Object value) {
        File file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return; // Failed to create file
            }
        }

        Map<String, Object> data = loadYaml(fileName);
        if (data == null) {
            data = new java.util.LinkedHashMap<>();
        }
        data.put(key, value);

        Yaml yaml = new Yaml();
        try (Writer writer = new FileWriter(file)) {
            yaml.dump(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object getValue(String fileName, String key) {
        Map<String, Object> data = loadYaml(fileName);
        if (data != null && data.containsKey(key)) {
            return data.get(key);
        }
        return null; // Key not found or YAML not loaded
    }

    public void removeValue(String fileName, String key) {
        Map<String, Object> data = loadYaml(fileName);
        if (data != null && data.containsKey(key)) {
            data.remove(key);
            Yaml yaml = new Yaml();
            try (Writer writer = new FileWriter(new File(plugin.getDataFolder(), fileName))) {
                yaml.dump(data, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createValue(String fileName, String key) {
        File file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return; // Failed to create file
            }
        }

        Map<String, Object> data = loadYaml(fileName);
        if (data == null) {
            data = new LinkedHashMap<>();
        }
        if (!data.containsKey(key)) {
            data.put(key, null);
        }

        Yaml yaml = new Yaml();
        try (Writer writer = new FileWriter(file)) {
            yaml.dump(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
