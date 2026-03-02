package com.qwq117458866249.createautotrack;

import com.qwq117458866249.createautotrack.register.Register;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CreateAutoTrack.MOD_ID)
public class CreateAutoTrack
{
    public static final String MOD_ID = "create_auto_track";

    public CreateAutoTrack(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
        Register.register(modEventBus);
    }
}
