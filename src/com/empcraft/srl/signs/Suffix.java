package com.empcraft.srl.signs;

import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import com.empcraft.srl.object.AbstractSign;

public class Suffix extends AbstractSign{

    public Suffix(String name) {
        super(name);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onRightClick(Player player, Location location) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onLeftClick(Player player, Location location) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String[] onPacketSending(Player player, Location location, String[] lines) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onFocus(Player player, Sign sign) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onUnfocus(Player player, Location location) {
        // TODO Auto-generated method stub
        
    }
    
}
