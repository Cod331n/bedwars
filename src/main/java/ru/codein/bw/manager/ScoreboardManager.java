package ru.codein.bw.manager;

import lombok.experimental.UtilityClass;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import ru.codein.bw.BedWars;
import ru.codein.bw.player.GamePlayer;

import java.util.List;

@UtilityClass
public class ScoreboardManager {
    private BukkitTask task;

    public static void setScoreboard(Player p) {
        Scoreboard scoreboard = new Scoreboard();
        ScoreboardObjective obj = scoreboard.registerObjective("zagd", IScoreboardCriteria.b);
        obj.setDisplayName("§fBedWars");
        PacketPlayOutScoreboardObjective createPacket = new PacketPlayOutScoreboardObjective(obj, 0);
        PacketPlayOutScoreboardDisplayObjective display = new PacketPlayOutScoreboardDisplayObjective(1, obj);

        ScoreboardScore line1 = new ScoreboardScore(scoreboard, obj, "§1 ");
        ScoreboardScore line2 = new ScoreboardScore(scoreboard, obj, "§7» Убийства:");
        ScoreboardScore line3 = new ScoreboardScore(scoreboard, obj, "§7» Сломанные кровати:");
        ScoreboardScore line4 = new ScoreboardScore(scoreboard, obj, "§7» Сломанные блоки:");
        ScoreboardScore line5 = new ScoreboardScore(scoreboard, obj, "§7» Победы:");
        ScoreboardScore line6 = new ScoreboardScore(scoreboard, obj, "§7» Поражения:");
        ScoreboardScore line7 = new ScoreboardScore(scoreboard, obj, "§1 ");

        GamePlayer gamePlayer = BedWars.getInstance().getGamePlayerManager().get(p);
        line1.setScore(0);
        line2.setScore(gamePlayer.getKills());
        line3.setScore(gamePlayer.getBrokenBeds());
        line4.setScore(gamePlayer.getBrokenBlocks());
        line5.setScore(gamePlayer.getWins());
        line6.setScore(gamePlayer.getLooses());
        line7.setScore(0);

        PacketPlayOutScoreboardObjective removePacket = new PacketPlayOutScoreboardObjective(obj, 1);
        PacketPlayOutScoreboardScore packet1 = new PacketPlayOutScoreboardScore(line1);
        PacketPlayOutScoreboardScore packet2 = new PacketPlayOutScoreboardScore(line2);
        PacketPlayOutScoreboardScore packet3 = new PacketPlayOutScoreboardScore(line3);
        PacketPlayOutScoreboardScore packet4 = new PacketPlayOutScoreboardScore(line4);
        PacketPlayOutScoreboardScore packet5 = new PacketPlayOutScoreboardScore(line5);
        PacketPlayOutScoreboardScore packet6 = new PacketPlayOutScoreboardScore(line6);
        PacketPlayOutScoreboardScore packet7 = new PacketPlayOutScoreboardScore(line7);

        sendPacket(removePacket, p);
        sendPacket(createPacket, p);
        sendPacket(display, p);

        sendPacket(packet1, p);
        sendPacket(packet2, p);
        sendPacket(packet3, p);
        sendPacket(packet4, p);
        sendPacket(packet5, p);
        sendPacket(packet6, p);
        sendPacket(packet7, p);
    }

    private static void sendPacket(Packet<?> packet, Player p) {
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
    }

    public static void createPacketScheduler(List<Player> playerList, int timer) {
        task = Bukkit.getScheduler().runTaskTimer(BedWars.getInstance(), () -> playerList.forEach(ScoreboardManager::setScoreboard), 0L, timer * 20L);
    }

    public static void cancelPacketScheduler() {
        task.cancel();
    }

}
