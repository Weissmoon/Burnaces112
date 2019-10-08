package weissmoon.metalfurnace;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.*;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import weissmoon.metalfurnace.block.BlockMetalFurnace;
import weissmoon.metalfurnace.block.TileMetalFurnace;
import weissmoon.metalfurnace.config.ConfigurationHandler;

import java.io.File;

/**
 * Created by Weissmoon on 10/4/19.
 */
@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, dependencies = "required-after:weisscore@[0.1.1,)")
public class MetalFurnace {

    @Instance
    public static MetalFurnace INSTANCE = new MetalFurnace();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        ConfigurationHandler.init(new File(event.getModConfigurationDirectory().getAbsolutePath() + Reference.MOD_ID));
        MinecraftForge.EVENT_BUS.register(new ModData());
    }

}
