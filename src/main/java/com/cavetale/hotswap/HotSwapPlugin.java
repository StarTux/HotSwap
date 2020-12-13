package com.cavetale.hotswap;

import org.bukkit.plugin.java.JavaPlugin;

public final class HotSwapPlugin extends JavaPlugin {
    HotSwapCommand hotswapCommand = new HotSwapCommand(this);

    @Override
    public void onEnable() {
        hotswapCommand.enable();
    }

    @Override
    public void onDisable() {
    }
}
