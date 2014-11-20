package com.empcraft.srl.object;

import org.bukkit.block.Sign;

public class AbstractSignInstance {
    public final AbstractSign sign;
    public final Sign block;
    
    public AbstractSignInstance(AbstractSign sign, Sign block) {
        this.sign = sign;
        this.block = block;
    }
}
