package xyz.mglolenstine.igspawners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static xyz.mglolenstine.igspawners.IGSpawners.ench;

public class Command implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        Player p = sender.getServer().getPlayer(sender.getName());
        World w = sender.getServer().getPlayer(sender.getName()).getWorld();
        if (args.length == 0) {
            sender.sendMessage("This command is currently WIP");
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("info")) {
                sender.sendMessage("There is currently "+IGSpawners.spawners.size()+" spawners registered!");
            }else if(args[0].equalsIgnoreCase("clear")){
                IGSpawners.spawners.clear();
                sender.sendMessage("All spawners have been removed!");
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("hand")) {
                ItemStack item = new ItemStack(Material.MOB_SPAWNER, 1);
                int level = Integer.parseInt(args[1]);
                item.addUnsafeEnchantment(ench, level);
                ItemMeta im = item.getItemMeta();
                if(level == 0) {
                    im.setDisplayName(ChatColor.GRAY + "Iron Golem Spawner");
                }else if (level == 1) {
                    im.setDisplayName(ChatColor.GOLD + "Golden" + ChatColor.GRAY + " Golem Spawner");
                } else if (level == 2) {
                    im.setDisplayName(ChatColor.AQUA + "Diamond" + ChatColor.GRAY + " Golem Spawner");
                } else if (level == 3) {
                    im.setDisplayName(ChatColor.GREEN + "Emerald" + ChatColor.GRAY + " Golem Spawner");
                }
                item.setItemMeta(im);
                p.getInventory().addItem(item);
            }
        }
        return true;
    }
}
