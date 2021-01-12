package org.ddnss.xecortex.corex_old;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class Configuration {
    private Main plugin;
    private String fileName;
    private File configFile;
    private FileConfiguration config;

    public Configuration(Main plugin, String configFileName) {
        this.plugin = plugin;
        this.fileName = configFileName;

        this.saveDefaultConfig();
    }

    // Get access to the configuration file
    public FileConfiguration getConfig() {
        if (this.config == null)
            this.saveDefaultConfig();

        return this.config;
    }

    // Write the configuration file to the disk if it doesn't exist yet
    private void saveDefaultConfig() {
        this.configFile = new File(this.plugin.getDataFolder(), this.fileName);

        if (!this.configFile.exists()) {
            this.configFile.getParentFile().mkdirs();
            this.plugin.saveResource(this.fileName, true);
        }

        reloadConfig();
    }

    // Save all changes to the disk
    public void saveConfig() {
        try {
            this.config.save(this.configFile);
        } catch (IOException exception) {
            this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.configFile, exception);
        }
    }

    // Reload the configuration file from the disk
    public void reloadConfig() {
        this.config = YamlConfiguration.loadConfiguration(this.configFile);
    }
}
