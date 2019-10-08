package weissmoon.metalfurnace;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import weissmoon.core.item.WeissItemBlock;
import weissmoon.metalfurnace.block.BlockMetalFurnace;
import weissmoon.metalfurnace.block.TileMetalFurnace;
import weissmoon.metalfurnace.lib.FurnaceType;

/**
 * Created by Weissmoon on 10/7/19.
 */
//@Mod.EventBusSubscriber
public class ModData {

    public static final BlockMetalFurnace[] metalFurnace = new BlockMetalFurnace[FurnaceType.values().length];

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> blockRegistry = event.getRegistry();

        for(FurnaceType variant:FurnaceType.values()) {
            metalFurnace[variant.ordinal()] = new BlockMetalFurnace(variant);
            blockRegistry.register(metalFurnace[variant.ordinal()]);
        }
        GameRegistry.registerTileEntity(TileMetalFurnace.class, "tilemetalfurnace");
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();

        for(FurnaceType variant:FurnaceType.values()) {
            registry.register(new WeissItemBlock(metalFurnace[variant.ordinal()]));
        }
//        registry.register(new BlockMetalChestItem(ChestType.TIERS, BlocksMC.METAL_CHEST));
//        registry.register(ItemsMC.CHEST_UPGRADE = new ItemChestUpgrade(RegistryMC.RESOURCE_CHEST_UPGRADE));
    }

}
