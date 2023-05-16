package ru.codein.bw.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import ru.codein.bw.BedWars;
import ru.codein.bw.game.GameStatus;
import ru.codein.bw.listener.event.GameStartEvent;
import ru.codein.bw.manager.ChatManager;
import ru.codein.bw.runnable.CountdownForAll;
import ru.codein.bw.util.Logger;

import java.util.logging.Level;

public class GameForceStart implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("force-start")) {
            BedWars bedWars = BedWars.getInstance();
            ChatManager chatManager = bedWars.getChatManager();

            if (sender instanceof Player && sender.isOp()) {
                if (bedWars.getDataManager().getGameStatus().equals(GameStatus.INGAME)) {
                    chatManager.sendError((Player) sender, "Игра уже запущена!");
                    Logger.writeLog(Level.WARNING, "game is already started");
                    return false;
                }

                chatManager.sendFine(((Player) sender), "Вы запустили игру");
                chatManager.sendMessage(sender.getName() + " принудительно запустил игру");

                int timer = bedWars.getConfig().getInt("countdownPreGame");
                BukkitRunnable countdownTask = new CountdownForAll(timer, "игра начнется через: ");
                countdownTask.runTaskTimer(bedWars, 0, 20L);

                Bukkit.getScheduler().runTaskLater(bedWars, () -> {
                    GameStartEvent gameStartEvent = new GameStartEvent();
                    Bukkit.getPluginManager().callEvent(gameStartEvent);
                }, timer * 20L);

            } else {
                if (sender instanceof Player) {
                    chatManager.sendError(((Player) sender), "У вас нет прав использовать эту команду!");
                    Logger.writeLog(Level.INFO, sender.getName() + " cannot use command: " + command.getName());
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
