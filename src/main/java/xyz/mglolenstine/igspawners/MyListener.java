package xyz.mglolenstine.igspawners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static xyz.mglolenstine.igspawners.IGSpawners.spawners;

public class MyListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        e.getPlayer().sendMessage(ChatColor.YELLOW+"[IGSpawners] This server is using IGSpawners plugin!");
    }

    @EventHandler
    public void onMobSpawnerClick(PlayerInteractEvent e){
        if(e.getClickedBlock() != null) {
            Block b = e.getClickedBlock();
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (b.getType() == Material.MOB_SPAWNER) {
                    Inventory inventory = Bukkit.createInventory(e.getPlayer(), 9, "Golem Spawner Shop");
                    e.getPlayer().openInventory(inventory);
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        //System.out.println("Block placed!");
        if(e.getBlockPlaced().getType() == Material.MOB_SPAWNER){
            //System.out.println("Block is mob spawner!");
            boolean contains = false;
            for(Enchantment e1 : e.getItemInHand().getEnchantments().keySet()){
                System.out.println(e1.getName()+": "+e.getItemInHand().getEnchantments().get(e1));
                if(e1.getName().equals("spawner_level")){
                    contains = true;
                }
            }
            if(contains){
                System.out.println("Added spawner!");
                spawners.add(new Spawner(e.getBlockPlaced().getLocation(), e.getItemInHand().getEnchantments().get(Enchantment.getByName("spawner_level"))));
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        Block b = e.getBlock();
        if(e.getBlock().getType() == Material.MOB_SPAWNER){
            System.out.println("Block broken at "+e.getBlock().getLocation()+" was a Mob spawner!");
            boolean contains = false;
            Spawner remove = null;
            for(Spawner s : spawners){
                if(s.x == b.getLocation().getBlockX() && s.y == b.getLocation().getBlockY() && s.z == b.getLocation().getBlockZ() ){
                    System.out.println("This Mob spawner was registered!");
                    remove = s;
                    contains = true;
                }
            }
            if(contains){
                System.out.println("Removed level "+remove.level+" spawner!");
                spawners.remove(remove);
            }
        }
    }

    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent e){
        if(e.getEntity().getType().equals(EntityType.PIG) && e.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.SPAWNER)){
            for(Spawner s : IGSpawners.spawners){
                if(e.getLocation().distance(new Location(IGSpawners.plugin.getServer().getWorld(s.world), s.x, s.y, s.z)) < 9){
                    Location tmp = e.getLocation();
                    IronGolem ig = (IronGolem) tmp.getWorld().spawnEntity(tmp, EntityType.IRON_GOLEM);
                    //ig.setLocation(tmp.getX(), tmp.getY(), tmp.getZ(), 0, 0);
                    if(s.level == 0) {
                    }else if (s.level == 1) {
                        ig.setCustomName(ChatColor.GOLD + "Golden" + ChatColor.GRAY + " Golem");
                        ig.setCustomNameVisible(true);
                    } else if (s.level == 2) {
                        ig.setCustomName(ChatColor.AQUA + "Diamond" + ChatColor.GRAY + " Golem");
                        ig.setCustomNameVisible(true);
                    } else if (s.level == 3) {
                        ig.setCustomName(ChatColor.GREEN + "Emerald" + ChatColor.GRAY + " Golem");
                        ig.setCustomNameVisible(true);
                    }
                    e.setCancelled(true);
                    break;
                }
            }
        }
    }
    @EventHandler
    public void onMobDeath(EntityDeathEvent e){
        if(e.getEntity().getType().equals(EntityType.IRON_GOLEM)){
            if(e.getEntity().getCustomName() != null && !e.getEntity().getCustomName().isEmpty()){
                ArrayList<ItemStack> drops = new ArrayList<>();
                if(ChatColor.stripColor(e.getEntity().getCustomName()).contains("Golden")){
                    e.getDrops().clear();
                    drops.add(new ItemStack(Material.RED_ROSE, ThreadLocalRandom.current().nextInt(0, 3 + 1)));
                    drops.add(new ItemStack(Material.GOLD_INGOT, ThreadLocalRandom.current().nextInt(0, 3 + 1)));
                    e.getDrops().addAll(drops);
                }else if(ChatColor.stripColor(e.getEntity().getCustomName()).contains("Diamond")){
                    e.getDrops().clear();
                    drops.add(new ItemStack(Material.RED_ROSE, ThreadLocalRandom.current().nextInt(0, 3 + 1)));
                    drops.add(new ItemStack(Material.DIAMOND, ThreadLocalRandom.current().nextInt(0, 3 + 1)));
                    e.getDrops().addAll(drops);
                }else if(ChatColor.stripColor(e.getEntity().getCustomName()).contains("Emerald")){
                    e.getDrops().clear();
                    drops.add(new ItemStack(Material.RED_ROSE, ThreadLocalRandom.current().nextInt(0, 3 + 1)));
                    drops.add(new ItemStack(Material.EMERALD, ThreadLocalRandom.current().nextInt(0, 3 + 1)));
                    e.getDrops().addAll(drops);
                }
            }
        }
    }
}
