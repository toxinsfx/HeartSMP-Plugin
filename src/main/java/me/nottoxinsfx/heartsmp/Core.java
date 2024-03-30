package me.nottoxinsfx.heartsmp;

import me.nottoxinsfx.heartsmp.commands.*;
import me.nottoxinsfx.heartsmp.events.*;
import me.nottoxinsfx.heartsmp.items.*;
import me.nottoxinsfx.heartsmp.items.hearts.*;
import me.nottoxinsfx.heartsmp.recipes.heartTraderItemRecipe;
import me.nottoxinsfx.heartsmp.utils.YamlReader;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;
import me.nottoxinsfx.heartsmp.recipes.reviveItemRecipe;

public final class Core extends JavaPlugin {
    public static Core INSTANCE;
    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new onPlayerJoin(this), this);
        getServer().getPluginManager().registerEvents(new onPlayerDeath(this), this);
        getServer().getPluginManager().registerEvents(new onPlayerKill(this), this);
        getServer().getPluginManager().registerEvents(new onPlayerRespawn(this), this);
        getServer().getPluginManager().registerEvents(new onPlayerDrop(), this);
        getServer().getPluginManager().registerEvents(new onPlayerRenameItem(), this);

        getServer().getPluginManager().registerEvents(new actionBarEvent(this), this);

        getServer().getPluginManager().registerEvents(new ReviveItem(this), this);
        getServer().getPluginManager().registerEvents(new TraderItem(new YamlReader(this)), this);

        getServer().getPluginManager().registerEvents(new Speed(), this);
        getServer().getPluginManager().registerEvents(new Strength(), this);
        getServer().getPluginManager().registerEvents(new Energy(), this);
        getServer().getPluginManager().registerEvents(new Life(), this);
        getServer().getPluginManager().registerEvents(new Water(), this);
        getServer().getPluginManager().registerEvents(new Fire(), this);
    }

    public void registerCommands() {
        getCommand("energy").setExecutor(new energyCommand(this));
        getCommand("setenergy").setExecutor(new setEnergyCommand(this));

        getCommand("givereviveitem").setExecutor(new giveri());
        getCommand("givetraderitem").setExecutor(new giveht());
        getCommand("givestrengthheart").setExecutor(new givesh(this));
        getCommand("givespeedheart").setExecutor(new givespeedh(this));
        getCommand("giveenergyheart").setExecutor(new giveeh(this));
        getCommand("givelifeheart").setExecutor(new givelh(this));
        getCommand("givewaterheart").setExecutor(new givewh(this));
        getCommand("givefireheart").setExecutor(new givefh(this));

        getCommand("checkheart").setExecutor(new checkheart(this));

    }
    @Override
    public void onEnable() {
        this.getLogger().info("[+] Loading HeartSMP-Core v1.6");
        INSTANCE = this;
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        File energyFile = new File(getDataFolder(), "energy.yml");
        if (!energyFile.exists()) {
            try {
                energyFile.createNewFile();
            } catch (IOException e) {
                getLogger().warning("Failed to create energy.yml file!");
                e.printStackTrace();
            }
        }
        File bannedFile = new File(getDataFolder(), "banned.yml");
        if (!bannedFile.exists()) {
            try {
                bannedFile.createNewFile();
            } catch (IOException e) {
                getLogger().warning("Failed to create banned.yml file!");
                e.printStackTrace();
            }
        }
        File heartsFile = new File(getDataFolder(), "hearts.yml");
        if (!heartsFile.exists()) {
            try {
                heartsFile.createNewFile();
            } catch (IOException e) {
                getLogger().warning("Failed to create hearts.yml file!");
                e.printStackTrace();
            }
        }

        registerCommands();
        registerEvents();
        reviveItemRecipe.registerRecipe(this);
        heartTraderItemRecipe.registerRecipe(this);
    }

    @Override
    public void onDisable() {

    }
}
