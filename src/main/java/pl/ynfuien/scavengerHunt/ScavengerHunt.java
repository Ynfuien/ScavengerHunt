package pl.ynfuien.scavengerHunt;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import pl.ynfuien.ydevlib.config.ConfigHandler;
import pl.ynfuien.ydevlib.config.ConfigObject;
import pl.ynfuien.ydevlib.messages.YLogger;

import java.util.HashMap;
import java.util.List;

public final class ScavengerHunt extends JavaPlugin {
    private static ScavengerHunt instance;

    private final ConfigHandler configHandler = new ConfigHandler(this);
    private ConfigObject config;

    @Override
    public void onEnable() {
        instance = this;

        // Set logger prefix
        YLogger.setup("<dark_aqua><gradient:gold:yellow>ScavengerHunt</gradient><dark_aqua>] <white>", getComponentLogger());

        // Configuration
        loadConfigs();
        loadLang();
        config = configHandler.getConfigObject(ConfigName.CONFIG);

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
//        commands.put("ygenerators", new MainCommand(this));
//        commands.put("doubledrop", new DoubledropCommand(this));

        for (String name : commands.keySet()) {
            CommandExecutor cmd = commands.get(name);

            getCommand(name).setExecutor(cmd);
            getCommand(name).setTabCompleter((TabCompleter) cmd);
        }
    }

    private void registerListeners() {
        Listener[] listeners = new Listener[] {
//                new BlockBreakListener(this),
//                new BlockFormListener(this),
//                new BlockPistonExtendListener(this),
//                new BlockPistonRetractListener(this),
//                new BlockPlaceListener(this),
//                new EntityExplodeListener(this),
//                new PlayerInteractListener(this),
//                new PrepareItemCraftListener(this)
        };

        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, this);
        }
    }

    private void loadConfigs() {
        configHandler.load(ConfigName.CONFIG, true, false, List.of("vanilla-generators.blocks"));
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
}
