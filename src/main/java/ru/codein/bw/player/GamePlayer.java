package ru.codein.bw.player;

import lombok.Data;

import java.util.UUID;

@Data
public class GamePlayer {

    private final UUID id;
    private final String name;

    private int kills;
    private int brokenBlocks;
    private int brokenBeds;
    private int wins;
    private int looses;

    public boolean forceSave;

    public void addKill() {
        kills += 1;
    }

    public void addBrokenBlock() {
        brokenBlocks += 1;
    }

    public void addBrokenBed() {
        brokenBeds += 1;
    }

    public void addWin() {
        wins += 1;
    }

    public void addLoose() {
        looses += 1;
    }

    public void load(PlayerData playerData) {
        kills = playerData.getKills();
        brokenBeds = playerData.getBrokenBeds();
        wins = playerData.getWins();
        looses = playerData.getLooses();
    }

    public PlayerData toData() {
        return new PlayerData(kills, brokenBlocks, brokenBeds, wins, looses);
    }
}
