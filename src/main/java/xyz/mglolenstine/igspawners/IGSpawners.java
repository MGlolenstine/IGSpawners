package xyz.mglolenstine.igspawners;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.mglolenstine.igspawners.enchantments.Level;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public final class IGSpawners extends JavaPlugin {
    boolean eRegistered = false;
    static Level ench = new Level(69);
    File spawner_storage = new File("plugins/IGSpawners/spawner_locations.json");
    static List<Spawner> spawners = new ArrayList<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        registerEnchantment();
        spawner_storage = new File("plugins/IGSpawners/spawner_locations.json");
        if (!spawner_storage.exists()) {
            try {
                spawner_storage.getParentFile().mkdirs();
                spawner_storage.createNewFile();
                System.out.println("New file created at " + spawner_storage.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            List<String> read = new ArrayList<>();
            try {
                read = Files.readAllLines(spawner_storage.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            StringBuilder json = new StringBuilder();
            for (String s : read) {
                json.append(s);
            }
            System.out.println(json);
            GsonBuilder builder = new GsonBuilder();
            //Object o = builder.create().fromJson(json.toString(), Spawners.class);
            spawners = builder.create().fromJson(json.toString(), Spawners.class).spawners;
        }

        this.getCommand("igspawners").setExecutor(new Command());
        this.getServer().getPluginManager().registerEvents(new MyListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        try (Writer writer = new FileWriter(spawner_storage)) {
            //Spawner s = new Spawner(this.getServer().getWorlds().get(0).getSpawnLocation(), 1);
            List<Spawner> ls = new ArrayList<>();
            ls.addAll(spawners);
            Spawners ss = new Spawners(ls);
            Gson gson = new GsonBuilder().create();
            gson.toJson(ss, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void registerEnchantment() {
        if(!eRegistered) {
            if (org.bukkit.enchantments.Enchantment.getByName("spawner_level") == null) {
                try {
                    Field f = org.bukkit.enchantments.Enchantment.class.getDeclaredField("acceptingNew");
                    f.setAccessible(true);
                    f.set(null, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                eRegistered = true;
                org.bukkit.enchantments.Enchantment.registerEnchantment(ench);
            }
        }
    }
}

class Spawner {
    UUID world;
    int x;
    int y;
    int z;
    int level;

    Spawner(Location loc, int level) {
        this.x = loc.getBlockX();
        this.y = loc.getBlockY();
        this.z = loc.getBlockZ();
        this.world = loc.getWorld().getUID();
        this.level = level;
    }
}

/*
{
    "spawners": [{
        "loc": "loc",
        "level": 1
    }]
}
*/
class Spawners {
    List<Spawner> spawners;

    Spawners(List<Spawner> spawners) {
        this.spawners = spawners;
    }
}
