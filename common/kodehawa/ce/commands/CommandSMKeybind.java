package common.kodehawa.ce.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatMessageComponent;

import org.lwjgl.input.Keyboard;

import common.kodehawa.ce.module.core.AbstractModule;
import common.kodehawa.ce.module.man.ModuleManager;
import common.kodehawa.ce.util.ConfigManager;

public class CommandSMKeybind extends CommandBase {

	@Override
	public String getCommandName() {
		return "smkey";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "add <module> <key> or delete <module> <key>";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		boolean successful = false;
			if(astring[0].equalsIgnoreCase("add")){
				for(AbstractModule m : ModuleManager.instance().avModules){
					if(astring[1].equalsIgnoreCase(m.getModuleName().replaceAll(" ", ""))){
						int i = Keyboard.getKeyIndex(astring[2].toUpperCase());
						if(i != Keyboard.KEY_NONE){
							successful = true;
							m.setKeybinding(i); 
							icommandsender.sendChatToPlayer(ChatMessageComponent.createFromText("Setted key: "+m.getKeybind()+"("+astring[2]+")"+" for module: "+m.getModuleName()));
							ConfigManager.instance().writeKeybindConfig(); 
							break;
						}
					}
				}
			}
			
			if(astring[0].equalsIgnoreCase("delete")){
				for(AbstractModule m : ModuleManager.instance().avModules){
					if(astring[1].equalsIgnoreCase(m.getModuleName().replaceAll(" ", ""))){
						successful = true;
						m.setKeybinding(0);
						icommandsender.sendChatToPlayer(ChatMessageComponent.createFromText("Removed key: "+m.getKeybind()+"("+astring[2]+")"+" for module: "+m.getModuleName()));
						ConfigManager.instance().writeKeybindConfig();
						break;
					}
				}
			}
			if(!successful){
				icommandsender.sendChatToPlayer(ChatMessageComponent.createFromText("Can't recognize module: "+astring[1]+" or command: "+astring[0]));
			}
		}
	
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender icommandsender) {
		return true;
	}
}
