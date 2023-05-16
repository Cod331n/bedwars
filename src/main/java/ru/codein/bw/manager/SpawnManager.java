package ru.codein.bw.manager;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.material.Bed;
import org.bukkit.scoreboard.Team;
import ru.codein.bw.BedWars;
import ru.codein.bw.util.LocationFormatter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpawnManager implements IManager {

    private final BedWars bedWars = BedWars.getInstance();
    private final ConfigManager configManager = bedWars.getConfigManager();

    private Map<Team, Location> getTeamSpawnMap() throws IOException, InvalidConfigurationException {
        TeamManager teamManager = bedWars.getTeamManager();

        Location spawnRed = configManager.getLocation("redTeam");
        Location spawnBlue = configManager.getLocation("blueTeam");
        Location spawnSpectators = configManager.getLocation("spectators");

        Map<Team, Location> spawnPoints = new HashMap<>();
        spawnPoints.put(teamManager.getTeam("red"), spawnRed);
        spawnPoints.put(teamManager.getTeam("blue"), spawnBlue);
        spawnPoints.put(teamManager.getTeam("spectator"), spawnSpectators);

        return spawnPoints;
    }

    public Location getTeamSpawn(Team team) throws IOException, InvalidConfigurationException {
        return getTeamSpawnMap().get(team);
    }

    public Location getLobbySpawn() throws IOException, InvalidConfigurationException {
        return LocationFormatter.format(configManager.getLocationsConfig().getString("lobby"));
    }

    public Location getSpectatorSpawn() throws IOException, InvalidConfigurationException {
        return LocationFormatter.format(configManager.getLocationsConfig().getString("spectators"));
    }

    /**
     * @param name "iron", "gold"
     */
    public List<Location> getGeneratorsLoc(String name) throws IOException, InvalidConfigurationException {
        List<String> locations = configManager.getLocationsConfig().getStringList(name);
        if (locations == null || locations.isEmpty()) {
            throw new InvalidConfigurationException("No generators found for " + name);
        }
        return LocationFormatter.format(locations);
    }

    public void spawnBeds() throws IOException, InvalidConfigurationException {
        List<Location> bedLocations = new ArrayList<>();
        bedLocations.add(configManager.getLocation("redTeamBedFoot"));
        bedLocations.add(configManager.getLocation("redTeamBedHead"));
        bedLocations.add(configManager.getLocation("blueTeamBedFoot"));
        bedLocations.add(configManager.getLocation("blueTeamBedHead"));

        bedWars.getDataManager().addBedToMap(bedLocations.get(0), bedWars.getTeamManager().getTeam("red"));
        bedWars.getDataManager().addBedToMap(bedLocations.get(1), bedWars.getTeamManager().getTeam("red"));
        bedWars.getDataManager().addBedToMap(bedLocations.get(2), bedWars.getTeamManager().getTeam("blue"));
        bedWars.getDataManager().addBedToMap(bedLocations.get(3), bedWars.getTeamManager().getTeam("blue"));

        Block redTeamBedFoot = bedLocations.get(0).getBlock();
        Block redTeamBedHead = bedLocations.get(1).getBlock();
        Block blueTeamBedFoot = bedLocations.get(2).getBlock();
        Block blueTeamBedHead = bedLocations.get(3).getBlock();

        bedCreate(redTeamBedHead, redTeamBedFoot);
        bedCreate(blueTeamBedHead, blueTeamBedFoot);
    }

    @SuppressWarnings("deprecation")
    private void bedCreate(Block head, Block foot) {
        BlockState headState = head.getState();
        BlockState footState = foot.getState();
        headState.setType(Material.BED_BLOCK);
        footState.setType(Material.BED_BLOCK);
        headState.setRawData((byte) 0);
        footState.setRawData((byte) 8);
        footState.update(true, false);
        headState.update(true, false);
        Bed bedHead = (Bed) headState.getData();
        bedHead.setHeadOfBed(true);
        bedHead.setFacingDirection(head.getFace(foot).getOppositeFace());
        Bed bedFeed = (Bed) footState.getData();
        bedFeed.setHeadOfBed(false);
        bedFeed.setFacingDirection(foot.getFace(head));
        footState.update(true, false);
        headState.update(true, true);
    }

    public void clearEntities(World world) {
        bedWars.getServer().getWorld(world.getName()).getEntities().clear();
    }
}
