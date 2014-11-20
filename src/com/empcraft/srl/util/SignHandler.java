package com.empcraft.srl.util;

import java.util.HashMap;
import java.util.HashSet;

import com.empcraft.srl.object.AbstractSign;
import com.empcraft.srl.signs.Command;

public class SignHandler {
    
    public static AbstractSign[] allowed;
    
    private static HashMap<String, AbstractSign> signs = new HashMap<String, AbstractSign>();
    
    private static HashSet<AbstractSign> protectedSigns = new HashSet<AbstractSign>();
    
    public static boolean isProtected(AbstractSign sign) {
        return protectedSigns.contains(sign);
    }
    
    public static void registerSign(AbstractSign sign, boolean isProtected) {
            signs.put(sign.NAME.toLowerCase(), sign);
            if (isProtected) {
                protectedSigns.add(sign);
            }
    }
    
    public static void unregisterSign(AbstractSign sign) {
        signs.remove(sign.NAME);
        protectedSigns.remove(sign);
    }
    
    public static AbstractSign getSign(String string) {
        return signs.get(string);
    }
}
