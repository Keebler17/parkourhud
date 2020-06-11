package io.github.keebler17.parkourhud.events;

import java.awt.GraphicsEnvironment;

import org.lwjgl.input.Keyboard;

import io.github.keebler17.parkourhud.Config;
import io.github.keebler17.parkourhud.ParkourHUD;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RenderEventHandler {
	long wTick = 0;

	long ticks = 0;

	boolean lockHH = false;
	boolean lastStateW = false;

	String color = "ffffff";

	int xyzDecimals = -1;
	int facingDecimals = -1;
	int velocityDecimals = -1;

	int loops = 0;

	double prevX = 0;
	double prevY = 0;
	double prevZ = 0;

	int hz;

	public RenderEventHandler() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		hz = ge.getDefaultScreenDevice().getDisplayMode().getRefreshRate();
	}

	@SubscribeEvent
	public void onRenderEvent(RenderGameOverlayEvent.Text e) {

		if (loops % 40 == 0) {
			xyzDecimals = Config.getInteger("xyzDecimals");
			facingDecimals = Config.getInteger("facingDecimals");
			velocityDecimals = Config.getInteger("velocityDecimals");
		}

		loops++;

		if (!Minecraft.getMinecraft().gameSettings.showDebugInfo) {
			drawInfo();
		}

		prevX = getX();
		prevY = getY();
		prevZ = getZ();
	}

	private void drawInfo() {
		handleTimings();

		ParkourHUD.drawString(EnumChatFormatting.AQUA + "Parkour HUD " + EnumChatFormatting.DARK_GRAY + "[V1.0]", 5, 5, color);
		ParkourHUD.drawString(EnumChatFormatting.DARK_GRAY + "------------------", 5, 15, color);
		ParkourHUD.drawString(
				EnumChatFormatting.AQUA + "FPS " + EnumChatFormatting.DARK_GRAY + "\u00bb " + getFpsColor() + getFps(), 5, 25,
				color);
		ParkourHUD.drawString(
				EnumChatFormatting.AQUA + "Ping " + EnumChatFormatting.DARK_GRAY + "\u00bb " + getPingColor() + getPing(), 5,
				35, color);
		ParkourHUD.drawString(EnumChatFormatting.AQUA + "X/Y/Z " + EnumChatFormatting.DARK_GRAY + "\u00bb "
				+ EnumChatFormatting.WHITE + getX() + EnumChatFormatting.GRAY + " | " + EnumChatFormatting.WHITE + getY()
				+ EnumChatFormatting.GRAY + " | " + EnumChatFormatting.WHITE + getZ(), 5, 45, color);
		ParkourHUD
				.drawString(
						EnumChatFormatting.AQUA + "Facing " + EnumChatFormatting.DARK_GRAY + "\u00bb " + EnumChatFormatting.WHITE
								+ getYaw() + EnumChatFormatting.GRAY + " | " + EnumChatFormatting.WHITE + getFacing(),
						5, 55, color);
		ParkourHUD.drawString(EnumChatFormatting.DARK_GRAY + "------------------", 5, 65, color);
		ParkourHUD.drawString(EnumChatFormatting.AQUA + "Last HH Timing " + EnumChatFormatting.DARK_GRAY + "\u00bb "
				+ getHHColor() + ParkourHUD.lastHH + "ms (" + ticks + "t)", 5, 75, color);
		ParkourHUD.drawString(EnumChatFormatting.AQUA + "Velocity " + EnumChatFormatting.DARK_GRAY + "\u00bb "
				+ EnumChatFormatting.WHITE + getVel() + "m/s", 5, 85, color);

	}

	private int getFps() {
		return Minecraft.getDebugFPS();
	}

	private String getFpsColor() {
		String fpsColor = EnumChatFormatting.RED + "";

		if (getFps() >= hz) {
			fpsColor = EnumChatFormatting.GREEN + "";
		} else if (getFps() >= hz / 2) {
			fpsColor = EnumChatFormatting.YELLOW + "";
		}

		return fpsColor;
	}

	private int getPing() {
		if(!Minecraft.getMinecraft().isSingleplayer())
		return (int) Minecraft.getMinecraft().getCurrentServerData().pingToServer;
		return 0;
	}

	private String getPingColor() {
		String pingColor = EnumChatFormatting.RED + "";

		if (getPing() < 50) {
			pingColor = EnumChatFormatting.GREEN + "";
		} else if (getPing() < 200) {
			pingColor = EnumChatFormatting.YELLOW + "";
		}
		return pingColor;
	}

	public double getX() {
		return ParkourHUD.getDecimals(Minecraft.getMinecraft().thePlayer.posX, xyzDecimals);
	}

	public double getY() {
		return ParkourHUD.getDecimals(Minecraft.getMinecraft().thePlayer.posY, xyzDecimals);
	}

	public double getZ() {
		return ParkourHUD.getDecimals(Minecraft.getMinecraft().thePlayer.posZ, xyzDecimals);
	}

	public double getVel() {
		return ParkourHUD.getDecimals(TickHandler.velocity, velocityDecimals);
	}

	public float getYaw() {
		return (float) ParkourHUD.getDecimals(net.minecraft.util.MathHelper.wrapAngleTo180_float(Minecraft.getMinecraft().thePlayer.rotationYaw),
				facingDecimals);
	}

	public String getFacing() {
		float yaw = getYaw();
		String facing;
		if (yaw > -45 && yaw < 45) {
			facing = "Z+";
		} else if (yaw >= 45 && yaw < 135) {
			facing = "X-";
		} else if (yaw > -135 && yaw <= -45) {
			facing = "X+";
		} else {
			facing = "Z-";
		}
		return facing;
	}

	private void handleTimings() {
		if (Keyboard.isCreated()) {
			if (Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode())
					|| Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindLeft.getKeyCode())
					|| Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindRight.getKeyCode())
					|| Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindBack.getKeyCode())) {
				
				if(!lastStateW) { // is pressed
					lockHH = false;
					wTick = Minecraft.getMinecraft().theWorld.getTotalWorldTime();
				} else { // held
					if(Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode()) && !lockHH) {
						wTick = Minecraft.getMinecraft().theWorld.getTotalWorldTime();
					}
				}
				lastStateW = true;
			} else {
				lastStateW = false;
			}
			
			if(Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode()) && !lockHH) {
				ticks = Minecraft.getMinecraft().theWorld.getTotalWorldTime() - wTick;
				lockHH = true;
			}
		}
	}

	private String getHHColor() {
		int color = ParkourHUD.lastHH;

		if (color > 50) {
			color %= 50;
		}

//		if (color == 50) {
//			hhColor = EnumChatFormatting.GREEN + "";
//		} else if (color > 24 && color < 50) {
//			hhColor = EnumChatFormatting.YELLOW + "";
//		} else if (color > 50 && color < 76) {
//			hhColor = EnumChatFormatting.YELLOW + "";
//		}

		String hhColor = "";

		if (color == 0) {
			hhColor = EnumChatFormatting.GREEN + "";
		} else if (color < 17) {
			hhColor = EnumChatFormatting.YELLOW + "";
		} else if (color < 33) {
			hhColor = EnumChatFormatting.RED + "";
		} else {
			hhColor = EnumChatFormatting.YELLOW + "";
		}

		return hhColor;
	}

}
