package pl.ynfuien.scavengerHunt;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import pl.ynfuien.scavengerHunt.commands.main.MainCommand;
import pl.ynfuien.scavengerHunt.core.hunts.Hunts;
import pl.ynfuien.scavengerHunt.core.tasks.Tasks;
import pl.ynfuien.scavengerHunt.listeners.EntityDeathListener;
import pl.ynfuien.scavengerHunt.listeners.EntityPickupItemListener;
import pl.ynfuien.scavengerHunt.listeners.PlayerJoinListener;
import pl.ynfuien.scavengerHunt.listeners.PlayerQuitListener;
import pl.ynfuien.ydevlib.config.ConfigHandler;
import pl.ynfuien.ydevlib.config.ConfigObject;
import pl.ynfuien.ydevlib.messages.YLogger;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public final class ScavengerHunt extends JavaPlugin {
    private static ScavengerHunt instance;

    private final ConfigHandler configHandler = new ConfigHandler(this);
    private ConfigObject config;

    private final Tasks tasks = new Tasks(this);
    private final Hunts hunts = new Hunts(this);

    @Override
    public void onEnable() {
        instance = this;

        // Set logger prefix
        YLogger.setup("<gold>[<yellow>ScavengerHunt<gold>] <white>", getComponentLogger());
        YLogger.setDebugging(true);

        // TMP lang deleting
        File folder = getDataFolder();
        if (folder.exists() && folder.isDirectory()) {
            File langFile = new File(folder, "lang.yml");
            if (langFile.exists()) {
                try {
                    langFile.delete();
                } catch (SecurityException ignore) {}
            }
        }

        // Configuration
        loadConfigs();
        loadLang();
        config = configHandler.getConfigObject(ConfigName.CONFIG);

        FileConfiguration config = this.config.getConfig();
        if (!tasks.load(config.getConfigurationSection("tasks"))) {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (!hunts.load(config.getConfigurationSection("hunts"))) {
            YLogger.error("Plugin couldn't load 'hunts' configuration!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        setupCommands();
        registerListeners();

        YLogger.info("Plugin successfully <green>enabled<white>!");
    }

    @Override
    public void onDisable() {
        YLogger.info("Plugin successfully <red>disabled<white>!");
    }

    private void setupCommands() {
        HashMap<String, CommandExecutor> commands = new HashMap<>();
        commands.put("scavengerhunt", new MainCommand(this));
//        commands.put("doubledrop", new DoubledropCommand(this));

        for (String name : commands.keySet()) {
            CommandExecutor cmd = commands.get(name);

            getCommand(name).setExecutor(cmd);
            getCommand(name).setTabCompleter((TabCompleter) cmd);
        }
    }

    private void registerListeners() {
        Listener[] listeners = new Listener[] {
                new PlayerJoinListener(this),
                new PlayerQuitListener(this),
                new EntityPickupItemListener(this),
                new EntityDeathListener(this),
        };

        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, this);
        }
    }

    private void loadConfigs() {
        configHandler.load(ConfigName.CONFIG, true, false, List.of("hunts.rewards.items.list"));
        configHandler.load(ConfigName.LANG, true, true);
    }

    private void loadLang() {
        FileConfiguration config = configHandler.getConfig(ConfigName.LANG);
        Lang.loadLang(config);
    }

    public boolean reloadPlugin() {
        boolean fullSuccess = true;

        // Stop intervals
//        doubledrop.stopInterval();
//        placedGenerators.stopUpdateInterval();
//
//        // Save data
//        placedGenerators.save();

        // Reload configs
        if (!configHandler.reloadAll()) fullSuccess = false;
        loadLang();

        // Database reload if needed
//        ConfigurationSection oldConfig = database.getConfig();
//        ConfigurationSection newConfig = config.getConfig().getConfigurationSection("database");
//        HashMap<String, Object> changes = YamlComparer.getChangedFields(oldConfig, newConfig);
//        if (!changes.isEmpty()) {
//            if (changes.size() == 1 && changes.containsKey("update-interval")) {
//                database.setConfig(newConfig);
//            } else {
//                loadDatabase();
//            }
//        }

//        loadGenerators();
//
//        doubledrop.load(database);
//        placedGenerators.load(database);
        return fullSuccess;
    }

    public static ScavengerHunt getInstance() {
        return instance;
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }

    @Override
    public @NotNull FileConfiguration getConfig() {
        return config.getConfig();
    }

    public Tasks getTasks() {
        return tasks;
    }

    public Hunts getHunts() {
        return hunts;
    }

    public static int randomBetween(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }
}
