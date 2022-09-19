package org.jasonkaranik.boatfly;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.List;

public class BoatNerfer implements Listener {
    @SuppressWarnings("FieldCanBeLocal")
    private final int HEIGHT_DIFF = 1;

    @EventHandler
    public void onVehicleMove(VehicleMoveEvent event) {
        Entity vehicle = event.getVehicle();

        // Must be a boat
        if (vehicle.getType() != EntityType.BOAT) {
            return;
        }

        vehicle.setGravity(vehicle.getPassengers().isEmpty() || vehicle.getLocation().getBlock().isLiquid());

        // Get the old vector
        Vector oldVector = event.getVehicle().getVelocity();

        List<Entity> entities = event.getVehicle().getPassengers();
        // Find if there is a player in the boat
        for (Entity entity : entities) {
            if (entity instanceof Player) {
                Player player = (Player) entity;
                boolean up = isHolding(player, Material.STICK);
                boolean down = isHolding(player, Material.LEVER);

                if (up) {
                    player.sendActionBar("§a§lAscending");
                    event.getVehicle().setVelocity(new Vector(oldVector.getX(), oldVector.getY() + HEIGHT_DIFF, oldVector.getZ()));
                } else if (down){
                    player.sendActionBar("§c§lDescending");
                    event.getVehicle().setVelocity(new Vector(oldVector.getX(), oldVector.getY() - HEIGHT_DIFF, oldVector.getZ()));
                } else if (!vehicle.getLocation().getBlock().isLiquid()) {
                    player.sendActionBar("§e§lCoasting");
                }
            }
        }
    }

    @EventHandler
    public void onEntityDismount(EntityDismountEvent event) {
        if (event.getDismounted().getType() == EntityType.BOAT) {
            event.getDismounted().setGravity(true);
        }
    }

    /**
     * Determines if the player is holding an item in their hand(s)
     * @param player The player to check
     * @param material The material to check for
     * @return True if the player is holding the item, false otherwise
     */
    private boolean isHolding(Player player, Material material) {
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        ItemStack offHand = player.getInventory().getItemInOffHand();
        return (mainHand != null && mainHand.getType() == material) || (offHand != null && offHand.getType() == material);
    }
}
