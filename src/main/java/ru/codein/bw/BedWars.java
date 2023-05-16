package ru.codein.bw;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import ru.codein.bw.cache.GeneratorCache;
import ru.codein.bw.cache.ListenerCache;
import ru.codein.bw.command.GameForceStart;
import ru.codein.bw.db.PlayerDao;
import ru.codein.bw.db.PlayerDataMapper;
import ru.codein.bw.db.UUIDArgumentFactory;
import ru.codein.bw.game.GameStatus;
import ru.codein.bw.generator.Generator;
import ru.codein.bw.generator.GoldGenerator;
import ru.codein.bw.generator.IronGenerator;
import ru.codein.bw.listener.*;
import ru.codein.bw.manager.*;
import ru.codein.bw.storage.MysqlPlayerStorage;
import ru.codein.bw.util.LocationFormatter;
import ru.codein.bw.util.Logger;

import java.sql.Types;
import java.util.logging.Level;

@Getter
public class BedWars extends JavaPlugin {

    private static BedWars instance;

    public static BedWars getInstance() {
        return instance;
    }

    private static final int MAXIMUM_POOL_SIZE = 10;

    private Jdbi jdbi;
    private PlayerDao playerDao;
    private DataManager dataManager;
    private ConfigManager configManager;
    private ChatManager chatManager;
    private SpawnManager spawnManager;
    private TeamManager teamManager;
    private ShopManager shopManager;
    private WorldGuardManager worldGuardManager;
    private GamePlayerManager gamePlayerManager;

    private GeneratorCache generatorCache;
    private ListenerCache listenerCache;
    private Generator ironGenerator;
    private Generator goldGenerator;


    @SneakyThrows
    @Override
    public void onEnable() {
        System.setProperty("console.encoding", "UTF-8");
        instance = this;

        // Настраиваем HikariConfig
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setUsername(System.getenv("DATABASE_USERNAME"));
        config.setPassword(System.getenv("DATABASE_PASSWORD"));
        config.setJdbcUrl(String.format("jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true&useSSL=false&serverTimezone=UTC",
                System.getenv("DATABASE_HOST"),
                System.getenv("DATABASE_PORT"),
                System.getenv("DATABASE_NAME")));
        config.setMaximumPoolSize(MAXIMUM_POOL_SIZE);

        // Подключаю JDBI
        jdbi = Jdbi.create(new HikariDataSource(config));
        jdbi.installPlugin(new SqlObjectPlugin());
        jdbi.registerArgument(new UUIDArgumentFactory(Types.BINARY));
        jdbi.registerColumnMapper(new PlayerDataMapper());

        playerDao = jdbi.onDemand(PlayerDao.class);

        listenerCache = new ListenerCache();
        generatorCache = new GeneratorCache();

        ironGenerator = new IronGenerator();
        goldGenerator = new GoldGenerator();

        dataManager = new DataManager();
        configManager = new ConfigManager();
        chatManager = new ChatManager();
        spawnManager = new SpawnManager();
        teamManager = new TeamManager();
        worldGuardManager = new WorldGuardManager();
        shopManager = new ShopManager();
        gamePlayerManager = new GamePlayerManager(new MysqlPlayerStorage(jdbi, playerDao));
        gamePlayerManager.initialize();

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new BedBreakListener(), this);
        pluginManager.registerEvents(new BlockBreakListener(), this);
        pluginManager.registerEvents(new BlockPlaceListener(), this);
        pluginManager.registerEvents(new GameEndListener(), this);
        pluginManager.registerEvents(new GameStartListener(), this);
        pluginManager.registerEvents(new InventoryListener(), this);
        pluginManager.registerEvents(new PlayerConnectListener(), this);
        pluginManager.registerEvents(new PlayerDisconnectListener(), this);
        pluginManager.registerEvents(new RespawnListener(), this);
        pluginManager.registerEvents(new VillagerInteractListener(), this);

        listenerCache.getListeners().forEach(listener -> Logger.writeLog(Level.INFO, listener.toString() + " initialized"));

        getCommand("force-start").setExecutor(new GameForceStart());

        BedWars bedWars = BedWars.getInstance();
        bedWars.getDataManager().setGameStatus(GameStatus.WAITING);
        bedWars.getSpawnManager().spawnBeds();
        LocationFormatter.format(bedWars.getConfigManager().getLocationsConfig().getStringList("nps")).forEach(location -> shopManager.spawnNPS(location));

        ScoreboardManager.createPacketScheduler(this.getDataManager().getCurrentPlayers(), 5);


        teamManager.register("red", "blue", "spectator");
        teamManager.allowAllToRespawn();

        Logger.writeLog(Level.INFO, "Plugin has started");
    }

    @Override
    public void onDisable() {
        BedWars bedWars = BedWars.getInstance();
        bedWars.getServer().getWorld("world").getEntities().clear();

        Logger.writeLog(Level.INFO, "Plugin is disabling");
    }

}
