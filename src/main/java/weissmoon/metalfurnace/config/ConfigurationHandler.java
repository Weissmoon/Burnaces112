package weissmoon.metalfurnace.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

/**
 * Created by Weissmoon on 10/4/19.
 */
public class ConfigurationHandler {

    public static Configuration configuration;

    public static void init (File configFile){
        if (configuration == null){
            configuration = new Configuration(configFile);
            loadCofiguration();
        }
    }

    private static void loadCofiguration (){
        if (configuration.hasChanged()){
            configuration.save();
        }
    }
}
