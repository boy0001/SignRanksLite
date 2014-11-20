package com.empcraft.srl;


public enum C {
    ERROR ("&8[&4SRL&8] &c"),
    WARNING ("&8[&6SRL&8] &e"),
    INFO ("&8[&1SRL&8] &7"),
    SUCCESS ("&8[&2SRL&8] &a"),
    
    ITEM_COLOR ("&5"),
    
    INVALID_LINE (WARNING+"'%s' at index %s is an invalid requirement and has been skipped."),
    PLUGIN_INFO (INFO+"Thanks for using SignRanksLite by Empire92"),
    
    MISSING_REQUIREMENT (ERROR+"You cannot use this as you are missing the requirement %s"),
    
    NO_PERM (ERROR + "You lack the permission node %s");
    
    private String def;
    private String val;
    
    C(final String def) {
        this.def = def;
        this.val = def;
    }
    
    public void s(String s) {
        this.val = s;
    }
    
    public String v() {
        if (SignRanksLite.language.contains(name())) {
            return SignRanksLite.language.getString(name());
        }
        return this.val;
    }
    
    public String d() {
        return this.def;
    }
}
