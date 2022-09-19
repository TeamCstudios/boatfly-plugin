package org.jasonkaranik.boatfly;

import org.bukkit.plugin.java.JavaPlugin;

public class BoatFly extends JavaPlugin {

    public static BoatFly plugin;

    @Override
    public void onEnable() {

        this.getLogger().info("BoatFly Enabled");

        plugin = this;

        getServer().getPluginManager().registerEvents(new BoatNerfer(), this);
        this.getLogger().info("BoatNerfer Enabled, absolute boat nerfing");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("BoatFly Disabled");
        plugin = null;
    }
}
