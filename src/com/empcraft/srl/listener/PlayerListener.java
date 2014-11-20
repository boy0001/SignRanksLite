package com.empcraft.srl.listener;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.empcraft.srl.C;
import com.empcraft.srl.object.AbstractSign;
import com.empcraft.srl.object.AbstractSignInstance;
import com.empcraft.srl.object.Requirement;
import com.empcraft.srl.signs.Extend;
import com.empcraft.srl.util.MainUtil;
import com.empcraft.srl.util.ReqHandler;
import com.empcraft.srl.util.SignHandler;

public class PlayerListener implements Listener {
    
    public static HashMap<String, AbstractSignInstance> focused = new HashMap<String, AbstractSignInstance>();
    
    public static AbstractSign getSign(Block target) {
        if (target.getType()==Material.SIGN || target.getType()==Material.SIGN_POST || target.getType()==Material.WALL_SIGN) {
            Sign sign = (Sign) target.getState();
            String line = sign.getLine(0);
            if (line == null) {
                return null;
            }
            if (line.startsWith("[") && line.endsWith("]")) {
                line = line.substring(1,line.length()-1);
            }
            AbstractSign as = SignHandler.getSign(ChatColor.stripColor(MainUtil.colorize(line.toLowerCase())));
            return as;
        }
        else {
            return null;
        }
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    public static void onBlockBreak(BlockBreakEvent event) {
        
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    public static void onSignChange(SignChangeEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        Sign sign = (Sign)block.getState();
        String line1 = event.getLine(0);
        String line2 = event.getLine(1);
        String line3 = event.getLine(2);
        String line4 = event.getLine(3);
        AbstractSign as = getSign(block);
        
        if (as == null) {
            return;
        }
        
        if (!MainUtil.hasPermission(player, "signranks.create."+as.NAME)) {
            ArrayList<String> lines;
            if (as instanceof Extend) {
                Block b = player.getWorld().getBlockAt(block.getLocation().clone().add(0, 1, 0));
                AbstractSign as2 = getSign(b);
                if (as2 == null) {
                    sign.setLine(0, "§4"+as.NAME);
                    MainUtil.sendMessage(player, C.NO_PERM, "signranks.create."+as.NAME);
                    sign.update();
                    return;
                }
                else {
                    lines = ReqHandler.getLines(player, new Sign[] {(Sign) b.getState(), sign});
                }
            }
            else {
                lines = ReqHandler.getLines(player, new Sign[] {sign});
            }
            String result = ReqHandler.validateRequirements(player, lines);
            if (result == null)  {
                sign.setLine(0, "§1"+as.NAME);
                MainUtil.sendMessage(player, C.CREATE_SIGN, as.NAME);
                sign.update();
                return;
            }
            else {
                sign.setLine(0, "§4"+as.NAME);
                MainUtil.sendMessage(player, C.INVALID_REQUIREMENT, result, as.NAME);
                sign.update();
                return;
            }
        }
        else {
            sign.setLine(0, "§4"+as.NAME);
            MainUtil.sendMessage(player, C.NO_PERM, "signranks.create."+as.NAME);
            sign.update();
            return;
        }
        
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    public static void onPlayerInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            AbstractSign as = getSign(block);
            if (as!=null) {
                Location loc = block.getLocation().clone().add(0, -1, 0);
                Block block2 = loc.getBlock();
                AbstractSign as2 = getSign(block2);
                
                Sign[] signs;
                
                if (as2 != null) {
                    signs = new Sign[] { (Sign) block.getState(), (Sign) block2.getState() };
                }
                else {
                    signs = new Sign[] { (Sign) block.getState() };
                }
                Player player = event.getPlayer();
                String result = ReqHandler.hasRequirements(player, ReqHandler.getRequirements(player, signs));
                if (result == null) {
                    as.onRightClick(event.getPlayer(), block.getLocation());
                }
                else {
                    MainUtil.sendMessage(player, C.MISSING_REQUIREMENT, result);
                }
            }
        }
        else if (action == Action.LEFT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            AbstractSign as = getSign(block);
            if (as!=null) {
                as.onLeftClick(event.getPlayer(), block.getLocation());
            }
        }
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    public static void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Block target = player.getTargetBlock(null, 5);
        String name = player.getName();
        if (focused.containsKey(name)) {
            AbstractSignInstance aSign = focused.get(name);
            if (target == null || (!aSign.block.getLocation().equals(target))) {
                aSign.sign.onUnfocus(player, aSign.block);
            }
        }
        else if (target.getType()==Material.SIGN || target.getType()==Material.SIGN_POST || target.getType()==Material.WALL_SIGN) {
            Sign sign = (Sign) target.getState();
            String line = sign.getLine(0);
            if (line!=null) {
                AbstractSign as = SignHandler.getSign(line);
                if (as!=null) {
                    as.onFocus(player, sign);
                    focused.put(name, new AbstractSignInstance(as, sign));
                }
            }
        }
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    public static void onTeleport(PlayerTeleportEvent event) {
        focused.remove(event.getPlayer().getName());
    }
    
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    public static void onWorldChange(PlayerChangedWorldEvent event) {
        focused.remove(event.getPlayer().getName());
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    public static void onQuit(PlayerQuitEvent event) {
        focused.remove(event.getPlayer().getName());
    }
    // Sign place event
    
    // Sign interact event
    
    // Sign finalize event
    
    // 
}
