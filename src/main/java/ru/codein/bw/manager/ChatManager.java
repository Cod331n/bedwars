package ru.codein.bw.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatManager implements IManager {

    public void sendMessage(String name, String message) {
        Bukkit.broadcastMessage(name + " " + message);
    }

    public void sendMessage(String message) {
        Bukkit.broadcastMessage(message);
    }

    public void sendFine(Player player, String message) {
        sendMessage("&2[+]", message, player);
    }

    public void sendWarning(Player player, String message) {
        sendMessage("&e[!]", message, player);
    }

    public void sendError(Player player, String message) {
        sendMessage("&4[-]", message, player);
    }

    private void sendMessage(String prefix, String message, Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + message));
    }


}
