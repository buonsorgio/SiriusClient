package dev.sirius.client;

import dev.sirius.client.cape.CapeManager;
import dev.sirius.client.config.ConfigManager;
import dev.sirius.client.module.ModuleManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = SiriusClient.MOD_ID, name = SiriusClient.MOD_NAME, version = SiriusClient.VERSION)
public class SiriusClient {

    public static final String MOD_ID = "sirius";
    public static final String MOD_NAME = "Sirius Client";
    public static final String VERSION = "1.0.0";

    @Mod.Instance(MOD_ID)
    public static SiriusClient instance;

    public static final Logger logger = LogManager.getLogger(MOD_NAME);

    private ModuleManager moduleManager;
    private ConfigManager configManager;
    private CapeManager capeManager;
    private File siriusDir;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        siriusDir = new File(event.getModConfigurationDirectory().getParentFile(), "sirius");
        if (!siriusDir.exists()) {
            siriusDir.mkdirs();
        }
        logger.info("Sirius Client pre-initialization...");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        logger.info("Sirius Client initializing...");

        moduleManager = new ModuleManager();
        configManager = new ConfigManager(siriusDir);
        capeManager = new CapeManager(siriusDir);

        moduleManager.registerModules();
        configManager.load(moduleManager);

        MinecraftForge.EVENT_BUS.register(moduleManager);
        MinecraftForge.EVENT_BUS.register(new dev.sirius.client.gui.hud.HUDEditor());
        MinecraftForge.EVENT_BUS.register(new dev.sirius.client.event.MainMenuHandler());
        MinecraftForge.EVENT_BUS.register(new dev.sirius.client.event.InputHandler(this));

        logger.info("Sirius Client initialized with " + moduleManager.getModules().size() + " modules.");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        logger.info("Sirius Client post-initialization complete.");
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public CapeManager getCapeManager() {
        return capeManager;
    }

    public File getSiriusDir() {
        return siriusDir;
    }

    public void shutdown() {
        configManager.save(moduleManager);
    }
}
