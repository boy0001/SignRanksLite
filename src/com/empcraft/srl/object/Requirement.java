package com.empcraft.srl.object;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public abstract class Requirement {
    public abstract boolean isRequirement(String string);
    
    public abstract boolean parseRequirement(String string);
    
    public abstract boolean hasRequirement(Player player, String string);
    
    public abstract String takeRequirement(Player player, String string);
}
