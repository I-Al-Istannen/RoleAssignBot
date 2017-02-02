package me.ialistannen.roleassigner.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

/**
 * Manages the config
 */
public enum ConfigManager {
    INSTANCE;

    private Map<String, Object> config;

    ConfigManager() {
        try {
            URI uri = getClass().getProtectionDomain().getCodeSource().getLocation().toURI();
            Path folderPath = Paths.get(uri).toAbsolutePath();
            while (!Files.isDirectory(folderPath)) {
                folderPath = folderPath.getParent();
            }

            Path configPath = folderPath.resolve("config.yml");

            if (Files.notExists(configPath)) {
                Files.copy(getClass().getResourceAsStream("/config.yml"), configPath);
            }

            Object load = new Yaml().load(Files.newInputStream(configPath, StandardOpenOption.READ));
            if (load instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> map = (Map<String, Object>) load;
                config = map;
            }
            else {
                throw new IllegalArgumentException("Config malformed, it is not a map");
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param key The key. Use "." to denote sections.
     *
     * @return The Object or null
     */
    public Object get(String key) {
        return impl_get(key, config);
    }

    /**
     * @param key The key. Use "." to denote sections.
     * @param clazz The clazz of the entry
     *
     * @return The Object or null
     */
    public <T> T get(String key, Class<T> clazz) {
        return clazz.cast(get(key));
    }

    /**
     * @param key The key. Use "." to denote sections.
     * @param type The type of the list
     *
     * @return The list or null, if not found
     */
    public <T> List<T> getList(String key, Class<T> type) {
        @SuppressWarnings("unchecked")
        List<T> t = (List<T>) get(key, List.class);
        return t;
    }

    private Object impl_get(String key, Map<String, Object> map) {
        if (key.contains(".")) {
            Object section = map.get(key.split("\\.")[0]);
            if (section == null || !(section instanceof Map)) {
                return null;
            }
            @SuppressWarnings("unchecked")
            Map<String, Object> sectionMap = (Map<String, Object>) section;
            return impl_get(key.substring(key.indexOf('.') + 1), sectionMap);
        }

        return map.get(key);
    }

    /**
     * @param key The key
     *
     * @return The String, or null if not found.
     *
     * @throws ClassCastException if it is not a String
     */
    public String getString(String key) {
        Object string = get(key);
        return string == null ? null : (String) string;
    }
}
