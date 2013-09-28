package common.kodehawa.ce.module.classes;

import org.lwjgl.input.Keyboard;

import common.kodehawa.ce.module.core.ModuleAbstract;
import common.kodehawa.ce.module.enums.Category;

public class WaterWalk extends ModuleAbstract {

	public WaterWalk() {
		super(Category.WORLD);
		this.setTick(true);
	}

	@Override
	public String getModuleName(){
		return "Water Walk";
	}
	
	@Override
	public int getKeybind(){
		return Keyboard.KEY_J;
	}
	
	@Override
	public void tick(){
		if(getPlayer().isInWater()){
			getMinecraft().gameSettings.keyBindJump.pressed = true;
		}
	}
}