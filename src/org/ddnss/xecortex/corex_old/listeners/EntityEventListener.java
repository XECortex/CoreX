package org.ddnss.xecortex.corex_old.listeners;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.ddnss.xecortex.corex_old.Main;
import org.ddnss.xecortex.corex_old.tasks.CombatHologramTask;

public class EntityEventListener implements Listener {
    Main plugin;

    public EntityEventListener(Main plugin) {
        this.plugin = plugin;
    }

    // Entity damage by entity event
    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Entity damager = event.getDamager();

        if (!(event.getFinalDamage() > 0D && entity instanceof LivingEntity))
            return;

        // Show a combat hologram
        new CombatHologramTask(plugin, entity, event.getFinalDamage(), CombatHologramTask.CombatHologramType.COMBAT_HOLOGRAM);

        // Display “blood“ particles
        Material bloodMaterial = Material.REDSTONE_BLOCK;

        // Show different blood colors for some entities
        switch (entity.getType()) {
            case BLAZE:
                bloodMaterial = Material.FIRE;
                break;
            case CAVE_SPIDER:
            case CREEPER:
            case PHANTOM:
                bloodMaterial = Material.EMERALD_BLOCK;
                break;
            case ENDERMAN:
            case ENDER_DRAGON:
                bloodMaterial = Material.PURPLE_CONCRETE_POWDER;
                break;
            case SLIME:
                bloodMaterial = Material.SLIME_BLOCK;
                break;
            case IRON_GOLEM:
                bloodMaterial = Material.IRON_BLOCK;
                break;
            case CHICKEN:
                bloodMaterial = Material.WHITE_WOOL;
                break;
            case MAGMA_CUBE:
                bloodMaterial = Material.MAGMA_BLOCK;
                break;
            case SKELETON:
                bloodMaterial = Material.BONE_BLOCK;
                break;
            case WITHER:
            case WITHER_SKELETON:
                bloodMaterial = Material.BLACK_CONCRETE_POWDER;
                break;
            case GUARDIAN:
            case ELDER_GUARDIAN:
                bloodMaterial = Material.PRISMARINE;
        }

        entity.getWorld().playEffect(entity.getLocation().add(0f, entity.getHeight() / 2f, 0f), Effect.STEP_SOUND, bloodMaterial);
    }

    // Entity regain health event
    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        Entity entity = event.getEntity();

        if (!(event.getAmount() > 0D && entity instanceof LivingEntity))
            return;

        // Show a regeneration hologram
        new CombatHologramTask(plugin, entity, event.getAmount(), CombatHologramTask.CombatHologramType.REGENERATION_HOLOGRAM);
    }
}
