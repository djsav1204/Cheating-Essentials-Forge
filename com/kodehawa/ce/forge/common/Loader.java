package com.kodehawa.ce.forge.common;

import java.util.Arrays;
import java.util.logging.Level;

import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.MinecraftForge;

import org.apache.commons.lang3.StringUtils;

import com.kodehawa.ce.forge.common.events.EventRegisterer;
import com.kodehawa.ce.forge.tick.TickHandler;
import com.kodehawa.ce.module.enums.EnumLogType;
import com.kodehawa.ce.module.loader.BaseLoader;
import com.kodehawa.ce.playerrelations.Enemy;
import com.kodehawa.ce.playerrelations.Friend;
import com.kodehawa.ce.reflect.ReflectionHelper;
import com.kodehawa.ce.util.FileManager;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Forge Class Loader for {@link CheatingEssentials} in {@link MinecraftForge}, mostly created for
 * Optifine users and users that like to use other mods with it. It should not be loaded in
 * a server envirioment, but some cheats / hacks are compatible with it.
 * The {@link ReflectionHelper} class is for some cheats that need a private or unaccesible value.
 * This loader initialize the singleton instance of all classes that need it for load it in the
 * class patch. I'm planning to add a API for most easy mod development.
 * The hardcore feature are mostly items, see also: {@link CEItemHardcoreConsole} and {@link CEItemHardcoreGui}
 * and now is mostly testing for future projects.
 * @version 3.3.2c
 * @author Kodehawa
 * @since 25/08/2013
 */

@Mod(modid="Cheating-Essentials", name="Cheating Essentials", version="3.3.2c", useMetadata=true) //Gets mod data
@NetworkMod(clientSideRequired=true, serverSideRequired=false) 
@SideOnly(Side.CLIENT)

public class Loader {
	
    public static TickHandler tickHandler = new TickHandler();
    static final int MAJOR_VERSION = 3;
    static final int MINOR_VERSION = 3;
    static final int REVISION_VERSION = 2;
    static final String REVISION_LETTER = "c";
	
    @Instance("Cheating-Essentials")
    public static Loader instance;
    
    @SidedProxy(clientSide="com.kodehawa.ce.forge.common.ClientProxy", serverSide="com.kodehawa.ce.forge.common.CommonProxy")
    public static CommonProxy proxyHandler;
   
    public static Loader instance(){
    	return instance;
    }
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	
		/* -------------------- CE METADATA DECLARATION ------------------------------ */
    	
    	ModMetadata modMeta = event.getModMetadata();
		modMeta.authorList = Arrays.asList(new String[] { "Kodehawa", "ReesZRB", "Luna" });
		modMeta.autogenerated = false;
		modMeta.credits = "Kodehawa, mod created with special help of ReesZRB.";
		modMeta.description = "Cheat Anything if you want, discover edges of Minecraft! First Forge cheating mod in 1.6.2";
		modMeta.url = "http://www.minecraftforum.net/topic/1846289-";
		
		/* ---------------------------------------------------------------------- */
		
    	FMLLog.log("Cheating Essentials", Level.INFO,
    			"Cheating Essentials Forge Loader: " + StringUtils.defaultString(Loader.class.getName()) +
    			" in Minecraft Forge " + ForgeVersion.getVersion());
    	FMLLog.log("Cheating Essentials", Level.INFO, "Loading mod instances...");
	    initializeSingletons();
    	TickRegistry.registerScheduledTickHandler(tickHandler, Side.CLIENT);
    }
       
    @EventHandler
    public void load(FMLInitializationEvent event) {
        proxyHandler.registerRenderers();
        MinecraftForge.EVENT_BUS.register(new EventRegisterer());
    }
   
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    	logWithCategory("Started Cheating Essentials "+getForgeCEVersion()+" in Minecraft 1.6.2 with Minecraft Forge " + ForgeVersion.getVersion(), EnumLogType.FINEST);
    }

	private void initializeSingletons(){
        Enemy.getInstance();
        Friend.getInstance();
        FileManager.getInstance();
        BaseLoader.getInstance();
    }
	
	public static String getForgeCEVersion(){
		return MAJOR_VERSION+"."+MINOR_VERSION+"."+REVISION_VERSION+REVISION_LETTER; //Return current version
	}
	
	public void log( String s ){
		FMLLog.log("Cheating Essentials", Level.INFO, s);
	}
	
	public void logWithCategory( String s, EnumLogType type ){
		FMLLog.log("Cheating Essentials", Level.INFO, "["+type+"]" + " " + s);
	}
}