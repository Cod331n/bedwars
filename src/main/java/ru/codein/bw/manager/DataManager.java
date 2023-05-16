package ru.codein.bw.manager;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import ru.codein.bw.BedWars;
import ru.codein.bw.game.GameStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Getter
public class DataManager implements IManager {

    private final int requiredPlayersToStart = BedWars.getInstance().getConfig().getInt("requiredPlayersToStart");
    private final ArrayList<Player> currentPlayers = new ArrayList<>();
    private final Map<Location, Team> bedsMap = new HashMap<>();
    @Setter
    private GameStatus gameStatus;
    @Setter
    private Team won;
    public void addCurrentPlayer(Player player) {
        currentPlayers.add(player);
    }

    public void removeCurrentPlayer(Player player) {
        currentPlayers.remove(player);
    }

    public void addBedToMap(Location location, Team team) {
        bedsMap.put(location, team);
    }

    public void removeBedFromMap(Team team) {
        bedsMap.forEach((k,v)-> {
            if (v.equals(team)) {
                bedsMap.remove(k);
            }
        });
    }

}
