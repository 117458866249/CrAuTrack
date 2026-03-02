package com.qwq117458866249.createautotrack.register;

import com.qwq117458866249.createautotrack.objects.BuilderBlock;
import com.qwq117458866249.createautotrack.objects.BuilderBlockItem;
import com.qwq117458866249.createautotrack.CreateAutoTrack;
import com.qwq117458866249.createautotrack.objects.PosSelector;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Register {

    // Things
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, CreateAutoTrack.MOD_ID);
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CreateAutoTrack.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, CreateAutoTrack.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateAutoTrack.MOD_ID);

    // Registers
    // Block
    public static final RegistryObject<Block> BUILDER_BLOCK_B = BLOCKS.register("builder_block",
            () -> new BuilderBlock(BlockBehaviour.Properties.of()
                    .sound(SoundType.NETHER_BRICKS)
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(1.2f, 3.2f)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()
                    ));

    // Item
    public static final RegistryObject<Item> BUILDER_BLOCK = ITEMS.register("builder_block",
            () -> new BuilderBlockItem(BUILDER_BLOCK_B.get(), new Item.Properties()));
    public static final RegistryObject<Item> POS_SELECTOR = ITEMS.register("pos_selector",
            () -> new PosSelector(new Item.Properties()));

    // Tab
    public static final RegistryObject<CreativeModeTab> CR_AU_TR_TAB = CREATIVE_MODE_TABS.register("tab",
            ()-> CreativeModeTab.builder().icon(()->new ItemStack(BUILDER_BLOCK.get()))
                    .title(Component.translatable("modname.create_auto_track"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(BUILDER_BLOCK.get());
                        pOutput.accept(POS_SELECTOR.get());
                    })
                    .build());

    // Event bus
    public static void register(IEventBus pEventBus){
        BLOCKS.register(pEventBus);
        ITEMS.register(pEventBus);
        BLOCK_ENTITIES.register(pEventBus);
        CREATIVE_MODE_TABS.register(pEventBus);
    }
}
