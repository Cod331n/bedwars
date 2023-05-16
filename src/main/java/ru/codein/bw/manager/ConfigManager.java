package ru.codein.bw.manager;

import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.codein.bw.util.LocationFormatter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class ConfigManager implements IManager {

    public YamlConfiguration getLocationsConfig() throws IOException, InvalidConfigurationException {
        YamlConfiguration yamlConfig = new YamlConfiguration();
        InputStream stream = getClass().getClassLoader().getResourceAsStream("locations.yml");
        Reader reader = null;
        if (stream != null) {
            reader = new InputStreamReader(stream);
        }
        yamlConfig.load(reader);
        return yamlConfig;
    }

    public Location getLocation(String path) throws IOException, InvalidConfigurationException {
        return LocationFormatter.format(getLocationsConfig().getString(path));
    }
}
