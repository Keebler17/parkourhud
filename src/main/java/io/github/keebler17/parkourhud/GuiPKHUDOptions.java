package io.github.keebler17.parkourhud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiPKHUDOptions extends GuiScreen {

	int longWidth = 200;

	int buttonWidth = 150;
	int buttonHeight = 20;

	String title = "Parkour HUD Options";
	String decimalsStr = "decimals";
	String xyz = "x/y/z decimals";
	String facing = "facing decimals";
	String vel = "velocity decimals";
	public static String shadow = "Shadow:";

	String prevShadow;

	public GuiScreen parent;

	int lastDecimals;
	int decimals;

	int lastXyzDecimals;
	int xyzDecimals;

	int lastFacingDecimals;
	int facingDecimals;

	int lastVelocityDecimals;
	int velocityDecimals;

	public static GuiButton shadowButton;

	public GuiPKHUDOptions(GuiScreen parent) {
		this.parent = parent;

		decimals = Config.getInteger("decimals");
		lastDecimals = decimals;

		xyzDecimals = Config.getInteger("xyzDecimals");
		lastXyzDecimals = xyzDecimals;

		facingDecimals = Config.getInteger("facingDecimals");
		lastFacingDecimals = facingDecimals;

		velocityDecimals = Config.getInteger("velocityDecimals");
		lastVelocityDecimals = velocityDecimals;
	}

	@SuppressWarnings("unchecked")
	public void initGui() { // x/y/z, facing, velocity
		// needs cleanup
		super.initGui();

		title = "Parkour HUD Options";
		decimalsStr = "decimals";
		xyz = "x/y/z decimals";
		facing = "facing decimals";
		vel = "velocity decimals";
		shadow = "Shadow:";
		String shadowVal = ParkourHUD.SHADOW ? " ON" : " OFF";

		this.buttonList.add(new GuiButton(Buttons.DONE.getValue(), width / 2 - longWidth / 2, (height / 5) + 120,
				longWidth, buttonHeight, "Done"));
		this.buttonList.add(new GuiSlider(Buttons.DECIMALS.getValue(), width / 2 - buttonWidth / 2, (height / 5),
				buttonWidth, buttonHeight, "", " " + decimalsStr, 0, 15, decimals, false, true));
		this.buttonList.add(new GuiSlider(Buttons.XYZ_DECIMALS.getValue(), width / 2 - buttonWidth / 2,
				(height / 5) + 24, buttonWidth, buttonHeight, "", " " + xyz, 0, 15, xyzDecimals, false, true));
		this.buttonList.add(new GuiSlider(Buttons.FACING_DECIMALS.getValue(), width / 2 - buttonWidth / 2,
				(height / 5) + 48, buttonWidth, buttonHeight, "", " " + facing, 0, 15, facingDecimals, false, true));
		this.buttonList.add(new GuiSlider(Buttons.VELOCITY_DECIMALS.getValue(), width / 2 - buttonWidth / 2,
				(height / 5) + 72, buttonWidth, buttonHeight, "", " " + vel, 0, 15, velocityDecimals, false, true));

		shadowButton = new GuiButton(Buttons.SHADOW.getValue(), width / 2 - buttonWidth / 2, (height / 5) + 96,
				buttonWidth, buttonHeight, shadow + shadowVal);

		this.buttonList.add(shadowButton);
		prevShadow = shadow + " " + shadowVal;
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();

		this.drawCenteredString(Minecraft.getMinecraft().fontRendererObj, title, width / 2, 25,
				Integer.parseInt("ffffff", 16));

		for (Object btn : this.buttonList) {
			if (btn instanceof GuiButton) {
				if (btn instanceof GuiSlider) {
					if (((GuiButton) btn).id == Buttons.DECIMALS.getValue()) {
						decimals = ((GuiSlider) btn).getValueInt();
					} else if (((GuiButton) btn).id == Buttons.XYZ_DECIMALS.getValue()) {
						xyzDecimals = ((GuiSlider) btn).getValueInt();
					} else if (((GuiButton) btn).id == Buttons.FACING_DECIMALS.getValue()) {
						facingDecimals = ((GuiSlider) btn).getValueInt();
					} else if (((GuiButton) btn).id == Buttons.VELOCITY_DECIMALS.getValue()) {
						velocityDecimals = ((GuiSlider) btn).getValueInt();
					}
				}

				if (((GuiButton) btn).id == Buttons.SHADOW.getValue()) {
					((GuiButton) btn).drawButton(mc, mouseX, mouseY);
					prevShadow = ((GuiButton) btn).displayString;
				}
			}
		}

		if (decimals != lastDecimals) {
			Config.setInteger("decimals", decimals);
			for (Object btn : this.buttonList) {
				if (btn instanceof GuiButton) {
					switch (((GuiButton) btn).id) {
					case 303:
					case 304:
					case 305:
						if (btn instanceof GuiSlider) {
							((GuiSlider) btn).setValue(decimals);
							((GuiSlider) btn).updateSlider();
							((GuiSlider) btn).sliderValue = ((GuiSlider) btn).sliderValue;
						}
						break;
					case 306:

						break;
					default:
						break;
					}
				}
			}
		}

		if (xyzDecimals != lastXyzDecimals) {
			Config.setInteger("xyzDecimals", xyzDecimals);
		}

		if (facingDecimals != lastFacingDecimals) {
			Config.setInteger("facingDecimals", facingDecimals);
		}

		if (velocityDecimals != lastVelocityDecimals) {
			Config.setInteger("velocityDecimals", velocityDecimals);
		}

		lastDecimals = decimals;
		lastXyzDecimals = xyzDecimals;
		lastFacingDecimals = facingDecimals;
		lastVelocityDecimals = velocityDecimals;

		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
