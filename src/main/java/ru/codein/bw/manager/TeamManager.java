package ru.codein.bw.manager;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import ru.codein.bw.game.TeamState;
import ru.codein.bw.util.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class TeamManager implements IManager {
    @Getter
    private final Map<Player, Team> teamsMap = new HashMap<>();
    @Getter
    private final Map<Team, TeamState> teamsState = new HashMap<>();
    private final Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

    public void register(String... names) {
        for (String name : names) {
            if (scoreboard.getTeam(name) == null) {
                Team team = scoreboard.registerNewTeam(name);
                setupParams(team);

                Logger.writeLog(Level.INFO, "Team: " + name + " has registered");
            }

            setupParams(scoreboard.getTeam(name));
            Logger.writeLog(Level.INFO, "Team: " + name + " is being registered");
        }
    }

    public void addPlayerInTeam(Player player, Team team) {
        scoreboard.getTeam(team.getName()).addEntry(player.getName());
        teamsMap.put(player, team);

        Logger.writeLog(Level.INFO, "Player: " + player.getName() + " added to team: " + team.getName());
    }

    public void removePlayerFromTeam(Player player) {
        teamsMap.remove(player);
        Team team = scoreboard.getTeam(scoreboard.getEntryTeam(player.getName()).getName());
        team.removeEntry(player.getName());

        Logger.writeLog(Level.INFO, "Player: " + player.getName() + " removed from team: " + team.getName());
    }

    public void addPlayerInLeastFullTeam(Player player) {
        int red = (int) teamsMap.values().stream().filter(v -> v.equals(scoreboard.getTeam("red"))).count();
        int blue = (int) teamsMap.values().stream().filter(v -> v.equals(scoreboard.getTeam("blue"))).count();

        if (red > blue || red == blue) {
            addPlayerInTeam(player, scoreboard.getTeam("red"));

            Logger.writeLog(Level.INFO, player + " added to red team");
        } else {
            addPlayerInTeam(player, scoreboard.getTeam("blue"));

            Logger.writeLog(Level.INFO, player + " added to blue team");
        }

    }

    public Team getTeam(String name) {
        return scoreboard.getTeam(name);
    }

    public void allowAllToRespawn() {
        scoreboard.getTeams().forEach(team -> teamsState.put(scoreboard.getTeam(team.getName()), TeamState.CAN_RESPAWN));
    }

    public void prohibitRespawn(Team team) {
        teamsState.put(team, TeamState.CANT_RESPAWN);
    }

    private void setupParams(Team team) {
        if (team.getName().equalsIgnoreCase("red")) {
            team.setPrefix(ChatColor.RED.toString());
        } else if (team.getName().equalsIgnoreCase("blue")) {
            team.setPrefix(ChatColor.BLUE.toString());
        } else {
            team.setPrefix(ChatColor.DARK_GRAY.toString());
        }

        team.setAllowFriendlyFire(false);
        team.setNameTagVisibility(NameTagVisibility.HIDE_FOR_OTHER_TEAMS);
    }
}
