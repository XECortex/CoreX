package org.ddnss.xecortex.corex.listeners

import org.bukkit.Effect
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.ddnss.xecortex.corex.Main

class EntityEventListener(private val plugin: Main) : Listener {
    @EventHandler
    fun onEntityDamage(event: EntityDamageEvent) {
        val entity = event.entity

        if (event.finalDamage <= 0.0 || entity !is LivingEntity)
            return;

        // Create a “blood“ effect
        var bloodMaterial = Material.REDSTONE_BLOCK

        // Show different blood colors for some entities
        when (entity.type) {
            EntityType.BLAZE -> bloodMaterial = Material.FIRE
            EntityType.CAVE_SPIDER, EntityType.CREEPER, EntityType.PHANTOM -> bloodMaterial = Material.EMERALD_BLOCK
            EntityType.ENDERMAN, EntityType.ENDERMITE, EntityType.ENDER_DRAGON -> bloodMaterial = Material.PURPLE_CONCRETE_POWDER
            EntityType.SLIME -> bloodMaterial = Material.SLIME_BLOCK
            EntityType.IRON_GOLEM -> bloodMaterial = Material.IRON_BLOCK
            EntityType.CHICKEN -> bloodMaterial = Material.WHITE_WOOL
            EntityType.MAGMA_CUBE -> bloodMaterial = Material.MAGMA_BLOCK
            EntityType.SKELETON -> bloodMaterial = Material.BONE_BLOCK
            EntityType.WITHER, EntityType.WITHER_SKELETON -> bloodMaterial = Material.BLACK_CONCRETE_POWDER
            EntityType.GUARDIAN, EntityType.ELDER_GUARDIAN -> bloodMaterial = Material.PRISMARINE
        }

        entity.world.playEffect(entity.location.add(0.0, entity.height / 2, 0.0), Effect.STEP_SOUND, bloodMaterial)
    }
}