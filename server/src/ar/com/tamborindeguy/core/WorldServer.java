package ar.com.tamborindeguy.core;

import ar.com.tamborindeguy.database.IDatabase;
import ar.com.tamborindeguy.manager.MapManager;
import ar.com.tamborindeguy.manager.ObjectManager;
import ar.com.tamborindeguy.manager.SpellManager;
import ar.com.tamborindeguy.network.NetworkComunicator;
import ar.com.tamborindeguy.network.inventory.InventoryUpdate;
import ar.com.tamborindeguy.objects.types.Obj;
import ar.com.tamborindeguy.objects.types.Type;
import ar.com.tamborindeguy.systems.RandomMovementSystem;
import com.artemis.Entity;
import com.artemis.SuperMapper;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import entity.Heading;
import entity.character.info.Inventory;

import java.util.Set;

import static com.artemis.E.E;

public class WorldServer {

    public static final int INVALID_USER = -1;
    private boolean running = true;
    private boolean pause = false;
    private static World world;
    private static IDatabase database;

    private final WorldConfigurationBuilder builder = new WorldConfigurationBuilder();
    private NetworkComunicator networkComunicator;

    public static World getWorld() {
        return world;
    }

    void initSystems() {
        System.out.println("Initializing systems...");
        MapManager.initialize();
        ObjectManager.load();
        SpellManager.load();
        KryonetServerMarshalStrategy server = new KryonetServerMarshalStrategy();
        networkComunicator = new NetworkComunicator(server);
        builder
                .with(new SuperMapper())
                .with(new ServerSystem(server))
                .with(new RandomMovementSystem());
        // Logic systems
        // TODO AI-NPC
    }


    void createWorld() {
        System.out.println("Creating world...");
        world = new World(builder.build());
        // testing
        Entity player2 = getWorld().createEntity();
        E(player2)
                .expExp(10000)
                .worldPosX(52)
                .worldPosY(55)
                .worldPosMap(1)
                .elvElv(1000)
                .levelLevel(45)
                .headingCurrent(Heading.HEADING_SOUTH)
                .headIndex(4)
                .bodyIndex(100)
                .weaponIndex(8)
                .shieldIndex(3)
                .helmetIndex(6)
                .healthMin(120)
                .healthMax(120)
                .hungryMin(100)
                .hungryMax(100)
                .manaMax(1000)
                .manaMin(1000)
                .staminaMin(100)
                .staminaMax(100)
                .thirstMax(100)
                .thirstMin(100)
                .criminal()
                .nameText("guidota2")
                .clanName("Clarineta")
                .canWrite()
                .character()
                .networkId(player2.getId())
                .randomMovement()
                .aOPhysics();
        MapManager.addPlayer(player2.getId());
    }

    void start() {
        new Thread(() -> {
            double ns = 1000000000.0 / 60.0;
            double delta = 0;

            long lastTime = System.nanoTime();
            long timer = System.currentTimeMillis();

            while (running) {
                if (pause) {
                    continue;
                }
                long now = System.nanoTime();
                delta += (now - lastTime) / ns;
                lastTime = now;

                while (delta >= 1) {
                    world.process();
                    delta--;
                }
            }
            System.out.println("Server down");
            networkComunicator.stop();
        }).start();
    }


    public void pause() {
        this.pause = true;
    }

    public void resume() {
        this.pause = false;
    }

    public void stop() {
        running = false;
    }
}
