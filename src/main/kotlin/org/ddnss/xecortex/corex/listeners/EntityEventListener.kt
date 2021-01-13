package org.ddnss.xecortex.corex.listeners

import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.ddnss.xecortex.corex.Main

class EntityEventListener(private val plugin: Main) : Listener {
    @EventHandler
    fun onEntityDamage(event: EntityDamageEvent) {
        val entity = event.entity

        if (event.finalDamage <= 0 || entity !is LivingEntity)
            return;
    }
}