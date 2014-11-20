package com.empcraft.srl.util;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import com.empcraft.srl.C;
import com.empcraft.srl.object.Requirement;

public class ReqHandler {
    public static Requirement[] requirements;
    
    public static Boolean hasRequirement(Player player, String line) { 
        for (Requirement req : requirements) {
            if (req.isRequirement(line)) {
                return req.hasRequirement(player, line);
            }
        }
        return null;
    }
    
    public static Boolean isValidRequirement(Player player, String line) { 
        for (Requirement req : requirements) {
            if (req.isRequirement(line)) {
                return req.parseRequirement(line);
            }
        }
        return null;
    }
    
    public static String takeRequirement(Player player, String line) { 
        for (Requirement req : requirements) {
            if (req.isRequirement(line)) {
                return req.takeRequirement(player, line);
            }
        }
        return null;
    }
    
    public static String hasRequirements(Player player, ArrayList<String> lines) {
        for (int i = 0; i < lines.size(); i++) {
            Boolean result = hasRequirement(player, lines.get(i));
            if (result == null) {
                MainUtil.sendMessage(player, C.INVALID_LINE, i+"", lines.get(i));
            }
            else if (result == false) {
                return lines.get(i);
            }
        }
        return null;
    }
    
    public static Integer validateRequirements(Player player, ArrayList<String> lines) {
        for (int i = 0; i < lines.size(); i++) {
            Boolean result = isValidRequirement(player, lines.get(i));
            if (result == null) {
                return i;
            }
            else if (result == false) {
                return i;
            }
        }
        return null;
    }

    public static Integer takeRequirements(Player player, ArrayList<String> lines) {
        for (int i = 0; i < lines.size(); i++) {
            Boolean result = hasRequirement(player, lines.get(i));
            if (result == null) {
                MainUtil.sendMessage(player, C.INVALID_LINE, i+"", lines.get(i));
            }
            else if (result == false) {
                return i;
            }
        }
        return null;
    }
    
    public static ArrayList<String> getRequirements(Player player, Sign[] signs) {
        ArrayList<String> lines = new ArrayList<String>();
        int index = 0;
        for (Sign sign: signs) {
            for (int i = 0; i<4; i++) {
                if (index==0 && i > 1) {
                    lines.add(sign.getLine(i));
                }
                else if (i > 0) {
                    lines.add(sign.getLine(i));
                }
            }
        }
        return lines;
    }
    
    public ReqHandler() {
        requirements = new Requirement[] {
                // $
                new Requirement() {
                    @Override
                    public boolean isRequirement(String string) {
                        if (string.startsWith("$")) {
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public boolean parseRequirement(String string) {
                        try {
                            Double.parseDouble(string.substring(1));
                            return true;
                        }
                        catch (Exception e) {
                            return false;
                        }
                    }
                    @Override
                    public boolean hasRequirement(Player player, String string) {
                        return EconHandler.hasMoney(player, Double.parseDouble(string.substring(1)));
                    }

                    @Override
                    public String takeRequirement(Player player, String string) {
                        EconHandler.takeMoney(player, Double.parseDouble(string.substring(1)));
                        return null;
                    }

                },
                // EXP
                new Requirement() {
                    @Override
                    public boolean isRequirement(String string) {
                        if (string.endsWith(" exp")) {
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public boolean parseRequirement(String string) {
                        try {
                            Double.parseDouble(string.split(" exp")[0]);
                            return true;
                        }
                        catch (Exception e) {
                            return false;
                        }
                    }
                    @Override
                    public boolean hasRequirement(Player player, String string) {
                        ExperienceManager expman = new ExperienceManager(player);
                        return expman.hasExp(Double.parseDouble(string.split(" exp")[0]));
                    }

                    @Override
                    public String takeRequirement(Player player, String string) {
                        ExperienceManager expman = new ExperienceManager(player);
                        expman.changeExp(-Double.parseDouble(string.split(" exp")[0]));
                        return null;
                    }

                },
                // LVL
                new Requirement() {
                    @Override
                    public boolean isRequirement(String string) {
                        if (string.endsWith(" lvl")) {
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public boolean parseRequirement(String string) {
                        try {
                            Integer.parseInt(string.split(" lvl")[0]);
                            return true;
                        }
                        catch (Exception e) {
                            return false;
                        }
                    }
                    @Override
                    public boolean hasRequirement(Player player, String string) {
                        ExperienceManager expman = new ExperienceManager(player);
                        return expman.getLevelForExp(expman.getCurrentExp()) >= Integer.parseInt(string.split(" lvl")[0]);
                    }

                    @Override
                    public String takeRequirement(Player player, String string) {
                        ExperienceManager expman = new ExperienceManager(player);
                        int current = expman.getLevelForExp(expman.getCurrentExp());
                        int change = Integer.parseInt(string.split(" lvl")[0]);
                        
                        int lvl = expman.getCurrentExp() - expman.getXpForLevel(current);
                        int lvl1 = expman.getXpForLevel(current-change);
                        int lvl2 = expman.getXpForLevel(current-change+1);
                        
                        int diff = expman.getXpForLevel(current+1) - expman.getXpForLevel(current);
                        
                        int keep = ((lvl2-lvl1)*lvl)/diff;
                        
                        int amount = expman.getCurrentExp() - lvl1 + keep;
                        expman.changeExp(-amount);
                        expman.changeExp(Double.parseDouble(string.split(" lvl")[0]));
                        return null;
                    }

                },
                // USES
                new Requirement() {
                    @Override
                    public boolean isRequirement(String string) {
                        return (StringUtils.isNumeric(string));
                    }

                    @Override
                    public boolean parseRequirement(String string) {
                        try {
                            Integer.parseInt(string);
                            return true;
                        }
                        catch (Exception e) {
                            return false;
                        }
                    }
                    @Override
                    public boolean hasRequirement(Player player, String string) {
                        return Integer.parseInt(string) > 0;
                    }

                    @Override
                    public String takeRequirement(Player player, String string) {
                        return (Integer.parseInt(string) - 1) + "";
                    }

                },
                // group
                new Requirement() {
                    @Override
                    public boolean isRequirement(String string) {
                        if (string.contains(" ") || string.contains("$")) {
                            return false;
                        }
                        try {
                            Double.parseDouble(string);
                            return false;
                        }
                        catch (Exception e) {
                            return true;
                        }
                    }

                    @Override
                    public boolean parseRequirement(String string) {
                        return EconHandler.isGroup(string);
                    }
                    @Override
                    public boolean hasRequirement(Player player, String string) {
                        return EconHandler.hasSubgroup(player, string);
                    }

                    @Override
                    public String takeRequirement(Player player, String string) {
                        return null;
                    }
                }
        };
    }
}
