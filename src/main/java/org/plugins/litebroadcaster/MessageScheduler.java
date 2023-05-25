package org.plugins.litebroadcaster;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MessageScheduler extends BukkitRunnable {

    private final LiteBroadcaster plugin;
    private final String message;
    private final boolean onScreen;

    public MessageScheduler(LiteBroadcaster plugin, String message, boolean onScreen) {
        this.plugin = plugin;
        this.message = message;
        this.onScreen = onScreen;
    }

    @Override
    public void run() {
        if (onScreen) {
            // Affichage du message au milieu de l'écran des joueurs
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                sendTitle(player, ChatColor.translateAlternateColorCodes('&', message));
            }
        } else {
            // Affichage du message uniquement dans le chat global
            plugin.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
        }

        // Planification du prochain message
        plugin.scheduleNextMessage();
    }

    private void sendTitle(Player player, String message) {
        player.sendTitle("", message, 10, 70, 20); // Modifier les valeurs de durée et de temporisation selon vos besoins
    }
}
