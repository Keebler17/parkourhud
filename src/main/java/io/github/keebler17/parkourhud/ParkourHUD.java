package io.github.keebler17.parkourhud;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import io.github.keebler17.parkourhud.events.ActionPerformedEventHandler;
import io.github.keebler17.parkourhud.events.InitGuiEventHandler;
import io.github.keebler17.parkourhud.events.RenderEventHandler;
import io.github.keebler17.parkourhud.events.TickHandler;
import io.github.keebler17.parkourhud.proxy.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ParkourHUD.MODID, version = ParkourHUD.VERSION, name = ParkourHUD.NAME)
public class ParkourHUD {

	public static Logger logger;

	public static final String MODID = "ParkourHud";
	public static final String VERSION = "1";
	public static final String NAME = "Parkour Hud";

	public static boolean SHADOW = false;

	@SidedProxy(clientSide = "io.github.keebler17.parkourhud.proxy.ClientProxy", serverSide = "io.github.keebler17.parkourhud.proxy.CommonProxy")
	public static CommonProxy proxy;

	public static File configFile;

	private static double lastW;
	private static boolean lockHH = true;
	private static boolean lastStateW = false;

	public static int lastHH = 0;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		logger = e.getModLog();
		MinecraftForge.EVENT_BUS.register(new RenderEventHandler());
		MinecraftForge.EVENT_BUS.register(new InitGuiEventHandler());
		MinecraftForge.EVENT_BUS.register(new ActionPerformedEventHandler());
		MinecraftForge.EVENT_BUS.register(new TickHandler());

		configFile = new File(e.getModConfigurationDirectory().getPath() + File.separator + "parkourhud.txt");

		if (!configFile.exists()) {
			try {
				configFile.createNewFile();

				PrintWriter w = new PrintWriter(configFile);
				w.println("version=1");
				w.println("decimals=3");
				w.println("xyzDecimals=3");
				w.println("facingDecimals=3");
				w.println("velocityDecimals=3");
				w.println("shadow=0");
				w.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		proxy.preInit(e);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent e) {
		proxy.init(e);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent e) {

		Runnable hh = new Runnable() {

			@Override
			public void run() {
				while (true) {
					if (Keyboard.isCreated()) {
						if (Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode())
								|| Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindLeft.getKeyCode())
								|| Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindRight.getKeyCode())
								|| Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindBack.getKeyCode())) {
							
							if(!lastStateW) { // is pressed
								lockHH = false;
								lastW = System.currentTimeMillis();
							} else { // held
								if(Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode()) && !lockHH) {
									lastW = System.currentTimeMillis();
								}
							}

							lastStateW = true;
						} else {
							lastStateW = false;
						}
						
						if(Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode()) && !lockHH) {
							lastHH = (int) (System.currentTimeMillis() - lastW);
							lockHH = true;
						}
					}
				}
			}
		};

		Thread hhTimingThread = new Thread(hh);
		hhTimingThread.start();

		int shadow = Config.getInteger("shadow");
		
		SHADOW = shadow == 1 ? true : false;
		
		proxy.postInit(e);
	}

	public static void drawString(String text, int x, int y, String color) {
		if (SHADOW) {
			Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, x, y, Integer.parseInt(color, 16));
		} else {
			Minecraft.getMinecraft().fontRendererObj.drawString(text, x, y, Integer.parseInt(color, 16));
		}
	}

	public static double getDecimals(double input, double decimals) {
		String format = "#.";
		for (int i = 0; i < decimals; i++, format += "#")
			;
		DecimalFormat df = new DecimalFormat(format);
		return Double.valueOf(df.format(input));
	}
}
