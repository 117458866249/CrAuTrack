package com.qwq117458866249.createautotrack.any;

import net.minecraft.util.StringRepresentable;

public enum States implements StringRepresentable {
    X("x"),
    Z("z");

    private final String name;
    States(String pName){
        name=pName;
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}
