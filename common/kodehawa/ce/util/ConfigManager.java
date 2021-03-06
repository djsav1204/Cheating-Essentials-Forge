package common.kodehawa.ce.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import net.minecraft.client.Minecraft;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Lists;
import common.kodehawa.ce.logger.DynamicLogger;
import common.kodehawa.ce.module.core.AbstractModule;
import common.kodehawa.ce.module.man.ModuleManager;

public class ConfigManager {

	private static ConfigManager instance = new ConfigManager();
	private File mcdata = Minecraft.getMinecraft().mcDataDir;
	private String configPath = "/config/Cheating Essentials/";
	private File keybindConfig = new File(mcdata, configPath+"CEKeybindConfig.txt");
	private File debugConfig = new File(mcdata, configPath+"CEDebugConfig.txt");
	private File testConfig = new File(mcdata, configPath+"CETestConfig.txt");
	private File friendConfig = new File(mcdata, configPath+"CEFriends.txt");
	private File enemyConfig = new File(mcdata, configPath+"CEEnemies.txt");
	public ArrayList<String> friends = Lists.newArrayList();
	public ArrayList<String> enemies = Lists.newArrayList();
	public boolean universalDebug = false;
	public boolean universalTest = false;
	
	public ConfigManager() {
		addDefaultFriends();
		write();
		readBooleanConfig();
		readFriendsConfig();
		readKeybindConfig();
	}
	
	public void writeKeybindConfig(){
		DynamicLogger.instance().writeLog("[CM] Writting Keybinding Configuration File...", Level.INFO);
		try{
			FileWriter filewriter = new FileWriter(keybindConfig);
			BufferedWriter bufferedwriter = new BufferedWriter(filewriter);
			for(AbstractModule module : ModuleManager.instance().avModules){
				String s = Keyboard.getKeyName(module.getKeybind());
			    bufferedwriter.write(/* 0 */"ce_key:" + /* 1 */module.getModuleName().toLowerCase().replaceAll(" ", "") + ":" + /* 2 */s +"\r\n");
			}
			bufferedwriter.close();
		}
		catch(Exception exception){
			exception.printStackTrace();
		}
	}
	
	public void readKeybindConfig(){
		try {
			DynamicLogger.instance().writeLog("[CM] Reading Keybinding Configuration File...", Level.INFO);
			FileInputStream imputstream = new FileInputStream(keybindConfig.getAbsolutePath());
			DataInputStream datastream = new DataInputStream(imputstream);
			BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(datastream));
			String key;
			while((key = bufferedreader.readLine()) != null){
				String line = key.trim();
				String[] string = line.split(":");
				String module1 = string[1];
				String keybinding = string[2].toUpperCase();
				for(AbstractModule module : ModuleManager.instance().avModules){
					//Add the modules to another List.
					List<String> modules = Arrays.asList(module.getModuleName());
					//Iterate into all modules.
					for(int i = 0; i < modules.size(); ++i){
						//See if the iterated module is the same than the one in the file
						if(module1.equalsIgnoreCase(modules.get(i).toLowerCase().replaceAll(" ", ""))){
							//If the module name in the list is the same than the declared in the file, set new keybinding
							module.setKeybinding(Keyboard.getKeyIndex(keybinding));
							if(universalDebug){
								//Debug it, if in the debug configuration is enabled "debug".
								DynamicLogger.instance().writeLog("[CM] Binded: "+module.getModuleName()+" | "+Keyboard.getKeyName(module.getKeybind()), Level.INFO); break;
							}
						}
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void writeFriendsConfig(){
		try{
			FileWriter filewriter = new FileWriter(friendConfig);
			BufferedWriter bufferedwriter = new BufferedWriter(filewriter);
			for(String s : friends){
				bufferedwriter.write(s+"\r\n");
			}
			bufferedwriter.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void readFriendsConfig(){
		DynamicLogger.instance().writeLog("[CM] Loading Friend Config File...", Level.INFO);
		try{
			FileInputStream imputstream = new FileInputStream(friendConfig.getAbsolutePath());
			DataInputStream datastream = new DataInputStream(imputstream);
			BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(datastream));
			String s;
			while((s = bufferedreader.readLine()) != null){
				friends.add(s.toLowerCase().trim());
			}
			bufferedreader.close();
		}
		catch(Exception e){
		}
	}
	
	public void writeBooleanConfig(){
		try{
			FileWriter filewriter = new FileWriter(debugConfig);
			BufferedWriter bufferedwriter = new BufferedWriter(filewriter);
			String s = String.valueOf(universalDebug);
			String string = String.valueOf(universalTest);
			bufferedwriter.write("ce.config.enableDebug:" + s);
			bufferedwriter.close();
		}
		catch(Exception exception){
			exception.printStackTrace();
		}
	}
	
	public void readBooleanConfig(){
		FileInputStream imputstream;
		try {
			imputstream = new FileInputStream(debugConfig.getAbsolutePath());
			DataInputStream datastream = new DataInputStream(imputstream);
			BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(datastream));
			String value;
			while((value = bufferedreader.readLine()) != null){
				String line = value.trim();
				String[] values = line.split(":");
				String value1 = values[1];
				try{
					if(value1.equalsIgnoreCase("true") || value1.equalsIgnoreCase("false")){
						universalDebug = Boolean.parseBoolean(value1);
					}
					else{
						DynamicLogger.instance().writeLog("[CM] Can't recognize boolean: "+value1, Level.WARNING);
					}
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
			bufferedreader.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addDefaultFriends(){
		friends.add("Kodehawa");
		friends.add("ReesZRB");
	}
	
	private void write(){
		if(!keybindConfig.exists()){
			keybindConfig.getParentFile().mkdirs();
			try { keybindConfig.createNewFile(); } catch (IOException e) { e.printStackTrace(); }
			writeKeybindConfig();
		}
		if(!debugConfig.exists()){
			debugConfig.getParentFile().mkdirs();
			try{ debugConfig.createNewFile(); } catch(Exception e){ e.printStackTrace(); }
			writeBooleanConfig();
		}
		if(!friendConfig.exists()){
			friendConfig.getParentFile().mkdirs();
			try{ friendConfig.createNewFile(); } catch(Exception e){ e.printStackTrace(); }
			writeFriendsConfig();
		}
	}

	
	public static ConfigManager instance(){
		return instance;
	}
}
