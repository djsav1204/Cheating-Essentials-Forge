package common.kodehawa.ce.util;

import java.util.logging.Level;

import com.google.common.base.Throwables;
import common.kodehawa.ce.logger.DynamicLogger;

import net.minecraft.crash.CrashReport;
import cpw.mods.fml.client.FMLClientHandler;

public class CrashManager implements IReport {

	private volatile static CrashManager instance = new CrashManager();
	public CrashManager() {}

	@Override
	public void propagate(String string, Throwable throwable) {
		DynamicLogger.instance().writeLogWithPrefix("Crash", "FATAL ERROR IN CHEATING ESSENTIALS, CRASH REPORT WILL BE DISPLAYED.", Level.WARNING, 1);
		System.err.println("FATAL ERROR IN CHEATING ESSENTIALS, CRASH REPORT WILL BE DISPLAYED.");
		FMLClientHandler.instance().getClient().displayCrashReport(new CrashReport(string, throwable));
		throw Throwables.propagate(throwable);
	}

	public static CrashManager instance(){
		return instance;
	}
}
