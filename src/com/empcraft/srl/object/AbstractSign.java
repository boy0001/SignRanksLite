package com.empcraft.srl.object;

import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public abstract class AbstractSign {
    
    public final String NAME;
    
    public AbstractSign(String name) {
        this.NAME = name;
    }
    
    public abstract void onRightClick(Player player, Location location);
    
    public abstract void onLeftClick(Player player, Location location);
    
    public abstract String[] onPacketSending(Player player, Location location, String[] lines);
    
    public abstract void onFocus(Player player, Sign sign);
    
    public abstract void onUnfocus(Player player, Sign sign);
    
    @Override
    public int hashCode() {
        return NAME.hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractSign other = (AbstractSign) obj;
        return (this.NAME.equals(other.NAME));
    }

    
}
