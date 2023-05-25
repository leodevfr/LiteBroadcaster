// Classe LiteBroadcaster

package org.plugins.litebroadcaster;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class LiteBroadcaster extends JavaPlugin {

    private List<MessageConfig> messageConfigs;
    private int messageIndex;
    private int interval;

    @Override
    public void onEnable() {
        // Chargement de la configuration
        saveDefaultConfig();
        loadConfig();

        // Planification du premier message
        scheduleNextMessage();

        // Plugin activé
        getLogger().info("LiteBroadcaster is enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin désactivé
        getLogger().info("LiteBroadcaster is disabled!");
    }

    private void loadConfig() {
        FileConfiguration config = getConfig();
        messageConfigs = new ArrayList<>();

        // Chargement des messages depuis la configuration
        List<Map<?, ?>> messageList = config.getMapList("messages");
        for (Map<?, ?> messageMap : messageList) {
            String message = (String) messageMap.get("message");
            boolean onScreen = (boolean) messageMap.get("onScreen");
            messageConfigs.add(new MessageConfig(message, onScreen));
        }

        // Vérification des messages
        if (messageConfigs.isEmpty()) {
            getLogger().warning("No message is defined in the configuration.");
            // Désactivation du plugin si aucun message n'est défini
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Chargement de l'intervalle entre les messages en secondes
        interval = config.getInt("interval");

        // Vérification de l'intervalle
        if (interval <= 0) {
            getLogger().warning("The interval must be greater than zero.");
            // Désactivation du plugin si l'intervalle est incorrect
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    void scheduleNextMessage() {
        // Récupération du prochain message à envoyer
        MessageConfig messageConfig = getNextMessage();
        String message = messageConfig.getMessage();
        boolean onScreen = messageConfig.isOnScreen();

        // Planification de l'envoi du message dans le chat
        BukkitTask bukkitTask = new MessageScheduler(this, message, onScreen).runTaskLater(this, interval * 20L);// Conversion en ticks (20 ticks par seconde)
    }

    private MessageConfig getNextMessage() {
        // Récupération du prochain message dans la liste en bouclant si nécessaire
        MessageConfig messageConfig = messageConfigs.get(messageIndex);
        messageIndex++;
        if (messageIndex >= messageConfigs.size()) {
            messageIndex = 0;
        }
        return messageConfig;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("litebroadcaster")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!player.isOp()) {
                    player.sendMessage(ChatColor.RED + "No permission..");
                    return true;
                }
            }
            sender.sendMessage(ChatColor.GOLD + "====+ LiteBroadcaster Plugin +====");
            sender.sendMessage(ChatColor.GOLD + "Version: 1.0.0");
            sender.sendMessage(ChatColor.GOLD + "Author: LeCarreTriangle");
            sender.sendMessage(ChatColor.GOLD + "Description: Plugin to broadcast messages in the chat.");

            // Creating clickable link
            TextComponent linkComponent = new TextComponent("Click here to visit the website.");
            linkComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://example.com"));

            // Sending the link to the sender
            sender.spigot().sendMessage(linkComponent);

            return true;
        }
        return false;
    }
}
