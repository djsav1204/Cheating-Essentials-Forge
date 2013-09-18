package com.kodehawa.ce.module.handlers;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang3.StringUtils;

import com.kodehawa.ce.forge.common.Loader;
import com.kodehawa.ce.module.annotations.ModuleExperimental;
import com.kodehawa.ce.module.annotations.ModuleTechnical;
import com.kodehawa.ce.module.core.CheatingEssentialsModule;
import com.kodehawa.ce.module.enums.EnumLogType;
import com.kodehawa.ce.util.Tickable;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod(modid = "CE-ModuleManager", name = "Cheating Essentials MM",
dependencies = "before:cebaseloader", version = "3.3.3a1")
public final class ModuleManager {

    public volatile List<CheatingEssentialsModule> modules;
    public List<String> enabledModules = new CopyOnWriteArrayList<String>();
    public List<Tickable> modInternalTicksArray = new CopyOnWriteArrayList<Tickable>();

    private volatile static ModuleManager instance;
	
	public ModuleManager( ){
        modules = new CopyOnWriteArrayList<CheatingEssentialsModule>();
    }
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent evt){
		ModMetadata modMeta = evt.getModMetadata();
		modMeta.parent = "Cheating-Essentials";
	}
	
	public void addModule(final CheatingEssentialsModule e) {
        synchronized (modules) {
            modules.add( e );
            Loader.instance().log("Module Loaded: ".concat(StringUtils.capitalize(e.getName())));
              if (e.getClass().isAnnotationPresent(ModuleExperimental.class)) {
            	  Loader.instance().log("Module \"".concat(e.getName()).concat("\" contains ModuleExperimental annotation, use it as your own risk!"));
           }
              if (e.getClass().isAnnotationPresent(ModuleTechnical.class)) {
            	  Loader.instance().log("Module \"".concat(e.getName()).concat("\" is a technical module!"));
           }
        }
	}

	public void removeModule(final CheatingEssentialsModule e) {
        synchronized (modules) {
                modules.remove( e );
        }
	}
	
    public final CheatingEssentialsModule getModuleByClass(final Class module) {
                synchronized (modules) {
                        for (final CheatingEssentialsModule e : modules) {
                                if (e.getClass().equals(module)) {
                                        return e;
                                }
                        }
                }
                return null;
        }

     public final List<CheatingEssentialsModule> getModules() {
                synchronized (modules) {
                        return Collections.unmodifiableList(modules);
                }
        }

    public void addToTick(Tickable tickable) {
        if (!modInternalTicksArray.contains(tickable))
        {
            modInternalTicksArray.add(tickable);
        }

    }

    public void removeFromCurrentTick(Tickable tickable) {
        if (modInternalTicksArray.contains(tickable))
        {
            modInternalTicksArray.remove(tickable);
        }
    }

 	public static ModuleManager getInstance(){
 		if(instance == null){
 			instance = new ModuleManager();
 		}
 		return instance;
 	}
 	
}

