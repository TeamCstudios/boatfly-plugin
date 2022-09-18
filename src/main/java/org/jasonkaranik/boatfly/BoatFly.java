package org.jasonkaranik.boatfly;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

public class BoatFly extends JavaPlugin {

    public static BoatFly plugin;

    public static ProtocolManager protocolManager;

    @Override
    public void onEnable() {

        this.getLogger().info("BoatFly Enabled");

        plugin = this;

        protocolManager = ProtocolLibrary.getProtocolManager();
        
        protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.HIGHEST, PacketType.Play.Client.BOAT_MOVE) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                Player player = event.getPlayer();
                Entity b = player.getVehicle();
                if (b == null) { return; }
                if (b instanceof Player) { return; }
                boatFlight(b,player);
            }
        });
        protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.HIGHEST, PacketType.Play.Client.VEHICLE_MOVE) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                Player player = event.getPlayer();
                Entity b = player.getVehicle();
                if (b == null) { return; }
                if (b instanceof Player) { return; }
                boatFlight(b,player);
            }
        });
        protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.HIGHEST, PacketType.Play.Client.STEER_VEHICLE) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                Player player = event.getPlayer();
                Entity b = player.getVehicle();
                if (b == null) { return; }
                if (b instanceof Player) { return; }
                boatFlight(b,player);
            }
        });
    }
    public void boatFlight(Entity b, Player p){
        if (b.getType() == EntityType.BOAT) {
            // Check to see if the player is in water
            if (b.getLocation().getBlock().isLiquid()) {
                return;
            }
            if (b.getPassengers().isEmpty()) {
                return;
            }
            Vector velocity = b.getVelocity();
            double motionY = velocity.getY();
            // Make sure motionY is never less than 0
            if (motionY <= 0) {
                motionY = 0;
                if(p.getInventory().getItemInMainHand().getType().equals(Material.STICK)) {
                    motionY = 1;
                }
                if(p.getInventory().getItemInMainHand().getType().equals(Material.BLAZE_ROD)) {
                    motionY = 2;
                }
            }
            if(p.getInventory().getItemInMainHand().getType().equals(Material.STICK)) {
                motionY += 0.5;
            }
            if(p.getInventory().getItemInMainHand().getType().equals(Material.BLAZE_ROD)) {
                motionY += 1;
            }
            b.setVelocity(new Vector(velocity.getX(), motionY, velocity.getZ()));
        } 
    }

    @Override
    public void onDisable() {
        this.getLogger().info("BoatFly Disabled");
        plugin = null;
    }
}
