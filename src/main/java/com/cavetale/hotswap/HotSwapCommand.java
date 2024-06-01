package com.cavetale.hotswap;

import com.cavetale.hotswap.util.Cuboid;
import com.cavetale.hotswap.util.WorldEdit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

@RequiredArgsConstructor
public final class HotSwapCommand implements TabExecutor {
    private final HotSwapPlugin plugin;

    public void enable() {
        plugin.getCommand("hotswap").setExecutor(this);
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String alias, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Player expected");
            return true;
        }
        if (args.length != 2) return false;
        String from = args[0];
        String to = args[1];
        boolean begin = true;
        if (from.startsWith("^")) {
            begin = true;
            from = from.substring(1);
        }
        if (from.endsWith("$")) {
            begin = false;
            from = from.substring(0, from.length() - 1);
        }
        Player player = (Player) sender;
        player.sendMessage("Replacing '" + from + "' at " + (begin ? "begnning" : "end") + " of material name with '" + to + "'");
        Cuboid cuboid = WorldEdit.getSelection(player);
        if (cuboid == null) {
            player.sendMessage(text("No selection!", RED));
            return true;
        }
        int count = 0;
        WorldEdit.begin(player);
        for (int y = cuboid.ay; y <= cuboid.by; y += 1) {
            for (int z = cuboid.az; z <= cuboid.bz; z += 1) {
                for (int x = cuboid.ax; x <= cuboid.bx; x += 1) {
                    Block block = player.getWorld().getBlockAt(x, y, z);
                    String fullName = block.getBlockData().getAsString();
                    String materialName;
                    String dataString;
                    int brackIndex = fullName.indexOf("[");
                    if (brackIndex >= 0) {
                        materialName = fullName.substring(0, brackIndex);
                        dataString = fullName.substring(brackIndex, fullName.length());
                    } else {
                        materialName = fullName;
                        dataString = "";
                    }
                    if (materialName.startsWith("minecraft:")) {
                        materialName = materialName.substring(10, materialName.length());
                    }
                    String newMaterialName;
                    if (begin) {
                        if (!materialName.startsWith(from)) continue;
                        newMaterialName = to + materialName.substring(from.length(), materialName.length());
                    } else {
                        if (!materialName.endsWith(from)) continue;
                        newMaterialName = materialName.substring(0, materialName.length() - from.length()) + to;
                    }
                    BlockData newData = null;
                    try {
                        newData = Bukkit.createBlockData(newMaterialName + dataString);
                    } catch (IllegalArgumentException iae) { }
                    if (newData == null) {
                        try {
                            newData = Bukkit.createBlockData(newMaterialName);
                        } catch (IllegalArgumentException iae) { }
                    }
                    if (newData == null) continue;
                    WorldEdit.set(block, newData);
                    count += 1;
                }
            }
        }
        WorldEdit.commit(player);
        player.sendMessage(count + " blocks changed");
        return true;
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String alias, final String[] args) {
        return null;
    }
}
