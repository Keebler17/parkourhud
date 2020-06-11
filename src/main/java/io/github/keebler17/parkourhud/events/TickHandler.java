package io.github.keebler17.parkourhud.events;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TickHandler {

	double prevX = 0;
	double prevZ = 0;

	public static double velocity = 0;

	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent e) {
		if (Minecraft.getMinecraft().theWorld != null) {
			double dx = Minecraft.getMinecraft().thePlayer.posX - prevX;
			double dz = Minecraft.getMinecraft().thePlayer.posZ - prevZ;

			velocity = Math.sqrt(dx * dx + dz * dz) * 20;
			prevX = Minecraft.getMinecraft().thePlayer.posX;
			prevZ = Minecraft.getMinecraft().thePlayer.posZ;
		}
	}

}
