package com.elevensef.createautotrack;

import net.minecraft.util.StringRepresentable;

public enum States implements StringRepresentable {
    X,
    Z;

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase();
    }
}
