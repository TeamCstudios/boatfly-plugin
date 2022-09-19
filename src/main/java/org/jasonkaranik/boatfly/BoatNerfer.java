package org.jasonkaranik.boatfly;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.List;

public class BoatNerfer implements Listener {
    @EventHandler
    public void onVehicleMove(VehicleMoveEvent event) {
        Entity vehicle = event.getVehicle();

        // Must be a boat
        if (vehicle.getType() != EntityType.BOAT) {
            return;
        }

        if (vehicle.getPassengers().isEmpty() || vehicle.getLocation().getBlock().isLiquid()) {
            vehicle.setGravity(true);
        } else {
            vehicle.setGravity(false);
        }

        // Get the old vector
        Vector oldVector = event.getVehicle().getVelocity();
        // Print this
//        System.out.println("Old vector: " + oldVector);

        double oldY = 0;

        List<Entity> entities = event.getVehicle().getPassengers();
        // Find if there is a player in the boat
        for (Entity entity : entities) {
            if (entity instanceof Player) {
                Player player = (Player) entity;
                ItemStack mainHand = player.getInventory().getItemInMainHand();
                ItemStack offHand = player.getInventory().getItemInOffHand();
                boolean up = (mainHand != null && mainHand.getType() == Material.STICK) || (offHand != null && offHand.getType() == Material.STICK);
                boolean down = (mainHand != null && mainHand.getType() == Material.LEVER) || (offHand != null && offHand.getType() == Material.LEVER);

                if (up) {
                    player.sendActionBar("§a§lAscending");
                    event.getVehicle().setVelocity(new Vector(oldVector.getX(), oldVector.getY() + 1, oldVector.getZ()));
                } else if (down){
                    player.sendActionBar("§c§lDescending");
                    event.getVehicle().setVelocity(new Vector(oldVector.getX(), oldVector.getY() - 1, oldVector.getZ()));
                } else {
                    player.sendActionBar("§e§lCoasting");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        // if the player is kicked for flying, and they are in a boat then cancel the event
        if (event.getReason().equals("Flying is not enabled on this server") && event.getPlayer().getVehicle() != null) {
            //
        }
    }

    @EventHandler
    public void onEntityDismount(EntityDismountEvent event) {
        if (event.getDismounted().getType() == EntityType.BOAT) {
            event.getDismounted().setGravity(true);
            if (event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                player.sendActionBar("§a§lBoatFly §7- §c§lDisabled");
            }
        }
    }
}
