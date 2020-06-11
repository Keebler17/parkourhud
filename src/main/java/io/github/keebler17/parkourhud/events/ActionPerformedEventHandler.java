package io.github.keebler17.parkourhud.events;

import io.github.keebler17.parkourhud.Buttons;
import io.github.keebler17.parkourhud.Config;
import io.github.keebler17.parkourhud.GuiPKHUDOptions;
import io.github.keebler17.parkourhud.ParkourHUD;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiOptions;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ActionPerformedEventHandler {

	@SubscribeEvent
	public void onActionPerformed(ActionPerformedEvent.Post e) {
		if (e.button.id == Buttons.PKHUD.getValue() && Minecraft.getMinecraft().currentScreen instanceof GuiOptions) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiPKHUDOptions(e.gui));
		} else if (e.button.id == Buttons.DONE.getValue() && Minecraft.getMinecraft().currentScreen instanceof GuiPKHUDOptions) {
			Minecraft.getMinecraft().displayGuiScreen(((GuiPKHUDOptions) e.gui).parent);
		} else if (e.button.id == Buttons.SHADOW.getValue() && Minecraft.getMinecraft().currentScreen instanceof GuiPKHUDOptions) {
//			e.getButton().displayString = e.getButton().displayString.equals(GuiPKHUDOptions.shadow + " ON") ? GuiPKHUDOptions.shadow + " OFF" : GuiPKHUDOptions.shadow + " ON";
			
			String shadowButtonString = GuiPKHUDOptions.shadowButton.displayString;
			
			if(shadowButtonString.equals(GuiPKHUDOptions.shadow + " ON")) {
				GuiPKHUDOptions.shadowButton.displayString = GuiPKHUDOptions.shadow + " OFF";
			} else {
				GuiPKHUDOptions.shadowButton.displayString = GuiPKHUDOptions.shadow + " ON";
			}
			
			ParkourHUD.SHADOW = !ParkourHUD.SHADOW;
			Config.setInteger("shadow", ParkourHUD.SHADOW ? 1 : 0);
		}
	}

}
